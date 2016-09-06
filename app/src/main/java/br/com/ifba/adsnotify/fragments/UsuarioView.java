package br.com.ifba.adsnotify.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.activity.AuthenticatorActivity;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.model.User;


/**
 * Classe responsavel por mostrar informações de usuário
 * @Author Robson Coutinho
 * @version 1.0
 * @since 22/04/2016.
 */
public class UsuarioView extends Fragment {
    private static final String TAG = UsuarioView.class.getSimpleName();
    private AccountManager mAccountManager;
    private User user;
    private TextView txtNome;
    private TextView txtMatricula;
    private TextView txtEmail;
    private Button btn;
    private String emailuser;
    private HashMap<String, String> paramsn;


    public UsuarioView() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.usuario_layout, container, false);

        user = ((MyApplication) getActivity().getApplicationContext()).getUser();
        mAccountManager = AccountManager.get(getActivity().getApplicationContext());


        getAccounts(null);
        if (mAccountManager.getAccountsByType(Config.ACCOUNT_TYPE).length == 0) {
            getActivity().finish();
        }


        txtNome = (TextView) rootView.findViewById(R.id.uNome);
        txtEmail = (TextView) rootView.findViewById(R.id.uEmail);
        txtMatricula = (TextView) rootView.findViewById(R.id.uMatricula);
        btn = (Button) rootView.findViewById(R.id.uBtn);

        user = ((MyApplication) getActivity().getApplicationContext()).getUser();
        mAccountManager = AccountManager.get(getActivity());
        getAccounts(null);

        final Account[] accounts = mAccountManager.getAccountsByType(Config.ACCOUNT_TYPE);
        String[] name = new String[accounts.length];


        btn = (Button) rootView.findViewById(R.id.uBtn);

        paramsn = new HashMap<String, String>();
        for (int i = 0; i < accounts.length; i++) {
            name[i] = accounts[i].name;
            emailuser = accounts[i].name;
            Log.d("Nome Conta: ", name[i].toString());
        }

        paramsn.put("email", emailuser);
        Log.d(TAG, "USER EMAIL: " + emailuser);

        final ProgressBar bar = (ProgressBar)rootView.findViewById(R.id.progressid);
        bar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Config.INFO_USER,
                new JSONObject(paramsn),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "USER INFO: " + response.toString());
                        try {
                            String aluno = response.getString("aluno");
                            if( aluno.length() >0){
                                JSONObject jsonObject = new JSONObject(aluno);
                                String nome = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String matricula = response.getString("matricula");

                                TextView  textView = (TextView)rootView.findViewById(R.id.matricula);
                                textView.setVisibility(View.VISIBLE);
                                txtMatricula.setVisibility(View.VISIBLE);
                                txtNome.setText(nome);
                                txtEmail.setText(email);
                                txtMatricula.setText(matricula);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try{
                            if(!response.getString("admin").isEmpty()){
                                String admin = response.getString("admin");
                                JSONObject jsonObject = new JSONObject(admin);
                                String nome = jsonObject.getString("name");
                                String email = jsonObject.getString("email");

                                TextView  textView = (TextView)rootView.findViewById(R.id.matricula);
                                textView.setVisibility(View.VISIBLE);
                                textView.setText("Autorização:");
                                txtMatricula.setVisibility(View.VISIBLE);


                                txtNome.setText(nome);
                                txtEmail.setText(email);
                                txtMatricula.setText("Administrador");

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try{
                            if(!response.getString("professor").isEmpty()){
                                String admin = response.getString("admin");
                                JSONObject jsonObject = new JSONObject(admin);
                                String nome = jsonObject.getString("name");
                                String email = jsonObject.getString("email");

                                TextView  textView = (TextView)rootView.findViewById(R.id.matricula);
                                textView.setVisibility(View.VISIBLE);
                                textView.setText("Autorização:");
                                txtMatricula.setVisibility(View.VISIBLE);

                                txtNome.setText(nome);
                                txtEmail.setText(email);
                                txtMatricula.setText("Professor");


                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        bar.setVisibility(View.GONE);

                            }

                 }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERRO VOLLEY", error.toString());
                        bar.setVisibility(View.GONE);
                    }
        }

        );

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);


        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAccountManager = (AccountManager) getActivity().getSystemService(Context.ACCOUNT_SERVICE);
                Account[] accounts = mAccountManager.getAccounts();
                for (int index = 0; index < accounts.length; index++) {
                    if (accounts[index].type.intern() == Config.ACCOUNT_TYPE)
                        mAccountManager.removeAccount(accounts[index], null, null);
                }

                Intent it = new Intent(getActivity(), AuthenticatorActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
            }
            });


            return rootView;

        }

    public void getAccounts(View view){
        mAccountManager.getAuthTokenByFeatures(Config.ACCOUNT_TYPE,
                Config.ACCOUNT_TOKEN_TYPE,
                null,
                getActivity(),
                null,
                null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle bundle = future.getResult();
                            Log.i("Script", "MainActivity.getAccounts()");
                            Log.i("Script", "MainActivity.getAccounts() : AccountType = " + bundle.getString(AccountManager.KEY_ACCOUNT_TYPE));
                            Log.i("Script", "MainActivity.getAccounts() : AccountName = " + bundle.getString(AccountManager.KEY_ACCOUNT_NAME));
                            Log.i("Script", "MainActivity.getAccounts() : Token = " + bundle.getString(AccountManager.KEY_AUTHTOKEN));

                            user.setAccountType(bundle.getString(AccountManager.KEY_ACCOUNT_TYPE));
                            user.setAccountName(bundle.getString(AccountManager.KEY_ACCOUNT_NAME));
                            user.setToken(bundle.getString(AccountManager.KEY_AUTHTOKEN));

                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },null);
    }

}



