package com.examplmakecodeeasy.questionprep;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.examplmakecodeeasy.questionprep.databinding.FragmentLeadorboardBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class LeadorboardFragment extends Fragment {


    public LeadorboardFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

    }

    FragmentLeadorboardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLeadorboardBinding.inflate(inflater,container,false);

        FirebaseFirestore database = FirebaseFirestore.getInstance();

       final ArrayList<User> users = new ArrayList<>();
        final LeaderbordsAdapter adapter = new LeaderbordsAdapter(getContext(),users);

        binding.Recyclerview.setAdapter(adapter);
        binding.Recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));


        database.collection("users")
                .orderBy("coins", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                for ( DocumentSnapshot snapshot : queryDocumentSnapshots ){
                  User user = snapshot.toObject(User.class);
                  users.add(user);

              }
                adapter.notifyDataSetChanged();
                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(View.GONE);


            }
        });

        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.shimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.shimmerViewContainer.startShimmer();

    }
}