> 虽然上面有提到热部署,但是在用idea开发时,会偶尔出现没有效果的现象,其实直接编写 target 里面的html文件就会直接有效果,然后想到可以用maven的自定义插件来实现这个功能
1. 使用
```
            <plugin>
                <groupId>kim.sesame.framework</groupId>
                <artifactId>reload-resources-file</artifactId>
                <version>${sesame.version}</version>
                <configuration>
                    <srcFiles>
                        <srcFile>${project.basedir}/src/main/resources/static</srcFile>
                        <srcFile>${project.basedir}/src/main/resources/templates</srcFile>
                    </srcFiles>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>reloadResourcesFile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```
##### html js 编写完之后直接点击这里就好
![输入图片说明](https://gitee.com/uploads/images/2018/0308/121056_2e05130f_1599674.png "屏幕截图.png")

2. 说明 : 这里除了设置了一个srcFile, 还有个 targetFile节点,如果不设置,会有默认行为,插件的逻辑代码如下,看过之后应该就知道怎么用了
```
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
```
