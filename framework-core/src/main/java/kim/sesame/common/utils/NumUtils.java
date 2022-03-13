package kim.sesame.common.utils;

import java.text.NumberFormat;
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


    /**
     * 计算百分比
     * @param count 分子
     * @param sum 分母,总数
     * @param num 保留几位小数
     * @return string
     */
    public static String rate(long count, long sum, int num) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(num);
        String result = numberFormat.format((float) count / (float) sum * 100);
        return result;
    }
}
