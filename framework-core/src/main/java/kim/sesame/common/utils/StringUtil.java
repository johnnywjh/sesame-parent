package kim.sesame.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串
 * Description:
 */
public class StringUtil {

    /**
     * 两个字符串之间的比较
     * @param str str
     * @param str2 str2
     * @return  true/false
     */
    public static boolean equals(String str, String str2) {

        return str.toLowerCase().equals(str2.toLowerCase());
    }

    public static boolean equals(String str, String[] arr) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        boolean flg = false;
        for (String s : arr) {
            flg = flg || equals(str, s);
            if (flg) {
                break;
            }
        }
        return flg;
    }

    /**
     * 评价两个int类型的数字成字符并转成Integer类型
     *
     * @param one one
     * @param two two
     * @return int
     */
    public static Integer joint2Int(int one, int two) {
        return Integer.parseInt(one + "" + two);
    }

    /**
     * 去掉前后引号
     * @param str str
     * @return string
     */
    public static String removeQuotes(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        int length = str.length();
        if (length <= 2) {
            return str;
        }
        if (str.substring(0, 1).equals("\"") && str.substring(length - 1, length).equals("\"")) {
            return str.substring(1, length - 1);
        }
        return str;
    }

    public static String transcoding(String val) {
        try {
            if (isMessyCode(val)) {
                return new String(val.getBytes("ISO8859-1"), "UTF-8");
            }
            return val;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 判断字符是否是中文
     *
     * @param c 字符
     * @return 是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 字符串分割成 list
     *
     * @param strs     待分割的字符串
     * @param splitstr 分隔符
     * @return
     */
//    @Deprecated
//    public static List<String> split(String strs, String splitstr) {
//        return Stream.of(strs.split(splitstr))
//                .map(String::trim).distinct()
//                .filter(StringUtil::isNotEmpty)
//                .collect(Collectors.toList());
//    }

}
