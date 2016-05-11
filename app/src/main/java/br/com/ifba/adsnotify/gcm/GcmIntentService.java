package br.com.ifba.adsnotify.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.EndPoints;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.model.User;

/*
* Este serviço estende IntentService que atua como um serviço de fundo. Este serviço basicamente usado para três finalidades:
* - Para conectar com o servidor GCM e buscar o token de registro. Usa o metodo registerGCM().
* - Assinar um tópico. Usa o método subscribeToTopic ("tópico").
* - Cancelar a assinatura de um tópico. Usa o método unsubscribeFromTopic("tópico").
* */

public class GcmIntentService extends IntentService {

    private static final String TAG = GcmIntentService.class.getSimpleName();

    public GcmIntentService() {
        super(TAG);
    }

    public static final String KEY = "key";
    public static final String TOPIC = "topic";
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";


    @Override
    protected void onHandleIntent(Intent intent) {
        String key = intent.getStringExtra(KEY);
        switch (key) {
            case SUBSCRIBE:
             // Subscrever um tópico
                String topic = intent.getStringExtra(TOPIC);
                subscribeToTopic(topic);
                break;
            case UNSUBSCRIBE:
                String topic1 = intent.getStringExtra(TOPIC);
                unsubscribeFromTopic(topic1);
                break;
            default:
                // Se a chave não for especificado, cadastre-se com GCM
                registerGCM();
        }

    }

    /**
     * Registrando com GCM e obtendo o ID de registro GCM
     */
    private void registerGCM() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = null;

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.e(TAG, "GCM Registration Token: " + token);

            // Enviar o ID de registro para o servidor
            sendRegistrationToServer(token);

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
    private void sendRegistrationToServer(final String token) {
        // Envia o token de inscrição para o  servidor
        // Para mantê-lo no Banco

            // verificação de sessão de login válido
            User user = MyApplication.getInstance().getPrefManager().getUser();
            if (user == null) {
                //  usuário não encontrado, redirecionando -o para a tela de login
                return;
            }

            String endPoint = EndPoints.USER.replace("_ID_", user.getId());

            Log.e(TAG, "endpoint: " + endPoint);

            StringRequest strReq = new StringRequest(Request.Method.PUT,
                    endPoint, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);

                    try {
                        JSONObject obj = new JSONObject(response);

                        // checando error
                        if (obj.getBoolean("error") == false) {
                            // token de transmissão enviada ao servidor
                            Intent registrationComplete = new Intent(Config.SENT_TOKEN_TO_SERVER);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
                        } else {
                            Toast.makeText(getApplicationContext(), "\n" +
                                    "Não foi possível enviar ID de registro GCM para o nosso server. " +
                                    obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("gcm_registration_id", token);

                    Log.e(TAG, "params: " + params.toString());
                    return params;
                }
            };
            MyApplication.getInstance().addToRequestQueue(strReq);
        }




    /**
     * Inscrever-se para um topico
     */
    public void subscribeToTopic(String topic) {
        GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
        String token = null;
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null) {
                pubSub.subscribe(token, "/topics/" + topic, null);
                Log.e(TAG, "Subscribed para topico: " + topic);
            } else {
                Log.e(TAG, "error: id gcm registration é null ");
            }
        } catch (IOException e) {
            Log.e(TAG, "Topico subscribe error. Topic: " + topic + ", error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void unsubscribeFromTopic(String topic) {
        GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
        String token = null;
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null) {
                pubSub.unsubscribe(token, "");
                Log.e(TAG, "Unsubscribed para topic: " + topic);
            } else {
                Log.e(TAG, "error: id gcm registration é null");
            }
        } catch (IOException e) {
            Log.e(TAG, "Topico unsubscribe error. Topic: " + topic + ", error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
