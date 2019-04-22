package org.sebak.sdk;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.novacrypto.base58.Base58;

import org.ethereum.util.RLP;
import org.stellar.sdk.KeyPair;

import shadow.org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.lang.String;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.google.common.base.Preconditions.checkNotNull;

public class Transaction {

    private TxHead H;
    private TxBody B;

    public Transaction(String sourceAccount, String sequencId, ArrayList<Operation> operations) {
        this.H = new TxHead();
        this.B = new TxBody(sourceAccount, sequencId, operations);
    }

    public String toJson() {
        String txToJson = "";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            txToJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        }
        catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        return txToJson;
    }

    public byte[] Doublesha256Hash(byte[] msg) throws NoSuchAlgorithmException{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(msg);
            MessageDigest result = MessageDigest.getInstance("SHA-256");
            result.update(messageDigest.digest());
            return result.digest();
    }

    public String doHashing() throws NoSuchAlgorithmException {
        byte[] Rlpencoded = RLP.encode(this.B.operationsArray());
        String Hashresult = Base58.base58Encode(this.Doublesha256Hash(Rlpencoded));
        return Hashresult;
    }

    public void sign(String secretSeed, String network_id) {
        String txHash = "";
        try {
            txHash = this.doHashing();
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] networkid = network_id.getBytes(StandardCharsets.UTF_8);
        byte[] hash = txHash.getBytes(StandardCharsets.UTF_8);
        try {
            byteArrayOutputStream.write(networkid);
            byteArrayOutputStream.write(hash);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        KeyPair keyPair = KeyPair.fromSecretSeed(secretSeed);

        byte[] result = keyPair.sign(byteArrayOutputStream.toByteArray());
        this.H.signature = Base58.base58Encode(result);
    }

    public String getSignature() {
        return this.H.signature;
    }

    public String getSourceAccount() {
        return this.B.source;
    }

    public String getFee() {
        return this.B.fee;
    }

    public static class Builder {
        private final String mSourceAccount;
        private final String mSequenceId;
        ArrayList<Operation> mOperations;
        private int mFee;

        public Builder(String source, String sequenceId) {
            checkNotNull(source, "secreteSeed cannot be null");
            mSourceAccount = source;
            mSequenceId = sequenceId;
            mOperations = new ArrayList<Operation>();
        }

        public Builder addOperation(Operation operation) {
            checkNotNull(operation, "operation cannot be null");
            mOperations.add(operation);
            return this;
        }

        public Transaction build() {    
            Transaction transaction = new Transaction(mSourceAccount, mSequenceId, mOperations);
            return transaction;
        }
    }
}

class TxBody{
    String source;
    String fee ;
    BigInteger sequence_id;
    ArrayList<Operation> operations;

    public TxBody(String source, String sequenceId, ArrayList<Operation> operations) {
        this.source = source;
        this.sequence_id = new BigInteger(sequenceId);
        this.operations = operations;
        if (operations.size()>1) {
            this.fee = Integer.toString(10000*operations.size());
        }else{
            this.fee = Integer.toString(10000);
        }
    }

    public Object[] operationsArray(){
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (Operation operation : this.operations) {
            arrayList.add(operation.toArray());
        }
        Object[] operationsList = arrayList.toArray(new Object[arrayList.size()]);
        Object[] result = {this.source,this.fee,this.sequence_id,operationsList};
        return result;
    }
}

class  TxHead {
    String  version, created, signature;

    public TxHead() {
        this.version = "1";
        this.created = this.setCreatedTime();
    }
    public String setCreatedTime() {
        Calendar calendar = Calendar.getInstance();
        Date createdTime = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String created = simpleDateFormat.format(createdTime);
        return created;
    }
    String getCreatedTime() {
        return this.created;
    }
}

class Util {
    public static String ByteArrayToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02X", b&0xff));
        }

        return sb.toString();
    }

    public static String toISO8601UTC(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}