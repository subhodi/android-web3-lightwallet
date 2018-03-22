package com.persistent.subhod_i.digitallocker;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class Simple_sol_simple extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b61020e8061001e6000396000f30060606040526004361061004b5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166365ff45d681146100505780637c261929146100a5575b600080fd5b341561005b57600080fd5b6100a360046024813581810190830135806020601f82018190048102016040519081016040528181529291906020840183838082843750949650509335935061010892505050565b005b34156100b057600080fd5b6100f660046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061017495505050505050565b60405190815260200160405180910390f35b806000836040518082805190602001908083835b6020831061013b5780518252601f19909201916020918201910161011c565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051908190039020555050565b600080826040518082805190602001908083835b602083106101a75780518252601f199092019160209182019101610188565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205490509190505600a165627a7a723058208efb23962811bc3865a50ecf3461abf0069b0d2afc88ba9caf6c98f4f926864c0029";

    protected Simple_sol_simple(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Simple_sol_simple(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> open(String _key, byte[] _value) {
        final Function function = new Function(
                "open", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_key), 
                new org.web3j.abi.datatypes.generated.Bytes32(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<byte[]> query(String _key) {
        final Function function = new Function("query", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_key)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public static RemoteCall<Simple_sol_simple> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Simple_sol_simple.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Simple_sol_simple> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Simple_sol_simple.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Simple_sol_simple load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Simple_sol_simple(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Simple_sol_simple load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Simple_sol_simple(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
