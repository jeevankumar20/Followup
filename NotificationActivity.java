package com.example.newapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReminders;
    private ReminderAdapter reminderAdapter;
    private List<ModelReminder> reminderList;
    private Button homeButton2;
    private DbHelper2 dbHelper2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);

        dbHelper2 = new DbHelper2(this);

        homeButton2 = findViewById(R.id.homeButton2);
        homeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(intent);            }
        });

        TextView textViewContactName = findViewById(R.id.textViewContactName);
        recyclerViewReminders = findViewById(R.id.recyclerViewReminders);

        loadData();

        String contactName = getIntent().getStringExtra("CONTACT_NAME");
        textViewContactName.setText(contactName);

        reminderList = new ArrayList<>();
        reminderList.add(new ModelReminder(contactName));

        recyclerViewReminders.setLayoutManager(new LinearLayoutManager(this));
        reminderAdapter = new ReminderAdapter(reminderList);
        recyclerViewReminders.setAdapter(reminderAdapter);
    }

    private void loadData() {
        try {
            reminderAdapter = new ReminderAdapter(dbHelper2.getAllReminder());
            recyclerViewReminders.setAdapter(reminderAdapter);
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