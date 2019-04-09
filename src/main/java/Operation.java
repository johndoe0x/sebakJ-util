import java.util.ArrayList;

public class Operation {
    String target, amount, linked;

    OperationHeader H;

    public Operation(OperationHeader H, String target, String amount, String linked) {
        this.H = H;
        this.target = target;
        this.amount = amount;
        this.linked = linked;
    }

    public  Object[] Operation() {
        try{
            if (H.type.matches("create-account")) {
                H.type = "create-account";
                CreateAccOps createAccOps = new CreateAccOps(this.H,this.target,this.amount,this.linked);
                return createAccOps.CreateAcc_Array();
            }
            if (H.type.matches("payment")) {
                H.type = "payment";
               PaymentOps  paymentOps = new PaymentOps(this.H,this.target,this.amount);
                return paymentOps.Payment_Array();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        String [] errMsg={"unknown operation type found:"};
        String[][] result = {errMsg,{this.H.type}};
        return result;
    }

}

class CreateAccOps{
    OperationHeader H;
    CreateAccOperationBody B = new CreateAccOperationBody();
    String target, amount, linked;


    public CreateAccOps(OperationHeader H, String target, String amount, String linked) {
        this.H = H;
        this.B.target = target;
        this.B.amount = amount;
        this.B.linked = linked;
    }

    public Object[][] CreateAcc_Array(){
         Object[][] result = {this.H.toArray(),this.B.CreateAcctoArray()};
         return result;

    }


}

class PaymentOps {
    OperationHeader H;
    paymentOperationBody B = new paymentOperationBody();
    String target,amount;

    public PaymentOps(OperationHeader H, String target, String amount) {
        this.H = H;
        this.B.target = target;
        this.B.amount = amount;
    }
    public Object[][] Payment_Array(){
        Object[][] result = {this.H.toArray(),this.B.PaymentArray()};
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

class CreateAccOperationBody{
    String target,amount,linked;

    public String[] CreateAcctoArray(){
        String[] array = {this.target,this.amount,this.linked};
        System.out.println();
        return array;
    }

}

class  paymentOperationBody{
    String target,amount;

    public String[] PaymentArray(){
        String[] array = {this.target,this.amount};
        return array;
    }
}