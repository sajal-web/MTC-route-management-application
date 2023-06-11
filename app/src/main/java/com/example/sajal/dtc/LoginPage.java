package com.example.sajal.dtc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.util.Locale;

public class LoginPage extends AppCompatActivity {
    TextView signupBtn;
    EditText addUserName, addPassword;
    private Button loginBtn;
public static String PREFS_NAME ="MyPretsFilee";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_layout);
        // This code use for hide toolbar for this activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        signupBtn = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.loginBtn);
        addUserName = findViewById(R.id.addUserName);
        addPassword = findViewById(R.id.addPassword);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Accessing user details...");


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, SignUp.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences(LoginPage.PREFS_NAME,0);
                 SharedPreferences.Editor editor = sharedPreferences.edit();
                 editor.putBoolean("hasLoggedIn",true);
                 editor.apply();


                String UserName = addUserName.getText().toString().toLowerCase(Locale.ROOT);
                String Password = addPassword.getText().toString();
                if (!UserName.isEmpty()) {
                    addUserName.setError(null);
                    addUserName.clearFocus();
                    if (!Password.isEmpty()) {
                        progressDialog.show();
                        String userEnteredUsername = addUserName.getText().toString().trim().toLowerCase(Locale.ROOT);
                        String userEnteredPassword = addPassword.getText().toString().trim();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("dataUser");
                        Query checkUser = reference.orderByChild("name").equalTo(userEnteredUsername);
                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    progressDialog.show();
                                    addUserName.setError(null);
                                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                                    if (passwordFromDB.equals(userEnteredPassword)) {
                                        progressDialog.show();
                                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                        finish();
                                    } else {
                                        addPassword.setError("Wrong Password");
                                        progressDialog.dismiss();
                                    }
                                } else {
                                    addUserName.setError("User doesn't exist");
                                    progressDialog.dismiss();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    } else
                        addPassword.setError("Please Enter the password");
                } else
                    addUserName.setError("Please Enter the username");
            }
        });
    }
}