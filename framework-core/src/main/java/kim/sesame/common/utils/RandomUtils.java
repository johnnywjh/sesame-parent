package kim.sesame.common.utils;

import java.util.Random;

/**
 * Created by mlick on 2017-02-13.
 */
public class RandomUtils {


    //生成随机数字和字母 默认为32位 并转为大写   c c++ java js html python
    public static String getStringRandom() {
        return getStringRandom(32).toUpperCase();
    }

    //生成随机数字和字母
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


    public static Integer getRandomNum(int num) {
        return new Random().nextInt(num) + 1;
    }


    /**
     * 生成四位数据的随机数
     * @return string
     */
    public static String getFourRandom() {
        return getFourRandom(4);
    }

    public static String getFourRandom(int num) {
        Random random = new Random();
        StringBuilder fourRandom = new StringBuilder(random.nextInt((int) Math.pow(10, num)) + "");
        int randLength = fourRandom.length();
        if (randLength < num) {
            for (int i = 1; i <= num - randLength; i++)
                fourRandom.insert(0, "0");
        }
        return fourRandom.toString();
    }

    /**
     * 仿微信的uuid生成QRCode的uuid
     * @return string
     */
    public static String getQRCodeUuid() {
        return getStringRandom(10) + "==";
    }



    public static void main(String[] args) {
//        for (int i = 0; i < 100; i++) {
////            System.out.println(getFourRandom());
////            System.out.println(getOrderNum());
//            System.out.println(getRandomNum(60));
//        }


        System.out.println(getStringRandom(10));
    }


}
