package br.com.ifba.adsnotify.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.app.MyApplication;

/**
 * Created by Robson on 25/05/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private TextInputLayout textInputMat, TextInputSenha;
    private EditText inputMatricula;
    private EditText inputSenha;
    private Button buttonLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*Verifique se há sessão de login . É usuário já estiver logado
          Redirecioná-lo à atividade principal*/
        if (MyApplication.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, MainActivity.class));

        }

        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textInputMat = (TextInputLayout) findViewById(R.id.input_layout_matricula);
        textInputMat = (TextInputLayout) findViewById(R.id.input_layout_senha);

        inputMatricula = (EditText) findViewById(R.id.input_matricula);
        inputSenha = (EditText) findViewById(R.id.input_senha);

        inputMatricula.addTextChangedListener(new MyTextWatcher(inputMatricula));
        inputSenha.addTextChangedListener(new MyTextWatcher(inputSenha));

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_matricula:

                    break;
                case R.id.input_senha:

                    break;
            }
        }
    }




    }

