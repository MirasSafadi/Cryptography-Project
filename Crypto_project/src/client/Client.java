package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import server.TwoFish;
import server.Utils;

public class Client {

	public static void main(String[] args) {
		File out = null;
		try {
			// path should be user provided
			String wav = convertWAVtoHEX(new File("C:\\Users\\Pc\\Desktop\\WAV Sample 3 seconds.wav"));
			System.out.println(wav);
			System.out.println("length = " + wav.length());
			File in = new File("C:\\Users\\Pc\\Desktop\\in.txt");
			BufferedReader reader = new BufferedReader(new FileReader(in));
			// this is just a test it will be removed later
			out = new File("C:\\Users\\Pc\\Desktop\\out.txt");
			out.createNewFile();
			FileWriter myWriter = new FileWriter(out.getAbsolutePath());
			String st;
			while ((st = reader.readLine()) != null) {
				// encrypt each line and write it in an out file
				String line = st.substring(0, 32);
				myWriter.write(line);
				int[] res = StringTointArray(line);
				myWriter.append('\n');
				myWriter.flush();
//				System.out.println(st);
			}
			System.out.println("Successfully wrote to the file.");
			reader.close();
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		File enc = encryptFile(out, "30302030302037442030302030302020");

		File dec = decryptFile(enc, "30302030302037442030302030302020");

	}

	// plainText file should be in format of 16 bytes per line all in hex (32 hex
	// characters)
	// key should be 16 bytes
	public static File encryptFile(File plainText, String key) {
		// this code opens an input file, writes it's contents on an out file (created
		// within)
		int[] keyArr = StringTointArray(key);
		BufferedReader reader = null;
		File cipherText = null;
		FileWriter myWriter = null;
		String fileName = plainText.getName().split("\\.")[0] + "_Encrypted.txt";
		try {
//			File in = new File("C:\\Users\\Pc\\Desktop\\in.txt");
			reader = new BufferedReader(new FileReader(plainText));
			// path should be computed by the server.
			cipherText = new File("C:\\Users\\Pc\\Desktop\\" + fileName);
			cipherText.createNewFile();
			myWriter = new FileWriter(cipherText.getAbsolutePath());
			String st;
			while ((st = reader.readLine()) != null) {
				// encrypt each line and write it in an out file
				int[] encryptedLine = TwoFish.encrypt(StringTointArray(st), keyArr);
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

	// plainText file should be in format of 16 bytes per line all in hex (32 hex
	// characters)
	// key should be 16 bytes
	public static File decryptFile(File cipherText, String key) {
		// this code opens an input file, writes it's contents on an out file (created
		// within)
		int[] keyArr = StringTointArray(key);
		BufferedReader reader = null;
		File plainText = null;
		FileWriter myWriter = null;
		String fileName = cipherText.getName().split("\\.")[0] + "_Decrypted.txt";
		try {
//			File in = new File("C:\\Users\\Pc\\Desktop\\in.txt");
			reader = new BufferedReader(new FileReader(cipherText));
			// path should be user provided
			plainText = new File("C:\\Users\\Pc\\Desktop\\" + fileName);
			plainText.createNewFile();
			myWriter = new FileWriter(plainText.getAbsolutePath());
			String st;
			while ((st = reader.readLine()) != null) {
				// encrypt each line and write it in an out file
				int[] encryptedLine = TwoFish.decrypt(StringTointArray(st), keyArr);
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

	private static int[] StringTointArray(String st) {
		int[] res = new int[st.length() / 2];
		int c = 0;
		for (int i = 0; i < st.length() - 1; i += 2) {
			int x = Integer.parseInt(st.substring(i, i + 2), 16);
			res[c++] = x;
		}
		int[] p = new int[4];
		for (int i = 0; i < p.length; i++) {
			p[i] = res[4 * i] + 256 * res[4 * i + 1] + (res[4 * i + 2] << 16) + (res[4 * i + 3] << 24);
		}
		return p;
	}

	private static String convertWAVtoHEX(File wavInput) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(wavInput));
		int value;
		while ((value = reader.read()) != -1) {
			for (byte b : TwoFish.asBytes(value)) {
				sb.append(String.format("%02X", b));
			}
		}
		reader.close();
		return sb.toString();
	}

	public static boolean sendToServer(File fileToSend) {
		return false;
	}
}
