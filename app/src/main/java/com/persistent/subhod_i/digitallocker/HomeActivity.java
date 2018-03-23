package com.persistent.subhod_i.digitallocker;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.github.clans.fab.FloatingActionButton;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.math.BigInteger;

/**
 * Created by subhod_i on 23-03-2018.
 */

public class HomeActivity extends Activity {
    String ethereum, password;
    Web3j web3j;
    Credentials credentials;
    TextView ethereumId;
    TextView accountBalance;
    FloatingActionButton quorum, ropsten, mainnet;
    BigInteger balance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.activity_home);
        ethereumId = (TextView) findViewById(R.id.ethereumId);
        accountBalance = (TextView) findViewById(R.id.accountBalance);
        quorum = (FloatingActionButton) findViewById(R.id.quorum);
        ropsten = (FloatingActionButton) findViewById(R.id.ropsten);
        mainnet = (FloatingActionButton) findViewById(R.id.mainnet);

        try {
            addEventListeners();
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Home-screen", e.toString());
        }
    }

    private void addEventListeners() {
        quorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new LongOperation().execute("quorumTransaction");
                } catch (Exception e) {

                }
            }
        });

        ropsten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new LongOperation().execute("ropstenTransaction");
                } catch (Exception e) {

                }
            }
        });

        mainnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new LongOperation().execute("mainnetTransaction");
                } catch (Exception e) {

                }
            }
        });
    }

    private void loadData() throws Exception {
        ethereum = getIntent().getExtras().getString("ethereumId");
        password = getIntent().getExtras().getString("password");
        ethereumId.setText(ethereum);
        new LongOperation().execute("loadBalance");
    }

    public void loadBalance() throws Exception {
        EthGetBalance ethGetBalance = web3j.ethGetBalance(ethereum, DefaultBlockParameterName.LATEST).sendAsync().get();
        balance = ethGetBalance.getBalance();
        Log.e("Home-screen", balance.toString());
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Wallet wallet = new Wallet();
            try {
                switch (params[0]) {
                    case "mainnetTransaction":
                        web3j = wallet.constructWeb3("https://mainnet.infura.io/esnqTlDOSuuLXjjlsT1M");
                        credentials = wallet.loadCredentials(password);
                        wallet.sendTransaction(web3j, credentials);
                        loadBalance();
                        return "mainnetTransaction";
                    case "ropstenTransaction":
                        web3j = wallet.constructWeb3("https://ropsten.infura.io/esnqTlDOSuuLXjjlsT1M");
                        credentials = wallet.loadCredentials(password);
                        wallet.sendTransaction(web3j, credentials);
                        loadBalance();
                        return "ropstenTransaction";
                    default:
                        web3j = wallet.constructWeb3("http://10.244.5.43:22000");
                        credentials = wallet.loadCredentials(password);
                        wallet.sendTransaction(web3j, credentials);
                        loadBalance();
                        return "quorumTransaction";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                accountBalance.setText(balance.toString());
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}

