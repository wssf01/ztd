package com.bike.ztd.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommonUtils {

    final static String[] LIKE_CHARS = new String[]{"%", "\\", "_"};

    final static String[] LIKE_REPLACE_CHARS = new String[]{"\\%", "\\\\", "\\_"};

    /**
     * 替换字符串
     *
     * @param value
     * @return
     */
    public static String likeReplace(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        return StringUtils.replaceEach(value, LIKE_CHARS, LIKE_REPLACE_CHARS);
    }

    public static String tranString(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    public static Integer tranInteger(Object obj) {
        Integer intObj;
        try {
            intObj = (obj != null && obj.toString().length() > 0) ? Integer.valueOf(obj.toString()) : 0;
        } catch (Exception e) {
            e.printStackTrace();
            intObj = 0;
        }
        return intObj;
    }

    public static Double tranDouble(Object obj) {
        Double doubleObj = null;
        try {
            doubleObj = (obj != null && obj.toString().length() > 0) ? Double.valueOf(obj.toString()) : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doubleObj;
    }

    public static Float tranFloat(Object obj) {
        Float floatObj = null;
        try {
            floatObj = (obj != null && obj.toString().length() > 0) ? Float.valueOf(obj.toString()) : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return floatObj;
    }

    public static Long tranLong(Object obj) {
        Long result = null;
        try {
            result = (obj != null && obj.toString().length() > 0) ? Long.valueOf(obj.toString()) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public static boolean checkParam(Object... objects) {
        if (null == objects) {
            return false;
        }
        for (Object obj : objects) {
            if (null == obj) {
                return false;
            }
            if (obj instanceof String) {
                if (obj.toString().trim().equals("")) {
                    return false;
                }
            } else if (obj instanceof Collection) {
                if (((Collection) obj).isEmpty()) {
                    return false;
                }
            } else if (obj instanceof Map) {
                if (((Map) obj).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Map<String, Object> createMap(String key, Object value) {
        Map<String, Object> map = createMap();
        map.put(key, value);
        return map;
    }

    public static Map<String, Object> createMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    /**
     * 获取时间，单位：毫秒。
     *
     * @param date
     * @param isBig 为空是否取最大值
     * @return
     */
    public static long getTime(Date date, boolean isBig) {
        return date == null ? (isBig ? Long.MAX_VALUE : -1) : date.getTime();
    }

    public static boolean contains(String source, String... strings) {
        if (strings != null && strings.length > 0) {
            for (String str : strings) {
                if (source.contains(str)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean equals(String source, String... strings) {
        if (strings != null && strings.length > 0) {
            for (String str : strings) {
                if (StringUtils.equals(source, str)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static String getUUID() {
        // TODO: 2018/5/2 替换为按时间生成
        String uuid = UUIDUtils.timeBasedStr();
        return uuid.replaceAll("-", "");
    }

    public static String getUUID(String code) {
        // TODO: 2018/5/2 替换为按时间生成
        String uuid = UUIDUtils.timeBasedStr();
        if (code != null) {
            uuid = code + uuid;
        }
        return uuid;
    }

    final static int startIndex = 12, endIndex = 16;

    final static String hideFlag = "****";

    public static String hiddenIdentity(String identity) {
        if (StringUtils.isNotBlank(identity)) {
            if(identity.length() > 18){
                //长度大于18位则尝试解密
                try {
                    CodecUtils.aesDecrypt(identity);
                } catch (Exception e) {
                    e.printStackTrace();
                    //解密失败还是用原来的
                }
            }
            identity = StringUtils.overlay(identity, hideFlag, startIndex, endIndex);
        }
        return identity;
    }

    public static String hiddenCellPhone(String cellPhone) {
        if (StringUtils.isNotBlank(cellPhone)) {
            if(cellPhone.length() > 13){
                //长度大于13位则尝试解密
                try {
                    CodecUtils.aesDecrypt(cellPhone);
                } catch (Exception e) {
                    e.printStackTrace();
                    //解密失败还是用原来的
                }
            }
            cellPhone = StringUtils
                    .overlay(cellPhone, hideFlag, 3, cellPhone.length() - 4);
        }
        return cellPhone;
    }

}
