package com.uzair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uzair.model.Items;
import com.uzair.sqliterecyclerview.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<ItemHolder>
{
    private List<Items> itemsList;
    private Context context;

    public MyAdapter(List<Items> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_view_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder childViewHolder, int position) {
        childViewHolder.availableStock.setText("Stock Available : " + itemsList.get(position).getBoxSize());
        childViewHolder.itemName.setText(itemsList.get(position).getItemName());
        childViewHolder.itemSqCode.setText("SKU Code : " + itemsList.get(position).getSkuCode());

        Glide.with(context)
                .load(itemsList.get(position).getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(childViewHolder.itemImage);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
