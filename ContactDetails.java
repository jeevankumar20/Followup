package com.example.newapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newapp.databinding.ActivityReminderSelectionBinding;

import java.util.Calendar;
import java.util.Locale;
import java.util.zip.DataFormatException;

public class ContactDetails extends AppCompatActivity {

    private TextView nameTv, phoneTv, emailTv, addedTimeTv, updatedTimeTv;
    private String id;
    private DbHelper dbHelper;
    private Button reminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_details);

        reminder = findViewById(R.id.reminder);
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = nameTv.getText().toString();
                Intent intent = new Intent(ContactDetails.this, ReminderSelection.class);
                intent.putExtra("CONTACT_NAME", contactName);
                startActivity(intent);
            }
        });

        dbHelper = new DbHelper(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("contactId");

        nameTv = findViewById(R.id.nameTv);
        phoneTv = findViewById(R.id.phoneTv);
        emailTv = findViewById(R.id.emailTv);
        addedTimeTv = findViewById(R.id.addedTimeTv);
        updatedTimeTv = findViewById(R.id.updatedTimeTv);

        loadDataById();
    }

    private void loadDataById() {
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE "
                + Constants.C_ID + " =\"" + id + "\"";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()) {
            do{
                String name = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME));
                String phone = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE));
                String email = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL));
                String addTime = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_TIME));
                String updateTime = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATED_TIME));

                Calendar calendar = Calendar.getInstance(Locale.getDefault());

                calendar.setTimeInMillis(Long.parseLong(addTime));
                String timeAdd = ""+ DateFormat.format("dd/MM/yy hh/mm/aa",calendar);

                calendar.setTimeInMillis(Long.parseLong(updateTime));
                String timeUpdate = ""+ DateFormat.format("dd/MM/yy hh/mm/aa",calendar);

                nameTv.setText(name);
                phoneTv.setText(phone);
                emailTv.setText(email);
                addedTimeTv.setText(timeAdd);
                updatedTimeTv.setText(timeUpdate);


            }while(cursor.moveToNext());
        }

        db.close();

    }
}