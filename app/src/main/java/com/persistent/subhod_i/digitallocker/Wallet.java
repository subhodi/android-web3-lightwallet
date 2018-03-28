package com.persistent.subhod_i.digitallocker;

import android.os.Environment;
import android.util.Log;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Created by subhod_i on 16-03-2018.
 */

public class Wallet {

    public String createWallet() throws Exception {
        String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();
        String fileName = WalletUtils.generateLightNewWalletFile("password", new File(path));
        return path + fileName;
    }

    public Credentials loadCredentials(String password) throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(
                password,
                "/storage/emulated/0/Download/UTC--2018-03-16T19-05-15.125--833e56c5df2a654372a252658006af4d3158e9f3.json");
        Log.i("Loading credentials", "Credentials loaded");
        return credentials;
    }

    public Web3j constructWeb3(String URL) throws IOException {
        Web3j web3 = Web3jFactory.build(new HttpService(URL));  // defaults to http://localhost:8545/
        Web3ClientVersion web3ClientVersion;
        web3ClientVersion = web3.web3ClientVersion().send();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        Log.i("Web3 verison", clientVersion);
        return web3;
    }

    public String sendTransaction(Web3j web3, Credentials credentials) throws Exception {
        TransactionReceipt transferReceipt = Transfer.sendFunds(web3, credentials,
                "0x19e03255f667bdfd50a32722df860b1eeaf4d635",  // you can put any address here
                BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                .send();
        return transferReceipt.getTransactionHash();
    }

    public String createBipWallet() throws Exception {
        String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();
        Bip39Wallet bip39Wallet = WalletUtils.generateBip39Wallet("password", new File(path));
        String filename = bip39Wallet.getFilename();
        String mnemonic = bip39Wallet.getMnemonic();
        return "Success";
    }

    public void checkWalletExist() {

    }
}
