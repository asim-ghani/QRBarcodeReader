package com.ghani.qrscanner.qrcodereader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private List<DataModel> dataList;

    public Adapter(Context context, List<DataModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        DataModel model = dataList.get(position);

        holder.type.setText(model.getType());
        holder.time.setText(model.getTimestamp());
        holder.value.setText(model.getValue());


        holder.delete.setOnClickListener(v -> {
            Log.d("DeleteItem", "Delete button clicked at position " + position);
            deleteItem(position);
        });

        holder.share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.share));
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, model.getValue());
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share)));
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView type, time, value;
        ImageView delete, share;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.type);
            time = itemView.findViewById(R.id.timestamp);
            value = itemView.findViewById(R.id.qr_result);
            delete = itemView.findViewById(R.id.btn_delete);
            share = itemView.findViewById(R.id.btn_share);
        }
    }

    private void deleteItem(int position){

        DataModel model = dataList.get(position);

        long deleteItemId = model.getId();
        Log.d("DeleteItem", "Deleting item with ID: " + deleteItemId);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.deleteData(deleteItemId);

        dataList.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

        Log.d("DeleteItem", "Item deleted from database and RecyclerView");
    }
}
