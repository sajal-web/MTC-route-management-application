package com.example.sajal.dtc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sajal.dtc.modelClass.storingUserData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp extends AppCompatActivity {
    ImageView backImage;
    Button signUp;
    EditText add_userName, add_email, add_contact_number, add_password;
    FirebaseFirestore dbroot;
    //    private String Name, Email, ContactNumber, Password;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        // This code use for hide toolbar for this activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        signUp = findViewById(R.id.signUp);
        add_userName = findViewById(R.id.add_userName);
        add_email = findViewById(R.id.add_email);
        add_password = findViewById(R.id.add_password);
        add_contact_number = findViewById(R.id.add_contact_number);
        dbroot = FirebaseFirestore.getInstance();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonClick();

            }
        });
    }


    private void cheackvalidation() {

    }

    public void insertdata() {

    }

    public void registerButtonClick() {
        String Name = add_userName.getText().toString();
        String Email = add_email.getText().toString();
        String Password = add_password.getText().toString();
        String ContactNumber = add_contact_number.getText().toString();

        if (!Name.isEmpty()) {
            add_userName.setError(null);
            add_userName.setEnabled(false);
            if (!Email.isEmpty()) {
                add_email.setError(null);
                add_email.setEnabled(false);
                if (!Password.isEmpty()) {
                    add_password.setError(null);
                    add_password.setEnabled(false);
                    if (!ContactNumber.isEmpty()) {
                        add_contact_number.setError(null);
                        add_contact_number.setEnabled(false);
                        if (!Email.matches("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b")) {

                            firebaseDatabase = FirebaseDatabase.getInstance();
                            reference = firebaseDatabase.getReference("dataUser");

                            String Name_d = add_userName.getText().toString();
                            String Email_d = add_email.getText().toString();
                            String Password_d = add_password.getText().toString();
                            String ContactNumber_d = add_contact_number.getText().toString();

                            storingUserData storingUserDatas = new storingUserData(Name_d, Email_d, Password_d, ContactNumber_d);
                            reference.child(Name_d).setValue(storingUserDatas);
                            Toast.makeText(getApplicationContext(), "Sign up successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        } else {
                            add_email.setError("Please enter a valid email");
                        }
                    } else {
                        add_contact_number.setError("Please enter the contact number");
                    }
                } else {
                    add_password.setError("Please enter the password");
                }
            } else {
                add_email.setError("Please Enter the email id");
            }
        } else {
            add_userName.setError("Please Enter the username");
        }

    }

    public void gotoLogin(View view) {
        Intent intent = new Intent(SignUp.this,LoginPage.class);
        startActivity(intent);
    }
}