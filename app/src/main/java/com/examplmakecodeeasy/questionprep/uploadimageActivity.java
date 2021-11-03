package com.examplmakecodeeasy.questionprep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.examplmakecodeeasy.questionprep.databinding.ActivityUploadimageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;

public class uploadimageActivity extends AppCompatActivity {
    ActivityUploadimageBinding mActivityUploadimageBinding;
    private Uri filepath;
    private final int PICK_IMAGE_REQUEST=22;

    FirebaseStorage mStorage;
    StorageReference mStorageReference;
    FirebaseFirestore database;
    User user;
    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityUploadimageBinding = ActivityUploadimageBinding.inflate(getLayoutInflater());
        setContentView(mActivityUploadimageBinding.getRoot());


        mStorage= FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();
        database = FirebaseFirestore.getInstance();
        loadimage();
        mActivityUploadimageBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                mActivityUploadimageBinding.edtname.setText(String.valueOf(user.getName()));
                mActivityUploadimageBinding.txtemailupload.setText(String.valueOf(user.getEmail()));

            }
        });
        mActivityUploadimageBinding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });



        mActivityUploadimageBinding.imageselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        mActivityUploadimageBinding.btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s= mActivityUploadimageBinding.edtname.getText().toString();
                uploadImage();


                database.collection("users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .update("name",s).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Intent intent = new Intent(uploadimageActivity.this,MainActivity.class);
                        startActivity(intent);

                    }
                });



            }
        });
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
                            .into(mActivityUploadimageBinding.imageselect);
              }
            });
    }

    private void chooseImage() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(
                Intent.createChooser(intent,"Select Image from here..."),PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null){
            filepath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                mActivityUploadimageBinding.imageselect.setImageBitmap(bitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void uploadImage(){

        if (filepath !=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("uploading Image");
            progressDialog.show();
            final StorageReference reference = mStorageReference.child("images/" + FirebaseAuth.getInstance().getUid());

            reference.putFile(filepath).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(uploadimageActivity.this,MainActivity.class);
                            startActivity(intent);

                            mStorageReference.child("images/" + FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    database.collection("users")
                                            .document(FirebaseAuth.getInstance().getUid())
                                            .update("profile",uri+"");

                                    img = String.valueOf(uri);

                                    // Got the download URL for 'users/me/profile.png'
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

                            Toast.makeText(uploadimageActivity.this, "Image Uploaded!..", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(uploadimageActivity.this, "Image Not Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double Progress = (100.0 * snapshot.getBytesTransferred()
                    /snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+ (int)Progress + "%");
                    progressDialog.setCancelable(false);
                    finish();


                }
            });
        }
    }

}