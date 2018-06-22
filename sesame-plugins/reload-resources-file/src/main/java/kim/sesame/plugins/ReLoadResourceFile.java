package kim.sesame.plugins;


import kim.sesame.framework.FileUtil;
import kim.sesame.framework.ZipCompressor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "reloadResourcesFile")
public class ReLoadResourceFile extends AbstractMojo {

    /**
     * 源码文件夹
     */
    @Parameter
    private List<String> srcFiles;

    /**
     * 目标文件夹
     */
    @Parameter
    private List<String> targetFiles;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("重新加载资源文件 开始..................");
        if (srcFiles != null && srcFiles.size() > 0) {
            if (targetFiles != null && targetFiles.size() > 0 && targetFiles.size() != srcFiles.size()) {
                getLog().error("srcFiles 和 targetFiles 个数不一样, targetFiles 可不设置,默认按照 src/main/resources 替换");
                return;
            }
            if (targetFiles == null) {
                targetFiles = new ArrayList<String>();
            }
            if (targetFiles.size() == 0) {
                for (String path : srcFiles) {
                    targetFiles.add(path.replace("src/main/resources", "target/classes"));
                }
            }
            // 先删除以前的文件
            for (String str : targetFiles) {
                FileUtil.clearFiles(new File(str));
                getLog().info("删除文件夹 : " + str);
            }
            getLog().info("--------------");
            List<String> zips = new ArrayList<>();
            try {
                for (int i = 0; i < srcFiles.size(); i++) {
                    String srcPath = srcFiles.get(i);// 原文件夹
                    String targetPath = targetFiles.get(i);

                    // 1  压缩文件夹到知道目录
                    String fileName = srcPath.substring(srcPath.lastIndexOf("/") + 1);
                    // 文件压缩
                    targetPath = targetPath + ".zip";
                    ZipCompressor zc = new ZipCompressor(targetPath);
                    zc.compress(srcPath);
                    getLog().info("文件压缩成功 : " + targetPath);

                    // 2 解压文件到当前目录
                    String dir = new File(targetFiles.get(i)).getPath();
                    dir = dir.substring(0, dir.lastIndexOf("\\"));
                    ZipCompressor.unZipFiles(new File(targetPath), dir + "/");
                    getLog().info("解压到 : " + dir);

                    zips.add(targetPath);
                    System.gc();
                }
                System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 3 删除压缩包
            for(String s : zips){
                FileUtil.clearFiles(new File(s));
                getLog().info(s + " , 删除成功");
            }
            getLog().info("--------------");
        } else {
            getLog().warn("没有要处理的文件夹目录");
        }
        getLog().info("重新加载资源文件 结束..................");
    }

    /*
    public void execute() throws MojoExecutionException {
        getLog().info("重新加载资源文件 开始..................");
        if (srcFiles != null && srcFiles.size() > 0) {
            if (targetFiles != null && targetFiles.size() > 0 && targetFiles.size() != srcFiles.size()) {
                getLog().error("srcFiles 和 targetFiles 个数不一样, targetFiles 可不设置,默认按照 src/main/resources 替换");
                return;
            }
            if (targetFiles == null) {
                targetFiles = new ArrayList<String>();
            }
            if (targetFiles.size() == 0) {
                for (String path : srcFiles) {
                    targetFiles.add(path.replace("src/main/resources", "target/classes"));
                }
            }
            // 先删除以前的文件
            for (String str : targetFiles) {
                FileUtil.clearFiles(new File(str));
                getLog().info("删除文件夹 : " + str);
            }
            getLog().info("--------------");
            for (int i = 0; i < srcFiles.size(); i++) {
                FileUtil.copy(srcFiles.get(i), targetFiles.get(i));
                getLog().info("复制文件 : "+srcFiles.get(i)+"\n\t\t\t\t --> "+targetFiles.get(i));
            }
            getLog().info("--------------");
        } else {
            getLog().warn("没有要处理的文件夹目录");
        }
        getLog().info("重新加载资源文件 结束..................");
    }
     */
}
