package com.example.rentingcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ImageButton loginButton;
    private ImageButton accountButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.firstloginUser );
        accountButton = findViewById(R.id.FirstloginAdmain);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogOnclick(v);
            }
        });

        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAccOnclick(v);
            }
        });
    }

    public void btnLogOnclick(View view) {

        Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
        startActivity(intent);


    }

    public void btnAccOnclick(View view) {

        Intent intent = new Intent(MainActivity.this, AdminLodin.class);
        startActivity(intent);


    }



}