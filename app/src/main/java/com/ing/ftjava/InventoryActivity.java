package com.ing.ftjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class InventoryActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView textView;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ArrayList<FarmItem> farmItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        sharedPreferences = getSharedPreferences("FarmItemList", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        recyclerView = findViewById(R.id.recyclerView);
        farmItemArrayList = new ArrayList<>();

        // Mevcut kayıtları al
        Set<String> existingRecords = sharedPreferences.getStringSet("allRecords", new HashSet<String>());

        // FarmItem nesnelerini oluşturup ArrayList'e ekle
        for (String record : existingRecords) {
            FarmItem farmItem = FarmItem.fromString(record);
            if (farmItem != null) {
                farmItemArrayList.add(farmItem);

            }
        }

        adapter = new ItemAdapter(farmItemArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

    }
}