package com.persistent.subhod_i.digitallocker;

import android.os.Environment;
import android.util.Log;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;


/**
 * Created by subhod_i on 16-03-2018.
 */

public class Wallet {

    public String createWallet() throws Exception {
        String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();
        Log.e("Create-wallet", path);
        String fileName = WalletUtils.generateLightNewWalletFile("password", new File(path));
        return path+fileName;
    }

    public Credentials loadCredentials(String password) throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(
                password,
                "/storage/emulated/0/Download/UTC--2018-03-16T19-05-15.125--833e56c5df2a654372a252658006af4d3158e9f3.json");
        Log.i("Loading credentials", "Credentials loaded");
        return credentials;
    }

    public Web3j constructWeb3() throws IOException {
        Web3j web3 = Web3jFactory.build(new HttpService("http://10.244.5.43:22000"));  // defaults to http://localhost:8545/
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

    public String deployContract(Web3j web3j, Credentials credentials) throws Exception {
        // using a raw transaction
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                "0x833e56c5df2a654372a252658006af4d3158e9f3", DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        RawTransaction rawTransaction = RawTransaction.createContractTransaction(
                nonce,
                BigInteger.valueOf(0),
                BigInteger.valueOf(0xE0000),
                BigInteger.valueOf(0),
                "0x6060604052341561000f57600080fd5b6103728061001e6000396000f30060606040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680632c46b20514610051578063e4dcb06b146100df575b600080fd5b341561005c57600080fd5b61006461013c565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100a4578082015181840152602081019050610089565b50505050905090810190601f1680156100d15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156100ea57600080fd5b61013a600480803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919050506101e4565b005b61014461028d565b60008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101da5780601f106101af576101008083540402835291602001916101da565b820191906000526020600020905b8154815290600101906020018083116101bd57829003601f168201915b5050505050905090565b80600090805190602001906101fa9291906102a1565b50806040518082805190602001908083835b602083101515610231578051825260208201915060208101905060208303925061020c565b6001836020036101000a03801982511681845116808217855250505050505090500191505060405180910390207ff302f1a25d304bd7348ee6fc1e2090f79ff9c7c809558b88c7876b528b66dd4860405160405180910390a250565b602060405190810160405280600081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106102e257805160ff1916838001178555610310565b82800160010185558215610310579182015b8281111561030f5782518255916020019190600101906102f4565b5b50905061031d9190610321565b5090565b61034391905b8082111561033f576000816000905550600101610327565b5090565b905600a165627a7a72305820e73ace75f51549c395ca185bd2161d3131afcf481b6287ed9ad3e79ef644da4e0029");

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
//        String transactionHash = web3j.ethSendRawTransaction(hexValue).send().getTransactionHash();

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
//        get contract address
//        EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(ethSendTransaction.getTransactionHash()).send();
//        String contractAddress = "";
//
//        if (transactionReceipt.getTransactionReceipt() != null) {
//            contractAddress = transactionReceipt.getTransactionReceipt().getContractAddress();
//        } else {
//            if (transactionReceipt.getTransactionReceipt() != null) {
//                contractAddress = transactionReceipt.getTransactionReceipt().getContractAddress();
//            } else {
//
//            }
//        }
        return "ContractAddress";
    }

    public String contractTransaction(Web3j web3j, Credentials credentials) throws Exception {

        ArrayList<Type> dataParams = new ArrayList<>();
        dataParams.add(new Uint(BigInteger.valueOf(35)));
        Function function = new Function("set", dataParams, Collections.<TypeReference<?>>emptyList());

        String encodedFunction = FunctionEncoder.encode(function);
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                "0x833e56c5df2a654372a252658006af4d3158e9f3", DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                BigInteger.valueOf(0),
                BigInteger.valueOf(0xE0000),
                "0x57f6e9596693364d33fd7c35ae525568a350cbae",
                encodedFunction);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        return transactionHash;
    }

    public String queryContract(Web3j web3j) throws Exception {
        ArrayList<Type> dataParams = new ArrayList<>();
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        outputParameters.add(new TypeReference<Uint>() {
        });
        Function function = new Function("get",
                inputParameters,
                outputParameters);

        String encodedFunction = FunctionEncoder.encode(function);

        org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction("0x833e56c5df2a654372a252658006af4d3158e9f3", "0x57f6e9596693364d33fd7c35ae525568a350cbae", encodedFunction),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        List<Type> someTypes = FunctionReturnDecoder.decode(
                response.getValue(), function.getOutputParameters());

        for (Type element : someTypes) {
            Log.e("elemnt", element.getValue().toString());
        }
        return "sdsd";
    }

    public String bip() throws Exception {
        String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();
        Bip39Wallet bip39Wallet = WalletUtils.generateBip39Wallet("ghhgvhh", new File(path));
        String filename = bip39Wallet.getFilename();
        String mnemonic = bip39Wallet.getMnemonic();
        return "Success";
    }

    public void checkWalletExist() {

    }
}
