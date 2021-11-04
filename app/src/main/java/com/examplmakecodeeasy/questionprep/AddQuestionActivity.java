package com.examplmakecodeeasy.questionprep;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.examplmakecodeeasy.questionprep.databinding.ActivityAddQuestionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddQuestionActivity extends AppCompatActivity {
    ActivityAddQuestionBinding binding;
    FirebaseFirestore database;
    int num;
    BroadcastReceiver mBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mBroadcastReceiver = new Connection();
        registoreNetworkBroadcast();




        setTitle(getIntent().getStringExtra("catName"));

        database= FirebaseFirestore.getInstance();
        final String catId = getIntent().getStringExtra("catId");


        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!(TextUtils.isEmpty(binding.edtquestion.getText()))) {

                    if (!(TextUtils.isEmpty(binding.edtoption1.getText()))) {

                        if (!(TextUtils.isEmpty(binding.edtoption2.getText()))) {
                            if (!(TextUtils.isEmpty(binding.edtoption3.getText()))) {
                                if (!(TextUtils.isEmpty(binding.edtoption4.getText()))) {
                                    if (!(TextUtils.isEmpty(binding.edtanswer.getText()))) {

                                        applyAnimations();

                                        String question = binding.edtquestion.getText().toString();
                                        String option1 = binding.edtoption1.getText().toString();
                                        String option2 = binding.edtoption2.getText().toString();
                                        String option3 = binding.edtoption3.getText().toString();
                                        String option4 = binding.edtoption4.getText().toString();
                                        String answer = binding.edtanswer.getText().toString();

                                        //textview invisible


                                        database.collection("categories")
                                                .document(catId)
                                                .collection("questions")


                                                .orderBy("index")
                                                .limit(200).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                num = queryDocumentSnapshots.getDocuments().size();


                                            }
                                        });


                                        ///   Create a new user with a first and last name
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("question", question);
                                        user.put("option1", option1);
                                        user.put("option2", option2);
                                        user.put("option3", option3);
                                        user.put("option4", option4);
                                        user.put("answer", answer);
                                        user.put("index", num);


                                        database.collection("categories")
                                                .document(catId)
                                                .collection("questions")
                                                .add(user)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(AddQuestionActivity.this, "question added", Toast.LENGTH_SHORT).show();
                                                        destroyAnimations();
                                                        binding.edtquestion.getText().clear();
                                                        binding.edtoption1.getText().clear();
                                                        binding.edtoption2.getText().clear();
                                                        binding.edtoption3.getText().clear();
                                                        binding.edtoption4.getText().clear();
                                                        binding.edtanswer.getText().clear();


                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        destroyAnimations();
                                                        Toast.makeText(AddQuestionActivity.this, "not question added", Toast.LENGTH_SHORT).show();

                                                    }
                                                });


                                    } else {
                                        Toast.makeText(AddQuestionActivity.this, "please fill answer field", Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(AddQuestionActivity.this, "please fill option4 field", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(AddQuestionActivity.this, "please fill option3 field", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(AddQuestionActivity.this, "please fill option2 field", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(AddQuestionActivity.this, "please fill option1 field", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AddQuestionActivity.this, "please fill option1 field", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void destroyAnimations() {
        binding.edtanswer.animate().alpha(1f).setDuration(400L);
        binding.edtquestion.animate().alpha(1f).setDuration(400L);
        binding.edtoption1.animate().alpha(1f).setDuration(400L);
        binding.edtoption2.animate().alpha(1f).setDuration(400L);
        binding.edtoption3.animate().alpha(1f).setDuration(400L);
        binding.edtoption4.animate().alpha(1f).setDuration(400L);
        binding.submit.animate().alpha(1f).setDuration(400L);
        binding.txtxl1.animate().alpha(1f).translationXBy(-1200f).setDuration(400L);
        binding.txtl2.animate().alpha(1f).translationXBy(-1200f).setDuration(400L);
        binding.txtl3.animate().alpha(1f).translationXBy(-1200f).setDuration(400L);
        binding.txt4.animate().alpha(1f).translationXBy(-1200f).setDuration(400L);
        binding.txt5.animate().alpha(1f).translationXBy(-1200f).setDuration(400L);
        binding.txt6.animate().alpha(1f).translationXBy(-1200f).setDuration(400L);


        Thread td = new Thread() {

            public void run() {
                try {
                    sleep(400);

                } catch (Exception ex) {
                    ex.printStackTrace();

                }
                finally{


                    binding.sucessbackground.animate().alpha(0f).setDuration(600L);
                    binding.sucessbackground.animate().rotation(-720f).setDuration(600L);
                    binding.sucessbackground.animate().scaleXBy(-900f).setDuration(800L);
                    binding.sucessbackground.animate().scaleYBy(-900f).setDuration(800L);
                }
            }

        };td.start();
        binding.edtanswer.setEnabled(true);
        binding.edtquestion.setEnabled(true);
        binding.edtoption1.setEnabled(true);
        binding.edtoption2.setEnabled(true);
        binding.edtoption3.setEnabled(true);
        binding.edtoption4.setEnabled(true);


    }

    private void applyAnimations() {
        binding.edtanswer.setEnabled(false);
        binding.edtquestion.setEnabled(false);
        binding.edtoption1.setEnabled(false);
        binding.edtoption2.setEnabled(false);
        binding.edtoption3.setEnabled(false);
        binding.edtoption4.setEnabled(false);

        binding.edtanswer.animate().alpha(0f).setDuration(400L);
        binding.edtquestion.animate().alpha(0f).setDuration(400L);
        binding.edtoption1.animate().alpha(0f).setDuration(400L);
        binding.edtoption2.animate().alpha(0f).setDuration(400L);
        binding.edtoption3.animate().alpha(0f).setDuration(400L);
        binding.edtoption4.animate().alpha(0f).setDuration(400L);
        binding.submit.animate().alpha(0f).setDuration(400L);
        binding.txtxl1.animate().alpha(0f).translationXBy(1200f).setDuration(400L);
        binding.txtl2.animate().alpha(0f).translationXBy(1200f).setDuration(400L);
        binding.txtl3.animate().alpha(0f).translationXBy(1200f).setDuration(400L);
        binding.txt4.animate().alpha(0f).translationXBy(1200f).setDuration(400L);
        binding.txt5.animate().alpha(0f).translationXBy(1200f).setDuration(400L);
        binding.txt6.animate().alpha(0f).translationXBy(1200f).setDuration(400L);

        Thread td = new Thread() {

            public void run() {
                try {
                    sleep(400);

                } catch (Exception ex) {
                    ex.printStackTrace();

                }
                finally{

                    binding.sucessbackground.animate().alpha(1f).setDuration(600L);
                    binding.sucessbackground.animate().rotation(720f).setDuration(600L);
                    binding.sucessbackground.animate().scaleXBy(900f).setDuration(800L);
                    binding.sucessbackground.animate().scaleYBy(900f).setDuration(800L);
                }
            }

        };td.start();
    }



    private void registoreNetworkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(mBroadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterdNetwork();
        binding=null;
    }

    private void unregisterdNetwork() {
        try{
            unregisterReceiver(mBroadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}