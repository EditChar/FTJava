package com.ing.ftjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class FARM extends AppCompatActivity {

    private ArrayList<FarmItem> farmItemList;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private EditText quantityEditText;
    private TextView totalTextView, counterTextView;
    private ImageView imageView;
    private int counter = 0;
    private TextView allTotalTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);


        farmItemList = new ArrayList<>();
        adapter = new ItemAdapter(farmItemList, this);

        // Örnek ürünler ekleyelim
        farmItemList.add(new FarmItem(R.drawable.gemblack));
        farmItemList.add(new FarmItem(R.drawable.gemgreen));
        farmItemList.add(new FarmItem(R.drawable.gemblue));
        farmItemList.add(new FarmItem(R.drawable.gemyellow));
        farmItemList.add(new FarmItem(R.drawable.gemred));
        farmItemList.add(new FarmItem(R.drawable.gemsilver));
        farmItemList.add(new FarmItem(R.drawable.drakiold));
        farmItemList.add(new FarmItem(R.drawable.drakisuper));
        farmItemList.add(new FarmItem(R.drawable.treblue));
        farmItemList.add(new FarmItem(R.drawable.tregreen));
        farmItemList.add(new FarmItem(R.drawable.trered));
        farmItemList.add(new FarmItem(R.drawable.fragarrogance));
        farmItemList.add(new FarmItem(R.drawable.fraggluttony));
        farmItemList.add(new FarmItem(R.drawable.fragrage));
        farmItemList.add(new FarmItem(R.drawable.fragsloth));
        farmItemList.add(new FarmItem(R.drawable.fraglechery));
        farmItemList.add(new FarmItem(R.drawable.fragjeolusy));
        farmItemList.add(new FarmItem(R.drawable.fragavarice));


        allTotalTextView = findViewById(R.id.allTotalTextView);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInventory();
            }
        });

        // Dinamik olarak oluşturulan liste görünümüne ürünleri ekleme
        LinearLayout productContainerLayout = findViewById(R.id.productContainerLayout);

        for (FarmItem farmItem : farmItemList) {
            View farmView = LayoutInflater.from(this).inflate(R.layout.item_farm, productContainerLayout, false);

            ImageView imageView = farmView.findViewById(R.id.imageView);
            TextView counterTextView = farmView.findViewById(R.id.counterTextView);
            TextView totalTextView = farmView.findViewById(R.id.totalTextView);
            EditText quantityEditText = farmView.findViewById(R.id.quantityEditText);

            imageView.setImageResource(farmItem.getImageResource());

            // Counter
            TextView minusButton = farmView.findViewById(R.id.minusButton);
            TextView plusButton = farmView.findViewById(R.id.plusButton);

            minusButton.setOnClickListener(v -> {
                if (farmItem.getCounter() > 0) {
                    farmItem.setCounter(farmItem.getCounter() - 1);
                    farmItem.updateTotal();
                    updateFarmItemView(farmItem, counterTextView, totalTextView);
                }
            });

            plusButton.setOnClickListener(v -> {
                farmItem.setCounter(farmItem.getCounter() + 1);
                farmItem.updateTotal();
                updateFarmItemView(farmItem, counterTextView, totalTextView);
            });

            quantityEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Boş bırakıyoruz
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String quantityString = quantityEditText.getText().toString();
                    if (!quantityString.isEmpty()) {
                        int quantity = Integer.parseInt(quantityString);
                        farmItem.setQuantity(quantity);
                        farmItem.updateTotal();
                        updateFarmItemView(farmItem, counterTextView, totalTextView);
                    } else {
                        // Giriş boşsa veya sadece boşluk karakterleri içeriyorsa, burada bir işlem yapabilirsiniz.
                        //quantityEditText.setError("Quantity cannot be empty");
                        quantityEditText.setText("0");
                        farmItem.setQuantity(0);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Boş bırakıyoruz
                }
            });
            // Quantity
            quantityEditText.setOnFocusChangeListener((v, hasFocus) -> {
                    try {
                        int quantity = Integer.parseInt(quantityEditText.getText().toString());
                        farmItem.setQuantity(quantity);
                        farmItem.updateTotal();
                        updateFarmItemView(farmItem, counterTextView, totalTextView);
                    } catch (NumberFormatException e) {
                        quantityEditText.setError("Invalid quantity");
                }
            });

            productContainerLayout.addView(farmView);
        }
        updateAllTotal();
    }

    private void saveInventory() {

        String currentDateTime = getCurrentDateTime();
        String allTotal = allTotalTextView.getText().toString();


        SharedPreferences sharedPreferences = getSharedPreferences("FarmItemList", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("allTotal", allTotal);
//        editor.putString("savedDateTime", currentDateTime);
        // Mevcut kayıtları al
        // Yeni FarmItem nesnesi oluştur
        FarmItem newFarmItem = new FarmItem(currentDateTime, allTotal);

        // Mevcut kayıtları al
        Set<String> existingRecordsSet = sharedPreferences.getStringSet("allRecords", new HashSet<String>());
        ArrayList<String> existingRecords = new ArrayList<>(existingRecordsSet);



        // Yeni kaydı ekleyip SharedPreferences'e kaydet
        existingRecords.add(newFarmItem.toString());
        editor.putStringSet("allRecords", new HashSet<>(existingRecords));
        editor.apply();

        startActivity(new Intent(FARM.this, InventoryActivity.class));
    }

    // Anlık tarih ve saat bilgisini al
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        Date currentDate = new Date();

        String date = dateFormatDate.format(currentDate);
        String time = dateFormatTime.format(currentDate);

        // İki değeri alt alta birleştir
        return date + "\n" + time;
    }


    public int getAllTotal() {
        SharedPreferences sharedPreferences = getSharedPreferences("FarmItemList", MODE_PRIVATE);
        return sharedPreferences.getInt("allTotal", 0);
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void updateAllTotal() {
        int allTotal = 0;

        for (FarmItem farmItem : farmItemList) {
            allTotal += farmItem.getTotal();
        }

        allTotalTextView.setText("Hepsi Toplam : " + allTotal);
    }

    private void updateFarmItemView(FarmItem farmItem, TextView counterTextView, TextView totalTextView) {
        counterTextView.setText(String.valueOf(farmItem.getCounter()));
        totalTextView.setText("Total: " + farmItem.getTotal());
        updateAllTotal();

    }

    private void updateCounter() {
        counterTextView.setText(""+counter);
    }
    private void updateTotal() {
        try {
            int number = Integer.parseInt(quantityEditText.getText().toString());
            int total = number * counter;
            totalTextView.setText("" + total);
        } catch (NumberFormatException e) {
            totalTextView.setText("Invalid number");
        }
    }

    private void updateFarmItem(FarmItem farmItem, TextView counterTextView, TextView totalTextView, EditText quantityEditText) {
        farmItem.updateTotal();
        updateFarmItemView(farmItem, counterTextView, totalTextView);
        quantityEditText.setText(String.valueOf(farmItem.getQuantity()));
    }

    public void onPlusClick(View view) {
        counter++;
        updateCounter();
        updateTotal();
    }

    public void onMinusClick(View view) {
        if (counter > 0) {
            counter--;
            updateCounter();
            updateTotal();
        }
    }


}