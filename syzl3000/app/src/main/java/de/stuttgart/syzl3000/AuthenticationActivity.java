package de.stuttgart.syzl3000;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class AuthenticationActivity extends AppCompatActivity {

    private final String TAG = AuthenticationActivity.class.getSimpleName();

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button signUpBtn;
    private static String email;
    private static String password;

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

        subscribeAmplifyHub();

        signUpBtn.setOnClickListener(v -> signUpBtnClicked());

        if (!redirectFromLoginActivity()) {
            setUpAmplifyWithAuth();
        }


        signOut();
        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );
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

    private void subscribeAmplifyHub() {
        Amplify.Hub.subscribe(HubChannel.AUTH,
                hubEvent -> {
                    if (hubEvent.getName().equals(InitializationStatus.SUCCEEDED.toString())) {
                        Log.i("AuthQuickstart", "Auth successfully initialized");
                    } else if (hubEvent.getName().equals(InitializationStatus.FAILED.toString())){
                        Log.i("AuthQuickstart", "Auth failed to succeed");
                    } else {
                        switch (AuthChannelEventName.valueOf(hubEvent.getName())) {
                            case SIGNED_IN:
                                Log.i("AuthQuickstart", "Auth just became signed in.");
                                amplifyFetchUserAttributes();
                                break;
                            case SIGNED_OUT:
                                Log.i("AuthQuickstart", "Auth just became signed out.");
                                break;
                            case SESSION_EXPIRED:
                                Log.i("AuthQuickstart", "Auth session just expired.");
                                break;
                            default:
                                Log.w("AuthQuickstart", "Unhandled Auth Event: " + AuthChannelEventName.valueOf(hubEvent.getName()));
                                break;
                        }
                    }
                }
        );
    }

    private void amplifyFetchUserAttributes() {
        Amplify.Auth.fetchUserAttributes(
                attributes -> Log.i("AuthDemo", "User attributes = " + attributes.toString()),
                error -> Log.e("AuthDemo", "Failed to fetch user attributes.", error)
        );
    }

    private void signUpBtnClicked() {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        Toast.makeText(this, email+password, Toast.LENGTH_SHORT).show();
        signUp(email, password);
    }

    private void signUp(String email, String password) {
        Amplify.Auth.signUp(
                email,
                password,
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email).build(),
                result -> {
                    Log.i("AuthQuickStart", "Result: " + result.toString());
                    startConfirmActivity();
                },
                error -> Log.e("AuthQuickStart", "Sign up failed", error)
        );
    }

    private void startConfirmActivity() {
        Log.i(TAG, "Starting Confirm Activity");
        Intent i = new Intent(AuthenticationActivity.this, ConfirmActivity.class);
        AuthenticationActivity.this.startActivity(i);
    }

    public void startLoginActivity(View view) {
        Log.i(TAG, "Starting Login Activity");
        Intent i = new Intent(AuthenticationActivity.this, LoginActivity.class);
        AuthenticationActivity.this.startActivity(i);
    }

    private void signOut() {
        Amplify.Auth.signOut(
                () -> Log.i("AuthQuickstart", "Signed out successfully"),
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }
}
