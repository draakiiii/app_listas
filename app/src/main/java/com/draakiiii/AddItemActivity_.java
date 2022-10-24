package com.draakiiii;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity_ extends AppCompatActivity {

    private EditText editText, editTextDescription;
    private Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editText = findViewById(R.id.editTextAdd);
        editTextDescription = findViewById(R.id.editTextDescription);
        button = findViewById(R.id.buttonAdd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText.getText().toString();
                String description = editTextDescription.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}