import com.fasterxml.jackson.databind.ObjectMapper;
import org.ethereum.vm.trace.Op;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.stellar.sdk.KeyPair;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TransactionTest {
    String secretSeed ="SDJLIFJ3PMT22C2IZAR4PY2JKTGPTACPX2NMT5NPERC2SWRWUE4HWOEE";
    String sequence_id= "1";
    String newtork_id = "sebak-test-network";
    String type1 = "payment";
    String type2 = "create-account";
    String amount1 = "100";
    String amount2 = "1000000";
    String target1 = "GB3AOQD2M5AKMNWUP2HFCVQYFQNGTAFTJ24BHZ56ONSGGOXMG3EBO6OE";
    String target2 = "GD54SAKFHJ2QSBLEHZIQV3UWQ42OD6VQ6HKF6TN6F72US3AUQNDSONEV";
    String signature = "nU46BuF6f1PUUCoHoy3EXMxdibvRC6ZYyzLPsr4aNJYJnDDvSdcn52Qf9CGy5R9UbkMgW6mdKGwrHNvd3oCoRsp";


    @Test
    public void doHashing() throws Exception {

        Operation operation1 = new Operation(type1,target1,amount1);
        Operation operation2 = new Operation(type2,target2,amount2);

        ArrayList<Operation> operations = new ArrayList<Operation>();

        operations.add(operation1);
        operations.add(operation2);
        Transaction transaction  = new Transaction(operations);
        transaction.B.source = KeyPair.fromSecretSeed(secretSeed).getAccountId();
        transaction.B.sequence_id = new BigInteger(sequence_id);
        transaction.H.signature = transaction.get_signature(secretSeed,transaction.doHashing(),newtork_id);

        assertEquals(signature,transaction.H.signature);



    }

    @Test
    public void formJson() throws Exception {



        Operation operation1 = new Operation(type1,target1,amount1);
        Operation operation2 = new Operation(type2,target2,amount2);
        ArrayList<Operation> operations = new ArrayList<Operation>();

        operations.add(operation1);
        operations.add(operation2);
        Transaction transaction  = new Transaction(operations);
        transaction.B.source = KeyPair.fromSecretSeed(secretSeed).getAccountId();
        transaction.B.sequence_id = new BigInteger(sequence_id);
        transaction.H.signature = transaction.get_signature(secretSeed,transaction.doHashing(),newtork_id);

        transaction.formJson();


    }

}