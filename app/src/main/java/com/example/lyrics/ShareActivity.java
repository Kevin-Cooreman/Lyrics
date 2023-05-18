package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        TextView Errormsg = findViewById(R.id.shareErrormsgTxt);
        Errormsg.setVisibility(View.INVISIBLE);
    }
}