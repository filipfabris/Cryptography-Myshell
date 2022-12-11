package hr.fer.zemris.java.hw05.crypto.demo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import hr.fer.zemris.java.hw05.crypto.Util;

public class Proba {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		
		byte[] array = Util.hextobyte("01aE22");
		
		for(int i = 0; i<array.length; i++) {
			System.out.println(array[i]);
		}
		
		String output = Util.bytetohex(array);
		
		System.out.println(output);
		
		String temp = Util.bytetohex(new byte[] {1, -82, 34});
		
		System.out.println(temp);
		
	}

}
