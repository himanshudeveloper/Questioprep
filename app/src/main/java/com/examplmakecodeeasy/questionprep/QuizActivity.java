package com.examplmakecodeeasy.questionprep;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.examplmakecodeeasy.questionprep.databinding.ActivityQuizBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    BroadcastReceiver mBroadcastReceiver;
    ActivityQuizBinding binding;
    ArrayList<Question> questions;
    int index=0;
    Question question;
    CountDownTimer timer;
    FirebaseFirestore database;
    int CorrectAnswer = 0;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    Boolean next = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        mBroadcastReceiver = new Connection();
        registoreNetworkBroadcast();

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
       final String catId = getIntent().getStringExtra("catId");
        Random random = new Random();
       final int rand = random.nextInt(50);
       quitButton();
        mAdView = findViewById(R.id.adView);
        final AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest1);




        database.collection("categories")
                .document(catId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index",rand)
                .orderBy("index")
                .limit(200).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() < 50){
                    database.collection("categories")
                            .document(catId)
                            .collection("questions")
                            .whereLessThanOrEqualTo("index",rand)
                            .orderBy("index")
                            .limit(50).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                for (DocumentSnapshot snapshot :queryDocumentSnapshots){
                                    Question question = snapshot.toObject(Question.class);
                                    questions.add(question);
                                }
                                binding.shimmerViewContainer.stopShimmer();
                                binding.shimmerViewContainer.setVisibility(View.GONE);
                            setNextQuestion();

                        }
                    });

                }else {
                    for (DocumentSnapshot snapshot :queryDocumentSnapshots){
                        Question question = snapshot.toObject(Question.class);
                        Toast.makeText(QuizActivity.this, "false", Toast.LENGTH_SHORT).show();
                        questions.add(question);
                    }
                    setNextQuestion();
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                }

            }
        });
        final AdRequest adRequest = new AdRequest.Builder().build();



        InterstitialAd.load(this,"ca-app-pub-4252816301618953/5959478193", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        super.onAdFailedToLoad(loadAdError);

                    }
                });
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();

            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest1);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Toast.makeText(QuizActivity.this, "Don't click on ad", Toast.LENGTH_SHORT).show();
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


       resetTimer();
       setNextQuestion();



    }
    protected void registoreNetworkBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(mBroadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        }
    }
    protected void unregisterdNetwork(){
        try{
            unregisterReceiver(mBroadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }




    void resetTimer(){
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));

            }

            @Override
            public void onFinish() {

            }
        };
    }

    void showAnswer(){
        if (question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else  if (question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else  if (question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else  if (question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
    }
    void setNextQuestion(){
        if (timer != null)
            timer.cancel();




        if (index<questions.size()){
            binding.questionCounter.setText(String.format("%d/%d",(index+1),questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());

            timer.start();


        }
    }
    void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString().trim().toLowerCase();

        if (selectedAnswer.equals(question.getAnswer().trim().toLowerCase())) {
            CorrectAnswer++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }
    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));


    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:

                TextView selected = (TextView) view;
                checkAnswer(selected);
                next = true;
                if (timer !=null)
                    timer.cancel();

                break;
            case R.id.nextBtn:
                if(next) {
                    reset();
                    if (index < questions.size()) {
                        index++;

                        setNextQuestion();
                        next = false;

                    } else {
                        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                        intent.putExtra("correct", CorrectAnswer);
                        intent.putExtra("total", questions.size());
                        startActivity(intent);

                        Toast.makeText(this, "Quiz finished", Toast.LENGTH_SHORT).show();

                    }
                    break;
                }else{
                    Toast.makeText(QuizActivity.this, "please select option", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void quitButton(){
        binding.quizbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(QuizActivity.this, MainActivity.class);
                startActivity(intent1);
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(QuizActivity.this);
                }

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.shimmerViewContainer.startShimmer();

    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.shimmerViewContainer.startShimmer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(questions!=null){
            questions.removeAll(Collections.singleton(true));
            unregisterdNetwork();



            questions = null;

        }





    }
}