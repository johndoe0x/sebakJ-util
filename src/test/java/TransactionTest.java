package org.sebak.sdk;

import org.ethereum.vm.trace.Op;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.stellar.sdk.KeyPair;

import static org.junit.Assert.*;

public class TransactionTest {
    
    @Test
    public void testDoHashing() {
        String secretSeed ="SDJLIFJ3PMT22C2IZAR4PY2JKTGPTACPX2NMT5NPERC2SWRWUE4HWOEE";
        String source = KeyPair.fromSecretSeed(secretSeed).getAccountId();
        String sequence_id= "1";
        String newtork_id = "sebak-test-network";
        String type1 = "payment";
        String amount1 = "100";
        String target1 = "GB3AOQD2M5AKMNWUP2HFCVQYFQNGTAFTJ24BHZ56ONSGGOXMG3EBO6OE";
        String type2 = "create-account";
        String amount2 = "1000000";
        String target2 = "GD54SAKFHJ2QSBLEHZIQV3UWQ42OD6VQ6HKF6TN6F72US3AUQNDSONEV";
        String expected_sig = "nU46BuF6f1PUUCoHoy3EXMxdibvRC6ZYyzLPsr4aNJYJnDDvSdcn52Qf9CGy5R9UbkMgW6mdKGwrHNvd3oCoRsp";

        Operation operation1 = new Operation(type1,target1,amount1);
        Operation operation2 = new Operation(type2,target2,amount2);

        Transaction transaction  = new Transaction.Builder(source, sequence_id)
                .addOperation(operation1)
                .addOperation(operation2)
                .build();

        transaction.sign(secretSeed, newtork_id);
        String str = transaction.toJson();
        System.out.println(str);

        assertEquals(expected_sig,transaction.getSignature());
    }

    @Test
    public void testDoHashing2() {
        String secretSeed = "SAC3GZZ53LSLXBLW5IQTJJE7NWSHXT2SMJCEWN5U3CFBITAYG2WIUOB2";
        String source = KeyPair.fromSecretSeed(secretSeed).getAccountId();
        String sequence_id = "0";
        String newtork_id = "sebak-test-network";
        String type1 = "create-account";
        String amount1 ="1000000";
        String target1 ="GBIHC37P5AQ4C462ZMEY3FOXOTGYAKBV52YVOQYUSOGMI6IM7P3GVCOS";
        String type2 ="payment";
        String target2 = "GCU2L2747DV76CMVSW77HYX34UF655GSYMQ4Y52JRIR5BXBSPXIQJ6KM";
        String amount2 = "99999";
        String expected_sig = "45VudhnAyVy9Gh5iZyxHK3wGx1ukXHRP2BYkRxdrnm5KXmc6boKzujnxFoFSZc1g1w4XVMU5KCGSR9Rfv6dzmpD7";

        Operation operation1 = new Operation(type1,target1,amount1);
        Operation operation2 = new Operation(type2,target2,amount2);

        Transaction transaction  = new Transaction.Builder(source, sequence_id)
                .addOperation(operation1)
                .addOperation(operation2)
                .build();

        transaction.sign(secretSeed, newtork_id);
        String str = transaction.toJson();
        System.out.println(str);

        assertEquals(expected_sig,transaction.getSignature());
    }
}