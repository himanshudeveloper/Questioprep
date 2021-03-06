package com.examplmakecodeeasy.questionprep;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.examplmakecodeeasy.questionprep.databinding.ActivityMainBinding;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    BroadcastReceiver mBroadcastReceiver;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBroadcastReceiver = new Connection();
        registoreNetworkBroadcast();




        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });






        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content,new HomeFragment());
        transaction.commit();


       binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
           @Override
           public boolean onItemSelect(int i) {
               FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
               switch (i){
                   case 0:
                       transaction.replace(R.id.content,new HomeFragment());
                       transaction.commit();

                       break;
                   case 1:
                       transaction.replace(R.id.content,new LeadorboardFragment());
                       transaction.commit();
                       break;
                   case 2:
                       transaction.replace(R.id.content,new walletFragment());
                       transaction.commit();
                       break;
                   case 3:
                       transaction.replace(R.id.content,new userProfile_Fragment());
                       transaction.commit();
                       break;

               }

               return false;
           }
       });










    }

    private void registoreNetworkBroadcast() {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.wallet) {
            Toast.makeText(this, "wallet is clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterdNetwork();
    }
}