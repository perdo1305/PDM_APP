package ipleiria.eec.pdm.pdm_app.manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import ipleiria.eec.pdm.pdm_app.MainActivity;
import ipleiria.eec.pdm.pdm_app.R;

/**
 * Atividade de inscrição para o aplicativo.
 */
public class SignUpActivity extends AppCompatActivity {
    EditText emailId, password, password2;
    FirebaseAuth firebaseAuth;

    /**
     * Chamado quando a atividade é criada.
     *
     * @param savedInstanceState o estado salvo da instância
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        password2 = findViewById(R.id.editTextPassword2);
        getSupportActionBar().hide();

        if (firebaseAuth.getCurrentUser() == null)
            Toast.makeText(this, R.string.please_register, Toast.LENGTH_SHORT).show();
        else
        {
            Toast.makeText(this, R.string.you_are_logged_in_, Toast.LENGTH_SHORT).show();
            Intent intToHome = new Intent(this, MainActivity.class);
            startActivity(intToHome);
        }

    }

    /**
     * Manipula o clique no botão de inscrição.
     *
     * @param view a view que foi clicada
     */
    public void onClickBtnSignUp(View view) {
        String email = emailId.getText().toString();
        String pwd = password.getText().toString();
        String pwd2 = password2.getText().toString();
        if (email.isEmpty()) {
            emailId.setError(getString(R.string.please_enter_email_id_));
            emailId.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailId.setError(getString(R.string.please_enter_a_valid_email));
            emailId.requestFocus();
        } else if (pwd.isEmpty()) {
            password.setError(getString(R.string.please_enter_your_password_));
            password.requestFocus();
        } else if (pwd.length() < 6) {
            password.setError(getString(R.string.password_should_be_at_least_6_characters));
            password.requestFocus();
        } else if (pwd2.isEmpty()) {
            password2.setError(getString(R.string.please_confirm_your_password));
            password2.requestFocus();
        } else if (!pwd.equals(pwd2)) {
            Toast.makeText(this, R.string.passwords_dont_match, Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email,
                    pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        if (task.getException() != null && task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUpActivity.this, R.string.the_email_adress_is_already_in_use_by_another_account, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(SignUpActivity.this, R.string.signup_unsuccessful_please_try_again, Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
            });
        }
    }

    /**
     * Manipula o clique no TextView de login.
     *
     * @param view a view que foi clicada
     */
    public void onClickTvSignIn(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}