package com.examplmakecodeeasy.questionprep;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.examplmakecodeeasy.questionprep.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class profileFragment extends Fragment {





    public profileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

    }
    FragmentProfileBinding binding;
    FirebaseFirestore database;
    User user;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();
        loadimage();
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);

            }
        });

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




        binding.updateprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s= binding.edtnam.getText().toString();


                database.collection("users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .update("name",s);
                Toast.makeText(getContext(), "profile updated", Toast.LENGTH_SHORT).show();



            }
        });

        binding.btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getContext(),SignupActivity.class);
                startActivity(intent);
                getActivity().finish();

            }

        });




        // Inflate the layout for this fragment
    return  binding.getRoot();
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
                        .resize(50, 50)
                        .centerCrop()
                        .into(binding.profileImage);






            }
        });

    }


}