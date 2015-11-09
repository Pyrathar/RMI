/**
 * Created by raz on 10/28/15.
 */

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Database
{

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    public static final int SALT_BYTE_SIZE = 24;
    public static final int HASH_BYTE_SIZE = 24;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;

    public static void main(String args[]) throws NoSuchAlgorithmException, SQLException, InvalidKeySpecException {
        login("seph", "1q2w3e4r");
    }

    /*
    *
    *
    * Registers a new user
    * NOT WORKING ATM
    *
    *
    * */



    public static String register(String user,String input) throws NoSuchAlgorithmException, SQLException, InvalidKeySpecException {

       String hashed = createHash(input);
       String sql = "INSERT INTO Users (id,username, password)"+
               " VALUES (?,?,?);";
       System.out.println(sql);
       runsql(sql,user,input);
       return null;
   }

    /*
    *
    *
    * Login
    *
    *
    *
    * */

    public static String login(String user,String input) throws NoSuchAlgorithmException, InvalidKeySpecException {

        {
            Connection c = null;
            Statement stmt = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:test.db");
                c.setAutoCommit(false);
                System.out.println("Opened database successfully");

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT password FROM Users where username = 'seph' " );
                while ( rs.next() ) {



                    String serverpass = rs.getString("password");
                    char[] userpass=input.toCharArray();

                    String[] params = serverpass.split(":");
                    int iterations = Integer.parseInt(params[ITERATION_INDEX]);
                    byte[] salt = fromHex(params[SALT_INDEX]);
                    byte[] hash = fromHex(params[PBKDF2_INDEX]);
                    // Compute the hash of the provided password, using the same salt,
                    // iteration count, and hash length
                    byte[] testHash = pbkdf2(userpass, salt, iterations, hash.length);
                    // Compare the hashes in constant time. The password is correct if
                    // both hashes match.

                    String result = String.valueOf(slowEquals(hash,testHash));
                    if (result.equals("true")){
                        System.out.print("Login Successful");
                    }
                    else {
                        System.out.print("Login Failed");
                    }

                    return String.valueOf(slowEquals(hash,testHash));
                }
                rs.close();
                stmt.close();
                c.close();
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            System.out.println("Operation done successfully");
        }



        return null;
    }

    public static void runsql(String sql,String user,String pass) throws SQLException {

        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            if(c == null) {
                c = DriverManager.getConnection("jdbc:sqlite:test.db");
                PreparedStatement stmt = null;
                stmt = c.prepareStatement(sql);
                stmt.setString(1, "2");
                stmt.setString(2, user);
                stmt.setString(3, pass);
                System.out.println(stmt);
                stmt.executeUpdate();
                stmt.close();
                c.close();

            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Done");
    }


    /*


   Create a Hash and Salts it

     */

    public static String createHash(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return createHash(password.toCharArray());
    }


    public static String createHash(char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);

        // Hash the password
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        // format iterations:salt:hash
        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" +  toHex(hash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    private static byte[] fromHex(String hex)
    {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }


    private static String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }


    /*
     *Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     */

    private static boolean slowEquals(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }



}