package kim.sesame.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NumUtils {
    public static String suijishu(int length) {
        StringBuffer sb = new StringBuffer("");
        Random ra = new Random();
        for (int i = 0; i < length; i++) {
            int num = ra.nextInt(10);
            sb.append(num);
        }

        return sb.toString();
    }

    public static String getOrderNum() {

        String format = "yyyyMMddHHmm";

        SimpleDateFormat sf = new SimpleDateFormat(format);

        return sf.format(new Date());

    }
}
