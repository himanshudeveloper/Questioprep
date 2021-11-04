package com.examplmakecodeeasy.questionprep;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.examplmakecodeeasy.questionprep.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karpuzoglu.enes.com.fastdialog.Animations;
import karpuzoglu.enes.com.fastdialog.FastDialog;
import karpuzoglu.enes.com.fastdialog.FastDialogBuilder;
import karpuzoglu.enes.com.fastdialog.Positions;
import karpuzoglu.enes.com.fastdialog.PositiveClick;
import karpuzoglu.enes.com.fastdialog.Type;


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

        final List<SlideModel> remoteImages = new ArrayList<>();

         FirebaseDatabase.getInstance().getReference().child("Slider")
                 .addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         for(DataSnapshot data: snapshot.getChildren())
                         {
                             remoteImages.add(new SlideModel(data.child("url").getValue().toString(),data.child("title").getValue().toString(), ScaleTypes.FIT));

                         }
                         binding.imageView.setImageList(remoteImages,ScaleTypes.FIT);

//                         binding.imageView.setItemClickListener(new ItemClickListener() {
//                             @Override
//                             public void onItemSelected(int i) {
//
//                                 Toast.makeText(getContext(), remoteImages.get(i).getTitle().toString(), Toast.LENGTH_SHORT).show();
//
//                             }
//                         });

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });


        binding.AddCategoryFloatingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FastDialog dialog= new FastDialogBuilder(getContext(), Type.INFO)
                        .setTitleText("Category")
                        .setText("Add new Category")
                        .positiveText("Ok")
                        .negativeText("Cancel")
                        .changeColor(ContextCompat.getColor(getContext(),R.color.different),
                                ContextCompat.getColor(getContext(),R.color.text2),
                                ContextCompat.getColor(getContext(),R.color.text))
                        .setHint("please enter Category name")
                        .setAnimation(Animations.GROW_IN)
                        .setPosition(Positions.TOP)
                        .create();
                dialog.positiveClickListener(new PositiveClick() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Toast.makeText(getContext(),dialog.getInputText().equals("")?"Field is Empty":dialog.getInputText(),Toast.LENGTH_SHORT).show();
                        if (!(TextUtils.isEmpty(dialog.getInputText()))) {

                            String Name =dialog.getInputText();


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
                                            Toast.makeText(getContext(), " Category Added", Toast.LENGTH_SHORT).show();


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Not Category Added"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        }else{
                            Toast.makeText(getContext(), "Please Enter Valid Category Name", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                dialog.show();
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