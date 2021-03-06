package kim.sesame.plugins;


import kim.sesame.framework.uitls.ZipCompressor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

/**
 * 压缩代码
 **/
@Mojo(name = "zip")
public class ZipCode extends AbstractMojo {

    @Parameter
    private List<String> paths;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("执行压缩插件..................");
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
            if (path != null && path.length() > 0) {
                File file = new File(path);
                if (file.exists()) {
                    String fileName = path.substring(path.lastIndexOf("/") + 1);
                    // 文件压缩
                    ZipCompressor zc = new ZipCompressor(path + ".zip");
                    zc.compress(path);
                    getLog().info("文件压缩成功 : " + path + ".zip");
                } else {
                    getLog().warn("path 路径不存在, : " + path);
                }
            } else {
                getLog().warn("path 路径不能为空");
            }
        }

        getLog().info("执行压缩插件结束..................");
    }

}
