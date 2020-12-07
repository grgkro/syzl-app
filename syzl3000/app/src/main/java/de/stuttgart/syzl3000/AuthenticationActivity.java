package de.stuttgart.syzl3000;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.BlockingDeque;

public class AuthenticationActivity extends AppCompatActivity {

    private final String TAG = AuthenticationActivity.class.getSimpleName();

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtnClicked();
            }
        });

//        try {
//            Amplify.addPlugin(new AWSCognitoAuthPlugin());
//            Log.d(TAG, "onCreate: plugin added");
//            Amplify.configure(getApplicationContext());
//            Log.i("MyAmplifyApp", "Initialized Amplify");
//            Log.d(TAG, "onCreate: configured");
//
//
////            Amplify.Auth.confirmSignUp(
////                    "username",
////                    "the code you received via email",
////                    result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
////                    error -> Log.e("AuthQuickstart", error.toString())
////            );
//        } catch (AmplifyException error) {
//            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
//        }
//
//        signOut();
//        Amplify.Auth.fetchAuthSession(
//                result -> Log.i("AmplifyQuickstart", result.toString()),
//                error -> Log.e("AmplifyQuickstart", error.toString())
//        );


        AWSMobileClient.getInstance().signOut();
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i(TAG, userStateDetails.getUserState().toString());
                switch (userStateDetails.getUserState()){
                    case SIGNED_IN:
                        Log.d(TAG, "onResult: Login successful");
                        Intent i = new Intent(AuthenticationActivity.this, SelectTopCategoryActivity.class);
                        AuthenticationActivity.this.startActivity(i);
                        break;
                    case SIGNED_OUT:
                        showSignIn();
                        break;
                    default:
                        AWSMobileClient.getInstance().signOut();
                        showSignIn();
                        break;
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "onError: error");
                Log.e(TAG, e.toString());
            }
        });
    }


    private void loginBtnClicked() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        Toast.makeText(this, email+password, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "awesomeButtonClicked: " + email + password);
        signUp(email, password);
    }

    private void signUp(String email, String password) {
        Amplify.Auth.signUp(
                email,
                password,
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email).build(),
                result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                error -> Log.e("AuthQuickStart", "Sign up failed", error)
        );
    }

    private void showSignIn() {
        try {
            AWSMobileClient.getInstance().showSignIn(this,
                    SignInUIOptions.builder().nextActivity(SelectTopCategoryActivity.class).build(),
                    new Callback<UserStateDetails>() {
                        @Override
                        public void onResult(UserStateDetails result) {
                            Log.d(TAG, "onResult: " + result.getUserState());
                            switch (result.getUserState()) {
                                case SIGNED_IN:
                                    Log.i("INIT", "logged in!");
                                    break;
                                case SIGNED_OUT:
                                    Log.i(TAG, "onResult: User did not choose to sign-in");
                                    break;
                                default:
                                    AWSMobileClient.getInstance().signOut();
                                    break;
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "onError: ", e);
                        }
                    }
                    );
        } catch (Exception e) {
            Log.d(TAG, "showSignIn: failed");
            Log.e(TAG, e.toString());
        }
    }

    private void signOut() {
        Amplify.Auth.signOut(
                () -> Log.i("AuthQuickstart", "Signed out successfully"),
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }
}
