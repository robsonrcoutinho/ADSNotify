package br.com.ifba.adsnotify.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.model.User;

/**
 * Created by Robson on 15/07/2016.
 */
public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private EditText inputNome;
    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnSignup;
    private TextView loginLink;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        inputNome = (EditText)findViewById(R.id.input_name);
        inputEmail = (EditText)findViewById(R.id.input_email);
        inputPassword = (EditText)findViewById(R.id.input_password);
        btnSignup =  (Button)findViewById(R.id.btn_signup);
        loginLink = (TextView)findViewById(R.id.link_login);
        user = new User();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Termine a tela de registro e volte à atividade de login
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnSignup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Criando Conta...");
        progressDialog.show();

       /*Logica com web service*/
    }


    public void onSignupSuccess() {
        btnSignup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnSignup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        user.setNome(inputNome.getText().toString());
        user.setEmail(inputEmail.getText().toString());
        user.setSenha(inputPassword.getText().toString());

        if (user.getNome().isEmpty() || user.getNome().length() < 3) {
            inputNome.setError("pelo menos 3 caracteres");
            valid = false;
        } else {
            inputNome.setError(null);
        }

        if (user.getEmail().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(user.getEmail().toString()).matches()) {
            inputEmail.setError("Digite um endereço de e-mail válido");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (user.getSenha().isEmpty() || user.getSenha().length() < 4 ||user.getSenha().length() > 10) {
            inputPassword.setError("Entre 4 e 10 caracteres alfanuméricos");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }
}
