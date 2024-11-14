package com.example.newapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.Calendar;


public class ReminderSelection extends AppCompatActivity{

    AutoCompleteTextView frequencyDropdownMenu;
    ArrayAdapter<String> adapterItems;
    private MaterialTimePicker timePicker;
    private Calendar selectedCalendar;
    private int contactId = 1;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TextView nameTv2;
    private Button homeButton, setReminderButton,
            cancelReminderButton, selectDateButton, selectTimeButton;
    private DbHelper2 dbHelper2;
    private String reminderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_selection);

        dbHelper2 = new DbHelper2(this);

        frequencyDropdownMenu = findViewById(R.id.frequencyDropdownMenu);
        Button setReminderButton = findViewById(R.id.setReminderButton);
        Button cancelReminderButton = findViewById(R.id.cancelReminderButton);
        Button selectTimeButton = findViewById(R.id.selectTimeButton);
        Button selectDateButton = findViewById(R.id.selectDateButton);

        nameTv2 = findViewById(R.id.nameTv2);
        Intent intent = getIntent();
        String contactName = intent.getStringExtra("CONTACT_NAME");
        nameTv2.setText(contactName);

        String[] frequencies = {"Daily", "Weekly", "Monthly"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, frequencies);

        frequencyDropdownMenu.setAdapter(adapter);
        selectedCalendar = Calendar.getInstance();

        selectDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedCalendar.set(year, month, dayOfMonth);
            }, selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH), selectedCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        selectTimeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedCalendar.set(Calendar.MINUTE, minute);
                selectedCalendar.set(Calendar.SECOND, 0);
                selectedCalendar.set(Calendar.MILLISECOND, 0);
            }, selectedCalendar.get(Calendar.HOUR_OF_DAY), selectedCalendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        setReminderButton.setOnClickListener(v -> {
            String frequency = frequencyDropdownMenu.getText().toString();
            if (frequency.isEmpty()) {
                Toast.makeText(this, "Please select frequency", Toast.LENGTH_SHORT).show();
                return;
            }
            scheduleNotificationForContact(selectedCalendar, frequency, contactId);

            String contactName2 = nameTv2.getText().toString();
            if (contactName2 == null || contactName2.isEmpty()) {
                Toast.makeText(ReminderSelection.this, "Contact name is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent2 = new Intent(ReminderSelection.this, NotificationActivity.class);
            intent2.putExtra("CONTACT_NAME", contactName2);
            startActivity(intent2);

            saveData();

            Toast.makeText(this, "Reminder set.", Toast.LENGTH_SHORT).show();
        });

        cancelReminderButton.setOnClickListener(v -> {
            cancelReminder(contactId);
            Toast.makeText(this, "Reminder canceled.", Toast.LENGTH_SHORT).show();
        });

        homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderSelection.this, MainActivity.class);
                startActivity(intent);            }
        });
    }


    private void saveData() {
        reminderName = nameTv2.getText().toString();

        if(!reminderName.isEmpty()){

            long id = dbHelper2.insertReminderContact(""+reminderName);
        }
        else {
            Toast.makeText(getApplicationContext(),"Nothing to save.", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleNotificationForContact(Calendar calendar, String frequency, int contactId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, contactId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long interval;
        switch (frequency) {
            case "Daily":
                interval = AlarmManager.INTERVAL_DAY;
                break;
            case "Weekly":
                interval = AlarmManager.INTERVAL_DAY * 7;
                break;
            case "Monthly":
                interval = AlarmManager.INTERVAL_DAY * 30;
                break;
            default:
                interval = AlarmManager.INTERVAL_DAY;
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
    }

    private void cancelReminder(int contactId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, contactId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }


    private void setReminderDate(long timeInMillis){
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,ReminderReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,
                intent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager!=null){
            try {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void openDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                setReminderDate(calendar.getTimeInMillis());
                selectDateButton.setText((month + 1) + "/" + day + "/" + year);
            }
        }, year, month, day);

        dialog.show();
    }

}