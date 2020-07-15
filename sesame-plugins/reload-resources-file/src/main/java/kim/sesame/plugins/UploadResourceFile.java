package kim.sesame.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "reloadResourcesFile")
public class UploadResourceFile extends AbstractMojo {

    /**
     * 源码文件夹
     */
    @Parameter
    private List<String> srcFiles;
    /**
     * 要处理的文件后缀. 不设置就不做控制
     */
    @Parameter
    private List<String> suffixs;
    /**
     * 直接更新全部. false:对比文件差异, true:直接更新所有文件
     */
    @Parameter
    private Boolean updateAll = false;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("更新资源文件 开始..................");
        if (srcFiles != null && srcFiles.size() > 0) {

            getLog().info("--------------");
            List<String> zips = new ArrayList<>();
            try {
                for (int i = 0; i < srcFiles.size(); i++) {
                    diffAndCover(new File(srcFiles.get(i)));
                }
            } catch (Exception e) {
                getLog().error(e.getMessage());
            }
            getLog().info("--------------");
        } else {
            getLog().warn("没有要处理的文件夹目录");
        }
        getLog().info("更新资源文件 结束..................");
    }

    /**
     * 比较并覆盖
     */
    public void diffAndCover(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                diffAndCover(files[i]);
            }
        } else {
            String srcPath = file.getAbsolutePath();  // 源代码文件目录
            String targetPath = srcPath.replace("src/main/resources", "target/classes")
                    .replace("src\\main\\resources", "target\\classes");

            boolean isSuffix = true; // 后缀校验是否通过
            if (suffixs != null && suffixs.size() > 0) {
                String suffix = srcPath.substring(srcPath.lastIndexOf("."));
                isSuffix = suffixs.contains(suffix);
            }

            try {
                if (isSuffix) {
                    File targetFile = new File(targetPath);
                    if (updateAll) {
                        FileUtils.copyFile(file, targetFile);
                        getLog().info("文件更新:" + srcPath);
                    } else {
                        if (file.length() != targetFile.length()) {
                            FileUtils.copyFile(file, targetFile);
                            getLog().info("文件更新:" + srcPath);
                        }
                    }
                }
            } catch (Exception e) {
                getLog().error(e.getMessage());
            }

        }
    }

}
