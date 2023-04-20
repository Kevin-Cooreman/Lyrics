package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /*public void onBtnLoginClicked(View Caller) {
        Intent intent = new Intent(this, OrderConfirmationActivity.class);
        startActivity(intent);
    }*/

    public void onBtnRegisterClicked(View Caller) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}