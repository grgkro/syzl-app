package de.stuttgart.syzl3000.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import de.stuttgart.syzl3000.CircleListActivity;
import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.SelectTopCategoryActivity;
import de.stuttgart.syzl3000.services.AuthService;

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = SignUpActivity.class.getSimpleName();

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button signUpBtn;
    private AuthService authService;
    private static String email;
    private static String password;
    private EncryptedPreferences encryptedPreferences;

    public static String getEmail() {
        return email;
    }

    public static String getPassword() {
        return password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        signUpBtn = findViewById(R.id.signUpBtn);
        authService = new AuthService();

        if (!redirectFromLoginActivity()) {
            setUpAmplifyWithAuth();
        }

        getCredentialsFromSharedPreferences();

//        tryLogIn();



            Amplify.Auth.signOut(
                    () -> {
                        Log.i("AuthQuickstart", "Signed out successfully");
                    },
                    error -> Log.e(TAG, error.toString())
            );


        signUpBtn.setOnClickListener(v -> signUpBtnClicked());
    }

    private void getCredentialsFromSharedPreferences() {
        encryptedPreferences = new EncryptedPreferences.Builder(this).withEncryptionPassword("MyTestPassword").build();
        email = encryptedPreferences.getString("email", null);
        password = encryptedPreferences.getString("pw", null);
    }

    private void tryLogIn() {
            Amplify.Auth.signIn(
                    email,
                    password,
                    result -> {
                        if (result.isSignInComplete()) {
                            Log.i(TAG, "Sign in succeeded");
                            startTheNextActivity(SelectTopCategoryActivity.class);
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

    private boolean redirectFromLoginActivity() {
        return LoginActivity.getRedirectFromLoginActivity();
    }

    private void setUpAmplifyWithAuth() {
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Log.d(TAG, "onCreate: plugin added");
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
            Log.d(TAG, "onCreate: configured");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }

    private void signUpBtnClicked() {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        if (authService.isEmailValid(email)) {
            saveEncryptedSharedPreferences(email, password);
            signUp(email, password);
        } else {
            Toast.makeText(getBaseContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEncryptedSharedPreferences(String email, String password) {
        encryptedPreferences.edit()
                .putString("email", email)
                .putString("pw", password)
                .apply();
    }

    private void signUp(String email, String password) {
        Amplify.Auth.signUp(
                email,
                password,
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email).build(),
                result -> {
                    Log.i("AuthQuickStart", "Result: " + result.toString());
                    startTheNextActivity(ConfirmActivity.class);
                },
                error -> {
                    Log.e(TAG, "signUp: ", error);
                    tellUserWhatWentWrong(error.getCause().toString());
                }
        );
    }

    private void tellUserWhatWentWrong(String cause) {
        if (isUsernameEmpty(cause)) {
            Toast.makeText(getBaseContext(), "USERNAME WAS EMPTY", Toast.LENGTH_SHORT).show();
        } else if (isUsernameWrong(cause)) {
            Toast.makeText(getBaseContext(), "USERNAME was wrong", Toast.LENGTH_SHORT).show();
        } else if (isEmailInvalid(cause)) {
            Toast.makeText(getBaseContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
        } else if (isAlreadySignedUp(cause)) {
            Toast.makeText(getBaseContext(), "The given email already exists", Toast.LENGTH_SHORT).show();
        } else if (isPasswordTooShort(cause)) {
            Toast.makeText(getBaseContext(), "Password was to short", Toast.LENGTH_SHORT).show();
        } else if (isPasswordWrong(cause)) {
            Toast.makeText(getBaseContext(), "Password was wrong", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Sign up failed due to unknown reasons", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUsernameEmpty(String cause) {
        if (cause.contains("'username' failed to satisfy constraint: Member must have length greater than or equal to 1")) {
            return true;
        }
        return false;
    }

    private boolean isUsernameWrong(String cause) {
        if (cause.contains("'username' failed to satisfy constraint: Member must satisfy regular expression pattern")) {
            return true;
        }
        return false;
    }

    private boolean isAlreadySignedUp(String cause) {
        if (cause.contains("email already exists")) {
            return true;
        }
        return false;
    }

    private boolean isEmailInvalid(String cause) {
        if (cause.contains("Invalid email address")) {
            return true;
        }
        return false;
    }

    private boolean isPasswordTooShort(String cause) {
        if (cause.contains("'password' failed to satisfy constraint: Member must have length greater than or equal to") || cause.contains("Password did not conform with policy: Password not long enough")) {
            return true;
        }
        return false;
    }

    private boolean isPasswordWrong(String cause) {
        if (cause.contains("'password' failed to satisfy constraint: Member must satisfy regular expression pattern")) {
            return true;
        }
        return false;
    }

    private void startTheNextActivity(Class target) {
        Log.i(TAG, "Starting Next Activity");
        Intent i = new Intent(SignUpActivity.this, target);
        SignUpActivity.this.startActivity(i);
    }

    public void startLoginActivity(View view) {
        Log.i(TAG, "Starting Login Activity");
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        SignUpActivity.this.startActivity(i);
    }

    public void startConfirmActivity(View view) {
        Log.i(TAG, "Starting Confirm Activity");
        Intent i = new Intent(SignUpActivity.this, ConfirmActivity.class);
        SignUpActivity.this.startActivity(i);
    }
}
