package com.authine.cloudpivot.ext.util;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public abstract class LicenseFactory {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {

        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }


    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return hex2BYTE(key);
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return byteHEX(key);
    }

    public static byte[] hex2BYTE(String hex) throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }

    /**
     * 编码byte
     *
     * @param ib
     * @return
     */
    public static String byteHEX(byte[] ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuffer strb = new StringBuffer();
        for (byte data : ib) {
            char[] ob = new char[2];
            ob[0] = Digit[(data >>> 4) & 0X0F];
            ob[1] = Digit[data & 0X0F];
            strb.append(ob);
        }
        return strb.toString();
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //例：
        String pubKey = "30819F300D06092A864886F70D010101050003818D0030818902818100A0B417DA8977B98D00732E96F854FB2D1174E93D9D159BD0612FB9841191C8CD8F1DAF441A0BB34EAE039252501709199C065D1D60D934A1D6BDD4AABA5B988900914128C10C48A2099CA0009678C8EA858B3CD74441A6AFB4F55CF61E24F60714832B2F3F84385AA63E3C44A7CB6141E4ED4A7727A1255634E09F70405DC6F50203010001";
        String priKey = "30820276020100300D06092A864886F70D0101010500048202603082025C02010002818100A0B417DA8977B98D00732E96F854FB2D1174E93D9D159BD0612FB9841191C8CD8F1DAF441A0BB34EAE039252501709199C065D1D60D934A1D6BDD4AABA5B988900914128C10C48A2099CA0009678C8EA858B3CD74441A6AFB4F55CF61E24F60714832B2F3F84385AA63E3C44A7CB6141E4ED4A7727A1255634E09F70405DC6F502030100010281804D67D1A18ADB673D9A7CE07109766D71BCD9D24AC23219561AA9D132E2FF947826BA46A487E320E0020368644B7D7CFD9FE094D344C5FA8B0593742F340A5082CE3989CA36074AAC10CDFB0E4BEF62955DC1D9381A51DE86040E07CE99CFACB38A7DD3C07372FE7F6FDEB66AA93357D171F324F268813544C130E6275153B8C1024100F47CAFFC197B77482023F710D075A9849CCBAEFCB0C2717A355FF69170B4D38D1670952C62890C11157874F2731D094AE8FFB7212215F556D763C4513D7F0865024100A8456337F603E3A4D57F26199A83A013C774C007758C41F76E0CCAE88756150127E64E0B0B57447FE1AC9027DA0BEE076EB9830A01650660F012F0A482803351024100DB5136F5694E8E744F10B4438B1CE1D2AA6D352E4CCA40CDA62FCD9A2E86A7F7BA9787D9E712959685B227068F290EFAFA04DA9C885D36568EA55AA89E07E4D902401EB69B878955E8651BBADB7F6CDE7D875EAE86A655E445DE1DAE24131130BB20BCE1A2790D96DE1FE15717F0CE31C33E840D4447BD3D6D4EA04334BD323CA8210240683656E294F976A0D2405EFE6465CA338DC25ADBC86325786231C291B27927786C2C2655A5697FE0CAF66B2914F56FECE1811D70BBBAEDE5A58D668985396D46";
        System.out.println("pubKey=" + pubKey);
        System.out.println("priKey=" + priKey);
        // 把“1231s23”加密得到sign
        String sign = "8DB5354E386755B61926E2DF64D7F00C3D02292588B95EE8DBA08DFD0A08E970EB3AA26451DF91EA06DDBA95AA6900BBEB7141225381B546BCD5A9DF4894BE3C0EBC0C6CAFE3C7EF435E22F56393CC8B0C58C971D7C8DE77855F403315C5F6AE777634622FE2B79604860C61F91AED1D0E9066DC9F2318B5957B76A043DECFBC";
        System.out.println("sign=" + sign);
        //拿内容、公钥、sign 验证
        System.out.println(verify("1231s23".getBytes(), pubKey, sign));
    }

}