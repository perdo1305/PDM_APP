package ipleiria.eec.pdm.pdm_app.manager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ipleiria.eec.pdm.pdm_app.R;

/**
 * Atividade de login para autenticaçao
 */
public class LoginActivity extends AppCompatActivity {
    EditText emailId, password;
    TextView tvSignUp;
    FirebaseAuth firebaseAuth;

    /**
     * Chamado quando a atividade é criada.
     *
     * @param savedInstanceState o estado salvo da instância
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        tvSignUp = findViewById(R.id.textView);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color));
        }

        if (firebaseAuth.getCurrentUser() == null)
            Toast.makeText(this, R.string.please_login, Toast.LENGTH_SHORT).show();


    }

    /**
     * Manipula o clique no TextView de inscrição.
     *
     * @param view a view que foi clicada
     */
    public void onClickTvSignUp(View view) {
        finish();
    }

    /**
     * Manipula o clique no botão de login.
     *
     * @param view a view que foi clicada
     */
    public void onClickBtnSignIn(View view) {
        String email = emailId.getText().toString();
        String pwd = password.getText().toString();
        if (email.isEmpty()) {
            emailId.setError(getString(R.string.please_enter_email_id));
            emailId.requestFocus();
        } else if (pwd.isEmpty()) {
            password.setError(getString(R.string.please_enter_your_password));
            password.requestFocus();
        } else if (pwd.length() < 6) {
            password.setError(getString(R.string.the_given_password_is_invalid_password_should_be_at_least_6_characters));
            password.requestFocus();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email,
                    pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, R.string.login_error_please_login_again, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.you_are_logged_in, Toast.LENGTH_SHORT).show();
                        Intent intToHome = new Intent(LoginActivity.this, SignUpActivity.class);
                        startActivity(intToHome);
                        finish();
                    }
                }
            });
        }
    }
}