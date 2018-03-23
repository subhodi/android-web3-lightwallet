package com.persistent.subhod_i.digitallocker;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
    TextView ethereumId;
    TextView accountBalance;
    BigInteger balance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.activity_home);
        ethereumId = (TextView) findViewById(R.id.ethereumId);
        accountBalance = (TextView) findViewById(R.id.accountBalance);
        try {
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Home-screen", e.toString());
        }
    }

    private void loadData() throws Exception {
        ethereum = getIntent().getExtras().getString("ethereumId");
        password = getIntent().getExtras().getString("password");
        ethereumId.setText(ethereum);
        new LongOperation().execute("");
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
                web3j = wallet.constructWeb3();
                loadBalance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            accountBalance.setText(balance.toString());
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}

