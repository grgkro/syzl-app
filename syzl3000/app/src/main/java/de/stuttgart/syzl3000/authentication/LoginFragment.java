package de.stuttgart.syzl3000.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.results.Token;
import com.amplifyframework.core.Amplify;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.SelectTopCategoryActivity;
import de.stuttgart.syzl3000.services.AuthService;

public class LoginFragment extends Fragment {

    private final String TAG = LoginFragment.class.getSimpleName();

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button loginBtn;
    private TextView newAccountTextView;
    private TextView forgotPwTextView;
    private static String email;
    private static String password;
    private AuthService authService;
    private EncryptedPreferences encryptedPreferences;

    public static String getEmail() {
        return email;
    }
    public static String getPassword() {
        return password;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        editTextEmail = getView().findViewById(R.id.editTextEmail);
        editTextPassword = getView().findViewById(R.id.editTextPassword);
        loginBtn = getView().findViewById(R.id.loginBtn);
        newAccountTextView = getView().findViewById(R.id.newAccountTextView);
        forgotPwTextView = getView().findViewById(R.id.forgotPwTextView);
        encryptedPreferences = new EncryptedPreferences.Builder(getActivity()).withEncryptionPassword("MyTestPassword").build();
        authService = new AuthService(encryptedPreferences);

        editTextEmail.requestFocus();
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    loginBtnClicked();
                    handled = true;
                }
                return handled;
            }
        });

        loginBtn.setOnClickListener(v -> loginBtnClicked());
        newAccountTextView.setOnClickListener(v -> {
            Log.i(TAG, "Starting SignUp Fragment");
            Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment).navigate(R.id.viewSignUp);
        });
        forgotPwTextView.setOnClickListener(v -> {
            Log.i(TAG, "Starting ForgotPassword Fragment");
            Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment).navigate(R.id.viewForgotPw);
        });
    }

    private void loginBtnClicked() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (email != null && authService.isEmailValid(email)) {
            encryptedPreferences.edit()
                    .putString("email", email)
                    .putString("pw", password)
                    .apply();
            login(email, password);
        } else {
            Toast.makeText(getActivity(), "Invalid email address", Toast.LENGTH_SHORT).show();
        }

    }

    private void login(String email, String password) {
        Amplify.Auth.signIn(
                email,
                password,
                result -> {
                    if (result.isSignInComplete()) {
                        Log.i(TAG, "Sign in succeeded");
                        rememberDevice();
                        String idToken = getIDToken();
                        Log.i(TAG, "login TOKEN (remove this line later): " + idToken);
                        saveEncryptedSharedPreferences(email, password, idToken);
                        Intent intent = new Intent(getActivity(), SelectTopCategoryActivity.class);
                        intent.putExtra("isRedirect", true);
                        LoginFragment.this.startActivity(intent);
                    } else {
                        Log.i(TAG,  "Sign in not complete");
                    }

                },
                error -> {

                    Log.e(TAG, error.toString());
                    Log.i(TAG, "tellUserWhatWentWrong: " + error.getRecoverySuggestion());
            //Toast.makeText(getBaseContext(), error.getRecoverySuggestion(), Toast.LENGTH_SHORT).show();
                }
        );
    }

    private String getIDToken() {
        try {
            Token idToken = AWSMobileClient.getInstance().getTokens().getIdToken();
            Log.i(TAG, "getIDToken: " +  idToken.getTokenString());
            return idToken.getTokenString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveEncryptedSharedPreferences(String email, String password, String idToken) {
        encryptedPreferences.edit()
                .putString("email", email)
                .putString("pw", password)
                .putString("idToken", idToken)
                .apply();
    }

    private void rememberDevice() {
        Amplify.Auth.rememberDevice(
                () -> Log.i(TAG, "Remember device succeeded"),
                error -> Log.e(TAG, "Remember device failed with error " + error.toString()));
    }
}
