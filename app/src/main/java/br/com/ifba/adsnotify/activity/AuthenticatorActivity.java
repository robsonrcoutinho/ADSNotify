package br.com.ifba.adsnotify.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.model.User;

/**
 * Created by Robson on 02/06/2016.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity implements View.OnClickListener {
    public static final String TAG = "AuthenticatorActivity";
    private AccountManager mAccountManager;
    private User user;
    private HashMap<String, String> paramsn;
    private EditText emailUsuario;
    private EditText senhaUsuario;


    public void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_login);
        accessViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, ".onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, ".onStop()");
    }


    // UTIL
    public void accessViews() {
        Log.i(TAG, ".accessViews()");
        //===
        paramsn = new HashMap<String,String>();
        user = ((MyApplication) getApplication()).getUser();
        user.setAccountType(getIntent().getStringExtra(Config.ARG_ACCOUNT_TYPE));
        user.setAccountName(getIntent().getStringExtra(Config.ARG_ACCOUNT_NAME));
        user.setAuthTokenType(getIntent().getStringExtra(Config.ARG_AUTH_TYPE));
        mAccountManager = AccountManager.get(AuthenticatorActivity.this);
        emailUsuario = (EditText) findViewById(R.id.etMatricula);
        senhaUsuario = (EditText) findViewById(R.id.etPasswordUsuario);
    }

    /**
     * Metodo responsavel por consultar webservice e verificar se usuario existe
     *
     * **/

    public void signInLocal(View view) {
        Log.i(TAG, ".signInLocal()");
        // findViewById(R.id.btnSignLocal).setEnabled(false);

        user.setEmail(emailUsuario.getText().toString());
        ;
        user.setSenha(senhaUsuario.getText().toString());
        user.setAuthTokenType(null);
        user.setToken(null);


        // testando login e token de sync
            Intent it = new Intent();
            it.putExtra(AccountManager.KEY_ACCOUNT_TYPE, user.getAccountType());
            it.putExtra(AccountManager.KEY_ACCOUNT_NAME, user.getEmail());
            it.putExtra(AccountManager.KEY_AUTHTOKEN, "dfadgsafhsghdfhnszdfbsdfgshthsrthsrthsrthsfghsfghfghsfghsfg");
            finish(it);

        ///



  /*      paramsn.put("matriculaUsuario", user.getEmail().toString());
        paramsn.put("senhaUsuario", user.getSenha().toString());
        paramsn.put("authTokenType", null);
        paramsn.put("token", null);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                Config.REQUEST_LOGIN,
                new JSONObject(paramsn),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Success Response: " + response.toString());
                        Intent it = new Intent();
                        try {
                            it.putExtra(AccountManager.KEY_ACCOUNT_TYPE, user.getAccountType());
                            it.putExtra(AccountManager.KEY_ACCOUNT_NAME, user.getEmail());
                            it.putExtra(AccountManager.KEY_AUTHTOKEN, response.getString("token"));
                            finish(it);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Mensagem de erro Volley: " + error.getMessage());
                        Toast.makeText(AuthenticatorActivity.this, "Erro Login: "+
                                "Dados de acesso incorretos !", Toast.LENGTH_LONG).show();
                    }
                }
        ) ;

        MyApplication.getInstance().addToRequestQueue(req);*/

    }

    public void finish(Intent it) {
        Log.i(TAG, ".finish()");
        String accountType = it.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
        String accountName = it.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String token = it.getStringExtra(AccountManager.KEY_AUTHTOKEN);
        Account account = new Account(accountName, accountType);
        int countAccounts = mAccountManager.getAccountsByType(accountType).length;

        if(token.equalsIgnoreCase("null")){
            Log.i(TAG,".finish() : if(token.equalsIgnoreCase(\"null\"))");
            Toast.makeText(AuthenticatorActivity.this, "Dados de acesso incorretos!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.btnSignLocal).setEnabled(true);
            return;
        }
        user.setAuthTokenType("full");
        mAccountManager.addAccountExplicitly(account, null, null);
        mAccountManager.setAuthToken(account, user.getAuthTokenType(), token);

        setAccountAuthenticatorResult(it.getExtras());
        finish();

        if(countAccounts == 0){
            startActivity(new Intent(AuthenticatorActivity.this, MainActivity.class));
        }

    }


    @Override
    public void onClick(View v) {

    }
}
