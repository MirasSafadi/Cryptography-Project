package client;

//java imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import common.ClientMessages;
//local imports
import common.CommonMethods;
import common.RSA;
import common.ServerResponse;
import common.Utils;
import server.Server;

public class Client {
	private String userID;
	private Server server;
	private String key;
	/*
	 * private static String byteArray2Hex(final byte[] hash) { Formatter formatter
	 * = new Formatter(); for (byte b : hash) { formatter.format("%02x", b); }
	 * formatter.close(); return formatter.toString(); }
	 */

	/*
	 * public String signImage(RSA rsa, String imagePath) throws IOException,
	 * NoSuchAlgorithmException {
	 * 
	 * File file = new File(imagePath);
	 * 
	 * FileInputStream imageStream = new FileInputStream(imagePath); byte[]
	 * imageInBytes = new byte[imageStream.available()];
	 * imageStream.read(imageInBytes);
	 * 
	 * MessageDigest md = MessageDigest.getInstance("SHA-1"); String resultOfHash =
	 * byteArray2Hex(md.digest(imageInBytes));
	 * 
	 * // String stringOfImage = new String(imageInBytes); return
	 * rsa.signature(resultOfHash);
	 * 
	 * }
	 */
	public static void main(String[] args) {
		RSA rsa = new RSA(2048);// in practice 2048 is more than enough
		// on sender side:
		String msg = "twoFish is nice";
		MessageDigest md;
		String digest = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			digest = new String(md.digest(msg.getBytes()));// create a digest of the encKey
			System.out.println(digest);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String sign = rsa.sign(digest);// sign the digest
		String encKey = rsa.encrypt(msg);// encrypt the message to be sent.
		System.out.println(encKey);
		// ==========================SEND TO RECEIVER(encKey,sign,RSA)======================================
		// on receiver side:
		String decKey = rsa.decrypt(encKey);// decrypt the received message
		System.out.println(decKey);

		// create a digest of the decrypted message
		MessageDigest md1;
		String digest1 = null;
		try {
			md1 = MessageDigest.getInstance("SHA-256");
			digest1 = new String(md1.digest(decKey.getBytes()));// create a digest of the encKey
			System.out.println(digest1);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// verify signature by comparing the digest of decrypted message and the
		// signature
		System.out.println(rsa.verifySignature(sign, digest1));

		
		
		
		
		
		
		
		File in = null;
		try {
			/*
			// path should be user provided for the WAV file
			String wav = Utils.convertWAVtoHEX(new File("C:\\Users\\Pc\\Desktop\\WAV Sample 3 seconds.wav"));
			// System.out.println(wav);
			System.out.println("length = " + wav.length());
			String[] output = Utils.Inputsplitter(wav, 32);
			// -------------------------------------------------------------------------------------------//
			// Fill the input file in the correct line length for further processing
			FileWriter writer = new FileWriter("C:\\Users\\Pc\\Desktop\\in.txt");
			int len = output.length;
			for (int i = 0; i < len; i++) {
				writer.write(output[i] + '\n');
				writer.flush();
			}
			writer.close();
			*/

			in = new File("C:\\Users\\Pc\\Desktop\\in.txt");
			BufferedReader reader = new BufferedReader(new FileReader(in));
			
			String st;
			StringBuffer sb = new StringBuffer();
			while ((st = reader.readLine()) != null) {
				//replace every line with the first 32 characters of it
				String line = st.substring(0, 32);
				sb.append(line);
				sb.append("\n");
			}
			System.out.println("Successfully wrote to the file.");
			
			
			FileWriter myWriter = new FileWriter(in.getAbsolutePath());
			myWriter.write(sb.toString());
			reader.close();
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		CommonMethods.encryptFile(in, "30302030302037442030302030302020");
		
		CommonMethods.decryptFile(in, "30302030302037442030302030302020");
	}
	
	private void login(String userID,String password) {
		String credentials[] = {userID,password};
		sendToServer(credentials, ClientMessages.login);
	}
	private void register(String userID,String password) {
		String credentials[] = {userID,password};
		sendToServer(credentials, ClientMessages.register);
	}
	private void unregister(String userID) {
		sendToServer(userID, ClientMessages.unregister);
	}
	private void exchangeKeys(String key) {
		RSA rsa = new RSA(2048);// in practice 2048 is more than enough
		CommonMethods.init();
		String digest = new String(CommonMethods.md.digest(key.getBytes()));// create a digest of the encKey
		
		String sign = rsa.sign(digest);// sign the digest
		
		String encKey = rsa.encrypt(key);// encrypt the message to be sent.
		Object res[] = {encKey,sign,rsa};
		sendToServer(res, ClientMessages.exchange_key);
	}
	private void storeFile(File fileToStore,String userID) {
		CommonMethods.encryptFile(fileToStore, this.key);
		CommonMethods.signFile(fileToStore);
		Object[] res = {fileToStore,userID};
		sendToServer(res, ClientMessages.store_file);
	}
	
	
	

	public void receiveFromServer(Object message, ServerResponse type) {
		if(type == ServerResponse.request_file_Result) {//return file
			
		}else {//return ack
			
		}
	}

	public void sendToServer(Object message, ClientMessages type) {
		switch(type){
		case login:
			break;
		case store_file:
			break;
		case register:
			break;
		case unregister:
			break;
		//--------------------------//
		case exchange_key:
			break;
		case request_file:
			break;
	}
	}
}
