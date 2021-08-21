package com.examplmakecodeeasy.questionprep;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.examplmakecodeeasy.questionprep.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);


    }

    FragmentHomeBinding binding;
    FirebaseFirestore database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();
       EventListener eventListener;

       final ArrayList<CategoryModel> categories = new ArrayList<>();
        final CategoryAdapter adapter = new CategoryAdapter(getContext(),categories);

        database.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categories.clear();
                        for (DocumentSnapshot snapshot: value.getDocuments()){
                            CategoryModel model =snapshot.toObject(CategoryModel.class);
                            model.setCategoryId(snapshot.getId());
                            categories.add(model);

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        binding.categoruList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoruList.setAdapter(adapter);



        final ArrayList<ImageMode> images = new ArrayList<>();
        final  ImageAdapter adapter1 =new ImageAdapter(getContext(),images);

        database.collection("Images")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        images.clear();
                        for (DocumentSnapshot snapshot: value.getDocuments()){
                            ImageMode mode = snapshot.toObject(ImageMode.class);
                            mode.setImageid(snapshot.getId());

                            images.add(mode);

                        }
                        adapter1.notifyDataSetChanged();

                    }
                });
        binding.imageList.setLayoutManager(new GridLayoutManager(getContext(),1));
        binding.imageList.setAdapter(adapter1);





        return binding.getRoot();
    }
}