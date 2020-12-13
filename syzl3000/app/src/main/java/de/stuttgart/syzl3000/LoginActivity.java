package de.stuttgart.syzl3000;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button loginBtn;
    private static String email;
    private static String password;
    private static boolean redirectFromLoginActivity;
    private EncryptedPreferences encryptedPreferences;

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
        encryptedPreferences = new EncryptedPreferences.Builder(this).withEncryptionPassword("MyTestPassword").build();

        loginBtn.setOnClickListener(v -> loginBtnClicked());
    }

    private void loginBtnClicked() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        Toast.makeText(this, email+password, Toast.LENGTH_SHORT).show();
        encryptedPreferences.edit()
                .putString("email", email)
                .putString("pw", password)
                .apply();
        login(email, password);
    }

    private void login(String email, String password) {
        Amplify.Auth.signIn(
                email,
                password,
                result -> {
                    if (result.isSignInComplete()) {
                        Log.i(TAG, "Sign in succeeded");
                        rememberDevice();
                        Intent i = new Intent(LoginActivity.this, SelectTopCategoryActivity.class);
                        LoginActivity.this.startActivity(i);
                    } else {
                        Log.i(TAG,  "Sign in not complete");
                    }

                },
                error -> Log.e(TAG, error.toString())
        );
    }

    private void rememberDevice() {
        Amplify.Auth.rememberDevice(
                () -> Log.i(TAG, "Remember device succeeded"),
                error -> Log.e(TAG, "Remember device failed with error " + error.toString()));
    }

    public void startSignUpActivity(View view) {
        redirectFromLoginActivity = true;
        Log.i(TAG, "Starting SignUp Activity");
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        LoginActivity.this.startActivity(i);
    }
}
