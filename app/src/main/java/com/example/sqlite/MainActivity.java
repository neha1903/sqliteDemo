package com.example.sqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String USER = "user", PASS = "pass";

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button loginButton;
    EditText username, password;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Database Intialization
        openHelper = new DatabaseHelper(this);

        loginButton = (Button) findViewById(R.id.loginButton);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        sharedPrefrence(); // Shared Prefrences if login Code
        onLoginClick(); // login Click Button

    }

    public void sharedPrefrence(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String uName = "";
        uName = pref.getString("user", "");
        if (uName.length() > 0) {
            openActivity2();
        }
    }

    public void onLoginClick(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = openHelper.getReadableDatabase();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.Table_Name + " WHERE " + DatabaseHelper.Col_3 + "=? AND " + DatabaseHelper.Col_4 + "=?", new String[]{user, pass});
                if(cursor!=null){
                    if(cursor.getCount()>0){
                        Toast.makeText(getApplicationContext(), "WELCOME    "+user , Toast.LENGTH_LONG).show();
                        saveData();
                        openActivity2();
                    }else{
                        Toast.makeText(getApplicationContext(), "Credentials Not Matched" , Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void openActivity2(){
        Intent in = new Intent(this, Activity2.class);
        startActivity(in);
        finish();
    }

    public void saveData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER, username.getText().toString());
        editor.putString(PASS, password.getText().toString());
        editor.apply();

    }

    public void onClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
