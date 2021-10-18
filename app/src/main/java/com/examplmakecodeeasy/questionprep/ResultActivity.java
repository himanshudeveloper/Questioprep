package com.examplmakecodeeasy.questionprep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.examplmakecodeeasy.questionprep.databinding.ActivityResultBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultActivity extends AppCompatActivity {


    ActivityResultBinding binding;
    int POINTS =10;
    String url = "https://play.google.com/store/apps/details?id=com.examplmakecodeeasy.questionprep";
    //private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());







        int CorrectAnswers = getIntent().getIntExtra("correct",0);
        int totalQuestions = getIntent().getIntExtra("total",0);

        long point = CorrectAnswers * POINTS;

        binding.score.setText(String.format("%d/%d",CorrectAnswers,totalQuestions));
        binding.earncoins.setText(String.valueOf(point));

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(point));
//
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });

//        AdRequest adRequest = new AdRequest.Builder().build();

//        InterstitialAd.load(this,"ca-app-pub-7167914745572974/4990531942", adRequest,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded( InterstitialAd interstitialAd) {
//                        // The mInterstitialAd reference will be null until
//                        // an ad is loaded.
//                        mInterstitialAd = interstitialAd;
//                        Log.i(TAG, "onAdLoaded");
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error
//                        Log.i(TAG, loadAdError.getMessage());
//                        mInterstitialAd = null;
//                    }
//                });




        binding.btnrestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finishAffinity();
//                if (mInterstitialAd != null) {
//                    mInterstitialAd.show(ResultActivity.this);
//                } else {
//                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
//                }

            }
        });

        binding.btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                Intent intent = sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });


    }


    @Override
    protected void onDestroy() {


        super.onDestroy();
    }





}