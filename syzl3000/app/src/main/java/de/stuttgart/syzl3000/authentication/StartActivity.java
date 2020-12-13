package de.stuttgart.syzl3000.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;

import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.SelectTopCategoryActivity;

public class StartActivity extends AppCompatActivity {

    private final String TAG = StartActivity.class.getSimpleName();

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button loginBtn;
    private static String email;
    private static String password;
    private static boolean redirectFromLoginActivity;

    public static String getEmail() {
        return email;
    }
    public static String getPassword() {
        return password;
    }
    public static boolean getRedirectFromLoginActivity() {
        return redirectFromLoginActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> loginBtnClicked());
    }

    private void loginBtnClicked() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        Toast.makeText(this, email+password, Toast.LENGTH_SHORT).show();
        login(email, password);
    }

    private void login(String email, String password) {
        Amplify.Auth.signIn(
                email,
                password,
                result -> {
                    if (result.isSignInComplete()) {
                        Log.i(TAG, "Sign in succeeded");
                        Intent i = new Intent(StartActivity.this, SelectTopCategoryActivity.class);
                        StartActivity.this.startActivity(i);
                    } else {
                        Log.i(TAG,  "Sign in not complete");
                    }

                },
                error -> Log.e(TAG, error.toString())
        );
    }

    public void startSignUpActivity(View view) {
        redirectFromLoginActivity = true;
        Log.i(TAG, "Starting SignUp Activity");
        Intent i = new Intent(StartActivity.this, SignUpActivity.class);
        StartActivity.this.startActivity(i);
    }
}
