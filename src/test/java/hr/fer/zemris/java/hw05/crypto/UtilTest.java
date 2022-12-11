package hr.fer.zemris.java.hw05.crypto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilTest {

	@Test
	void hexToByte() {
		byte[] output = Util.hextobyte("01aE22");
        assertArrayEquals(new byte[] {1, -82, 34}, output);
	}
	
	@Test
	void byteToHex() {
		String output = Util.bytetohex(new byte[] {1, -82, 34});
        assertEquals("01ae22", output);
	}

}
