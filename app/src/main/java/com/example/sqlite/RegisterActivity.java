package com.example.sqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button registerButton;
    EditText email, username, password;

    @Override/**/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        openHelper = new DatabaseHelper(this);
        registerButton =  (Button) findViewById(R.id.RegisterButton);
        email = (EditText) findViewById(R.id.Remail);
        username = (EditText) findViewById(R.id.Rusername);
        password = (EditText) findViewById(R.id.Rpassword);
        registrationClick();


    }

    public void registrationClick(){

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db = openHelper.getWritableDatabase();
                String emailid = email.getText().toString();
                String usernameid = username.getText().toString();
                String pass = password.getText().toString();
                if (insertData(emailid, usernameid, pass)){
                    Toast.makeText(getApplicationContext(), "Registration Sucessfull", Toast.LENGTH_LONG).show();
                    openLoginActivity();
                }else{
                    Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public Boolean insertData(String emailid, String usernameid, String pass){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.Col_2, emailid);
        contentValues.put(DatabaseHelper.Col_3, usernameid);
        contentValues.put(DatabaseHelper.Col_4, pass);

        long id =  db.insert(DatabaseHelper.Table_Name, null, contentValues);

        return true;

    }

    public void openLoginActivity(){
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
