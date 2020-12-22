package de.stuttgart.syzl3000.menu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.results.Tokens;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import de.stuttgart.syzl3000.BaseActivity;
import de.stuttgart.syzl3000.CircleListActivity;
import de.stuttgart.syzl3000.LambdaSimpleProxyClient;
import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.RecipeListActivity;
import de.stuttgart.syzl3000.SelectTopCategoryActivity;
import de.stuttgart.syzl3000.authentication.SignUpActivity;
import de.stuttgart.syzl3000.services.AuthService;;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private List<AuthUserAttribute> userAttributeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void deleteAccount(View view) {
        Log.d(TAG, "deleteAccount: delete Btn clicked");
        Log.i(TAG, "going to fetch User attributes");
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i("AuthDemo", "User attributes = " + attributes);
                    userAttributeList = attributes;
                    Log.d(TAG, "deleteAccount: " + userAttributeList.get(0).getValue());
                    Log.d(TAG, "deleteAccount email: " + userAttributeList.get(2).getValue());
                    new MyTask().execute();
                },
                error -> Log.e("AuthDemo", "Failed to fetch user attributes.", error)
        );
        Log.i(TAG, "end of fetching User attributes");

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        String result;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ApiClientFactory factory = new ApiClientFactory();
                final LambdaSimpleProxyClient client = factory.build(LambdaSimpleProxyClient.class);
                client.usersDelete(userAttributeList.get(0).getValue());
                result = "Successfully deleted account " + userAttributeList.get(2).getValue();
                Intent intent = new Intent(ProfileActivity.this, SignUpActivity.class);
                intent.putExtra("isRedirect", true);
                startActivity(intent);
                return null;
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
                result = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }


}