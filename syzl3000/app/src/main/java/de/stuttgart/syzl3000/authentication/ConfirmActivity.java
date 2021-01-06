package de.stuttgart.syzl3000.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.results.Token;
import com.amplifyframework.core.Amplify;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.SelectTopCategoryActivity;
import de.stuttgart.syzl3000.services.AuthService;

public class ConfirmActivity extends AppCompatActivity {

    private final String TAG = ConfirmActivity.class.getSimpleName();

    private EditText editTextConfirmationCode;
    private Button confirmSignUpBtn;
    private AuthService authService;
    private EncryptedPreferences encryptedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
//        editTextConfirmationCode = findViewById(R.id.editTextConfirmationCode);
//        confirmSignUpBtn = findViewById(R.id.confirmBtn);
//        encryptedPreferences = new EncryptedPreferences.Builder(this).withEncryptionPassword("MyTestPassword").build();
//        authService = new AuthService(encryptedPreferences);
//
//        editTextConfirmationCode.requestFocus();
//
//        confirmSignUpBtn.setOnClickListener(v -> confirmSignUpBtnClicked());
    }

//    @Override
//    public void onBackPressed() {
//
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }
//
//    }

//    private void confirmSignUpBtnClicked() {
//        String email = SignUpActivity.getEmail();
//        if (email == null) {
//            email = getEmailFromSharedPreferences();
//        }
//        if (email != null && authService.isEmailValid(email)) {
//            String confirmationCode = editTextConfirmationCode.getText().toString();
//            confirmSignUp(email, confirmationCode);
//        } else {
//            Toast.makeText(getBaseContext(), "Invalid Email address", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    private String getEmailFromSharedPreferences() {
//
//        return encryptedPreferences.getString("email", null);
//    }
//
//    private String getPasswordFromSharedPreferences() {
//        return encryptedPreferences.getString("pw", null);
//    }

//    private void confirmSignUp(String email, String confirmationCode) {
//        Amplify.Auth.confirmSignUp(
//                email,
//                confirmationCode,
//                result -> {
//                    Log.i(TAG, result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
//                    rememberDevice();
//                    login(email, getPasswordFromSharedPreferences());
//                },
//                error -> Log.e(TAG, error.toString())
//        );
//    }

//    private void rememberDevice() {
//        Amplify.Auth.rememberDevice(
//                () -> Log.i(TAG, "Remember device succeeded"),
//                error -> Log.e(TAG, "Remember device failed with error " + error.toString()));
//    }
//
//    private void login(String email, String password) {
//        Amplify.Auth.signIn(
//                email,
//                password,
//                result -> {
//                    if (result.isSignInComplete()) {
//                        Log.i(TAG, "Sign in succeeded");
//                        String idToken = getIDToken();
//                        saveTokenInEncryptedSharedPreferences(idToken);
//                        Intent i = new Intent(ConfirmActivity.this, SelectTopCategoryActivity.class);
//                        ConfirmActivity.this.startActivity(i);
//                    } else {
//                        Log.i(TAG,  "Sign in not complete");
//                    }
//                },
//                error -> Log.e(TAG, error.toString())
//        );
//    }
//
//    private String getIDToken() {
//        try {
//            Token idToken = AWSMobileClient.getInstance().getTokens().getIdToken();
//            Log.d(TAG, "getIDToken: " +  idToken.getTokenString());
//            return idToken.getTokenString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private void saveTokenInEncryptedSharedPreferences(String token) {
//        encryptedPreferences.edit()
//                .putString("idToken", token)
//                .apply();
//    }
//
//    public void resendConfirmationCode(View v) {
//        String email = SignUpActivity.getEmail();
//        Amplify.Auth.resendSignUpCode(
//                email,
//                result -> {
//                    Toast.makeText(getBaseContext(), "Code has been resend", Toast.LENGTH_SHORT).show();
//                    Log.i("Code resend", "Code resend");
//                },
//                error -> {
//                    Log.e(TAG, error.toString());
//                    if (isUsernameEmpty(error.getCause().toString())) {
//                        Toast.makeText(getBaseContext(), "USERNAME WAS EMPTY", Toast.LENGTH_SHORT).show();
//                    } else if (isAlreadyConfirmed(error.getCause().toString())) {
//                        Toast.makeText(getBaseContext(), "This email is already confirmed.", Toast.LENGTH_SHORT).show();
//                    } else if (isCodeEmpty(error.getCause().toString())) {
//                        Toast.makeText(getBaseContext(), "Please enter a code.", Toast.LENGTH_SHORT).show();
//                    } else if (isCodeNotMatchingRegex(error.getCause().toString())) {
//                        Toast.makeText(getBaseContext(), "Please only enter numbers as code", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getBaseContext(), "Confirm sign up failed due to unknown reasons", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private boolean isUsernameEmpty(String cause) {
//        if (cause.contains("Member must not be null")) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean isCodeEmpty(String cause) {
//        if (cause.contains("'confirmationCode' failed to satisfy constraint: Member must have length greater than or equal to 1")) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean isCodeNotMatchingRegex(String cause) {
//        if (cause.contains("'confirmationCode' failed to satisfy constraint: Member must satisfy regular expression pattern")) {
//            return true;
//        }
//        return false;
//    }
//
//
//    private boolean isAlreadyConfirmed(String cause) {
//        if (cause.contains("Current status is CONFIRMED")) {
//            return true;
//        }
//        return false;
//    }
}
