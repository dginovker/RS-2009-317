package org.gielinor.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    /**
     * Hashes text in MD5 using MessageDigest.
     *
     * @param plainText The plain text to be hashed.
     * @return the hashed text
     * @throws NoSuchAlgorithmException
     */
    public static String hash(String plainText) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plainText.getBytes());

        byte byteData[] = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte aByteData : byteData) {
            sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * @param password
     * @param passwordHash
     * @return
     */
    public static boolean checkPassword(String password, String passwordHash, String salt) throws NoSuchAlgorithmException {
        String passwordHashed = MD5.hash(MD5.hash(password) + salt);
        return passwordHash.equals(passwordHashed);
    }
}
