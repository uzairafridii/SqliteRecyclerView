package com.uzair.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uzair.model.Items;
import com.uzair.activity.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Items> itemsList;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public MyAdapter(List<Items> itemsList) {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("vieTYpe", "onCreateViewHolder: "+viewType);
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_layout, parent, false);
            return new ItemHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder childViewHolder, int position) {
        if (childViewHolder instanceof ItemHolder) {
            ((ItemHolder) childViewHolder).availableStock.setText("Stock Available : " + itemsList.get(position).getBoxSize());
            ((ItemHolder) childViewHolder).itemName.setText(itemsList.get(position).getItemName());
            ((ItemHolder) childViewHolder).itemSqCode.setText("SKU Code : " + itemsList.get(position).getSkuCode());

            Glide.with(((ItemHolder) childViewHolder).itemImage.getContext())
                    .load(itemsList.get(position).getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(((ItemHolder) childViewHolder).itemImage);

        } else{
            Log.d("progressbarLoad", "onBindViewHolder: ");
            ((LoadingViewHolder) childViewHolder).progressBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return itemsList == null ? 0 : itemsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
}
