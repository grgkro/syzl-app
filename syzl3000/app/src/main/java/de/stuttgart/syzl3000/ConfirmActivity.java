package de.stuttgart.syzl3000;

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

public class ConfirmActivity extends AppCompatActivity {

    private final String TAG = ConfirmActivity.class.getSimpleName();

    private EditText editTextConfirmationCode;
    private Button confirmSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        editTextConfirmationCode = findViewById(R.id.editTextConfirmationCode);
        confirmSignUpBtn = findViewById(R.id.confirmBtn);

        confirmSignUpBtn.setOnClickListener(v -> confirmSignUpBtnClicked());
    }

    private void confirmSignUpBtnClicked() {
        String email = AuthenticationActivity.getEmail();
        String confirmationCode = editTextConfirmationCode.getText().toString();
        Amplify.Auth.confirmSignUp(
                email,
                confirmationCode,
                result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }
}
