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

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthChannelEventName;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.InitializationStatus;
import com.amplifyframework.hub.HubChannel;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = SignUpActivity.class.getSimpleName();

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button signUpBtn;
    private static String email;
    private static String password;
    private String rememberErrorCause;
    private SharedPreferences sharedPref;

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

        if (!redirectFromLoginActivity()) {
            setUpAmplifyWithAuth();
        }

        sharedPref = getSharedPreferences("de.stuttgart.syzl3000.rem", Context.MODE_PRIVATE);
        email = sharedPref.getString("email", null);
        password = sharedPref.getString("pw", null);

        tryLogIn();

        try {
            SecretKey key = generateTrulyRandomAESKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        signUpBtn.setOnClickListener(v -> signUpBtnClicked());




    }

    private void tryLogIn() {
            Amplify.Auth.signIn(
                    email,
                    password,
                    result -> {
                        if (result.isSignInComplete()) {
                            Log.i(TAG, "Sign in succeeded");
                            rememberDevice();
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

    public static SecretKey generateTrulyRandomAESKey() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecureRandom secureRandom = new SecureRandom();
        // Do *not* seed secureRandom! Automatically seeded from system entropy.
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        return key;
//        Note that the security of this approach relies on safeguarding the generated key, which is is predicated on the security of the internal storage. Leaving the target file unencrypted (but set to MODE_PRIVATE) would provide similar security.
//        https://android-developers.googleblog.com/2013/02/using-cryptography-to-store-credentials.html
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
        saveInSharedPreferences(email, password);
        signUp(email, password);
    }

    private void saveInSharedPreferences(String email, String password) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.putString("pw", password);
        editor.apply();
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
                    Log.d(TAG, "signUp: "+error);
                    Log.e(TAG, "signUp: ", error);
                    rememberErrorCause = error.getCause().toString();
                }
        );
        if(rememberErrorCause != null) {
            if (isUsernameEmpty(rememberErrorCause)) {
                Toast.makeText(getBaseContext(), "USERNAME WAS EMPTY", Toast.LENGTH_SHORT).show();
            } else if (isUsernameWrong(rememberErrorCause)) {
                Toast.makeText(getBaseContext(), "USERNAME was wrong", Toast.LENGTH_SHORT).show();
            } else if (isEmailInvalid(rememberErrorCause)) {
                Toast.makeText(getBaseContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            } else if (isAlreadySignedUp(rememberErrorCause)) {
                Toast.makeText(getBaseContext(), "The given email already exists", Toast.LENGTH_SHORT).show();
            } else if (isPasswordTooShort(rememberErrorCause)) {
                Toast.makeText(getBaseContext(), "Password was to short", Toast.LENGTH_SHORT).show();
            } else if (isPasswordWrong(rememberErrorCause)) {
                Toast.makeText(getBaseContext(), "Password was wrong", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "Sign up failed due to unknown reasons", Toast.LENGTH_SHORT).show();
            }
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