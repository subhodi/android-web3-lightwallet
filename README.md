# android-web3-lightwallet
Ethereum wallet and Signing in Android using web3j library

## Reference 
### Smart contrat wrapper: https://docs.web3j.io/smart_contracts.html#smart-contract-wrappers 

### Offlien trsanction: https://docs.web3j.io/transactions.html#offline-transaction-signing 

### Install web3j binary: Download Binary from https://github.com/web3j/web3j/releases 

### Install solc: ```npm install -g solc```

## Generate smart contract wrapper
### Use solc and web3j binary

```bash

$ # create contract.sol and then use solc command to generate bin and abi file
$ solc <contract>.sol --bin --abi --optimize -o <output-dir>/
$ # Generate smart contract from bin and abi file
$ web3j solidity generate [--javaTypes|--solidityTypes] /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

```
### Web3j generates a java file which can be directly imported to the project and can be used to transact with contract.
