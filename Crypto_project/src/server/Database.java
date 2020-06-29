package server;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
	//password should be hashed before insertion
	private static HashMap<String,String> users;//save tuples of (userID,password)
	private static HashMap<String,ArrayList<File>> files;
	private static MessageDigest md;
	
	public Database() throws NoSuchAlgorithmException {
		users = new HashMap<>();
		files = new HashMap<>();
		md = MessageDigest.getInstance("SHA-256");
	}
	public boolean addUser(String userID,String password) {
		String hashPass = new String(md.digest(password.getBytes()));
		if(users.containsKey(userID)) {
			System.out.println("user exists");
			return false;
		}
		users.put(userID, hashPass);
		files.put(userID, new ArrayList<>());
		return true;
	}
	public boolean removeUser(String userID) {
		if(!users.containsKey(userID)) {
			System.out.println("no such user");
			return false;
		}
		users.remove(userID);
		files.remove(userID);
		return true;
	}
	public boolean checkCredentials(String userID,String password) {
		if(!users.containsKey(userID)) {
			System.out.println("no such user");
			return false;
		}
		String hashPass = new String(md.digest(password.getBytes()));
		String storedPass = users.get(userID);
		return hashPass.equals(storedPass);
	}
	public boolean addFile(String userID,File file) {
		if(!users.containsKey(userID)) {
			System.out.println("no such user");
			return false;
		}
		ArrayList<File> filesList = files.get(userID);
		filesList.add(file);
		files.replace(userID, files.get(userID), filesList);
		return true;
	}
	
}
