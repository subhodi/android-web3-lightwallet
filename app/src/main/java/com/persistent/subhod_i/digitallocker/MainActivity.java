package com.persistent.subhod_i.digitallocker;

import android.Manifest;
import android.annotation.TargetApi;
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
import org.web3j.utils.Numeric;

import java.util.Collections;

import static java.lang.String.join;
import static org.web3j.utils.Numeric.hexStringToByteArray;


public class MainActivity extends AppCompatActivity {
    Wallet wallet = new Wallet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Welcome to Eth-wallet", Toast.LENGTH_LONG).show();
        checkPermissions();
 //      wallet.createWallet();
        makeTransaction();
    }

    private void addNotification(String transactionHash) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.notification_icon);
        mBuilder.setContentTitle("New transaction sent");
        mBuilder.setContentText("Tx hash: "+transactionHash);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(102, mBuilder.build());
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
                    Web3j web3j = wallet.constructWeb3();
                    Credentials credentials = wallet.loadCredentials();
//                    String transactionHash = wallet.sendTransaction(web3j, credentials);
//                    addNotification(transactionHash);
//                    wallet.bip();
//                    String contractAddress = wallet.deployContract(web3j, credentials);
//                    Log.e("Contract deployment", contractAddress);
//                    addNotification(contractAddress);
//                    String transactionHash = wallet.contractTransaction(web3j, credentials);
//                    Log.e("Contract deployment", transactionHash);
//                        wallet.queryContract(web3j);
                    Contract contract = new Contract(web3j,credentials);
//                    String contractAddress = contract.deploy();
//                    Log.e("Contract" , contractAddress);

                    String transactionHash= contract.open("alice", Numeric.hexStringToByteArray(asciiToHex("myString")));
                    Log.e("Contract", transactionHash);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Web3 Error ",  e.toString());
                }
            }
        });

        thread.start();
    }

    @TargetApi(Build.VERSION_CODES.O)
    public  String asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString() + "".join("", Collections.nCopies(32 - (hex.length()/2), "00"));
    }

}
