> 有时候我们在项目打包时,想把一些资源文件打包成zip放到 jar里面,每次手动压缩太麻烦,而且文件有时候更新了要重新弄一遍,所以我写了一个maven插件,将其绑定到 生命周期 compiler 上,来实现这个功能
1. 用法
```
        <!--自定义插件, 在编译时压缩资源文件-->
            <plugin>
                <groupId>kim.sesame.framework</groupId>
                <artifactId>zip</artifactId>
                <version>${sesame.version}</version>
                <configuration>
                    <paths>
                        <path>${project.basedir}/target/classes/com/sesame/theme</path>
                    </paths>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>zip</goal>
                        </goals>
                        <phase>compile</phase>
                    </execution>
                </executions>
            </plugin>
```
2. 插件逻辑代码
```
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
```