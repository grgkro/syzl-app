package de.stuttgart.syzl3000.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.results.Token;
import com.amplifyframework.core.Amplify;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import org.jetbrains.annotations.NotNull;

import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.SelectTopCategoryActivity;
import de.stuttgart.syzl3000.services.AuthService;

public class ConfirmFragment extends Fragment {

    private final String TAG = ConfirmFragment.class.getSimpleName();

    private EditText editTextConfirmationCode;
    private Button confirmSignUpBtn;
    private AuthService authService;
    private EncryptedPreferences encryptedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        editTextConfirmationCode = getView().findViewById(R.id.editTextConfirmationCode);
        confirmSignUpBtn = getView().findViewById(R.id.confirmBtn);
        encryptedPreferences = new EncryptedPreferences.Builder(getActivity()).withEncryptionPassword("MyTestPassword").build();
        authService = new AuthService(encryptedPreferences);
        Log.i(TAG, "onStart: ConfirmFragment started");

        editTextConfirmationCode.requestFocus();

        confirmSignUpBtn.setOnClickListener(v -> confirmSignUpBtnClicked());
    }

    private void confirmSignUpBtnClicked() {
        String email = SignUpActivity.getEmail();
        if (email == null) {
            email = getEmailFromSharedPreferences();
        }
        if (email != null && authService.isEmailValid(email)) {
            String confirmationCode = editTextConfirmationCode.getText().toString();
            confirmSignUp(email, confirmationCode);
        } else {
            Toast.makeText(getActivity().getBaseContext(), "Invalid Email address", Toast.LENGTH_SHORT).show();
        }

    }

    private String getEmailFromSharedPreferences() {

        return encryptedPreferences.getString("email", null);
    }

    private String getPasswordFromSharedPreferences() {
        return encryptedPreferences.getString("pw", null);
    }

    private void confirmSignUp(String email, String confirmationCode) {
        Amplify.Auth.confirmSignUp(
                email,
                confirmationCode,
                result -> {
                    Log.i(TAG, result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    rememberDevice();
                    login(email, getPasswordFromSharedPreferences());
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    private void rememberDevice() {
        Amplify.Auth.rememberDevice(
                () -> Log.i(TAG, "Remember device succeeded"),
                error -> Log.e(TAG, "Remember device failed with error " + error.toString()));
    }

    private void login(String email, String password) {
        Amplify.Auth.signIn(
                email,
                password,
                result -> {
                    if (result.isSignInComplete()) {
                        Log.i(TAG, "Sign in succeeded");
                        String idToken = getIDToken();
                        saveTokenInEncryptedSharedPreferences(idToken);
                        Intent i = new Intent(getActivity(), SelectTopCategoryActivity.class);
                        ConfirmFragment.this.startActivity(i);
                    } else {
                        Log.i(TAG,  "Sign in not complete");
                    }
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    private String getIDToken() {
        try {
            Token idToken = AWSMobileClient.getInstance().getTokens().getIdToken();
            Log.d(TAG, "getIDToken: " +  idToken.getTokenString());
            return idToken.getTokenString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveTokenInEncryptedSharedPreferences(String token) {
        encryptedPreferences.edit()
                .putString("idToken", token)
                .apply();
    }

    public void resendConfirmationCode(View v) {
        String email = SignUpActivity.getEmail();
        Amplify.Auth.resendSignUpCode(
                email,
                result -> {
                    Toast.makeText(getActivity().getBaseContext(), "Code has been resend", Toast.LENGTH_SHORT).show();
                    Log.i("Code resend", "Code resend");
                },
                error -> {
                    Log.e(TAG, error.toString());
                    if (isUsernameEmpty(error.getCause().toString())) {
                        Toast.makeText(getActivity().getBaseContext(), "USERNAME WAS EMPTY", Toast.LENGTH_SHORT).show();
                    } else if (isAlreadyConfirmed(error.getCause().toString())) {
                        Toast.makeText(getActivity().getBaseContext(), "This email is already confirmed.", Toast.LENGTH_SHORT).show();
                    } else if (isCodeEmpty(error.getCause().toString())) {
                        Toast.makeText(getActivity().getBaseContext(), "Please enter a code.", Toast.LENGTH_SHORT).show();
                    } else if (isCodeNotMatchingRegex(error.getCause().toString())) {
                        Toast.makeText(getActivity().getBaseContext(), "Please only enter numbers as code", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "Confirm sign up failed due to unknown reasons", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isUsernameEmpty(String cause) {
        if (cause.contains("Member must not be null")) {
            return true;
        }
        return false;
    }

    private boolean isCodeEmpty(String cause) {
        if (cause.contains("'confirmationCode' failed to satisfy constraint: Member must have length greater than or equal to 1")) {
            return true;
        }
        return false;
    }

    private boolean isCodeNotMatchingRegex(String cause) {
        if (cause.contains("'confirmationCode' failed to satisfy constraint: Member must satisfy regular expression pattern")) {
            return true;
        }
        return false;
    }


    private boolean isAlreadyConfirmed(String cause) {
        if (cause.contains("Current status is CONFIRMED")) {
            return true;
        }
        return false;
    }
}
