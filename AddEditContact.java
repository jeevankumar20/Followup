package com.example.newapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class AddEditContact extends AppCompatActivity {

    private EditText nameEt, phoneEt, emailEt;
    private FloatingActionButton fab;

    private String name,phone,email;
    private DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_contact);

        dbHelper = new DbHelper(this);

        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailEt = findViewById(R.id.emailEt);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v){
                saveData();
            }
        });
    }
    private void saveData() {
        name=nameEt.getText().toString();
        phone=phoneEt.getText().toString();
        email=emailEt.getText().toString();

        String timeStamp = ""+System.currentTimeMillis();

        if(!name.isEmpty()||!phone.isEmpty()||!email.isEmpty()){

            long id = dbHelper.insertContact(""+name,""+phone,
            ""+email,""+timeStamp,
                    ""+timeStamp);

            Toast.makeText(getApplicationContext(), "Inserted "+id, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Nothing to save.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //noinspection deprecation
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}