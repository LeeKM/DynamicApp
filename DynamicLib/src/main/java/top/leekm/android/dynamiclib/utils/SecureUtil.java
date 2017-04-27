package top.leekm.android.dynamiclib.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import top.leekm.android.dynamiclib.DynamicBundle;

/**
 * Created by lkm on 2017/4/27.
 */

public class SecureUtil {

    public static Cipher generateRSAPublicKeyDecryptor(final String publicKey) {
        final AtomicReference<Cipher> cipher = new AtomicReference<>();
        if (!TextUtils.isEmpty(publicKey)) {
            RunOrThrow.wrapper(new RunOrThrow() {
                @Override
                protected void todo() throws Throwable {
                    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decode(publicKey, Base64.NO_WRAP));
                    KeyFactory factory = KeyFactory.getInstance("RSA");
                    PublicKey key = factory.generatePublic(publicKeySpec);
                    Cipher decrypter = Cipher.getInstance("RSA");
                    decrypter.init(Cipher.DECRYPT_MODE, key);
                    cipher.set(decrypter);
                }
            });
        }
        return cipher.get();
    }

    public static Cipher generateRSAPrivateKeyEncryptor(final String privateKey) {
        final AtomicReference<Cipher> encryptor = new AtomicReference<>();
        if (!TextUtils.isEmpty(privateKey)) {
            RunOrThrow.wrapper(new RunOrThrow() {
                @Override
                protected void todo() throws Throwable {
                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.NO_WRAP));
                    KeyFactory factory = KeyFactory.getInstance("RSA");
                    PrivateKey key = factory.generatePrivate(keySpec);
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE, key);
                    encryptor.set(cipher);
                }
            });
        }
        return encryptor.get();
    }

    public static boolean checkBundleDigest(DynamicBundle bundle, Cipher decrypter) {
        if (null != decrypter) {
            try {
                byte[] secure = decrypter.doFinal(Base64.decode(bundle.secure, Base64.NO_WRAP));
                return Arrays.equals(secure, FileUtils.getFileSHA256Digest(new File(bundle.filePath)));
            } catch (Throwable throwable) {
                return false;
            }
        }
        return true;
    }

    public static void getBundleDigest(String rsaPrivate, DynamicBundle bundle)
            throws IOException, BadPaddingException, IllegalBlockSizeException {
        Cipher encrypter = SecureUtil.generateRSAPrivateKeyEncryptor(rsaPrivate);
        byte[] digest = FileUtils.getFileSHA256Digest(new File(bundle.filePath));
        bundle.secure = Base64.encodeToString(encrypter.doFinal(digest), Base64.NO_WRAP);
    }
}
