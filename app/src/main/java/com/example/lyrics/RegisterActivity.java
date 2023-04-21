package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    private static final String url = "jdbc:mysql://mysql.studev.groept.be:3306/a22ib2a03";
    private static final String user = "a22ib2a03";
    private static final String password = "secret";
    private  String table = "histories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onBtnCreateAccountClicked(View Caller) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}