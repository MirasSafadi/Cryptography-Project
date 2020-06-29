package client;
//java imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;

//local imports
import server.TwoFish;
import server.Utils;
import server.RSA;

public class Client {
	
//	private static String byteArray2Hex(final byte[] hash) {
//        Formatter formatter = new Formatter();
//        for (byte b : hash) {
//            formatter.format("%02x", b);
//        }
////        String s = formatter.toString();
//        formatter.close();
//        return formatter.toString();
//    }
	
	/*
    public String signImage(RSA rsa, String imagePath) throws IOException, NoSuchAlgorithmException {

        File file = new File(imagePath);

            FileInputStream imageStream = new FileInputStream(imagePath);
            byte[] imageInBytes = new byte[imageStream.available()];
            imageStream.read(imageInBytes);

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            String resultOfHash = byteArray2Hex(md.digest(imageInBytes));

            // String stringOfImage = new String(imageInBytes);
            return rsa.signature(resultOfHash);

    }*/
	
	
	
	
	public static void main(String[] args) {
		RSA rsa = new RSA(2048);//in practice 2048 is more than enough
		//on sender side:
		String msg = "twoFish is nice";
		MessageDigest md;
		String digest = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			digest = new String(md.digest(msg.getBytes()));//create a digest of the encKey
			System.out.println(digest);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String sign = rsa.sign(digest);//sign the digest
		String encKey = rsa.encrypt(msg);//encrypt the message to be sent.
		System.out.println(encKey);
		//==========================SEND TO RECEIVER(encKey,sign,RSA)======================================
		//on receiver side:
		String decKey = rsa.decrypt(encKey);//decrypt the received message
		System.out.println(decKey);
		
		//create a digest of the decrypted message
		MessageDigest md1;
		String digest1 = null;
		try {
			md1 = MessageDigest.getInstance("SHA-256");
			digest1 = new String(md1.digest(decKey.getBytes()));//create a digest of the encKey
			System.out.println(digest1);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//verify signature by comparing the digest of decrypted message and the signature
		System.out.println(rsa.verifySignature(sign, digest1));
		
		
		
		File out = null;
		try {
			
			//path should be user provided for the WAV file
			String wav = convertWAVtoHEX(new File("C:\\Users\\Static\\Desktop\\M1F1-Alaw-AFsp.wav"));
			//System.out.println(wav);
			System.out.println("length = " + wav.length());
			String[] output=Inputsplitter(wav,32);
			//-------------------------------------------------------------------------------------------//
			//Fill the input file in the correct line length for further processing
			FileWriter writer = new FileWriter("C:\\Users\\Static\\Desktop\\in.txt");
			int len = output.length;
			for (int i = 0; i < len; i++) {
			   writer.write(output[i]+'\n');
			}
			writer.close();
			
			File in = new File("C:\\Users\\Static\\Desktop\\in.txt");
			BufferedReader reader = new BufferedReader(new FileReader(in));
			// this is just a test it will be removed later
			out = new File("C:\\Users\\Static\\Desktop\\out.txt");
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
			cipherText = new File("C:\\Users\\Static\\Desktop\\" + fileName);
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
			plainText = new File("C:\\Users\\Static\\Desktop\\" + fileName);
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
	
	private static String[] Inputsplitter(String input, int length)
	{
		int len=input.length();
		int zeroPadCheck=len%32;		//Save how many zeros we need to zero-pad the last input line
		if(zeroPadCheck!=0) len=(input.length()/32)+1;
		else len=input.length()/32;
		
	    String[] output = new String[len];
	    int pos = 0;
	    for(int i=0;i<len;i++)
	    {	
	    	//Zero-padding the last line in an uneven Modulo32 input
	    	if((i==len-1)&&(zeroPadCheck!=0)) {				
	    		output[i]=input.substring(pos, pos+zeroPadCheck);
		    	char[] repeat = new char[zeroPadCheck];
		    	Arrays.fill(repeat, '0');
		    	output[i] += new String(repeat);
	    		break;
	    	}
	        output[i] = input.substring(pos, pos+length);
	        pos = pos + length;
	        
	    }
	    return output;
	}

	public static boolean sendToServer(File fileToSend) {
		return false;
	}
}
