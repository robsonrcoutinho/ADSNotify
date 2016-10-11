package br.com.ifba.adsnotify.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
 * Classe usada na autenticação do usuario com o aplicativo
 * @Author Robson Coutinho
 * @version 1.0
 * @since 02/06/2016.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    public static final String TAG = "AuthenticatorActivity";
    private AccountManager mAccountManager;
    private User user;
    private HashMap<String, String> paramsn;
    private EditText emailUsuario;
    private EditText senhaUsuario;
    private ProgressDialog pDialog;
    private Button btnSignLocal;
    private TextView signupLink;
    private static final int REQUEST_SIGNUP = 0;

    public void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.login);
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


    /*
    * Metodo usado para iniciação de elementos gráficos do aplicativo
    * */
    public void accessViews() {
        Log.i(TAG, ".accessViews()");
        //===
        paramsn = new HashMap<String,String>();
        user = ((MyApplication) getApplication()).getUser();
        user.setAccountType(getIntent().getStringExtra(Config.ARG_ACCOUNT_TYPE));
        user.setAccountName(getIntent().getStringExtra(Config.ARG_ACCOUNT_NAME));
        user.setAuthTokenType(getIntent().getStringExtra(Config.ARG_AUTH_TYPE));

        mAccountManager = AccountManager.get(AuthenticatorActivity.this);
        emailUsuario = (EditText) findViewById(R.id.etEmail);
        senhaUsuario = (EditText) findViewById(R.id.etPasswordUsuario);

        btnSignLocal = (Button) findViewById(R.id.btnSignLocal);
        btnSignLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInLocal();
            }
        });

       /* signupLink = (TextView) findViewById(R.id.idSignup);
        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });*/
    }

    /**
     * Metodo responsavel por consultar webservice e verificar se usuario existe e logar no app
     *
     **/
    public void signInLocal() {
        Log.i(TAG, ".signInLocal()");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        btnSignLocal.setEnabled(false);

        pDialog = new ProgressDialog(AuthenticatorActivity.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Autenticando...");
        pDialog.setCancelable(false);
        pDialog.show();


        user = new User();

        user.setEmail(emailUsuario.getText().toString());
        user.setSenha(senhaUsuario.getText().toString());
        user.setAccountType(Config.ACCOUNT_TYPE);

        paramsn.put("email", user.getEmail().toString());
        paramsn.put("password", user.getSenha().toString());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                Config.REQUEST_LOGIN,
                new JSONObject(paramsn),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Success Response: " + response.toString());
                        hidePDialog();
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
                        onLoginFailed();
                    }
                }
        ) ;

        MyApplication.getInstance().addToRequestQueue(req);

    }

    /*
    * Metodo para fazer tratamento das informações repassadas pelo servidos
    * Se a autenticação estiver correta, usuário será encaminhado para MainActivity.
    * */
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

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Dados de acesso incorretos!", Toast.LENGTH_LONG).show();
        hidePDialog();
        btnSignLocal.setEnabled(true);
    }

    /*
    * Metodo usado para validação de campos de email e senha
    * */
    public boolean validate() {
        boolean valid = true;

        String email = emailUsuario.getText().toString();
        String password = senhaUsuario.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailUsuario.setError("Digite um endereço de e-mail válido");
            valid = false;
        } else {
            emailUsuario.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            senhaUsuario.setError("Entre 4 e 10 caracteres alfanuméricos");
            valid = false;
        } else {
            senhaUsuario.setError(null);
        }

        return valid;
    }
}
