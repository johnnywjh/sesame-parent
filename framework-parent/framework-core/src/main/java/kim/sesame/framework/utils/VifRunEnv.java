package kim.sesame.framework.utils;

import java.net.URL;

/**
 * 判断运行时的环境
 */
public class VifRunEnv {

    /**
     * 判断运行环境
     * @param clazz 目标类
     * @return true : jar方式运行, false : 源码方式运行
     */
    public static boolean isJar(Class clazz){
        URL url = clazz.getResource("/");
        System.out.println(url.toString());
        if(url.toString().startsWith("jar:")){
            return true;
        }else{
            return false;
        }
    }

}
