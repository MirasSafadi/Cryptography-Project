package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import common.Utils;

public class CommonMethods {
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
	
	
	
	public static String signMsg(File fileToSign,RSA rsa) {
		
		return null;
	}
	
	
}
