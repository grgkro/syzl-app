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

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.SelectTopCategoryActivity;
import de.stuttgart.syzl3000.services.AuthService;


public class SignUpFragment extends Fragment {


    private final String TAG = SignUpFragment.class.getSimpleName();

//    @Inject
//    String someRandomString;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button signUpBtn;
    private AuthService authService;
    private static String email;
    private static String password;
    private EncryptedPreferences encryptedPreferences;
    private boolean wasFramgmentNotCalledYet;

    public static String getEmail() {
        return email;
    }

    public static String getPassword() {
        return password;
    }

//    public SignUpFragment() {
//        super(R.layout.fragment_signup);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        editTextEmail = getView().findViewById(R.id.editTextEmail);
        editTextPassword = getView().findViewById(R.id.editTextPassword);
        TextView loginTextView = getView().findViewById(R.id.loginTextView);
        TextView confirmTextView = getView().findViewById(R.id.confirmTextView);

        signUpBtn = getView().findViewById(R.id.signUpBtn);
        encryptedPreferences = new EncryptedPreferences.Builder(getActivity()).withEncryptionPassword("MyTestPassword").build();
        authService = new AuthService(encryptedPreferences);

        editTextEmail.requestFocus();
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    signUpBtnClicked();
                    handled = true;
                }
                return handled;
            }
        });

        Log.i(TAG, "onCreate testing Hilt: " + "someRandomString");
        if (!authService.isAmplifyConfiguered()) {
            setUpAmplifyWithAuth();
        }

        getCredentialsFromSharedPreferences();

//        tryLogIn();



//            Amplify.Auth.signOut(
//                    () -> {
//                        Log.i("AuthQuickstart", "Signed out successfully");
//                    },
//                    error -> Log.e(TAG, error.toString())
//            );


        signUpBtn.setOnClickListener(v -> signUpBtnClicked());
        loginTextView.setOnClickListener(v -> {
            Log.i(TAG, "Starting Login Fragment");
            Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment).navigate(R.id.viewLogin);
        });
        confirmTextView.setOnClickListener(v -> {
            Log.i(TAG, "Starting Confirm Fragment");
            Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment).navigate(R.id.viewConfirm);
        });
    }

    private void getCredentialsFromSharedPreferences() {
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

    private void setUpAmplifyWithAuth() {
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Log.d(TAG, "onCreate: plugin Auth added");
            Amplify.configure(getActivity().getApplicationContext());
            authService.setAmplifyConfiguered(true);
            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify.", error);
        }
    }

    private void signUpBtnClicked() {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        if (email != null && authService.isEmailValid(email)) {
            saveEncryptedSharedPreferences(email, password);
            signUp(email, password);
        } else {
            Toast.makeText(getActivity().getBaseContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
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
                    Log.i(TAG, "SignUp result: " + result.toString());

                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment).navigate(R.id.viewConfirm);
                            }
                        });


//                    startTheNextActivity(ConfirmActivity.class);
                },
                error -> {
                    Log.e(TAG, "signUp Error: ", error);
                    tellUserWhatWentWrong(error.getCause().toString());
                }
        );

    }

    private void tellUserWhatWentWrong(String cause) {
        if (isUsernameEmpty(cause)) {
            Log.d(TAG, "tellUserWhatWentWrong: USERNAME WAS EMPTY");
//            Toast.makeText(getActivity(), "USERNAME WAS EMPTY", Toast.LENGTH_SHORT).show();
        } else if (isUsernameWrong(cause)) {
            Log.d(TAG, "tellUserWhatWentWrong: USERNAME was wrong");
//            Toast.makeText(getActivity(), "USERNAME was wrong", Toast.LENGTH_SHORT).show();
        } else if (isEmailInvalid(cause)) {
            Log.d(TAG, "tellUserWhatWentWrong: Invalid email address");
//            Toast.makeText(getActivity(), "Invalid email address", Toast.LENGTH_SHORT).show();
        } else if (isAlreadySignedUp(cause)) {
            Log.d(TAG, "tellUserWhatWentWrong: The given email already exists");
//            Toast.makeText(getActivity(), "The given email already exists", Toast.LENGTH_SHORT).show();
        } else if (isPasswordTooShort(cause)) {
            Log.d(TAG, "tellUserWhatWentWrong: Password was to short");
//            Toast.makeText(getActivity(), "Password was to short", Toast.LENGTH_SHORT).show();
        } else if (isPasswordWrong(cause)) {
            Log.d(TAG, "tellUserWhatWentWrong: Password was wrong");
//            Toast.makeText(getActivity(), "Password was wrong", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "tellUserWhatWentWrong: Sign up failed due to unknown reasons");
//            Toast.makeText(getActivity(), "Sign up failed due to unknown reasons", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(getActivity(), target);
        SignUpFragment.this.startActivity(i);
    }

    public void startLoginActivity(View view) {
        Log.i(TAG, "Starting Login Activity");
        Intent i = new Intent(getActivity(), LoginActivity.class);
        SignUpFragment.this.startActivity(i);
    }

    public void startConfirmActivityFromSignUp(View view) {
        Log.i(TAG, "Starting Confirm Activity");
        Intent i = new Intent(getActivity(), AuthenticationActivity.class);
        SignUpFragment.this.startActivity(i);
    }
}
