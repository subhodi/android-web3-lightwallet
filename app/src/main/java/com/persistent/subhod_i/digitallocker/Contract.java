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
                BigInteger.valueOf(4700000)).send();
        return contract.getContractAddress();
    }

    public String open(String key, byte[] value) throws Exception {
        Simple_sol_simple contract = Simple_sol_simple.load(
                "0x2f7b3ff7f918d68d172cc2ae2baa2f10aa79ea9d", web3j, credentials, BigInteger.valueOf(0), BigInteger.valueOf(100000));
        TransactionReceipt transactionReceipt = contract.open(key,value).send();
        return transactionReceipt.getTransactionHash();
    }

    public byte[] query(String key) throws Exception {
        Simple_sol_simple contract = Simple_sol_simple.load(
                "0x2f7b3ff7f918d68d172cc2ae2baa2f10aa79ea9d", web3j, credentials, BigInteger.valueOf(0), BigInteger.valueOf(100000));
        return contract.query(key).send();
    }
}
