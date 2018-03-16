package com.persistent.subhod_i.digitallocker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
//import org.web3j.sample.contracts.generated.Greeter;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import org.web3j.protocol.Web3jFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this,
                "Your Message", Toast.LENGTH_LONG).show();
//        Credentials credentials = WalletUtils.loadCredentials("dsdfs","/path/to/<walletfile>");
        try {
            String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();
            Log.e("path", path);
            String TAG = "Permisiion";
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted1");
                } else {

                    Log.v(TAG, "Permission is revoked1");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);

                }
            } else { //permission is automatically granted on sdk<23 upon installation
                Log.v(TAG, "Permission is granted1");

            }


            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted2");
//                        String fileName = WalletUtils.generateLightNewWalletFile("password", new File(path ));

                } else {

                    Log.v(TAG, "Permission is revoked2");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

                }
            } else { //permission is automatically granted on sdk<23 upon installation
                Log.v(TAG, "Permission is granted2");
//                      String fileName = WalletUtils.generateLightNewWalletFile("password", new File(path ));

            }


        } catch (Exception e) {
            Log.e("paasword-genmeration", e.toString());
        }


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Web3j web3 = Web3jFactory.build(new HttpService("https://ropsten.infura.io/esnqTlDOSuuLXjjlsT1M"));  // defaults to http://localhost:8545/
                    Web3ClientVersion web3ClientVersion;
                    web3ClientVersion = web3.web3ClientVersion().send();
                    String clientVersion = web3ClientVersion.getWeb3ClientVersion();
                    Log.e("Web3 verison", clientVersion);
                    Credentials credentials = WalletUtils.loadCredentials(
                            "password",
                            "/storage/emulated/0/Download/UTC--2018-03-16T19-05-15.125--833e56c5df2a654372a252658006af4d3158e9f3.json");
                    Log.e("Loading credentials", "Credentials loaded");
                    TransactionReceipt transferReceipt = Transfer.sendFunds(web3, credentials,
                            "0x19e03255f667bdfd50a32722df860b1eeaf4d635",  // you can put any address here
                            BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                            .send();
                    Log.e("Transaction", "Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                            + transferReceipt.getTransactionHash());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error ", "Web3 connection error " + e.toString());
                }
            }
        });

        thread.start();

    }
}
