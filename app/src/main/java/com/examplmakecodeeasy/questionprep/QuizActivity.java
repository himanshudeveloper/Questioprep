package com.examplmakecodeeasy.questionprep;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.examplmakecodeeasy.questionprep.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    BroadcastReceiver mBroadcastReceiver;
    ActivityQuizBinding binding;
    ArrayList<Question> questions;
    int index=0;
    Question question;
    CountDownTimer timer;
    FirebaseFirestore database;
    int CorrectAnswer = 0;
    Boolean next = false;
    ArrayList<CategoryModel> categoryModels;


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
       final String catName = getIntent().getStringExtra("catName");
       setTitle(catName+"");

       binding.questionfloatingbutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(QuizActivity.this,AddQuestionActivity.class);
               intent.putExtra("catId",catId);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               intent.putExtra("catName",catName);
               startActivity(intent);
           }
       });
       quitButton();

                  database.collection("categories")
                .document(catId)
                .collection("questions")
              //  .whereGreaterThanOrEqualTo("index",rand)
               // .orderBy("index")
               // .limit(200)
                          .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() < 50){
                    database.collection("categories")
                            .document(catId)
                            .collection("questions")
                           // .whereLessThanOrEqualTo("index",rand)
                          //  .orderBy("index")
                          //  .limit(50)
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                        ///Toast.makeText(QuizActivity.this, "false", Toast.LENGTH_SHORT).show();
                        questions.add(question);
                    }
                    setNextQuestion();
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                }

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
            binding.questionCounter.setText(String.format("%d/%d",(index)+1,questions.size()));
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
        String selectedAnswer = textView.getText().toString().trim().toLowerCase().replaceAll("\\s","");

        if (selectedAnswer.equals(question.getAnswer().trim().toLowerCase().replaceAll("\\s",""))) {
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
                    if (index < questions.size()-1) {
                        applyAnimations();

                        index++;
                        setNextQuestion();

                     next = false;
                    } else {

                        Toast.makeText(this, "Quiz finished", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                        intent.putExtra("correct", CorrectAnswer);
                        intent.putExtra("total", questions.size());
                        startActivity(intent);
                        index =1;
                        finish();


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
                finishAffinity();

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
    private   void  applyAnimations (){
        binding.nextBtn.setClickable(false);
        binding.quizbtn.setClickable(false);



        binding.option1.animate().alpha(0.3f).translationXBy(200f).setDuration(400L);
        binding.option2.animate().alpha(0.3f).translationXBy(-200f).setDuration(400L);
        binding.option3.animate().alpha(0.3f).translationXBy(200f).setDuration(400L);
        binding.option4.animate().alpha(0.3f).translationXBy(-200f).setDuration(400L);
        binding.nextBtn.animate().alpha(0.3f).translationYBy(200f).setDuration(400L);
        binding.quizbtn.animate().alpha(0.3f).translationYBy(200f).setDuration(400L);
        binding.question.animate().alpha(0.3f).translationYBy(-200f).setDuration(400L);


        Thread td = new Thread() {

            public void run() {
                try {
                    sleep(800);

                } catch (Exception ex) {
                    ex.printStackTrace();

                }
                finally{
                    destroyAnimations();

                }
            }

        };td.start();




    }
    private  void  destroyAnimations(){
        binding.nextBtn.setClickable(true);
        binding.quizbtn.setClickable(true);
//        binding.option1.setEnabled(true);
//        binding.option4.setEnabled(true);
//        binding.option2.setEnabled(true);
//        binding.option3.setEnabled(true);


        binding.option1.animate().alpha(1f).translationXBy(-200f).setDuration(400L);
        binding.option2.animate().alpha(1f).translationXBy(200f).setDuration(400L);
        binding.option3.animate().alpha(1f).translationXBy(-200f).setDuration(400L);
        binding.option4.animate().alpha(1f).translationXBy(200f).setDuration(400L);
        binding.nextBtn.animate().alpha(1f).translationYBy(-200f).setDuration(400L);
        binding.quizbtn.animate().alpha(1f).translationYBy(-200f).setDuration(400L);
        binding.question.animate().alpha(1f).translationYBy(200f).setDuration(400L);


        Thread td = new Thread() {

            public void run() {
                try {
                    sleep(400);

                } catch (Exception ex) {
                    ex.printStackTrace();

                }
                finally{

                }
            }

        };td.start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterdNetwork();


        if(questions!=null){
            questions.clear();


        }


    }


}