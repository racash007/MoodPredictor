package com.rp.mood.diary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.rp.mood.diary.R;

public class LoginActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goToMainScreen(View view) {
        Intent moodScreenIntent = new Intent(this, MoodActivity.class);
        startActivity(moodScreenIntent);
    }
}
