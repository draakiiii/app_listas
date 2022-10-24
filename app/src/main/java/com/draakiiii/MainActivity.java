package com.draakiiii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemAdapter.ItemClickListener{

    private RecyclerView recyclerView;
    private Button button;
    private ArrayList<Item> arrayList;
    private EditText input;
    private EditText input1;
    private ItemAdapter adapter;
    private int posList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<>();


        recyclerView = findViewById(R.id.rvItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(this, arrayList);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        posList = adapter.getItemCount();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // swipeDir --> 8 right, 4 left
                // arrayList initial number 1, listview initial number 0
                try {
                    int pos = viewHolder.getLayoutPosition();
                    switch (swipeDir) {
                        case 8: //Right swipe. Delete
                            arrayList.remove(pos);
                            adapter.notifyItemRemoved(pos);
                            break;
                        case 4: //Left swipe. Duplicate
                            Item duplicatedItem = arrayList.get(pos);
                            duplicatedItem.setTitle(duplicatedItem.getTitle() + " duplicado");
                            arrayList.add(duplicatedItem);
                            adapter.notifyItemInserted(arrayList.size()-1);
                            break;
                        default:
                            throw new Exception("error");
                    }

                } catch (Exception e) {
                    System.out.println("Error: " + e.getLocalizedMessage());
                }


            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddItem();
            }
        });



    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this,"Has hecho click",Toast.LENGTH_SHORT);
    }

    //Muestra el diálogo para añadir un nuevo item
    public void dialogAddItem() {
        AlertDialog.Builder builder = dialogBuilder();

        builder.setTitle("add");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int pos = arrayList.size();
                arrayList.add(pos, new Item( input.getText().toString(),input1.getText().toString()));
                addItem();
                Toast.makeText(getApplicationContext(), "Creado correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        builder.create();
        builder.show();
    }

    //Muestra el diálogo para editar el item
    public void dialogEditItem() {
        AlertDialog.Builder builder = dialogBuilder();

        builder.setTitle("edit");

        builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                arrayList.add(new Item(input.getText().toString(),input1.getText().toString()));
                Toast.makeText(getApplicationContext(), "Creado correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        builder.create();
        builder.show();
    }


    public AlertDialog.Builder dialogBuilder() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layoutInput= new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);

        input = new EditText(this);
        input1 = new EditText(this);

        input.setHint("title");
        input1.setHint("description");

        layoutInput.addView(input);
        layoutInput.addView(input1);

        builder.setView(layoutInput);

        return builder;
    }

    public void addItem() {
        updatePos();
        adapter.notifyItemInserted(posList);
    }

    public void updatePos() {
        posList = adapter.getItemCount();
    }



}