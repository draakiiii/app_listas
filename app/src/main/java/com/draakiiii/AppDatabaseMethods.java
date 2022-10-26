package com.draakiiii;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import java.util.List;


public class AppDatabaseMethods {
    @SuppressLint("StaticFieldLeak")
    private static AppDatabaseMethods db;
    private ItemDao mItemDao;

    AppDatabaseMethods(Context context) {
        Context appContext = context.getApplicationContext();
        AppDatabase database = Room.databaseBuilder(appContext, AppDatabase.class, "item")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        mItemDao = database.itemDao();
    }

    public AppDatabaseMethods get(Context context) {
        if (db == null) {
            db = new AppDatabaseMethods(context);
        }
        return db;
    }

    public List<Item> getItems() {
        return mItemDao.getAll();
    }

    public Item getItem(int id) {
        return mItemDao.findById(id);
    }

    public void addItem(Item item) {
        mItemDao.insert(item);
    }

    public void updateItem(Item i) {
        mItemDao.updateItem(i.getTitle(), i.getDescription(), i.getId());
    }

    public void deleteItem(int id) {
        mItemDao.deleteById(id);
    }
}
