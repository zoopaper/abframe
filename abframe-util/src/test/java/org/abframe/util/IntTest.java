package org.abframe.util;


import java.math.BigInteger;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/8/24
 * Time: 14:17
 */
public class IntTest {

    public static void main(String[] args) {
        BigInteger dd = new BigInteger(8388598 + "");
        boolean d = dd.testBit(22);
        System.out.println(d);
    }
}
