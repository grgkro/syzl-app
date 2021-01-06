package de.stuttgart.syzl3000.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class NewPwFragment extends Fragment {

    private final String TAG = NewPwFragment.class.getSimpleName();

    private EditText editTextCode;
    private EditText editTextPassword;
    private Button changePwBtn;
    private TextView newAccountTextView;
    private TextView helpTextView;
    private EncryptedPreferences encryptedPreferences;
private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_pw, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        editTextCode = getView().findViewById(R.id.editTextCode);
        editTextPassword = getView().findViewById(R.id.editTextPassword);
        changePwBtn = getView().findViewById(R.id.changePwBtn);
        newAccountTextView = getView().findViewById(R.id.newAccountTextView);
        helpTextView = getView().findViewById(R.id.helpTextView);
        encryptedPreferences = new EncryptedPreferences.Builder(getActivity()).withEncryptionPassword("MyTestPassword").build();
        email = encryptedPreferences.getString("email", null);

        changePwBtn.setOnClickListener(v -> changePwBtnClicked());
        newAccountTextView.setOnClickListener(v -> {
            Log.i(TAG, "Starting SignUp Fragment");
            Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment).navigate(R.id.viewSignUp);
        });
        helpTextView.setOnClickListener(v -> {
            Log.i(TAG, "helpTextView clicked");
            Toast.makeText(getActivity(), "We sent a request code to " + email + ". Please wait a few minutes and check your Spam folder.", Toast.LENGTH_LONG).show();
        });

    }

    private void changePwBtnClicked() {
        String code = editTextCode.getText().toString();
        String password = editTextPassword.getText().toString();
        Amplify.Auth.confirmResetPassword(
                password,
                code,
                () -> {
                    Log.i("AuthQuickstart", "New password confirmed");
                    Toast.makeText(getActivity(), "New password confirmed", Toast.LENGTH_SHORT).show();
                    login(email, password);
                },
                error -> {
                    Log.e("AuthQuickstart", error.toString());
                            Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
        );
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
                        NewPwFragment.this.startActivity(intent);
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
