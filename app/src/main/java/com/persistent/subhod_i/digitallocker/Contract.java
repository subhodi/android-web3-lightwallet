package com.persistent.subhod_i.digitallocker;

import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.protocol.Web3j;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

/**
 * Created by subhod_i on 22-03-2018.
 */

public class Contract {
    private Web3j web3j;
    private Credentials credentials;

    public Contract(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public String deploy() throws Exception {
        Simple_sol_simple contract = Simple_sol_simple.deploy(
                web3j, credentials,
                BigInteger.valueOf(0),
                BigInteger.valueOf(10000000)).send();
        return contract.getContractAddress();
    }

    public String open(String key, byte[] value) throws Exception {
        Simple_sol_simple contract = Simple_sol_simple.load(
                "0x9ac8a5efc282afab3b2a7286704f232767645733", web3j, credentials, BigInteger.valueOf(0), BigInteger.valueOf(100000));
        TransactionReceipt transactionReceipt = contract.open(key,value).send();
        return transactionReceipt.getTransactionHash();
    }
}
