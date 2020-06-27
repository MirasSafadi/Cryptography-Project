package server;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;


public class RSA {
    private final static SecureRandom random = new SecureRandom();
    private BigInteger privateKey;//this is d
    private BigInteger publicKey;//this is e
    private BigInteger modulus;//this is n=p*q
    

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public BigInteger getModulus() {
        return modulus;
    }

    

    //generate an N-bit (roughly) public and private key
    public RSA(int N) {
    	//generate two random prime numbers
        BigInteger p = BigInteger.probablePrime(N / 2, random);
        BigInteger q = BigInteger.probablePrime(N / 2, random);
        //compute phi(n)=phi(p*q)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        //compute n = p*q
        modulus = p.multiply(q);
        publicKey = new BigInteger("65537");// common value in practice = 2^16 + 1 value of e
        //compute the mod inverse of e modulo phi, i.e. d*e = 1 mod phi
        privateKey = publicKey.modInverse(phi);
    }

    //when encrypting need to choose message, e, and n.
    public String encrypt(String message) {
    	//represent the message as a number
        byte[] bytes = message.getBytes();
        BigInteger messageBigInt = new BigInteger(bytes);
        //encrypt this number using e and n
        BigInteger encrypted = messageBigInt.modPow(this.publicKey, this.modulus);
        //return the encrypted message as a string
        return Base64.getEncoder().encodeToString(encrypted.toByteArray());
    }
    //only this instance of the class can decrypt
    public String decrypt(String encrypted) {

        BigInteger encryptedInt = new BigInteger(Base64.getDecoder().decode(encrypted));
        BigInteger decrypted = encryptedInt.modPow(this.privateKey, this.modulus);
        
        return new String(decrypted.toByteArray());

    }
    
    //============================DIGITAL SIGNATURE=======================================//
    //message = encrypted message OR encrypted key
    //signature is very similar to encryption only difference is in signature we use private key
    //instead of public key
    public String sign(String message) {
    	//convert message to a number
        byte[] bytes = message.getBytes();
        BigInteger messageBigInt = new BigInteger(bytes);
        //sign it using the same encryption technique only using the private key instead of the public one
        BigInteger signature = messageBigInt.modPow(this.privateKey, this.modulus);
        //return the signature as a string
        return Base64.getEncoder().encodeToString(signature.toByteArray());
    }
    //signature - the signature to verify, msg - encrypted key/message
    public boolean verifySignature(String signature, BigInteger encryptedMsg) {
    	//
//    	BigInteger encryptedMsgInt = new BigInteger(Base64.getDecoder().decode(encryptedMsg));
    	//decode the signature message as a number
        BigInteger decodedSignature = new BigInteger(Base64.getDecoder().decode(signature));
        //encrypt the signature 
        BigInteger encryptedDecodedSignature = decodedSignature.modPow(this.publicKey, this.modulus);
        //===========>hash both encryptedDecodedSignature and msg and compare hash values<===================//
        //compare encrypted decoded signature and  
        return encryptedMsg.equals(encryptedDecodedSignature);
//        return encryptedDecodedSignature.equals(encryptedMsg);
    }

    @Override
    public String toString() {
        return String.format("public = %s\nprivate = %s\nmodulus = %s\n", 
        		this.publicKey.toString(),this.privateKey.toString(),this.modulus.toString());
    }

}