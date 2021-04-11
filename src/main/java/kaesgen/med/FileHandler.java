/**
 * CovidImpfplaner
 * Dieses Programm unterstützt Arztpraxen bei der Organisation der Impfungen
 * gegen COVID-19
 *
 * CovidImpfPlaner  Copyright (C) 2021  Philipp Käsgen
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *
 * pkaesgen(AT)freenet.de
 */
package kaesgen.med;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class FileHandler {

    /*
        I'm no crypto expert. At the time of implementation, all I needed to
        know I read here

        https://www.novixys.com/blog/aes-encryption-decryption-password-java/

        and here

        https://security.stackexchange.com/questions/211/how-to-securely-hash-passwords/31846#31846
    */

    /** crypto stuff. */
    private static final String SECRET_KEY_INSTANCE = "PBKDF2WithHmacSHA256";
    /** crypto stuff part 2. */
    private static final String CIPHER_INSTANCE = "AES/CBC/PKCS5Padding";

    /**
     * Default constructor.
     */
    public FileHandler() {
    }

    /**
     * Encrypts.
     * @param password
     * @param fileName
     * @param content
     * @throws Exception
     */
    private void encrypt(final String password, final String fileName,
    final List<? extends Serializable> content) throws Exception {

        SecureRandom srandom = new SecureRandom();

        byte[] salt = new byte[8];
        srandom.nextBytes(salt);
        SecretKeyFactory factory = SecretKeyFactory
            .getInstance(SECRET_KEY_INSTANCE);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec skey = new SecretKeySpec(tmp.getEncoded(), "AES");

        byte[] iv = new byte[128 / 8];
        srandom.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        FileOutputStream fos = new FileOutputStream(fileName, false);

        //first write the salt and iv as header. It's equired for decryption
        fos.write(salt);
        fos.write(iv);

        Cipher ci = Cipher.getInstance(CIPHER_INSTANCE);
        ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);
        CipherOutputStream cos = new CipherOutputStream(fos, ci);

        ObjectOutputStream oos = new ObjectOutputStream(cos);

        for (Serializable s : content) {
            oos.writeObject(s);
        }

        fos.flush();
        cos.flush();
        oos.flush();

        oos.close();
        cos.close();
        fos.close();

    }

    /**
     * Decrypts.
     * @param <T>
     * @param returnType
     * @param password
     * @param fileName
     * @return List of the class type
     * @throws Exception
     */
    private <T> List<T> decrypt(final Class<T> returnType,
    final String password, final String fileName) throws Exception {

        FileInputStream fis = new FileInputStream(fileName);

        byte[] salt = new byte[8];
        byte[] iv = new byte[128 / 8];
        fis.read(salt);
        fis.read(iv);

        SecretKeyFactory factory = SecretKeyFactory
            .getInstance(SECRET_KEY_INSTANCE);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000,
            128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec skey = new SecretKeySpec(tmp.getEncoded(), "AES");


        Cipher ci = Cipher.getInstance(CIPHER_INSTANCE);
        ci.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(iv));

        CipherInputStream cis = new CipherInputStream(fis, ci);
        ObjectInputStream ois = new ObjectInputStream(cis);

        List<T> result = new ArrayList<T>();

        while (true) {
            try {
                result.add(returnType.cast(ois.readObject()));
            } catch (EOFException e) {
                break;
            }
        }


        fis.close();
        cis.close();
        ois.close();

        return result;
    }

    /**
     * Encrypts and saves a list of serializable objects into a file.
     * @param password
     * @param fileName
     * @param content
     * @throws Exception
     */
    public final void saveFile(final String password, final String fileName,
    final List<? extends Serializable> content) throws Exception {

        encrypt(password, fileName, content);

    }

    /**
     * Decrypts and loads a file of a given class into a list of that
     * class's type.
     * @param <T>
     * @param returnType
     * @param password
     * @param fileName
     * @return list of type returnType
     * @throws Exception
     */
    public final <T> List<T> loadFile(final Class<T> returnType,
    final String password, final String fileName)  throws Exception {

        return decrypt(returnType, password, fileName);

    }

}
