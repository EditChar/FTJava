package com.ing.ftjava;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ArrayList<FarmItem> farmItemList;
    private Context context;


    public ItemAdapter(ArrayList<FarmItem> farmItemList, Context context) {
        this.farmItemList = farmItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FarmItem item = farmItemList.get(position);
        holder.dateTimeTextView.setText(item.getDateTime());
        holder.allTotalTextView.setText(item.getAllTotal());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Seçilen öğeyi listeden ve SharedPreferences'ten sil
                //deleteFarmItem(holder.getAdapterPosition());
                showDeleteConfirmationDialog(item, holder.getAdapterPosition());

            }
        });
    }

    private void showDeleteConfirmationDialog(FarmItem item, int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Silme İşlemi");
        builder.setMessage(item.getAllTotal() + " değerli öğeyi silinsin mi?");

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Seçilen öğeyi listeden ve SharedPreferences'ten sil
                deleteFarmItem(adapterPosition);
            }
        });

        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // İptal etme işlemi
                dialog.dismiss();
            }
        });

        builder.show();
    }


    private void deleteFarmItem(int position) {
        // Seçilen öğeyi listeden sil
        farmItemList.remove(position);
        notifyItemRemoved(position);

        // SharedPreferences'ten de sil
        saveFarmItemListToSharedPreferences();
    }
    private void saveFarmItemListToSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("FarmItemList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Mevcut listeyi temizle
        editor.clear();

        // Yeni listeyi kaydet
        Set<String> farmItemSet = new HashSet<>();
        for (FarmItem farmItem : farmItemList) {
            farmItemSet.add(farmItem.toString());
        }
        editor.putStringSet("allRecords", farmItemSet);

        editor.apply();
    }

    @Override
    public int getItemCount() {
        return farmItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView deleteButton;
        public TextView dateTimeTextView;
        public TextView allTotalTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
            allTotalTextView = itemView.findViewById(R.id.allTotalTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

        }
    }
}

