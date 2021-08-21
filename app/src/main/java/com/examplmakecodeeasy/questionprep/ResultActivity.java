package com.examplmakecodeeasy.questionprep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.examplmakecodeeasy.questionprep.databinding.ActivityResultBinding;
import com.examplmakecodeeasy.questionprep.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    int POINTS =10;


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


        binding.btnrestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finishAffinity();

            }
        });





    }



}