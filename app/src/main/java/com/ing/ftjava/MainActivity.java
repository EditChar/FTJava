package com.ing.ftjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.text.Layout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity {

    LinearLayout ft_layout, farm_layout;
    TextView btn_continue, btn_cancel;
    Animation fade;
    Button inventory;


    //bg color #eceff1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ft_layout = findViewById(R.id.ft_layout);
        farm_layout = findViewById(R.id.farm_layout);
        btn_continue = findViewById(R.id.btn_continue);
        btn_cancel = findViewById(R.id.btn_cancel);
//        inventory = findViewById(R.id.inventory);

        fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        btn_continue.setAlpha(0); // item seçilmeden başlangıçta alpha(0) ile btn_continue gözükmüyor.
        btn_cancel.setAlpha(0);




        ft_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ft_layout.setBackground(getDrawable(R.drawable.bg_item_selected));
                btn_continue.setAlpha(1); // default olarak item seçilmeden yukarıda alpha(0) verdiğimiz için alpha(1) yapmazsak gözükmez seçilince de.
                btn_cancel.setAlpha(1);
                btn_continue.startAnimation(fade);//drink_layout seçildikten sonra btn_continue animasyonla beliriyor.
                btn_cancel.startAnimation(fade); // animasyon

                selectedLayoutListener(ft_layout);

                btn_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,FT.class);
                        startActivity(intent);
                    }
                });
            }
        });
        farm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                farm_layout.setBackground(getDrawable(R.drawable.bg_item_selected));
                btn_continue.setAlpha(1);
                btn_cancel.setAlpha(1);
                btn_continue.startAnimation(fade);
                btn_cancel.startAnimation(fade);

                selectedLayoutListener(farm_layout);

                btn_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this, FARM.class);
                        startActivity(intent);
                    }
                });
            }
        });


        // Cancel tıklandığında btn_continue kayboluyor yani alpha(0) oluyor.
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft_layout.setBackground(getDrawable(R.drawable.bg_item));
                btn_continue.setAlpha(0);
                btn_cancel.setAlpha(0);
            }
        });
    }

    public void onImageViewClick(View view) {
        // ImageView'a tıklandığında yapılacak işlemleri burada gerçekleştirin

        // Yeni aktiviteye geçiş yapmak için Intent kullanın
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }

    private void selectedLayoutListener(LinearLayout selectedLayout){
        if(selectedLayout == ft_layout){
            ft_layout.setBackground(getDrawable(R.drawable.bg_item_selected));
        }else {
            ft_layout.setBackground(getDrawable(R.drawable.btn_bg_cancel));
        }
        if(selectedLayout == farm_layout){
            farm_layout.setBackground(getDrawable(R.drawable.bg_item_selected));
        }else{
            farm_layout.setBackground(getDrawable(R.drawable.btn_bg_cancel));

        }

    }
}


