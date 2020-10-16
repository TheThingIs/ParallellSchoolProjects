package Model;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

public class Login {
    private final ArrayList<String> userNames = new ArrayList<>();
    private final HashMap<String, String> passwords = new HashMap<>();
    private static SecretKeySpec secretKey;
    private final static String keyString = "QeThWmZq4t7w!z%C*F-JaNcRfUjXn2r5";
    private static byte[] key;

    protected Login() {
    }

    /**
     * Creates a new user with username and password
     *
     * @param userName The username of the employee/admin
     * @param password The password of the employee/admin
     */
    protected void newUser(String userName, String password) {
        userNames.add(userName);
        passwords.put(userName, encrypt(password));
    }

    /**
     * Removes a user with the specified username and password
     *
     * @param userName the username of the user to remove
     * @param password the password of the user to remove
     */
    protected void removeUser(String userName, String password) {
        if (userNames.contains(userName)) {
            if (decrypt(passwords.get(userName)).equals(password)) {
                passwords.remove(userName);
                userNames.remove(userName);
            }
        }
    }

    /**
     * Checks if the provided username and password matches a user inside the list of all users
     *
     * @param userName the username of the user
     * @param password the password of the user
     * @return If the username and password matches a current user
     */
    public boolean isLoginInformationCorrect(String userName, String password) {
        if (userNames.contains(userName)) {
            return decrypt(passwords.get(userName)).equals(password);
        }
        return false;
    }

    /**
     * See https://howtodoinjava.com/java/java-security/java-aes-encryption-example/ for how it works
     */
    private static void setKey() {
        MessageDigest sha;
        try {
            key = keyString.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * See https://howtodoinjava.com/java/java-security/java-aes-encryption-example/ for how it works
     */
    private static String encrypt(String strToEncrypt) {
        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    /**
     * See https://howtodoinjava.com/java/java-security/java-aes-encryption-example/ for how it works
     */
    private static String decrypt(String strToDecrypt) {
        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }


}
