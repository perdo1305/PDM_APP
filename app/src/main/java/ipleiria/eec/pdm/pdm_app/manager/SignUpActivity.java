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

public class SignUpActivity extends AppCompatActivity {
    EditText emailId, password, password2;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        password2 = findViewById(R.id.editTextPassword2);

        if (firebaseAuth.getCurrentUser() == null)
            Toast.makeText(this, "Please register", Toast.LENGTH_SHORT).show();
        else
        {
            Toast.makeText(this, "You are Logged in", Toast.LENGTH_SHORT).show();
            Intent intToHome = new Intent(this, MainActivity.class);
            startActivity(intToHome);
        }

    }

    public void onClickBtnSignUp(View view) {
        String email = emailId.getText().toString();
        String pwd = password.getText().toString();
        String pwd2 = password2.getText().toString();
        if (email.isEmpty()) {
            emailId.setError("Please enter email id");
            emailId.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailId.setError("Please enter a valid email");
            emailId.requestFocus();
        } else if (pwd.isEmpty()) {
            password.setError("Please enter your password");
            password.requestFocus();
        } else if (pwd.length() < 6) {
            password.setError("Password should be at least 6 characters!");
            password.requestFocus();
        } else if (pwd2.isEmpty()) {
            password2.setError("Please confirm your password");
            password2.requestFocus();
        } else if (!pwd.equals(pwd2)) {
            Toast.makeText(this, "Passwords dont match!", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email,
                    pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        if (task.getException() != null && task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUpActivity.this, "The email adress is already in use by another account!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(SignUpActivity.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
            });
        }
    }

    public void onClickTvSignIn(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}