package kim.sesame.framework.utils;

/**
 * 字符串
 *
 * @author johnny
 * date :  2016年2月23日 上午10:43:55
 * Description:
 */
public class StringUtil {

    /**
     * string 非空校验
     */
    public static boolean isNotEmpty(Object obj) {
        if (obj == null || obj.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * string 对象为空的判断
     */
    public static boolean isEmpty(Object obj) {
        return !isNotEmpty(obj);
    }

    /**
     * 两个字符串之间的比较
     *
     * @author johnny
     * date :  2016年9月29日 下午5:12:44
     */
    public static boolean equals(String str, String str2) {

        return str.toLowerCase().equals(str2.toLowerCase());
    }

    public static boolean equals(String str, String[] arr) {
        boolean flg = false;
        for (String s : arr) {
            flg = flg || equals(str, s);
        }

        return flg;
    }

    /**
     * 评价两个int类型的数字成字符并转成Integer类型
     * @param one one
     * @param two two
     * @return int
     */
    public static Integer joint2Int(int one, int two) {
        return Integer.parseInt(one + "" + two);
    }

    public static boolean isBlank(String str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }


}
