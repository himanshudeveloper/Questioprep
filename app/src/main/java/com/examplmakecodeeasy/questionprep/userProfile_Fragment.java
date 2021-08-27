package com.examplmakecodeeasy.questionprep;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.examplmakecodeeasy.questionprep.databinding.FragmentUserProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class userProfile_Fragment extends Fragment {

    public userProfile_Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

    }
    FragmentUserProfileBinding binding;
    FirebaseFirestore database;
    User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUserProfileBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();
        loadimage();



        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                binding.edtnam.setText(String.valueOf(user.getName()));
                binding.txtemail.setText(String.valueOf(user.getEmail()));




            }
        });

        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),uploadimageActivity.class);
                intent.putExtra("name",String.valueOf(user.getName()));
                intent.putExtra("email",String.valueOf(user.getEmail()));

                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void loadimage() {
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);


                String  url =String.valueOf(user.getProfile());

                Picasso.get()
                        .load(url)
                        .resize(512,512)
                        .centerCrop()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.loading)
                        .into(binding.profileImage);


            }
        });



            }
}