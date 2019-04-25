package org.sebak.sdk;

import org.junit.Test;

import static org.junit.Assert.*;

public class TxHeadTest {
    @Test
    public void testGetCreatedTime() throws Exception {
        TxHead txHead = new TxHead();
        System.out.println(txHead.getCreatedTime());
    }
}