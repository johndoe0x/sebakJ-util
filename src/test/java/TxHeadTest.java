import org.junit.Test;

import static org.junit.Assert.*;

public class TxHeadTest {
    @Test
    public void getCreated() throws Exception {
        TxHead txHead = new TxHead();
        System.out.println(txHead.created);

    }

}