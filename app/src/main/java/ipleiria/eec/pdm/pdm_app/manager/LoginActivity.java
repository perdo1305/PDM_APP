package ipleiria.eec.pdm.pdm_app.manager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
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

import ipleiria.eec.pdm.pdm_app.R;

public class LoginActivity extends AppCompatActivity {
    EditText emailId, password;
    TextView tvSignUp;
    FirebaseAuth firebaseAuth;
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
            Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();


    }

    public void onClickTvSignUp(View view) {
        finish();
    }

    public void onClickBtnSignIn(View view) {
        String email = emailId.getText().toString();
        String pwd = password.getText().toString();
        if (email.isEmpty()) {
            emailId.setError("Please enter email id");
            emailId.requestFocus();
        } else if (pwd.isEmpty()) {
            password.setError("Please enter your password");
            password.requestFocus();
        } else if (pwd.length() < 6) {
            password.setError("The given password is invalid. Password should be at least 6 characters!");
            password.requestFocus();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email,
                    pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Error, Please Login  Again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "You are Logged in",
                                Toast.LENGTH_SHORT).show();
                        Intent intToHome = new Intent(LoginActivity.this, SignUpActivity.class);
                        startActivity(intToHome);
                        finish();
                    }
                }
            });
        }
    }
}