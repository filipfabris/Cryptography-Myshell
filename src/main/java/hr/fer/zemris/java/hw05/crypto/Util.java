package hr.fer.zemris.java.hw05.crypto;

public class Util {
	
	private static final String HEX = "0123456789abcdef";	
	
	public static byte[] hextobyte(String keyText) {
		if(keyText.length() % 2 == 1) {
			throw new IllegalArgumentException();
		}
		
        keyText = keyText.toLowerCase();
        int length = keyText.length();
        byte[] bytearray = new byte[length / 2];
        
        for(int i = 0; i<length; i += 2) {
        	//Gornja 4 bita prvi znak, donja 4 drugi znak
            bytearray[i / 2] = (byte) ((HEX.indexOf(keyText.charAt(i)) << 4) + HEX.indexOf(keyText.charAt(i + 1)));
        }
		return bytearray;
	}

	public static String bytetohex(byte[] bytearray) {
			if(bytearray == null) {
				return null;
			}
			
	        StringBuilder sb = new StringBuilder();
	        int length = bytearray.length;

	        for(int i = 0; i<length; i++) {
	        	//Prvo uzmemo gornja 4 i appendamo pa uzemmo donja 4 i appendamo
	        	sb.append(HEX.charAt((0xF0 & bytearray[i]) >> 4));
	        	sb.append(HEX.charAt(0x0F & bytearray[i]));
	        }
		
		
	       return sb.toString();
	}

	
	
}
