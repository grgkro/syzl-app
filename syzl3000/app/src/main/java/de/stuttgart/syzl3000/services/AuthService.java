package de.stuttgart.syzl3000.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import de.stuttgart.syzl3000.CircleListActivity;
import de.stuttgart.syzl3000.authentication.SignUpActivity;

public class AuthService {

    private static final String TAG = "AuthService";
    private static String token;
    private static String email;
    private static String password;
    private EncryptedPreferences encryptedPreferences;
    private boolean isAmplifyConfiguered;

    public boolean isAmplifyConfiguered() {
        return isAmplifyConfiguered;
    }

    public void setAmplifyConfiguered(boolean amplifyConfiguered) {
        isAmplifyConfiguered = amplifyConfiguered;
    }

    public AuthService(EncryptedPreferences encryptedPreferences) {
        this.encryptedPreferences = encryptedPreferences;
        getCredentialsFromSharedPreferences();
    }

    public boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private void getCredentialsFromSharedPreferences() {
        email = encryptedPreferences.getString("email", null);
        password = encryptedPreferences.getString("pw", null);
        token = encryptedPreferences.getString("token", null);
    }

    private void saveEmailToSharedPreferences(String email) {
        encryptedPreferences.edit()
                .putString("email", email)
                .apply();
    }

    private void savePasswordToSharedPreferences(String password) {
        encryptedPreferences.edit()
                .putString("pw", password)
                .apply();
    }

    private void saveTokenToSharedPreferences(String token) {
        encryptedPreferences.edit()
                .putString("token", token)
                .apply();
    }
}
