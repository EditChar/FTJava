package com.ing.ftjava;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CountdownTimerHelper.TimerListener{

    private Button startButton, button1, button2, button3, button4, geri, ileri;
    private Handler handler;
    private Runnable runnable;
    private boolean timerRunning;
    private int secondsPassed;
    private int x = 0; // Toplu süre artışını temsil eden parametre.

    private TextToSpeech textToSpeech;
    private int selectedButton = 1;
    TextView mobText, changeTimeTxt, tvTimer;
    private float changeTime = 0.0f;
    private ProgressBar progressBar;
    private CountdownTimerHelper countdownTimerHelper;



    //bg color #eceff1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobText = findViewById(R.id.mob);
        changeTimeTxt = findViewById(R.id.changeTimeTxt);

        progressBar = findViewById(R.id.progressBar);
        tvTimer = findViewById(R.id.tvTimer);
        countdownTimerHelper = new CountdownTimerHelper(this);
        //countdownTimerHelper.startTimer();

        //progressBar.setIndeterminate(true); // ProgressBar'ı indeterminate moduna geçir


        startButton = findViewById(R.id.startBtn);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        geri = findViewById(R.id.geri);
        ileri = findViewById(R.id.ileri);
        handler = new Handler(Looper.getMainLooper());

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int langResult = textToSpeech.setLanguage(new Locale("tr","TR"));
                    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Dil desteklenmiyor veya eksikse hata mesajı gösterebilirsiniz
                    }
                    // Ses hızını burada ayarlayabiliriz. Örnek olarak 0.5 (yarı hızında) kullanıyoruz.
                    textToSpeech.setSpeechRate(0.9f);
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (timerRunning) {
//                    pauseTimer();
//                } else {
//                    startTimer();
//                }
                if(!timerRunning){
                    startTimer();
                }
                toggleButtons(); // Diğer butonların tıklanabilirlik durumunu güncelle
                //updateSelectedButtonOpacity(); // Varsayılan olarak 1. butonu seçili göster
            }
        });

        // Diğer butonları başlangıçta pasif hale getir
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
        geri.setEnabled(false);
        ileri.setEnabled(false);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = 1;
                buttonSelectedOpacity1();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = 2;
                buttonSelectedOpacity2();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = 3;
                buttonSelectedOpacity3();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = 4;
                buttonSelectedOpacity4();
            }
        });


        // 1 saniye ileri
        ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                    secondsPassed += 1;
                    startTimer();
                    Toast.makeText(MainActivity.this, "1sn ileri alındı.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        // yarım saniye geri
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    secondsPassed -= 0.5;
                    Toast.makeText(MainActivity.this, "0.5sn geri alındı.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void startTimer() {
        timerRunning = true;
        //startButton.setText("Pause Timer");

        runnable = new Runnable() {
            @Override
            public void run() {
                secondsPassed++;
                updateCountdownText();
                int secondsPassed2 = secondsPassed + x; // Süreye "x" parametresini ekleyerek süreyi değiştiriyoruz.
                //setX(3);

                // Seçilen butona göre metinleri sesli olarak okut
                if (selectedButton == 1) {
                    buttonSelectedOpacity1();
                    if (secondsPassed == 2) {
                        speakText("Nova hazır, ikii, bir, bas");//2.5 sn konuşma süresi
                        mobText.setText("Lord Orc");
                        long desiredTimeInMillis = 5000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 8) { //+1 eklenebilir
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 36000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 14) { //-0.5 veya 1
                        speakText("Lord ork turu, Nova vurduysa bir, Meteor vurduysa iki");
                        mobText.setText("Orc Sniper");
                    }else if (secondsPassed == 44) { //-.5
                        speakText("Nova hazır, iki, bir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 50) {
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Troll Berserker");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 53) { // -1sn
                        speakText("Nova vurduysa bir, Meteor vurduysa iki");
                        mobText.setText("Baron");
                    }else if (secondsPassed == 57) {
                        speakText("Toz bas TeBeleri tekle");
                        mobText.setText("Baron");
                        long desiredTimeInMillis = 5000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 61) { // 1sn
                        speakText("Nova hazır, ikii, biir, baas");
                            mobText.setText("Death Knight");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 103) { // +1
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Crimson Wing");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 116) {
                        speakText("Toz bas CeVeleri tekle");
                        mobText.setText("Scolar");
                        long desiredTimeInMillis = 10000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 121) { // bırak dediğinde 2ye denk geliyor geç komut
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Tyon");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 132) { // bırak dediğinde 2ye denk geliyor geç komut
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Troll");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 140) {
                        speakText("Troll geliyor magic sheild bas");
                        mobText.setText("Ash Knight");
                        long desiredTimeInMillis = 52000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 191) {
                        speakText("Toz bas");
                        mobText.setText("Ash Knight");
                    }else if (secondsPassed == 192) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Haunga");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 204) { // ++++++++++
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Deruvish");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 234) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Lamia");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 240) { //
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Uruk Hai");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 266) {// bıraktan sonra komut başladı
                        speakText("Toz bas");
                        mobText.setText("Uruk Hai");
                    }else if (secondsPassed == 269) { // bıraktan sonra komut başladı
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Harpy");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 281) { // bıraktan sonra komut başladı
                        speakText("Meteor hazır, ikii, biir, bas");
                        mobText.setText("Dragon Tooth Knight");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 293) {// bırak deyince 1e denk geldi
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Uruk Blade");
                        long desiredTimeInMillis = 36000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 304) {//BOSS
                        speakText("Magic Sheild bas bosa zehir atmadan vur");
                        mobText.setText("UrukBlade");
                    }else if (secondsPassed == 327) {
                        speakText("Toz bas");
                        mobText.setText("UrukBlade");
                    }else if (secondsPassed == 329) { // bıraktan yarım sn sonra başladı.
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Wraith");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 341) { // 0.5olabilir
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Apostle");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 383) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Garuna");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 398) { //
                        speakText("Toz bas");
                        mobText.setText("Garuna");
                    }else if (secondsPassed == 400) { //
                        speakText("Meteor hazır, ikii, biir, bas");
                        mobText.setText("Lamiros");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 430) { ///4 geç başlıyor
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Deruvish");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 442) { //3sn geç başllıyor
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Blood Seeker");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 469) {
                        speakText("Toz bas");
                        mobText.setText("Blood Seeker");
                    }else if (secondsPassed == 472) {
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 477) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Stone Golem");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 489) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Haunga");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 501) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Bugbear");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 507) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Raven Harpy");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 547) { //
                        speakText("Toz bas");
                        mobText.setText("Raven Harpy");
                    }else if (secondsPassed == 549) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Sheriff");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 557) {//REBUFF
                        speakText("rebaf zamanı");
                        mobText.setText("Sheriff");
                    }else if (secondsPassed == 561) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Dragon Tooth Knight");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 567) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Lamentation");
                        long desiredTimeInMillis = 72000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 635) { // tam bıraka geliyor 41
                        speakText("Toz bas");
                        mobText.setText("Lamentation");
                    }else if (secondsPassed == 638) { //
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Dark Stone");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 652) {
                        speakText("deseleri tekle.");
                        mobText.setText("Lich");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 656) { //
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Hob Goblin");
                        long desiredTimeInMillis = 54000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 706) { //
                        speakText("Toz bas");
                        mobText.setText("Hob Goblin");
                    }else if (secondsPassed == 710) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Troll");
                        long desiredTimeInMillis = 9000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 721) {//733->736
                        speakText("Troll geliyor magic sheild bas");
                        mobText.setText("Uruk Tron");
                        long desiredTimeInMillis = 9000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 728) {  // ????????
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Sheriff");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 736) {
                        speakText("azıcık gezin seve bul");
                        mobText.setText("Sheriff");
                    }else if (secondsPassed == 805) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Manticore");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 815) { //
                        speakText("Manticore geliyor geç vur zehir yeme.");
                        mobText.setText("Burning Skeleton");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 829) { //6şar sn kayma var.
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Lamia");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 835) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Fallen Angel");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 868) {//fallen erken komut
                        speakText("Fallen geliyor, Magic Sheild bas toz bas çiftle full vur.");
                        mobText.setText("Fallen Angel");
                    }else if (secondsPassed == 876) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Grell");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 888) {// 3sn geç
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Mastadon");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 894) { //3sn geç
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Centaur");
                        long desiredTimeInMillis = 60000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 903) {//BOSS
                        speakText("Skorpiyon bosa zehir atmadan vur");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 945) {
                        speakText("Centaur son on saniye");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 950) {
                        speakText("Magic Sheild bas komutla vur");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 953) {// 956 da cıkıyor centaur bas demesi
                        speakText("Toz bas");
                        mobText.setText("Centaur");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 954) {// 956 da cıkıyor centaur bas demesi
                        speakText("Centaur hazır baas");
                        mobText.setText("Goblin Bouncer");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 965) { //3sn ileri aldık 2 ye denk geliyor
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Harpy");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 983) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Tyon");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1022) {
                        speakText("Toz bas");
                        mobText.setText("Tyon");
                    }else if (secondsPassed == 1025) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Stone Golem");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1037) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Beast");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1043) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Gaint Golem");
                        long desiredTimeInMillis = 60000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1050) { //REBUFF
                        speakText("yapmadıysan şimdi rebaf yap");
                        mobText.setText("Gaint Golem");
                    }else if (secondsPassed == 1100) {//
                        speakText("Toz bas");
                        mobText.setText("Gaint Golem");
                    }else if (secondsPassed == 1102) {
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Haunga Warrior");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1114) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Troll Warrior");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1126) {
                        speakText("fere at taliya giy");
                        mobText.setText("Troll Warrior");
                    }else if (secondsPassed == 1144) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Reaper");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1155) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Dark Mare");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1162) {
                        speakText("Dark Mare dikkat");
                        mobText.setText("Volcanic Rock");
                        long desiredTimeInMillis = 4000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1166) {//1182
                        speakText("Magic Sheild bas Volkanik çıkıyor.");
                        mobText.setText("Volcanic Rock");
                    }else if (secondsPassed == 1420) {
                        speakText("fereler bitiyor yenile");
                    }







                }
                if (selectedButton == 2) {
                    buttonSelectedOpacity1();
                    if (secondsPassed == 2) {
                        speakText("Nova hazır, ikii, bir, bas");//2.5 sn konuşma süresi
                        mobText.setText("Lord Orc");
                        long desiredTimeInMillis = 5000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 8) { //+1 eklenebilir
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 36000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 14) { //-0.5 veya 1
                        speakText("Lord ork turu, Nova vurduysa bir, Meteor vurduysa iki");
                        mobText.setText("Orc Sniper");
                    }else if (secondsPassed == 44) { //-.5
                        speakText("Nova hazır, iki, bir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 50) {
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Troll Berserker");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 53) { // -1sn
                        speakText("Nova vurduysa bir, Meteor vurduysa iki");
                        mobText.setText("Baron");
                    }else if (secondsPassed == 57) {
                        speakText("Toz bas TeBeleri tekle");
                        mobText.setText("Baron");
                        long desiredTimeInMillis = 5000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 61) { // 1sn
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Death Knight");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 103) { // +1
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Crimson Wing");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 116) {
                        speakText("Toz bas CeVeleri tekle");
                        mobText.setText("Scolar");
                        long desiredTimeInMillis = 10000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 121) { // bırak dediğinde 2ye denk geliyor geç komut
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Tyon");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 132) { // bırak dediğinde 2ye denk geliyor geç komut
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Troll");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 140) {
                        speakText("Troll geliyor magic sheild bas");
                        mobText.setText("Ash Knight");
                        long desiredTimeInMillis = 52000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 191) {
                        speakText("Toz bas");
                        mobText.setText("Ash Knight");
                    }else if (secondsPassed == 192) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Haunga");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 198) { // ++++++++++
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Deruvish");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 228) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Lamia");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 240) { //
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Uruk Hai");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 266) {// bıraktan sonra komut başladı
                        speakText("Toz bas");
                        mobText.setText("Uruk Hai");
                    }else if (secondsPassed == 269) { // bıraktan sonra komut başladı
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Harpy");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 281) { // bıraktan sonra komut başladı
                        speakText("Meteor hazır, ikii, biir, bas");
                        mobText.setText("Dragon Tooth Knight");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 287) {// bırak deyince 1e denk geldi
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Uruk Blade");
                        long desiredTimeInMillis = 36000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 304) {//BOSS
                        speakText("Magic Sheild bas bosa zehir atmadan vur");
                        mobText.setText("UrukBlade");
                    }else if (secondsPassed == 327) {
                        speakText("Toz bas");
                        mobText.setText("UrukBlade");
                    }else if (secondsPassed == 329) { // bıraktan yarım sn sonra başladı.
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Wraith");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 341) { // 0.5olabilir
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Apostle");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 377) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Garuna");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 398) { //
                        speakText("Toz bas");
                        mobText.setText("Garuna");
                    }else if (secondsPassed == 400) { //
                        speakText("Meteor hazır, ikii, biir, bas");
                        mobText.setText("Lamiros");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 430) { ///4 geç başlıyor
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Deruvish");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 436) { //3sn geç başllıyor
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Blood Seeker");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 463) {
                        speakText("Toz bas");
                        mobText.setText("Blood Seeker");
                    }else if (secondsPassed == 466) {
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 477) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Stone Golem");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 489) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Haunga");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 495) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Bugbear");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 507) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Raven Harpy");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 547) { //
                        speakText("Toz bas");
                        mobText.setText("Raven Harpy");
                    }else if (secondsPassed == 549) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Sheriff");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 561) {//REBUFF
                        speakText("rebaf zamanı");
                        mobText.setText("Sheriff");
                    }else if (secondsPassed == 555) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Dragon Tooth Knight");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 567) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Lamentation");
                        long desiredTimeInMillis = 72000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 635) { // tam bıraka geliyor 41
                        speakText("Toz bas");
                        mobText.setText("Lamentation");
                    }else if (secondsPassed == 638) { //
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Dark Stone");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 652) {
                        speakText("deseleri tekle.");
                        mobText.setText("Lich");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 656) { //
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Hob Goblin");
                        long desiredTimeInMillis = 54000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 702) { //
                        speakText("Toz bas");
                        mobText.setText("Hob Goblin");
                    }else if (secondsPassed == 704) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Troll");
                        long desiredTimeInMillis = 9000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 721) {//733->736
                        speakText("Troll geliyor magic sheild bas");
                        mobText.setText("Uruk Tron");
                        long desiredTimeInMillis = 9000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 728) {  // ????????
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Sheriff");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 736) {
                        speakText("azıcık gezin seve bul");
                        mobText.setText("Sheriff");
                    }else if (secondsPassed == 805) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Manticore");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 814) { //
                        speakText("Manticore geliyor geç vur zehir yeme.");
                        mobText.setText("Burning Skeleton");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 823) { //6şar sn kayma var.
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Lamia");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 835) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Fallen Angel");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 868) {//fallen erken komut
                        speakText("Fallen geliyor, Magic Sheild bas toz bas çiftle full vur.");
                        mobText.setText("Fallen Angel");
                    }else if (secondsPassed == 876) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Grell");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 882) {// 3sn geç
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Mastadon");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 894) { //3sn geç
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Centaur");
                        long desiredTimeInMillis = 60000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 903) {//BOSS
                        speakText("Skorpiyon bosa zehir atmadan vur");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 945) {
                        speakText("Centaur son on saniye");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 950) {
                        speakText("Magic Sheild bas komutla vur");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 953) {// 956 da cıkıyor centaur bas demesi
                        speakText("Toz bas");
                        mobText.setText("Goblin Bouncer");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 954) {// 956 da cıkıyor centaur bas demesi
                        speakText("Centaur hazır baas");
                        mobText.setText("Goblin Bouncer");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 965) { //3sn ileri aldık 2 ye denk geliyor
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Harpy");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 983) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Tyon");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1022) {
                        speakText("Toz bas");
                        mobText.setText("Tyon");
                    }else if (secondsPassed == 1025) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Stone Golem");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1031) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Beast");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1043) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Gaint Golem");
                        long desiredTimeInMillis = 60000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1050) { //REBUFF
                        speakText("yapmadıysan şimdi rebaf yap");
                        mobText.setText("Gaint Golem");
                    }else if (secondsPassed == 1100) {//
                        speakText("Toz bas");
                        mobText.setText("Gaint Golem");
                    }else if (secondsPassed == 1102) {
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Haunga Warrior");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1114) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Troll Warrior");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1126) {
                        speakText("fere at taliya giy");
                        mobText.setText("Troll Warrior");
                    }else if (secondsPassed == 1144) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Reaper");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1149) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Dark Mare");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1162) {
                        speakText("Dark Mare dikkat");
                        mobText.setText("Volcanic Rock");
                        long desiredTimeInMillis = 4000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1166) {//1182
                        speakText("Magic Sheild bas Volkanik çıkıyor.");
                        mobText.setText("Volcanic Rock");
                    }else if (secondsPassed == 1420) {
                        speakText("fereler bitiyor yenile");
                    }
                }


                if (selectedButton == 3) {
                    buttonSelectedOpacity1();
                    if (secondsPassed == 2) {
                        speakText("Nova hazır, ikii, bir, bas");//2.5 sn konuşma süresi
                        mobText.setText("Lord Orc");
                        long desiredTimeInMillis = 5000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 8) { //+1 eklenebilir
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 36000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 14) { //-0.5 veya 1
                        speakText("Lord ork turu, Nova vurduysa bir, Meteor vurduysa iki");
                        mobText.setText("Orc Sniper");
                    }else if (secondsPassed == 44) { //-.5
                        speakText("Nova hazır, iki, bir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 50) {
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Troll Berserker");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 53) { // -1sn
                        speakText("Nova vurduysa bir, Meteor vurduysa iki");
                        mobText.setText("Baron");
                    }else if (secondsPassed == 57) {
                        speakText("Toz bas TeBeleri tekle");
                        mobText.setText("Baron");
                        long desiredTimeInMillis = 5000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 67) { // 1sn
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Death Knight");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 103) { // +1
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Crimson Wing");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 116) {
                        speakText("Toz bas CeVeleri tekle");
                        mobText.setText("Scolar");
                        long desiredTimeInMillis = 10000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 127) { // bırak dediğinde 2ye denk geliyor geç komut
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Tyon");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 132) { // bırak dediğinde 2ye denk geliyor geç komut
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Troll");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 140) {
                        speakText("Troll geliyor magic sheild bas");
                        mobText.setText("Ash Knight");
                        long desiredTimeInMillis = 52000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 191) {
                        speakText("Toz bas");
                        mobText.setText("Ash Knight");
                    }else if (secondsPassed == 192) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Haunga");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 204) { // ++++++++++
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Deruvish");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 234) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Lamia");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 246) { //
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Uruk Hai");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 266) {// bıraktan sonra komut başladı
                        speakText("Toz bas");
                        mobText.setText("Uruk Hai");
                    }else if (secondsPassed == 275) { // bıraktan sonra komut başladı
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Harpy");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 281) { // bıraktan sonra komut başladı
                        speakText("Meteor hazır, ikii, biir, bas");
                        mobText.setText("Dragon Tooth Knight");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 293) {// bırak deyince 1e denk geldi
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Uruk Blade");
                        long desiredTimeInMillis = 36000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 304) {//BOSS
                        speakText("Magic Sheild bas bosa zehir atmadan vur");
                        mobText.setText("UrukBlade");
                    }else if (secondsPassed == 327) {
                        speakText("Toz bas");
                        mobText.setText("UrukBlade");
                    }else if (secondsPassed == 335) { // bıraktan yarım sn sonra başladı.
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Wraith");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 341) { // 0.5olabilir
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Apostle");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 383) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Garuna");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 398) { //
                        speakText("Toz bas");
                        mobText.setText("Garuna");
                    }else if (secondsPassed == 400) { //
                        speakText("Meteor hazır, ikii, biir, bas");
                        mobText.setText("Lamiros");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 430) { ///4 geç başlıyor
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Deruvish");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 442) { //3sn geç başllıyor
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Blood Seeker");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 469) {
                        speakText("Toz bas");
                        mobText.setText("Blood Seeker");
                    }else if (secondsPassed == 472) {
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 483) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Stone Golem");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 489) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Haunga");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 501) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Bugbear");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 513) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Raven Harpy");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 547) { //
                        speakText("Toz bas");
                        mobText.setText("Raven Harpy");
                    }else if (secondsPassed == 549) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Sheriff");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 557) {//REBUFF
                        speakText("rebaf zamanı");
                        mobText.setText("Sheriff");
                    }else if (secondsPassed == 561) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Dragon Tooth Knight");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 573) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Lamentation");
                        long desiredTimeInMillis = 72000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 635) { // tam bıraka geliyor 41
                        speakText("Toz bas");
                        mobText.setText("Lamentation");
                    }else if (secondsPassed == 638) { //
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Dark Stone");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 652) {
                        speakText("deseleri tekle.");
                        mobText.setText("Lich");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 662) { //
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Hob Goblin");
                        long desiredTimeInMillis = 54000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 706) { //
                        speakText("Toz bas");
                        mobText.setText("Hob Goblin");
                    }else if (secondsPassed == 710) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Troll");
                        long desiredTimeInMillis = 9000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 721) {//733->736
                        speakText("Troll geliyor magic sheild bas");
                        mobText.setText("Uruk Tron");
                        long desiredTimeInMillis = 9000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 728) {  // ????????
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Sheriff");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 736) {
                        speakText("azıcık gezin seve bul");
                        mobText.setText("Sheriff");
                    }else if (secondsPassed == 811) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Manticore");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 816) { //
                        speakText("Manticore geliyor geç vur zehir yeme.");
                        mobText.setText("Burning Skeleton");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 829) { //6şar sn kayma var.
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Lamia");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 841) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Fallen Angel");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 868) {//fallen erken komut
                        speakText("Fallen geliyor, Magic Sheild bas toz bas çiftle full vur.");
                        mobText.setText("Fallen Angel");
                    }else if (secondsPassed == 876) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Grell");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 888) {// 3sn geç
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Mastadon");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 900) { //3sn geç
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Centaur");
                        long desiredTimeInMillis = 60000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 905) {//BOSS
                        speakText("Skorpiyon bosa zehir atmadan vur");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 945) {
                        speakText("Centaur son on saniye");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 950) {
                        speakText("Magic Sheild bas komutla vur");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 953) {// 956 da cıkıyor centaur bas demesi
                        speakText("Toz bas");
                        mobText.setText("Goblin Bouncer");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 960) {// 956 da cıkıyor centaur bas demesi
                        speakText("Centaur hazır baas");
                        mobText.setText("Goblin Bouncer");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 965) { //3sn ileri aldık 2 ye denk geliyor
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Harpy");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 989) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Tyon");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1022) {
                        speakText("Toz bas");
                        mobText.setText("Tyon");
                    }else if (secondsPassed == 1025) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Stone Golem");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1037) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Beast");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1049) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Gaint Golem");
                        long desiredTimeInMillis = 60000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1050) { //REBUFF
                        speakText("yapmadıysan şimdi rebaf yap");
                        mobText.setText("Gaint Golem");
                    }else if (secondsPassed == 1100) {//
                        speakText("Toz bas");
                        mobText.setText("Gaint Golem");
                    }else if (secondsPassed == 1108) {
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Haunga Warrior");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1114) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Troll Warrior");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1126) {
                        speakText("fere at taliya giy");
                        mobText.setText("Troll Warrior");
                    }else if (secondsPassed == 1144) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Reaper");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1155) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Dark Mare");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1162) {
                        speakText("Dark Mare dikkat");
                        mobText.setText("Volcanic Rock");
                        long desiredTimeInMillis = 4000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1173) {//1182
                        speakText("Magic Sheild bas Volkanik çıkıyor.");
                        mobText.setText("Volcanic Rock");
                    }else if (secondsPassed == 1420) {
                        speakText("fereler bitiyor yenile");
                    }



                }
                if (selectedButton == 4) {
                    buttonSelectedOpacity1();
                    if (secondsPassed == 2) {
                        speakText("Nova hazır, ikii, bir, bas");//2.5 sn konuşma süresi
                        mobText.setText("Lord Orc");
                        long desiredTimeInMillis = 5000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 8) { //+1 eklenebilir
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 36000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 14) { //-0.5 veya 1
                        speakText("Lord ork turu, Nova vurduysa bir, Meteor vurduysa iki");
                        mobText.setText("Orc Sniper");
                    }else if (secondsPassed == 44) { //-.5
                        speakText("Nova hazır, iki, bir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 50) {
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Troll Berserker");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 53) { // -1sn
                        speakText("Nova vurduysa bir, Meteor vurduysa iki");
                        mobText.setText("Baron");
                    }else if (secondsPassed == 57) {
                        speakText("Toz bas TeBeleri tekle");
                        mobText.setText("Baron");
                        long desiredTimeInMillis = 5000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 67) { // 1sn
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Death Knight");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 109) { // +1
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Crimson Wing");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 116) {
                        speakText("Toz bas CeVeleri tekle");
                        mobText.setText("Scolar");
                        long desiredTimeInMillis = 10000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 127) { // bırak dediğinde 2ye denk geliyor geç komut
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Tyon");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 138) { // bırak dediğinde 2ye denk geliyor geç komut
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Troll");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 143) {
                        speakText("Troll geliyor magic sheild bas");
                        mobText.setText("Ash Knight");
                        long desiredTimeInMillis = 52000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 197) {
                        speakText("Toz bas");
                        mobText.setText("Ash Knight");
                    }else if (secondsPassed == 198) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Haunga");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 204) { // ++++++++++
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Deruvish");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 234) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Lamia");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 246) { //
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Uruk Hai");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 273) {// bıraktan sonra komut başladı
                        speakText("Toz bas");
                        mobText.setText("Uruk Hai");
                    }else if (secondsPassed == 275) { // bıraktan sonra komut başladı
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Harpy");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 287) { // bıraktan sonra komut başladı
                        speakText("Meteor hazır, ikii, biir, bas");
                        mobText.setText("Dragon Tooth Knight");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 293) {// bırak deyince 1e denk geldi
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Uruk Blade");
                        long desiredTimeInMillis = 36000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 304) {//BOSS
                        speakText("Magic Sheild bas bosa zehir atmadan vur");
                        mobText.setText("UrukBlade");
                    }else if (secondsPassed == 327) {
                        speakText("Toz bas");
                        mobText.setText("UrukBlade");
                    }else if (secondsPassed == 335) { // bıraktan yarım sn sonra başladı.
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Wraith");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 347) { // 0.5olabilir
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Apostle");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 383) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Garuna");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 404) { //
                        speakText("Toz bas");
                        mobText.setText("Garuna");
                    }else if (secondsPassed == 406) { //
                        speakText("Meteor hazır, ikii, biir, bas");
                        mobText.setText("Lamiros");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 436) { ///4 geç başlıyor
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Deruvish");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 442) { //3sn geç başllıyor
                        speakText("Meteor hazır, iki, bir, bas");
                        mobText.setText("Blood Seeker");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 469) {
                        speakText("Toz bas");
                        mobText.setText("Blood Seeker");
                    }else if (secondsPassed == 472) {
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Orc Sniper");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 483) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Stone Golem");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 495) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Haunga");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 501) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Bugbear");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 513) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Raven Harpy");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 553) { //
                        speakText("Toz bas");
                        mobText.setText("Raven Harpy");
                    }else if (secondsPassed == 555) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Sheriff");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 561) {
                        speakText("Nova hazır, ikii, biir, baas");
                        mobText.setText("Dragon Tooth Knight");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 573) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Lamentation");
                        long desiredTimeInMillis = 72000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 580) {//REBUFF
                        speakText("rebaf zamanı");
                        mobText.setText(" ");
                    }else if (secondsPassed == 642) { //
                        speakText("Toz bas");
                        mobText.setText("Lamentation");
                    }else if (secondsPassed == 644) { //
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Dark Stone");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 652) {
                        speakText("deseleri tekle.");
                        mobText.setText("Lich");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 662) { //
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Hob Goblin");
                        long desiredTimeInMillis = 54000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 706) { //
                        speakText("Toz bas");
                        mobText.setText("Hob Goblin");
                    }else if (secondsPassed == 710) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Troll");
                        long desiredTimeInMillis = 9000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 721) {//733->736
                        speakText("Troll geliyor magic sheild bas");
                        mobText.setText("Uruk Tron");
                        long desiredTimeInMillis = 9000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 734) {  // ????????
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Sheriff");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 740) {
                        speakText("azıcık gezin seve bul");
                        mobText.setText("Sheriff");
                    }else if (secondsPassed == 811) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Manticore");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 821) { //
                        speakText("Manticore geliyor geç vur zehir yeme.");
                        mobText.setText("Burning Skeleton");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 829) { //6şar sn kayma var.
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Lamia");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 841) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Fallen Angel");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 875) {//fallen erken komut
                        speakText("Fallen geliyor, Magic Sheild bas toz bas çiftle full vur.");
                        mobText.setText("Fallen Angel");
                    }else if (secondsPassed == 882) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Grell");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 888) {//
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Mastadon");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 900) { //3sn geç
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Centaur");
                        long desiredTimeInMillis = 60000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 903) {//BOSS
                        speakText("Skorpiyon bosa zehir atmadan vur");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 945) {
                        speakText("Centaur son on saniye");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 950) {
                        speakText("Magic Sheild bas komutla vur");
                        mobText.setText("Centaur");
                    }else if (secondsPassed == 953) {// 956 da cıkıyor centaur bas demesi
                        speakText("Toz bas");
                        mobText.setText("Goblin Bouncer");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 960) {// 956 da cıkıyor centaur bas demesi
                        speakText("Centaur hazır baas");
                        mobText.setText("Goblin Bouncer");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 971) { //3sn ileri aldık 2 ye denk geliyor
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Harpy");
                        long desiredTimeInMillis = 18000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    } else if (secondsPassed == 989) { //
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Tyon");
                        long desiredTimeInMillis = 42000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1022) {
                        speakText("Toz bas");
                        mobText.setText("Tyon");
                    }else if (secondsPassed == 1031) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Stone Golem");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1037) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Beast");
                        long desiredTimeInMillis = 6000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1049) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Gaint Golem");
                        long desiredTimeInMillis = 60000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1055) { //REBUFF
                        speakText("yapmadıysan şimdi rebaf yap");
                        mobText.setText("Gaint Golem");
                    }else if (secondsPassed == 1106) {//
                        speakText("Toz bas");
                        mobText.setText("Gaint Golem");
                    }else if (secondsPassed == 1108) {
                        speakText("Nova hazır, ikii, biir, bas");
                        mobText.setText("Haunga Warrior");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1120) {
                        speakText("Meteor hazır, ikii, bir, bas");
                        mobText.setText("Troll Warrior");
                        long desiredTimeInMillis = 30000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1126) {
                        speakText("fere at taliya giy");
                        mobText.setText("Troll Warrior");
                    }else if (secondsPassed == 1150) {
                        speakText("Nova hazır, ikii, bir, bas");
                        mobText.setText("Reaper");
                        long desiredTimeInMillis = 12000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1155) {
                        speakText("Meteor hazır, ikii, biir, baas");
                        mobText.setText("Dark Mare");
                        long desiredTimeInMillis = 8000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1162) {
                        speakText("Dark Mare dikkat");
                        mobText.setText("Volcanic Rock");
                        long desiredTimeInMillis = 4000;
                        countdownTimerHelper.startTimerWithTime(desiredTimeInMillis);
                    }else if (secondsPassed == 1173) {//1182
                        speakText("Magic Sheild bas Volkanik çıkıyor.");
                        mobText.setText("Volcanic Rock");
                    }else if (secondsPassed == 1420) {
                        speakText("fereler bitiyor yenile");
                    }


                }

                handler.postDelayed(this, 1000); // Her 1 saniye sonra tekrar çalıştır
            }
        };
        handler.postDelayed(runnable, 1000); // İlk çalıştırma
    }

    private void pauseTimer() {
        timerRunning = false;
        startButton.setText("Start Timer");
        handler.removeCallbacks(runnable); // Runnable'ı durdur
    }

    // Örneğin, başka bir metot üzerinden "x" parametresini ayarlamak için
    private void setX(int newX) {
        x = newX;
    }
    private void updateCountdownText() {
        int minutes = secondsPassed / 60;
        int seconds = secondsPassed % 60;
        String timePassedFormatted = String.format("%02d:%02d", minutes, seconds);
        startButton.setText(timePassedFormatted);
    }

    private void speakText(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void toggleButtons() {
        // Start butonuna tıklandıktan sonra diğer butonları tıklanabilir yap
        button1.setEnabled(timerRunning);
        button2.setEnabled(timerRunning);
        button3.setEnabled(timerRunning);
        button4.setEnabled(timerRunning);
        geri.setEnabled(timerRunning);
        ileri.setEnabled(timerRunning);
    }
//SelectedOpacityleri runable içinde verdiğimizde 1000msde güncellendiği için opacity geçişleri delaylı olmakta.
    private void buttonSelectedOpacity1(){

        button1.setAlpha(0.5f);
        button1.setBackground(getDrawable(R.drawable.btn_selected_bg));
        button2.setAlpha(1f);
        button3.setAlpha(1f);
        button4.setAlpha(1f);
    }
    private void buttonSelectedOpacity2(){
        button1.setAlpha(1f);
        button2.setAlpha(0.5f);
        button2.setBackground(getDrawable(R.drawable.btn_selected_bg));
        button3.setAlpha(1f);
        button4.setAlpha(1f);
    }
    private void buttonSelectedOpacity3(){
        button1.setAlpha(1f);
        button2.setAlpha(1f);
        button3.setAlpha(0.5f);
        button3.setBackground(getDrawable(R.drawable.btn_selected_bg));
        button4.setAlpha(1f);
    }
    private void buttonSelectedOpacity4(){
        button1.setAlpha(1f);
        button2.setAlpha(1f);
        button3.setAlpha(1f);
        button4.setAlpha(0.5f);
        button4.setBackground(getDrawable(R.drawable.btn_selected_bg));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TextToSpeech'i kapat
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public void onTimerTick(long secondsUntilFinished, int progress) {
//        progressBar.setProgress(progress);
//        tvTimer.setText(String.valueOf(secondsUntilFinished));

        long minutesRemaining = secondsUntilFinished / 60;
        long secondsRemaining = secondsUntilFinished % 60;
        String timeString = String.format("%02d:%02d", minutesRemaining, secondsRemaining);
        tvTimer.setText(timeString);

        progressBar.setProgress((int) (minutesRemaining*100));

    }

    @Override
    public void onTimerFinish() {
        progressBar.setProgress(0);
        //tvTimer.setText("Süre doldu!");
    }
}

