package com.bike.ztd.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 编码解码工具. 包括 AES 对称加密，GZip 压缩，RSA 非对称加密.
 *
 * @author Liu Jian
 */
@Slf4j
public class CodecUtils {

    private static final byte[] key = {'e', 'm', 'v', '$', 'k', 'C', 'V', '*', 't', 'k', 'f', '4', '!', 't', '7', 'v'};
    private static final byte[] iv = {'^', '1', 'O', 'h', 'r', 'l', '9', 'v', 'A', 'D', '%', '3', 's', '!', 'b', 'f'};
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final String CHARSET = "UTF-8";
    private static final String RSA_ALGORITHM = "RSA";

    private static SecretKeySpec getKey() {
        return new SecretKeySpec(key, "AES");
    }

    private static IvParameterSpec getIv() {
        return new IvParameterSpec(iv);
    }

    public static Cipher getCipher(int opmode) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(opmode, getKey(), getIv());
        return cipher;
    }

    /**
     * 使用 AES 对内容进行加密，再使用 base64 编码转换成字符串.
     *
     * @param content 需要加密的内容
     * @return 加密后的内容
     * @throws Exception 加密过程中异常
     */
    public static String aesEncrypt(String content) throws Exception {
        byte[] bytes = content.getBytes("utf-8");

        byte[] result = getCipher(Cipher.ENCRYPT_MODE).doFinal(bytes);

        return new String(Base64.getEncoder().encode(result), "utf-8");
    }

    /**
     * 对加密内容进行解密. 先使用 base64 对内容进行解码，在使用 aes 进行解密.
     * @param content 需要解密的内容
     * @return 解密后的内容
     * @throws Exception 解密过程中，发生的错误。
     */
    public static String aesDecrypt(String content) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(content);

        byte[] result = getCipher(Cipher.DECRYPT_MODE).doFinal(bytes);

        return new String(result, "utf-8");
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return "";
        }

        for (byte b : bytes) {
            int value = b & 0xFF;
            String str = Integer.toHexString(value);
            if (str.length() < 2) {
                sb.append(0);
            }
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 对字符串进行分组异或处理.
     * @param src 待异或的字符串
     * @param key 异或的 key
     * @return 异或后新的字符串
     */
    public static String xor(String src, String key) {
        if (src == null || src.length() == 0 || key == null || key.length() == 0) {
            return src;
        }
        char[] sc = src.toCharArray();
        char[] kc = key.toCharArray();
        int len = key.length();
        for (int idx = 0; idx < sc.length; ++idx) {
            sc[idx] = (char)(sc[idx] ^ kc[idx % len]);
        }

        return new String(sc);
    }

    /**
     * 使用 GZip 对字符串进行压缩.
     * @param src 待压缩的字符串.
     * @return 压缩后的字符串
     * @throws IOException 如果压缩出错，将会抛出该异常
     */
    public static String compress(String src) throws IOException {
        if (src == null || src.length() <= 0) {
            return src;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(src.getBytes(StandardCharsets.UTF_8.name()));

        // You have to call close() on the GZIPOutputStream before you attempt to read it.
        // see https://stackoverflow.com/a/24531640
        gzip.close();

        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        String dest = out.toString(StandardCharsets.ISO_8859_1.name());

        // close stream
        out.close();

        return dest;
    }

    /**
     * 字符串的解压.
     * @param src 对字符串解压
     * @return 返回解压缩后的字符串
     * @throws IOException 如果解压失败，返回该异常
     */
    public static String unCompress(String src) throws IOException {
        if (src == null || src.length() <= 0) {
            return src;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(src.getBytes(StandardCharsets.ISO_8859_1.name()));
        GZIPInputStream gzip = new GZIPInputStream(in);
        byte[] buffer = new byte[512];
        int n;
        while ((n = gzip.read(buffer)) >= 0) {
            // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
            out.write(buffer, 0, n);
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        String dest = out.toString(StandardCharsets.UTF_8.name());

        // close stream
        gzip.close();
        in.close();
        out.close();

        return dest;
    }

    /**
     * 生成RSA 公钥，私钥键值对.
     * @param keySize the key size. This is an algorithm-specific metric, such as
     *                modulus length, specified in number of bits. 最小值为 512
     * @return map 对象，两个key，分别为 "publicKey" 和 "privateKey"
     */
    public static Map<String, String> generateRsaKeyPair(int keySize) {
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch(NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm [" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();

        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(publicKey.getEncoded());

        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws NoSuchAlgorithmException if no Provider supports a KeyFactorySpi implementation for
     *   the specified algorithm.
     * @throws InvalidKeySpecException if the given key specification is inappropriate for this
     *   key factory to produce a public key.
     */
    private static RSAPublicKey getRsaPublicKey(String publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(org.apache.commons.codec.binary.Base64.decodeBase64(publicKey));
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

    /**
     * 得到私钥
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws NoSuchAlgorithmException if no Provider supports a KeyFactorySpi implementation for
     *   the specified algorithm.
     * @throws InvalidKeySpecException if the given key specification is inappropriate for this
     *   key factory to produce a private key.
     */
    private static RSAPrivateKey getRsaPrivateKey(String privateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        //通过 PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(org.apache.commons.codec.binary.Base64.decodeBase64(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 公钥加密.
     * @param data 待加密的数据.
     * @param publicKey 经过 Base64 编码后的公钥 key.
     * @return 加密后的内容，经过 Base64 编码变换.
     * @throws GeneralSecurityException 加解密异常时，抛出该异常
     * @throws UnsupportedEncodingException Base64 编码错误时，抛出该异常
     */
    public static String rsaPublicEncrypt(String data, String publicKey)
            throws GeneralSecurityException, UnsupportedEncodingException {

        RSAPublicKey pk = getRsaPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pk);
        byte[] bytes = rsaSplitCodec(
                cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), pk.getModulus().bitLength());
        return org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(bytes);
    }

    /**
     * 公钥加密.
     * @param data 待加密的数据.
     * @param publicKey 经过 Base64 编码后的公钥 key.
     * @return 加密后的内容，经过 Base64 编码变换.
     * @throws GeneralSecurityException 加解密异常时，抛出该异常
     * @throws UnsupportedEncodingException Base64 编码错误时，抛出该异常
     */
    public static String rsaPublicEncryptByEncodeBase64String(String data, String publicKey)
            throws GeneralSecurityException, UnsupportedEncodingException {

        RSAPublicKey pk = getRsaPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pk);
        byte[] bytes = rsaSplitCodec(
                cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), pk.getModulus().bitLength());
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }


    /**
     * 私钥解密.
     * @param data 待加密的数据.
     * @param privateKey 经过 Base64 编码后的私钥 key.
     * @return 加密后的内容，经过 Base64 编码变换.
     * @throws GeneralSecurityException 加解密异常时，抛出该异常
     * @throws UnsupportedEncodingException Base64 编码错误时，抛出该异常
     */
    public static String rsaPrivateDecrypt(String data, String privateKey)
            throws GeneralSecurityException, UnsupportedEncodingException {

        RSAPrivateKey pk = getRsaPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, pk);
        byte[] decodeData = org.apache.commons.codec.binary.Base64.decodeBase64(data);
        byte[] decryptData = rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, decodeData, pk.getModulus().bitLength());
        return new String(decryptData, CHARSET);
    }


    /**
     * 私钥加密.
     * @param data 待加密的数据.
     * @param privateKey 经过 Base64 编码后的私钥 key.
     * @return 加密后的内容，经过 Base64 编码变换.
     * @throws GeneralSecurityException 加解密异常时，抛出该异常
     * @throws UnsupportedEncodingException Base64 编码错误时，抛出该异常
     */
    public static String rsaPrivateEncrypt(String data, String privateKey)
            throws GeneralSecurityException, UnsupportedEncodingException {

        RSAPrivateKey pk = getRsaPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pk);
        byte[] bytes = rsaSplitCodec(
                cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), pk.getModulus().bitLength());
        return org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(bytes);
    }

    /**
     * 共钥解密.
     * @param data 待加密的数据.
     * @param publicKey 经过 Base64 编码后的私钥 key.
     * @return 加密后的内容，经过 Base64 编码变换.
     * @throws GeneralSecurityException 加解密异常时，抛出该异常
     * @throws UnsupportedEncodingException Base64 编码错误时，抛出该异常
     */
    public static String rsaPublicDecrypt(String data, String publicKey)
            throws GeneralSecurityException, UnsupportedEncodingException {

        RSAPublicKey pk = getRsaPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, pk);
        byte[] decodeData = org.apache.commons.codec.binary.Base64.decodeBase64(data);
        byte[] decryptData = rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, decodeData, pk.getModulus().bitLength());
        return new String(decryptData, CHARSET);
    }


    /**
     * 分组进行加/解密.
     * @param cipher 加/解密 Cipher
     * @param opMode 加密模式，加密/解密
     * @param data 待加/解密的数据
     * @param keySize 密钥大小
     * @return 加/解密之后的字节.
     * @throws IllegalStateException - if this cipher is in a wrong state (e.g., has not been initialized)
     * @throws IllegalBlockSizeException - if this cipher is a block cipher, no padding has been requested
     *   (only in encryption mode), and the total input length of the data processed by this cipher is not
     *   a multiple of block size; or if this encryption algorithm is unable to process the input data provided.
     * @throws BadPaddingException - if this cipher is in decryption mode, and (un)padding has been requested,
     *   but the decrypted data is not bounded by the appropriate padding bytes
     * @throws AEADBadTagException - if this cipher is decrypting in an AEAD mode (such as GCM/CCM), and the
     *   received authentication tag does not match the calculated value
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opMode, byte[] data, int keySize)
            throws IllegalStateException, IllegalBlockSizeException, BadPaddingException, AEADBadTagException {
        int maxBlock;
        if (opMode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }

        int offSet = 0;
        byte[] buff;
        int i = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (data.length > offSet) {
            // 分组加/解密
            if (data.length - offSet > maxBlock) {
                buff = cipher.doFinal(data, offSet, maxBlock);
            } else {
                buff = cipher.doFinal(data, offSet, data.length - offSet);
            }
            out.write(buff, 0, buff.length);
            i++;
            offSet = i * maxBlock;
        }

        byte[] result = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            log.info("Cannot close output stream due to", e);
        }
        return result;
    }


}