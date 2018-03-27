package com.persistent.subhod_i.digitallocker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.github.clans.fab.FloatingActionButton;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * Created by subhod_i on 23-03-2018.
 */

public class HomeActivity extends Activity {
    String ethereum, password;
    Web3j web3j;
    Credentials credentials;
    TextView ethereumId;
    TextView accountBalance;
    TextView response;
    FloatingActionButton quorum, ropsten, mainnet, quorumDeploy, quorumTransaction, quorumQuery;
    BigInteger balance;
    String contractAddress = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.activity_home);
        ethereumId = (TextView) findViewById(R.id.ethereumId);
        accountBalance = (TextView) findViewById(R.id.accountBalance);
        response = (TextView) findViewById(R.id.response);
        quorum = (FloatingActionButton) findViewById(R.id.quorum);
        ropsten = (FloatingActionButton) findViewById(R.id.ropsten);
        mainnet = (FloatingActionButton) findViewById(R.id.mainnet);
        quorumDeploy = (FloatingActionButton) findViewById(R.id.quorumDeploy);
        quorumTransaction = (FloatingActionButton) findViewById(R.id.quorumTransaction);
        quorumQuery = (FloatingActionButton) findViewById(R.id.quorumQuery);

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

        quorumDeploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new LongOperation().execute("quorumContractDeploy");
                } catch (Exception e) {

                }
            }
        });

        quorumTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new LongOperation().execute("quorumContractTransaction");
                } catch (Exception e) {

                }
            }
        });

        quorumQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new LongOperation().execute("quorumContractQuery");
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

                    case "quorumTransaction":
                        web3j = wallet.constructWeb3("http://10.51.233.3:22000");
                        credentials = wallet.loadCredentials(password);
                        wallet.sendTransaction(web3j, credentials);
                        loadBalance();
                        return "quorumTransaction";

                    case "quorumContractDeploy":
                        web3j = wallet.constructWeb3("http://10.51.233.3:22000");
                        credentials = wallet.loadCredentials(password);
                        Contract contract = new Contract(web3j, credentials);
                        contractAddress = contract.deploy();
                        return "Contract deployed successfully: " + contractAddress;

                    case "quorumContractTransaction":
                        web3j = wallet.constructWeb3("http://10.51.233.3:22000");
                        credentials = wallet.loadCredentials(password);
                        contract = new Contract(web3j, credentials);
                        String transactionHash = contract.open("alice", Numeric.hexStringToByteArray(asciiToHex("myString")), contractAddress);
                        return "Transaction sent " + transactionHash;

                    case "quorumContractQuery":
                        web3j = wallet.constructWeb3("http://10.51.233.3:22000");
                        credentials = wallet.loadCredentials(password);
                        contract = new Contract(web3j, credentials);
                        byte[] result = contract.query("alice", contractAddress);
                        return new String(result, StandardCharsets.UTF_8);

                    default:
                        final EditText txtUrl = new EditText(this);

// Set the default text to a link of the Queen
                        txtUrl.setHint("http://www.librarising.com/astrology/celebs/images2/QR/queenelizabethii.jpg");

                        new AlertDialog.Builder(this)
                                .setTitle("Moustachify Link")
                                .setMessage("Paste in the link of an image to moustachify!")
                                .setView(txtUrl)
                                .setPositiveButton("Moustachify", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String url = txtUrl.getText().toString();
                                        moustachify(null, url);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                                .show();
                        web3j = wallet.constructWeb3("http://10.51.233.3:22000");
                        loadBalance();
                        return "loadbalance";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result == "ropstenTransaction" || result == "mainnetTransaction" || result == "quorumTransaction" || result=="loadbalance")
                    accountBalance.setText(balance.toString());
                else {
                    response.setText(result);
                }
            } catch (Exception e) {
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

    @TargetApi(Build.VERSION_CODES.O)
    public String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString() + "".join("", Collections.nCopies(32 - (hex.length() / 2), "00"));
    }
}

