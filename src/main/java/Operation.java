package org.sebak.sdk;

public class Operation {

    OperationHeader H;
    OperationBody B;

    public  Operation(String type, String target, String amount) {
        this.H = new OperationHeader(type);
        try{
            if (H.type.matches("create-account")) {
                this.B = new CreateAccountOp(target,amount,"");

            }
            if (H.type.matches("payment")) {
                this.B = new PaymentOp(target,amount);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object[] toArray(){
        Object[] result = {this.H.toArray(), this.B.toArray()};
        return result;
    }
}

class CreateAccountOp implements OperationBody{

    String target, amount, linked;

    public CreateAccountOp( String target, String amount, String linked) {
        this.target = target;
        this.amount = amount;
        this.linked = linked;
    }

    @Override
    public String[] toArray() {
        String[] result = {this.target,this.amount,this.linked};
        return result;
    }
}

class PaymentOp implements OperationBody{
    String target,amount;

    public PaymentOp( String target, String amount) {
        this.target = target;
        this.amount = amount;
    }

    @Override
    public String[] toArray() {
        String[] result = {this.target,this.amount};
        return result;
    }
}

class OperationHeader {
    public String type;

    public OperationHeader(String type) {
        this.type = type;
    }

    public String[] toArray() {
        String[] array = {this.type};
        return array;
    }
}

interface OperationBody {
    abstract String[] toArray();
}
