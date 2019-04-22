# Sebak java-sdk

## Requirements

- JVM (java virtual machine) 1.8 or more latest. 

- Gradle version 5.3.1 or more latest.

- IntelliJ (recommended)

## Test

- You have to check your JVM version is `1.8`.

- running `$ ./gradlew test` or `$ gradle test`. 

Then you can see the test result. 

## Before transaction create... You need

- **Secret seed** of sender. 

- **network id** 
  
  You can find network id access to [sebak main-net](https://mainnet.blockchainos.org/) or [sebak test-net]( https://testnet-sebak.blockchainos.org) on policy section. 
  
  ```bash
  $ curl -v https://testnet-sebak.blockchainos.org
  ...
    "policy": {
      "network-id": "sebak-test-network",
      "initial-balance": "10000000000000000000",
      "base-reserve": "1000000",
  ...

  ```
   

- **Public keys** of recipients.

## transaction

Transaction format is Json on basis.

### Transaction header

Transaction header consists of `version`,`created`(ISO8601 format) and `signature`.

Every elements of Header `type` are `String`.
 
### Transaction body

Transaction body consists of sender's public key (a.k.a **source**), **fee**(transaction fee), **sequence id**, 
and **Lists of operation**.

- sender's public key( A.K.A **source**): This is generated from the sender's**secret seed**.

- fee : It is multiplied by the number of operations in one transaction.

- sequence id : sequence_id is the last state of account. 
                It will be varied when your account state is changed, so you should check the latest sequence_id from network. 
                You can get the [laetst sequence_id in here.](https://bosnet.github.io/sebak/api/#accounts-account-details-get) .

- Lists of operations : Please see the below [decription](###operations).

### operations

For the nature of transaction of SEBAK, one Transction can have multiple operations. 
Operation is the base unit of operating accounts like creating new account and payment. 
Currently SEBAK supports various kind of operations, but most of users will use CreateAccount and Payment operations.

when you organize operation, you have to include operation `type` , `target`address and `amount`.

- amount have to be **String** type, and its unit is `GON`. 
  `GON` is BOScoin's base unit that like 'GWEI' in Ethereum. 
  
  `1BOS` equal to `10000000GON`. 
 
  so you **HAVE TO AWARE YOU PUT BASE UNIT OF BOScoin TRANSFER.**   

- `target` address is have to be valid. 

- At this time, you can up to 100 operations in one transaction. This number can be adjusted. 
This limit also can be checked by node information.

You can see the how operation lists make :

```java
String newtork_id = "sebak-test-network";
String sequence_id; // you have to search Sequence id on Sebak network through API.
String secretSeed = "SAC3GZZ53LSLXBLW5IQTJJE7NWSHXT2SMJCEWN5U3CFBITAYG2WIUOB2";
String source = KeyPair.fromSecretSeed(secretSeed).getAccountId();
String type1 = "payment";
String type2 = "create-account";
String amount1 = "100";
String amount2 = "1000000";
String targetAddress1 = "GB3AOQD2M5AKMNWUP2HFCVQYFQNGTAFTJ24BHZ56ONSGGOXMG3EBO6OE";
String targetAddress2 = "GD54SAKFHJ2QSBLEHZIQV3UWQ42OD6VQ6HKF6TN6F72US3AUQNDSONEV";
Operation operation1 = new Operation(type1, amount1, targetAddress1);
Operation operation2 = new Operation(type2, amount2, targetAddress2);
``` 
#### Create account operation

Target address must be new account, this means, it does not exist in the SEBAK network. 
You can check the account status thru account API of SEBAK. 
Please see [here](https://bosnet.github.io/sebak/api/#accounts-account-details-get) .
Amount for creating account must be bigger than base reserve, you can check the amount from SEBAK node information like 'network-id'
through access sebak main-net or test network that mentioned above.

#### Payment operation

Target address must exist in network.



## Building a Transaction

Check the existence of senders's account.
Get the sequece id of sender's account.
Start Building a transation.

```java
Transaction transaction  = new Transaction.Builder(source, sequence_id)
            .addOperation(operation1)
            .addOperation(operation2)
            .build();
```

#### make transaction signature
   
Make a signature with **secret seed** ,**network id**.
For instance:

```java
transaction.sign(secretSeed, newtork_id);
```

If you successfully sign your transaction, you can serialize your transaction instance to 'json'.

```json

String json = transaction.toJson();

{
  "H" : {
    "version" : "1",
    "created" : "2019-04-10T13:13:54.000000951+09:00",
    "signature" : "nU46BuF6f1PUUCoHoy3EXMxdibvRC6ZYyzLPsr4aNJYJnDDvSdcn52Qf9CGy5R9UbkMgW6mdKGwrHNvd3oCoRsp"
  },
  "B" : {
    "source" : "GAG5EESGOZIHTKK5N2NBHX25EWRC3S3TWZT7RMCSBX65A3KTJKILQKCF",
    "fee" : "20000",
    "sequence_id" : 1,
    "operations" : [ {
      "H" : {
        "type" : "payment"
      },
      "B" : {
        "target" : "GB3AOQD2M5AKMNWUP2HFCVQYFQNGTAFTJ24BHZ56ONSGGOXMG3EBO6OE",
        "amount" : "100"
      }
    }, {
      "H" : {
        "type" : "create-account"
      },
      "B" : {
        "target" : "GD54SAKFHJ2QSBLEHZIQV3UWQ42OD6VQ6HKF6TN6F72US3AUQNDSONEV",
        "amount" : "1000000",
        "linked" : ""
      }
    } ]
  }
}

```

## Sending Transaction

So you are ready to submit transactons, you can just post your json to SEBAK node.

```sh
$ curl -v \
    -XPOST \
    -H 'Content-Type: application/json' \
    -d @your-transaction.json \
    https://testnet-sebak.blockchainos.org/api/v1/transactions
...
> POST /api/v1/transactions HTTP/2
> Host: testnet-sebak.blockchainos.org
> User-Agent: curl/7.62.0
> Accept: */*
> Content-Type: application/json
> Content-Length: 749
>
* Connection state changed (MAX_CONCURRENT_STREAMS == 100)!
* We are completely uploaded and fine
< HTTP/2 200
< content-type: application/problem+json
< x-ratelimit-limit: 100
< x-ratelimit-remaining: 98
< x-ratelimit-reset: 1542432619
< date: Sat, 17 Nov 2018 05:30:08 GMT
< content-length: 101
< via: 1.1 google
< alt-svc: clear
<
* Connection #0 to host testnet-sebak.blockchainos.org left intact
...
```

The API of sending transaction, please see https://bosnet.github.io/sebak/api/#trasactions-transactions-post .





