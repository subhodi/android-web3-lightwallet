package com.persistent.subhod_i.digitallocker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.content.Context;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;


public class MainActivity extends AppCompatActivity {
    Wallet wallet = new Wallet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Welcome to Eth-wallet", Toast.LENGTH_LONG).show();
        checkPermissions();
//        wallet.createWallet();
        makeTransaction();
    }

    private void addNotification(String transactionHash) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.notification_icon);
        mBuilder.setContentTitle("New transaction sent");
        mBuilder.setContentText("Tx hash: "+transactionHash);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(101, mBuilder.build());
    }
    private void checkPermissions() {
        String TAG = "Permisiion";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");
            } else {
                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted1");
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
            } else {
                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2");
        }
    }

    private void makeTransaction() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Web3j web3 = wallet.constructWeb3();
                    Credentials credentials = wallet.loadCredentials();
                    String transactionHash = wallet.sendTransaction(web3, credentials);
                    addNotification(transactionHash);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error ", "Web3 connection error " + e.toString());
                }
            }
        });

        thread.start();
    }
}
