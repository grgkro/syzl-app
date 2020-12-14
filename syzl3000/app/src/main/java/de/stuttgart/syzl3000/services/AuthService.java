package de.stuttgart.syzl3000.services;

import android.content.Intent;
import android.util.Log;

import com.amplifyframework.core.Amplify;

import de.stuttgart.syzl3000.CircleListActivity;
import de.stuttgart.syzl3000.authentication.SignUpActivity;

public class AuthService {

    public AuthService() {
    }

    public boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
