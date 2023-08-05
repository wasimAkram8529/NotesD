package com.example.notesd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    EditText username, password;
    Button signIn, signUp, btnForgetPassword, btnForgetUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findId();
        signIn.setOnClickListener(v -> {
            String userName = username.getText().toString();
            String passWord = password.getText().toString();

            SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String Uname = pref.getString("Username", "baggc");
            String Pass = pref.getString("Pass", "bcgdc");

            if (userName.equals(Uname) && passWord.equals(Pass)) {
                Intent intent = new Intent(login.this, MainActivity.class);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
                startActivity(intent);
            } else {
                Toast.makeText(login.this, "Invalid User Id or Password", Toast.LENGTH_SHORT).show();
            }

        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, NewUser.class);
                startActivity(intent);
            }
        });

        btnForgetUserId.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, forget_userid.class);
            startActivity(intent);
        });

        btnForgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, forget_password.class);
            startActivity(intent);
        });
    }

    public void findId(){

        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        signIn   = findViewById(R.id.btnSignIn);
        signUp  = findViewById(R.id.btnSignUp);
        btnForgetPassword = findViewById(R.id.btnForgetPassword);
        btnForgetUserId = findViewById(R.id.btnForgetUserId);
    }

}