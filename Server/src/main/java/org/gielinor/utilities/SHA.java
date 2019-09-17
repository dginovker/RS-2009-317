package org.gielinor.utilities;

import org.gielinor.utilities.security.BCrypt;

public class SHA {

    public static boolean checkPassword(String rawPassword, String hashedPassword, String salt) {
        return hashedPassword.equals(hashPassword(rawPassword, salt));
    }

    /**
     * Hashes a given password.
     *
     * @param password
     *                - the password to hash
     * @param salt
     *                - the salt to hash with
     * @return a secure hashed and salted password
     */
    public static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password, "$2a$13$" + salt);
    }

    /**
     * @return a string 22 characters long with random characters to be used as
     *         a salt
     */
    public static String generateSalt() {
        return BCrypt.gensalt();
    }

}
