package com.rp.mood.diary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.rp.mood.diary.databinding.UserLoginBinding;
import com.rp.mood.diary.service.IdentityService;

import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.ContentValues.TAG;

@AndroidEntryPoint
public class LoginActivity  extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    @Inject
    IdentityService identityService;
    UserLoginBinding userLoginBinding;
    ActivityResultContracts.StartActivityForResult startActivityForResult;
    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        userLoginBinding = UserLoginBinding.inflate(getLayoutInflater());
        userLoginBinding.googleSignIn.setOnClickListener((view)-> googleSignIn());
        startActivityForResult =
                new ActivityResultContracts.StartActivityForResult();
        activityResultLauncher = registerForActivityResult(startActivityForResult, (result -> {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            handleSignInResult(task);
        }));
        setContentView(userLoginBinding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null && account.isExpired()==false){
            updateUI(account);
        }

    }

    private void updateUI(GoogleSignInAccount account) {
        this.identityService.setUniqueId(Optional.ofNullable(account).map(a->a.getEmail()).orElse(null));
        goToMainScreen(null);
    }

    public void goToMainScreen(View view) {
        startActivity(new Intent(this, MoodActivity.class));
    }

    public void googleSignIn() {
        activityResultLauncher.launch(mGoogleSignInClient.getSignInIntent());
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }


}
