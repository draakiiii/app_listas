package com.draakiiii;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.ItemRecycleClick {

    private RecyclerView recyclerView;
    private Button button;
    private ArrayList<Item> arrayList;
    private EditText inputTitle;
    private EditText inputDescription;
    private ItemAdapter adapter;
    private int posList;
    private Codes codes;
    private AppDatabaseMethods database;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new AppDatabaseMethods(this);

        codes = new Codes();

        arrayList = (ArrayList<Item>) database.getItems();

        recyclerView = findViewById(R.id.rvItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(this, arrayList, this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);

        posList = adapter.getItemCount();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // swipeDir --> 8 right, 4 left
                // arrayList initial number 1, listview initial number 0
                try {
                    int pos = viewHolder.getLayoutPosition();
                    adapter.notifyItemChanged(viewHolder.getLayoutPosition());
                    switch (swipeDir) {
                        case 4: //Right swipe. Delete
                            deleteItem(arrayList.get(pos), pos);
                            break;
                        case 8: //Left swipe. Duplicate
                            duplicateItem(arrayList.get(pos));
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

    public AlertDialog.Builder dialogBuilder() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layoutInput= new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);

        inputTitle = new EditText(this);
        inputDescription = new EditText(this);

        inputTitle.setMaxLines(1);
        inputDescription.setMaxLines(1);

        inputTitle.setInputType(InputType.TYPE_CLASS_TEXT);
        inputDescription.setInputType(InputType.TYPE_CLASS_TEXT);

        inputTitle.setHint(getResources().getString(R.string.set_title));
        inputDescription.setHint(getResources().getString(R.string.set_description));

        layoutInput.addView(inputTitle);
        layoutInput.addView(inputDescription);

        builder.setView(layoutInput);

        return builder;
    }

    //Muestra el diálogo para añadir un nuevo item
    public void dialogAddItem() {
        AlertDialog.Builder builder = dialogBuilder();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!inputTitle.getText().toString().equals("") && !inputDescription.getText().toString().equals("")) {
                    addItem(new Item(inputTitle.getText().toString(), inputDescription.getText().toString()));
                    Toast.makeText(getApplicationContext(), "Creado correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancel).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        builder.create();
        builder.show();
    }

    public void duplicateItem(Item item) {

        Item itemDuplicated = new Item(item.getTitle(),item.getDescription());

        //Comprueba si la cadena contiene algún número
        if (itemDuplicated.getTitle().matches(".*\\d+")) {
            String title = itemDuplicated.getTitle().substring(0, itemDuplicated.getTitle().length()-1);
            int number = Integer.parseInt(itemDuplicated.getTitle().substring(itemDuplicated.getTitle().length()-1)) + 1;
            itemDuplicated.setTitle(title+number);
        }
        addItem(itemDuplicated);
    }


    public void editItem(Item item, int pos) {
        database.updateItem(item);
        arrayList.set(pos, item);
        adapter.notifyItemChanged(pos);
    }

    public void deleteItem(Item item, int pos) {
        database.deleteItem(item.getId());
        arrayList.remove(pos);
        adapter.notifyItemRemoved(pos);
    }

    public void addItem(Item item) {
        database.addItem(item);
        arrayList.add(item);
        adapter.notifyItemInserted(arrayList.size()-1);
        System.out.println(item);
    }

    @Override
    public void onRecycleClick(int position) {
        Intent intent_itemview = new Intent(this, ItemView.class);
        Item item = arrayList.get(position);
        intent_itemview.putExtra("id",item.getId());
        intent_itemview.putExtra("position",position);
        openItemViewActivity.launch(intent_itemview);
    }

    ActivityResultLauncher<Intent> openItemViewActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != codes.cancel_code) {
                        int position = result.getData().getExtras().getInt("position");
                        Item item = database.getItem(result.getData().getExtras().getInt("id"));
                        if (result.getResultCode() == codes.edit_code) {
                            item.setTitle(result.getData().getStringExtra("title"));
                            item.setDescription(result.getData().getStringExtra("description"));
                            editItem(item, position);
                        } else if (result.getResultCode() == codes.delete_code) {
                            deleteItem(item, position);
                        }
                    }
                }
            });


    public ArrayList<Item> getArrayList() {
        return arrayList;
    }
}