package com.examplmakecodeeasy.questionprep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.examplmakecodeeasy.questionprep.databinding.ActivitySpinBinding;

public class spinActivity extends AppCompatActivity {
    ActivitySpinBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivitySpinBinding.inflate(getLayoutInflater());

        setContentView(bind.getRoot());
    }
}