package hr.fer.zemris.java.hw05.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//java -cp target/hw05-0036532834-1.0.0.jar crypto.Crypto checksha hw05test.bin
public class Crypto {

	private static final String DIGEST = "SHA-256";
	private static final int BUFFER = 4096;
	private static final String CIPHER = "AES/CBC/PKCS5Padding";
	//ECB nece

	public static void main(String... args) {

		if (args.length < 2 || args.length > 3) {
			System.err.println("There should be 2 or 3 input arguments");
			System.exit(-1);
		}

		try (Scanner sc = new Scanner(System.in)) {
			if (args[0].equals("checksha")) {
				System.out.print("Please provide expected sha-256 digest for " + args[1] + ":\n> ");

				String inputDigest = sc.nextLine();
				String generatedDigest = digest(args[1]);

				System.out.print("Digesting completed. ");

				if (inputDigest.equalsIgnoreCase(generatedDigest)) {
					System.out.println("Digest of " + args[1] + " matches expected digest.");
				} else {
					System.out.println("Digest of " + args[1] + " does not match the expected digest. Digest was: " + generatedDigest);
				}
			} else if (args[0].equals("encrypt")) {
                System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits): \n> ");
                String key = sc.nextLine();
                
                System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits): \n> ");
                String iVector = sc.nextLine();
                
                cipher(key, iVector, args[1], args[2], true);
                
                System.out.println("Encryption completed. Generated file " + args[2] + " based on file " + args[1]);
                
			} else if (args[0].equals("decrypt")) {
	               System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits): \n> ");
	                String key = sc.nextLine();
	                
	                System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits): \n> ");
	                String iVector = sc.nextLine();
	                
	                cipher(key, iVector, args[1], args[2], false);
	                
	                System.out.println("Decryption completed. Generated file " + args[2] + " based on file " + args[1]);
	                
			} else {
				throw new IllegalArgumentException();
			}

		} catch (Exception e) {
//			e.printStackTrace();
			System.err.println("Error with Encryption/Decryption");
		}

	}

	public static void cipher(String key, String iVector, String inputFileName, String outputFileName, boolean encript)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
		Cipher aesCipher = Cipher.getInstance(CIPHER);
		SecretKey secretKey = new SecretKeySpec(Util.hextobyte(key), "AES");
		AlgorithmParameterSpec iVectorParameter = new IvParameterSpec(Util.hextobyte(iVector));

		if(encript == true) {
			aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, iVectorParameter);			
		}else {
			aesCipher.init(Cipher.DECRYPT_MODE, secretKey, iVectorParameter);			
		}

		try (BufferedInputStream input = new BufferedInputStream(Files.newInputStream(Path.of(inputFileName)));
				BufferedOutputStream output = new BufferedOutputStream(
						Files.newOutputStream(Path.of(outputFileName)))) {

			byte[] buffer = new byte[BUFFER];

			while (true) {
				int i = input.read(buffer);

				if (i == -1) {
					output.write(aesCipher.doFinal());
					break;
				}

				output.write(aesCipher.update(buffer, 0, i));
			}
		}

	}

	public static String digest(String fileName) throws NoSuchAlgorithmException, IOException {
		MessageDigest sha256 = MessageDigest.getInstance(DIGEST);

		// InputStream is = new BufferedInputStream( new FileInputStream( fileName ) );
		// starija verzija
		// The FileReader automatically converts the raw bytes into characters by using
		// the platform's default character encoding
		// new FileReader( "input.txt" );
		// FileInputSream if you want to read streams of raw bytes
		try (BufferedInputStream input = new BufferedInputStream(Files.newInputStream(Path.of(fileName)))) {
			byte[] buffer = new byte[BUFFER];

			while (true) {
				int i = input.read(buffer);

				if (i == -1)
					break;

				sha256.update(buffer, 0, i);
			}

			return Util.bytetohex(sha256.digest());

		}

	}
}
