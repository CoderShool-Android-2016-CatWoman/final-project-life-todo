package com.catwoman.lifetodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.catwoman.lifetodo.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Pass login
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
