package com.draakiiii;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class ItemActivity_ extends AppCompatActivity {
    private TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        textView = findViewById(R.id.textViewItem);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        Item item = new Item(title, description);
        textView.setText(item.toString());
    }
}