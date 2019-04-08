import java.util.ArrayList;

public class Operation {
    String type,target, amount, linked;
    String[] H = new String[1];

    public Operation(String type, String target, String amount, String linked) {
        this.type = type;
        this.target = target;
        this.amount = amount;
        this.linked = linked;
    }

    public  Object[] Operation() {
        try{
            if (type.matches("create-account")) {
                H[0] = "create-account";
                CreateAccOps createAccOps = new CreateAccOps(this.H,this.target,this.amount,this.linked);
                return createAccOps.CreateAcc_Array();
            }
            if (type.matches("payment")) {
                H[0] = "payment";
                PaymentOps paymentOps = new PaymentOps(this.H,this.target,this.amount);
                return paymentOps.Payment_Array();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        String [] errMsg={"unknown operation type found:"};
        String[][] result = {errMsg,{type}};
        return result;
    }

}

class CreateAccOps{
    Object[] H = new Object[1];
    String target, amount, linked;


    public CreateAccOps(Object[] H, String target, String amount, String linked) {
        this.H = H;
        this.target = target;
        this.amount = amount;
        this.linked = linked;
    }

    public Object[][] CreateAcc_Array(){
         Object[][] result = {this.H,{this.target,this.amount,this.linked}};
         return result;

    }


}


class PaymentOps{
    Object[] H;
    String target,amount;

    public PaymentOps(Object[] H, String target, String amount) {
        this.H = H;
        this.target = target;
        this.amount = amount;
    }
    public Object[][] Payment_Array(){
        Object[][] result = {this.H,{this.target,this.amount}};
        return result;

    }

}
