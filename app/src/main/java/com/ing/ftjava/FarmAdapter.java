package com.ing.ftjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.ViewHolder> {

    private List<FarmItem> farmItemList;
    private Context context;

    public FarmAdapter(Context context, List<FarmItem> farmItemList) {
        this.context = context;
        this.farmItemList = farmItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FarmItem farmItem = farmItemList.get(position);

        //int imageResourceId = context.getResources().getIdentifier(farmItem.getImageName(), "drawable", context.getPackageName());
        //holder.imageView.setImageResource(imageResourceId);

       // holder.priceEditText.setText(String.valueOf(farmItem.getPrice()));
        holder.quantityTextView.setText(String.valueOf(farmItem.getQuantity()));
        holder.totalTextView.setText(context.getString(R.string.total, formatTotal(farmItem.getTotal())));

    }

    @Override
    public int getItemCount() {
        return farmItemList.size();
    }

    private String formatTotal(double total) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(total);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView priceEditText;
        public TextView quantityTextView;
        public TextView totalTextView;
        public ImageView imageView;
        public TextView minusButton;
        public TextView plusButton;

        public ViewHolder(View itemView) {
            super(itemView);

//            priceEditText = itemView.findViewById(R.id.priceEditText);
//            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            totalTextView = itemView.findViewById(R.id.totalTextView);
            imageView = itemView.findViewById(R.id.imageView);
            minusButton = itemView.findViewById(R.id.minusButton);
            plusButton = itemView.findViewById(R.id.plusButton);

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        //farmItemList.get(position).decreaseQuantity();
                        notifyItemChanged(position);
                    }
                }
            });

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                       // farmItemList.get(position).increaseQuantity();
                        notifyItemChanged(position);
                    }
                }
            });
        }
    }
}