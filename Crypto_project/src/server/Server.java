package server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import client.Client;
import common.ClientMessages;
import common.CommonMethods;
import common.RSA;
import common.ServerResponse;
import common.Utils;

public class Server {
	private Database db;
	private Client client;
	private String key;
	
	public Server() {
		try {
			this.db = Database.getInstance();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void login(String userID,String password) {
		boolean loginResult = db.checkCredentials(userID, password);
		sendToClient(loginResult, ServerResponse.login_Result);
	}
	private void register(String userID,String password) {
		boolean registerResult = db.addUser(userID, password);
		sendToClient(registerResult,ServerResponse.register_Result);
	}
	private void unregister(String userID) {
		boolean unregisterResult = db.removeUser(userID);
		sendToClient(unregisterResult, ServerResponse.unregister_Result);
	}
	
	/*
	 * to store a file the user must first exchange keys
	 */
	private void exchangeKey(String encKey,String signature,RSA rsa) {
		//must decrypt key using rsa and validate authenticity
		String decKey = rsa.decrypt(encKey);// decrypt the received key
		System.out.println(decKey);
		//if md is null, initialise it
		CommonMethods.init();
		// create a digest of the decKey
		String digest = new String(CommonMethods.md.digest(decKey.getBytes()));
		System.out.println(digest);
		//verify signature by comparing the digest of decrypted message and the signature
		boolean exchgKeyResult = rsa.verifySignature(signature, digest);
		if(exchgKeyResult)
			this.key = decKey;
		sendToClient(exchgKeyResult, ServerResponse.exchange_Key);
	}
	private void storeFile(File encryptedFile,RSA rsa,String userID) {
		//need to "destroy" the key for this session, i.e. assigning it to null
		String signature = Utils.extactSignature(encryptedFile);
		CommonMethods.decryptFile(encryptedFile, this.key);
		CommonMethods.init();
		String contents = Utils.fileToString(encryptedFile);
		String digest = new String(CommonMethods.md.digest(contents.getBytes()));
		boolean verifySign = rsa.verifySignature(signature, digest);
		boolean res = false;
		if(verifySign) {
			res = db.addFile(userID, encryptedFile);
			this.key = null;
		}
		sendToClient(res, ServerResponse.store_file_Result);
	}
	private void getFile(String userID,String filename) {
		File f = db.getFile(userID, filename);
		//encrypt file using key exchanged earlier and send it to client.
		CommonMethods.encryptFile(f, this.key);
		//sign the file before sending it
		CommonMethods.signFile(f);
		//need to "destroy" the key for this session, i.e. assigning it to null
		this.key = null;
		sendToClient(f, ServerResponse.request_file_Result);
	}
	
	public void receiveFromClient(Object message,ClientMessages type) {
		switch(type){
			case login:
				break;
			case store_file:
				break;
			case register:
				break;
			case unregister:
				break;
			case exchange_key:
				break;
			//--------------------------//	
			case request_file:
				
				break;
		}
	}
	public void sendToClient(Object message,ServerResponse type) {
		if(type == ServerResponse.request_file_Result) {//return file
			client.receiveFromServer(message, type);
		}else {//return ack
			client.receiveFromServer(message, type);
		}
	}
}
