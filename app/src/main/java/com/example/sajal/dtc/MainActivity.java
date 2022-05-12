package com.example.sajal.dtc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    // Object creation
    public static String PREFS_NAME = "MyPretsFilee";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView openBtn, swap_btn;
    Button searchBus;
    FloatingActionButton log_out_btn;
    public AutoCompleteTextView to_stopage, from_stopage;
    String[] busStopList = {"Midnapore", "Pairaguri","Digha","Purulia"};
    String[] stopage = busStopList;
    ArrayAdapter<String> adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// hide the status bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Progress dialouge
        checkConnection();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Accessing bus details...");

        drawerLayout = findViewById(R.id.drawer);
        openBtn = findViewById(R.id.openBtn);
        navigationView = findViewById(R.id.navigationView);
        searchBus = findViewById(R.id.searchBus);
        log_out_btn = findViewById(R.id.log_out_btn);
        to_stopage = findViewById(R.id.to_stopage);
        from_stopage = findViewById(R.id.from_stopage);
        swap_btn = findViewById(R.id.swap_btn);
        SharedPreferences sharedPreferences = getSharedPreferences(LoginPage.PREFS_NAME, 0);
// For autocomplete text in textview
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stopage);
        from_stopage.setThreshold(1);
        from_stopage.setAdapter(new ArrayAdapter<String>(this, R.layout.autocomplete_custom, busStopList));
        from_stopage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                search_view1.setText(adapter.getItem(position));
            }
        });
        to_stopage.setThreshold(1);
        to_stopage.setAdapter(new ArrayAdapter<String>(this, R.layout.autocomplete_custom, busStopList));
        to_stopage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        // get text from search engine and pass data to next activity through sharedpreferance

        searchBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
                if (from_stopage.getText().toString().length() == 0){
                    Toast.makeText(MainActivity.this, "Please select a proper stoppage🥺", Toast.LENGTH_SHORT).show();
                }
                else if (to_stopage.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "Please select a proper stoppage🥺 ", Toast.LENGTH_SHORT).show();
                }  else if (!checkConnection() ){
                    String from = from_stopage.getText().toString().trim();
                    from = from.replaceAll("\\s","");
                    String to = to_stopage.getText().toString().trim();
                    to =  to.replaceAll("\\s","");
                    SharedPreferences shrd = getSharedPreferences("demo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = shrd.edit();
                    editor.putString("keystart",from);
                    editor.putString("keystop",to);
                    editor.apply();


                    Intent intent = new Intent(MainActivity.this, BusList.class);
                    startActivity(intent);
                }else {
                    checkConnection();
                }
            }
        });
        openBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
                if (!drawerLayout.isDrawerOpen(Gravity.START))
                    drawerLayout.openDrawer(Gravity.START);
                else drawerLayout.closeDrawer(Gravity.END);
            }
        });
        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(MainActivity.this, "log out successfully..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });


        swap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation aniRotateClk = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
                swap_btn.startAnimation(aniRotateClk);
                onswap();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.share_app:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage = "\nTry out this awesome app on My website!\n\n";
                            shareMessage = shareMessage + "https://www.google.com" + BuildConfig.APPLICATION_ID + "\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            //e.toString();
                        }
                        break;
                    case R.id.whatsapp_share:
                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Application of social rating share with your friend");
                        try {
                            MainActivity.this.startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
                        }
                        break;
                    case R.id.rate_us:
                        Toast.makeText(getApplicationContext(), "Rate us!", Toast.LENGTH_SHORT).show();
                        break;
//                   
                    case R.id.feature:
                        Toast.makeText(MainActivity.this, "Suggest a feature ", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.contact_us:
                        Intent mailintent = new Intent(android.content.Intent.ACTION_SEND);
                        mailintent.setType("text/plain");
                        mailintent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]
                                {"mahatosajal65@gmail.com"});
                        mailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "info...");
                        startActivity(mailintent);
                        break;
//                    case R.id.subscription:
//                        Toast.makeText(MainActivity.this, "Subscription", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.share:
//                        Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
//                        break;
                }
                return true;
            }
        });
    }


    int counter = 0;

    @Override
    public void onBackPressed() {
        counter++;
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (counter == 1) {

            Toast.makeText(this, "Tap back button to exit", Toast.LENGTH_SHORT).show();
        } else if (counter == 2) {

//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setCancelable(false);
//            builder.setMessage("Do you want to exit?");
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            });
//            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    counter = 0;
//                    dialog.cancel();
//
//                }
//            });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//
//

            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            counter = 0;
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.onBackPressed();
                        }
                    }).create().show();
        }
    }

    public void onswap() {
        to_stopage = findViewById(R.id.to_stopage);
        from_stopage = findViewById(R.id.from_stopage);
        String to = String.valueOf(to_stopage.getText());
        String from = String.valueOf(from_stopage.getText());
        to_stopage.setText(from);
        from_stopage.setText(to);
    }

    public boolean checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null == activeNetwork) {
            Toast.makeText(this, "No internet Connection🥺😯!! Please connect to your cellular data or wifi😃😊", Toast.LENGTH_LONG).show();
            return true;
        }else {
            return false;
        }
    }
}