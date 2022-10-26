package com.draakiiii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolderCustom> {

    private ArrayList<Item> mData;
    private LayoutInflater mInflater;
    private ItemRecycleClick onItemRecycleListener;

    // data is passed into the constructor
    public ItemAdapter(Context context, ArrayList<Item> data, ItemRecycleClick onItemRecycleListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.onItemRecycleListener = onItemRecycleListener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolderCustom onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview, parent, false);
        return new ViewHolderCustom(view, onItemRecycleListener);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolderCustom holder, int position) {
        Item item = mData.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // convenience method for getting data at click position
    Item getItem(int id) {
        return mData.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemRecycleClick {
        void onRecycleClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolderCustom extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, description;
        ItemRecycleClick onItemRecycleListener;

        ViewHolderCustom(View itemView, ItemRecycleClick onItemRecycleListener) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            this.onItemRecycleListener = onItemRecycleListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemRecycleListener.onRecycleClick(getBindingAdapterPosition());
        }
    }
}