package com.example.transitapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class FirebaseUIActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_firebase_ui);
        FirebaseApp.initializeApp(this);
        checkUser();
        // [END auth_fui_create_intent]
    }



    private void checkUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // User not signed in
        if(auth.getCurrentUser() == null){
            System.out.println(auth.getCurrentUser());
            showLogin();
        }
        else{
            //update UI
            updateUI();
        }
    }

    private void updateUI() {
        Intent dashboard = new Intent(this, DriverDashboard.class);
        startActivity(dashboard);
        finish();
    }


    private void showLogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.drawable.logo)
                        .setTheme(R.style.FirebaseUI_TextInputEditText)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println(user.getDisplayName());
                System.out.println(user.getUid());
                updateUI();
                // ...
            } else {
                // TODO: Handle no network error
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

}
