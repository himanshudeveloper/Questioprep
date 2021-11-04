package com.examplmakecodeeasy.questionprep;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.examplmakecodeeasy.questionprep.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

    }
    FragmentHomeBinding binding;
    FirebaseFirestore database;





    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();




       final ArrayList<CategoryModel> categories = new ArrayList<>();
        final CategoryAdapter adapter = new CategoryAdapter(getContext(),categories);

        binding.AddCategoryFloatingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddNewCategoryActivity.class);
                startActivity(intent);
            }
        });



        database.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categories.clear();
                        for (DocumentSnapshot snapshot: value.getDocuments()){
                            CategoryModel model = snapshot.toObject(CategoryModel.class);
                            model.setCategoryId(snapshot.getId());
                            categories.add(model);
                        }

//                        binding.categoruList.setVisibility(View.VISIBLE);
                      adapter.notifyDataSetChanged();
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);


                    }
                });


        binding.categoruList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoruList.setAdapter(adapter);

        database.collection("image")
                .document("hkkk")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String url = (String) documentSnapshot.get("hk");

//                 Glide.with(getContext())
//                .load(url)
//                .into(binding.imageView);

                Picasso.get().load(url)
                        .placeholder(R.drawable.welcome)
                        .error(R.drawable.welcome)
                        .into(binding.imageView);


            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.shimmerViewContainer.startShimmer();
        //binding.temp.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.shimmerViewContainer.startShimmer();
        //binding.temp.setVisibility(View.VISIBLE);
    }
}