package de.stuttgart.syzl3000.authentication;

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

import com.amplifyframework.core.Amplify;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.services.AuthService;

public class ForgotPwFragment extends Fragment {

    private final String TAG = ForgotPwFragment.class.getSimpleName();

    private EditText editTextEmail;
    private Button resetPwBtn;
    private TextView newAccountTextView;
    private TextView backToLoginTextView;
    private String email;
    private AuthService authService;
    private EncryptedPreferences encryptedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_pw, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        editTextEmail = getView().findViewById(R.id.editTextEmail);
        resetPwBtn = getView().findViewById(R.id.resetPwBtn);
        newAccountTextView = getView().findViewById(R.id.newAccountTextView);
        backToLoginTextView = getView().findViewById(R.id.backToLoginTextView);
        encryptedPreferences = new EncryptedPreferences.Builder(getActivity()).withEncryptionPassword("MyTestPassword").build();
        authService = new AuthService(encryptedPreferences);
        email = editTextEmail.getText().toString();
        editTextEmail.setText(email);

        resetPwBtn.setOnClickListener(v -> resetPwBtnClicked());
        newAccountTextView.setOnClickListener(v -> {
            Log.i(TAG, "Starting SignUp Fragment");
            Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment).navigate(R.id.viewSignUp);
        });
        backToLoginTextView.setOnClickListener(v -> {
            Log.i(TAG, "Starting Login Fragment");
            Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment).navigate(R.id.viewLogin);
        });
    }

    private void resetPwBtnClicked() {
        Log.i(TAG, "resetPwBtnClicked");
        String email = editTextEmail.getText().toString();
        if (email != null && authService.isEmailValid(email)) {
            encryptedPreferences.edit()
                    .putString("email", email)
                    .apply();
            resetPw(email);
        } else {
            Toast.makeText(getActivity(), "Invalid email address", Toast.LENGTH_SHORT).show();
        }

    }

    private void resetPw(String email) {
        Amplify.Auth.resetPassword(
                email,
                result -> {
                    Log.i("ResetPw result", result.toString());
                    Log.i(TAG, "Starting NewPw Fragment");
                    Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment).navigate(R.id.newPwFragment);
                },
                error -> Log.e("ResetPw error:", error.toString())
        );
    }
}
