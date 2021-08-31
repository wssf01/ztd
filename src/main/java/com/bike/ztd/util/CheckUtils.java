package com.bike.ztd.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CheckUtils {

    /**
     * 联系号码验证，若为手机号返回true
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        //String regexPhone = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        String regexTellQu = "^([0-9]{3,4}-)?[0-9]{7,8}$";
        String regexTell = "^[0-9]{7,8}$";
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        if (phone.length() == 11) {
            Pattern p = Pattern.compile(regexPhone);
            Matcher m = p.matcher(phone);
            return m.matches();
        } else if (phone.length() == 13 || phone.length() == 12) {
            Pattern p = Pattern.compile(regexTellQu);
            Matcher m = p.matcher(phone);
            return m.matches();
        } else if (phone.length() == 7 || phone.length() == 8) {
            Pattern p = Pattern.compile(regexTell);
            Matcher m = p.matcher(phone);
            return m.matches();
        } else {
            return false;
        }
    }

    private static final String regexPhone = "^\\d{11}$";

    /**
     * 手机号码验证，若为手机号返回true
     *
     * @param phone
     * @return
     */
    public static boolean isMobilePhone(String phone) {
        //String regexPhone = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regexPhone);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }

    /**
     * 固定号码验证，若为手机号返回true
     *
     * @param phone
     * @return
     */
    public static boolean isCellPhone(String phone) {
        String regexTellQu = "^([0-9]{3,4}-)?[0-9]{7,8}$";
        String regexTell = "^[0-9]{7,8}$";
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        if (phone.length() == 13 || phone.length() == 12) {
            Pattern p = Pattern.compile(regexTellQu);
            Matcher m = p.matcher(phone);
            return m.matches();
        } else if (phone.length() == 7 || phone.length() == 8) {
            Pattern p = Pattern.compile(regexTell);
            Matcher m = p.matcher(phone);
            return m.matches();
        } else {
            return false;
        }
    }

    /**
     * 将文件转化为字节数组字符串，并对其进行Base64编码处理
     *
     * @param in
     * @return
     */
    public static String getBase64FromOutputStream(InputStream in) {
        // 将文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取字节数组
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new String(Base64.encodeBase64(data));
    }

}
