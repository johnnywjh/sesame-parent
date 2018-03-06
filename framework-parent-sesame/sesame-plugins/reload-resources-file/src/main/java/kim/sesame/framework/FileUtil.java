package kim.sesame.framework;

import java.io.*;

/**
 * 文件操作
 *
 * @author johnny
 * date :  2016年4月1日 上午11:18:22
 * Description:
 */
public class FileUtil {


    /**
     * 删除文件和目录
     *
     * @param file void
     * @author johnny
     * date :  2017年5月8日 下午10:57:16
     */
    public static void clearFiles(File file) {
        if (file.exists()) {
            deleteFile(file);
        }
    }

    /**
     * 删除目录里的所有文件,保存源文件
     *
     * @param file void
     * @author johnny
     * date :  2017年5月11日 下午9:27:46
     */
    public static void cleanDir(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        }
    }

    /**
     * 递归删除文件夹和里面的文件
     *
     * @param file void
     * @author johnny
     * date :  2017年5月11日 下午9:27:08
     */
    private static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        }
        file.delete();
    }

    //复制方法
    public static void copy(String src, String des) {
        try {
            //初始化文件复制
            File file1 = new File(src);
            //把文件里面内容放进数组
            File[] fs = file1.listFiles();
            //初始化文件粘贴
            File file2 = new File(des);
            //判断是否有这个文件有不管没有创建
            if (!file2.exists()) {
                file2.mkdirs();
            }
            //遍历文件及文件夹
            for (File f : fs) {
                if (f.isFile()) {
                    //文件
                    fileCopy(f.getPath(), des + "\\" + f.getName()); //调用文件拷贝的方法
                } else if (f.isDirectory()) {
                    //文件夹
                    copy(f.getPath(), des + "\\" + f.getName());//继续调用复制方法      递归的地方,自己调用自己的方法,就可以复制文件夹的文件夹了
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件复制的具体方法
     */
    private static void fileCopy(String src, String des) throws Exception {
        //io流固定格式
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des));
        int i = -1;//记录获取长度
        byte[] bt = new byte[2014];//缓冲区
        while ((i = bis.read(bt)) != -1) {
            bos.write(bt, 0, i);
        }
        bis.close();
        bos.close();
        //关闭流
    }


}
