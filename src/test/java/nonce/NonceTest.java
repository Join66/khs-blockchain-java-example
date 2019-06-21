package nonce;

import static org.junit.Assert.*;

import org.junit.Test;
import helpers.SHA256;

public class NonceTest {

	@Test
	public void test() {
		String message = "Keyhole Software";

		System.out.println("Message: " + message);

		String hashValue = SHA256.generateHash(message);

		System.out.println(String.format("Hash: %s", hashValue));

		String nonceKey = "12345";
		// E.g. "00000" :
		String zeroGoal = new String(new char[nonceKey.length()]).replace("\0", "0");

		long nonce = 0;
		boolean isNonceFound = false;
		String nonceHash = "";

		long start = System.currentTimeMillis();

		while (!isNonceFound) {

			nonceHash = SHA256.generateHash(message + nonce);
			isNonceFound = nonceHash.substring(0, nonceKey.length()).equals(zeroGoal);
			if (!isNonceFound) {
				nonce++;
			}
		}

		long ms = System.currentTimeMillis() - start;

		System.out.println(String.format("Nonce: %d", nonce));
		System.out.println(String.format("Nonce Hash: %s", nonceHash));
		System.out.println(String.format("Nonce Search Time: %s ms", ms));

		assertTrue(isNonceFound);

	}

	@Test
	public void test1() {
	    // ASCII码为0的一个不可见字符
        System.out.println("\0");
        System.out.println((int)"\0".toCharArray()[0]);

        char tp='0';
        System.out.println((int)tp); // 48
        char ch='\0';
        System.out.println((int)ch); // 0

		char[] cs = new char[16];
		System.out.println('\0' == cs[15]);
    }

}
