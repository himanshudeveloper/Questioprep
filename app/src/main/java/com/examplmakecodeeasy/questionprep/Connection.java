package com.examplmakecodeeasy.questionprep;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class Connection extends BroadcastReceiver {
    Context mcontext;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (isConnected(context)){


        }else{
            showDialog(context);

            Toast.makeText(context, "Internet not connected", Toast.LENGTH_SHORT).show();

        }




    }

    public void showDialog(Context context){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_internet,null);
        Button btn = view.findViewById(R.id.refresh);

        builder.setView(view);

        final Dialog dialog = builder.create();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();


    }

    public  boolean isConnected(Context context){

        try{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo !=null && networkInfo.isConnected());


        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }


    }
}
