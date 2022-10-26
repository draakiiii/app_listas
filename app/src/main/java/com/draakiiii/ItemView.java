package com.draakiiii;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ItemView extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private Button button_confirm_edit;
    private Button button_cancel_edit;
    private Button button_confirm_delete;
    private Codes codes;
    private AppDatabaseMethods database;
    private Item item;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        database = new AppDatabaseMethods(this);

        codes = new Codes();

        title = findViewById(R.id.editTextTitle);
        description = findViewById(R.id.editTextDescription);

        Item item = database.getItem(getIntent().getExtras().getInt("id"));
        position = getIntent().getExtras().getInt("position");

        title.setText(item.getTitle());
        description.setText(item.getDescription());

        // Botón para confirmar edición
        button_confirm_edit = findViewById(R.id.button_confirm_edit);
        button_confirm_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem(item.getId());
            }
        });

        // Botón para borrar item
        button_confirm_delete = findViewById(R.id.button_confirm_delete);
        button_confirm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(item.getId());
            }
        });

        // Botón para cancelar edición
        button_cancel_edit = findViewById(R.id.button_cancel_edit);
        button_cancel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }


    public void editItem(int id) {
        Intent returnIntentEdit = new Intent();
        returnIntentEdit.putExtra("position",position);
        returnIntentEdit.putExtra("title", title.getText().toString());
        returnIntentEdit.putExtra("description", description.getText().toString());
        returnIntentEdit.putExtra("id", id);
        setResult(codes.edit_code,returnIntentEdit);
        Toast.makeText(getApplicationContext(), "Editado correctamente", Toast.LENGTH_SHORT).show();
        exitView();
    }

    public void deleteItem(int id) {
        Intent returnIntentDelete = new Intent();
        returnIntentDelete.putExtra("position",position);
        returnIntentDelete.putExtra("id", id);
        setResult(codes.delete_code,returnIntentDelete);
        Toast.makeText(getApplicationContext(), "Borrado correctamente", Toast.LENGTH_SHORT).show();
        exitView();
    }

    public void cancel() {
        Intent returnIntentCancel = new Intent();
        setResult(codes.cancel_code,returnIntentCancel);
        exitView();
    }

    public void exitView() {
        finish();
    }
}