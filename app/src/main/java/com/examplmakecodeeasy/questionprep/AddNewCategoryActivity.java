package com.examplmakecodeeasy.questionprep;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.examplmakecodeeasy.questionprep.databinding.ActivityAddNewCategoryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNewCategoryActivity extends AppCompatActivity {
    FirebaseFirestore database;
    ActivityAddNewCategoryBinding binding;
    BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAddNewCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mBroadcastReceiver = new Connection();
        registoreNetworkBroadcast();
        setContentView(R.layout.activity_add_new_category);

        database = FirebaseFirestore.getInstance();

        binding.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddNewCategoryActivity.this,"hay", Toast.LENGTH_SHORT).show();

                if (!(TextUtils.isEmpty(binding.name.getText()))) {

                    String Name = binding.name.getText().toString();


                    ///   Create a new user with a first and last name
                    Map<String, Object> user = new HashMap<>();
                    user.put("categoryName", Name);
                    user.put("categoryImage", "Lovelace");

                    // Add a new document with a generated ID
                    database.collection("categories")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(AddNewCategoryActivity.this, " Category Added", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddNewCategoryActivity.this,MainActivity.class);
                                    startActivity(intent);

                                    finishAffinity();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddNewCategoryActivity.this, "Not Category Added"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                }else{
                    Toast.makeText(AddNewCategoryActivity.this, "Please Enter Valid Category Name", Toast.LENGTH_SHORT).show();
                }




            }
        });



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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterdNetwork();
    }
}