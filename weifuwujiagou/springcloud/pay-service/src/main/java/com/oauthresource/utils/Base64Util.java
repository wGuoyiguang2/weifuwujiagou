package com.oauthresource.utils;

import java.util.Base64;


public class Base64Util {

    /***
     * 普通解密操作
     * @param encodedText：密文
     * @return
     */
    public static byte[] decode(String encodedText){
        final Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(encodedText);
    }

    /***
     * 普通加密操作
     * @param data
     * @return
     */
    public static String encode(byte[] data){
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    /***
     * 解密操作
     * @param encodedText
     * @return
     */
    public static byte[] decodeURL(String encodedText){
        final Base64.Decoder decoder = Base64.getUrlDecoder();
        return decoder.decode(encodedText);
    }

    /***
     * 加密操作
     * @param data
     * @return
     */
    public static String encodeURL(byte[] data){
        final Base64.Encoder encoder = Base64.getUrlEncoder();
        return encoder.encodeToString(data);
    }

    public static void main(String[] args) throws Exception {
        String str = "你好！赵尚志！";
        String encode = encode(str.getBytes("UTF-8"));
        byte[] decode = decode(encode);
        //   你好！赵尚志！
        System.out.println(new String(decode, "UTF-8"));
        //
        System.out.println(encode);

        String s = encodeURL(str.getBytes("UTF-8"));
        byte[] bytes = decodeURL(s);
        //  你好！赵尚志！
        System.out.println(new String(bytes, "UTF-8"));
        //
        System.out.println(s);

    }


}
