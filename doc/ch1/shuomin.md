> 之前是没有这一章的,后来我把架构上传到maven中央仓库后出现了一些问题,所以还是记录一下,避免大家遇到坑
1. maven的父子项目有这个问题,父项目里的插件一定会在子项目中执行
2. 上传构建到 maven,又必须要用到 gpg 插件
- 所以如果不是 install 源码到本地仓库的,那么肯定会遇到pgp找不到系统文件,这个错误
- [maven仓库地址 : kim.sesame](http://search.maven.org/#search%7Cga%7C1%7Ckim.sesame)

#### 下面说一下使用方式
1. 如果是 install 源码,那怎么用都不会有问题,也建议使用这种方式 (**推荐下面的方式一**)
2. 如果直接用maven里的依赖,有两种方式
- 方式一
```
    <parent>
        <groupId>kim.sesame.framework</groupId>
        <artifactId>sesame-parent</artifactId>
        <version>---last.version----</version>
    </parent>
```
- 方式二
```
 <dependency>
    <groupId>kim.sesame.framework</groupId>
    <artifactId>sesame-parent</artifactId>
    <version>---last.version----</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```
3. 使用方式一 : 会继承 父模块里能被继承的所有元素,也会继承 pgp 插件, 所以用这种方式需要安装 pgp 命令
- pgp 官网下载地址 [https://www.gnupg.org/download/](https://www.gnupg.org/download/)
- 然后在项目里覆盖这个插件,让其跳过
```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-gpg-plugin</artifactId>
    <configuration>
        <skip>true</skip>
    </configuration>
</plugin>
```
4. 使用方式二 : 就不需要安装 pgp 工具, 但是,有很多常量都不会被继承过来,需要自己去定义,可查看源码,复制过来

