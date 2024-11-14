package com.example.newapp;



import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView contactRv;
    private DbHelper dbHelper;
    private AdapterContact adapterContact;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        button = findViewById(R.id.remindersButton);
        fab = findViewById(R.id.fab);
        contactRv = findViewById(R.id.contactRv);

        contactRv.setHasFixedSize(true);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditContact.class);
                startActivity(intent);
            }
        });
        //App doesn't run when load data is called.
        loadData();
    }

    private void loadData() {
        try {
            adapterContact = new AdapterContact(this, dbHelper.getAllData());
            contactRv.setAdapter(adapterContact);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message to the user)
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //App doesn't run when load data is called.
        loadData();
    }


}