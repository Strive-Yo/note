package site.qifen.note.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

public class PassUtil {


    public static String en(byte[] key, String enText) {
        AES aes = SecureUtil.aes(key);
        return aes.encryptHex(enText);
    }

    public static String de(byte[] key, String deText) {
        AES aes = SecureUtil.aes(key);
        return aes.decryptStr(deText);
    }


    public static byte[] getKey() {
        return SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
    }


}
