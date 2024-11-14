package com.example.newapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public ReminderViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textViewContactName); // Ensure this ID matches your layout
    }
}

