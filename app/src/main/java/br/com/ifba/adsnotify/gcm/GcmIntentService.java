package br.com.ifba.adsnotify.gcm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.model.User;

/*
* Este serviço estende IntentService que atua como um serviço de fundo. Este serviço basicamente usado para três finalidades:
* - Para conectar com o servidor GCM e buscar o token de registro. Usa o metodo registerGCM().
* - Assinar um tópico. Usa o método subscribeToTopic ("tópico").
* - Cancelar a assinatura de um tópico. Usa o método unsubscribeFromTopic("tópico").
* */

public class GcmIntentService extends IntentService {
    private AccountManager mAccountManager;
    private User user;


    private static final String TAG = GcmIntentService.class.getSimpleName();

    public GcmIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    /**
     * Registrando com GCM e obtendo o ID de registro GCM
     */
    private void registerGCM() {
        String emailuser = null;
        String token = null;

        user = ((MyApplication) getBaseContext().getApplicationContext()).getUser();
        mAccountManager = AccountManager.get(getBaseContext().getApplicationContext());

        final Account[] accounts = mAccountManager.getAccountsByType(Config.ACCOUNT_TYPE);
        String[] name = new String[accounts.length];

        for (int i = 0; i < accounts.length; i++) {
            name[i] = accounts[i].name;
            emailuser = accounts[i].name;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.d(TAG, "GCM Registro Token INTENT: " + token);

            // Enviar o ID de registro para o servidor
            sendRegistrationToServer(token, emailuser);

            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);

            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /*
    * método para enviar o token de registro GCM para o nosso servidor para atualizá-lo em MySQL banco de dados.
    * */
    private void sendRegistrationToServer(final String token, final String email) {
        // Envia o token de inscrição para o  servidor
        Map<String,String> paramsn = new HashMap<String, String>();
        paramsn.put("email", email);
        paramsn.put("token", token);

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                Config.TOKEN_GCM,
                new JSONObject(paramsn),
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Resposta GCM servidor: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.d(TAG, "Volley error conexão de rede: " + error.getMessage() + ", code: " + networkResponse);
              //  Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getInstance().addToRequestQueue(strReq);
        }
}
