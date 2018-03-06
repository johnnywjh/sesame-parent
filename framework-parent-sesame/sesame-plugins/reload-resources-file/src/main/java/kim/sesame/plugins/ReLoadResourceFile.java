package kim.sesame.plugins;


import kim.sesame.framework.FileUtil;
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

}
