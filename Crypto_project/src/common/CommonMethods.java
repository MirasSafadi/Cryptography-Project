package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import common.Utils;

public class CommonMethods {
	public static MessageDigest md =  null;
	// plainText file should be in format of 16 bytes per line all in hex (32 hex
	// characters)
	// key should be 16 bytes
	public static void initMD() {
		if(md == null) {
			try {
				md = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static File encryptFile(File plainText, String key) {
		// this code opens an input file, writes it's contents on an out file (created
		// within)
		int[] keyArr = Utils.StringTointArray(key);
		BufferedReader reader = null;
		File cipherText = null;
		FileWriter myWriter = null;
		String fileName = plainText.getName().split("\\.")[0] + "_Encrypted.txt";
		try {
//			File in = new File("C:\\Users\\Pc\\Desktop\\in.txt");
			reader = new BufferedReader(new FileReader(plainText));
			// path should be computed by the server.
			cipherText = new File("C:\\Users\\Static\\Desktop\\" + fileName);
			cipherText.createNewFile();
			myWriter = new FileWriter(cipherText.getAbsolutePath());
			String st;
			while ((st = reader.readLine()) != null) {
				// encrypt each line and write it in an out file
				int[] encryptedLine = TwoFish.encrypt(Utils.StringTointArray(st), keyArr);
				String cipherLine = Utils.writeInput(encryptedLine);
				myWriter.write(cipherLine);
				myWriter.append('\n');
				myWriter.flush();
			}
			System.out.println("Successfully encrypted file.");
			reader.close();
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cipherText;
	}
	
	// cipherText file should be in format of 16 bytes per line all in hex (32 hex
	// characters)
	// key should be 16 bytes
	public static File decryptFile(File cipherText, String key) {
		// this code opens an input file, writes it's contents on an out file (created
		// within)
		int[] keyArr = Utils.StringTointArray(key);
		BufferedReader reader = null;
		File plainText = null;
		FileWriter myWriter = null;
		String fileName = cipherText.getName().split("\\.")[0] + "_Decrypted.txt";
		try {
//			File in = new File("C:\\Users\\Pc\\Desktop\\in.txt");
			reader = new BufferedReader(new FileReader(cipherText));
			// path should be user provided
			plainText = new File("C:\\Users\\Static\\Desktop\\" + fileName);
			plainText.createNewFile();
			myWriter = new FileWriter(plainText.getAbsolutePath());
			String st;
			while ((st = reader.readLine()) != null) {
				// encrypt each line and write it in an out file
				int[] encryptedLine = TwoFish.decrypt(Utils.StringTointArray(st), keyArr);
				String cipherLine = Utils.writeInput(encryptedLine);
				myWriter.write(cipherLine);
				myWriter.append('\n');
				myWriter.flush();
			}
			System.out.println("Successfully decrypted file.");
			reader.close();
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return plainText;
	}
	
	
	
	//md should be initialised with SHA-256
	//rsa should also be initialised with 2048
	public static String signMsg(String msg2sign,RSA rsa,MessageDigest md) {
		String digest = new String(md.digest(msg2sign.getBytes()));//create a digest of the file
		return rsa.sign(digest);
	}
	
}
