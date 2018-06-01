##### spring boot 自己的一些属性我就不介绍了,这里主要介绍我整合的架构里的属性配置
- 配置这些属性的类都符合这个 *Properties 格式
- web : WebProperties.java
- mybaits : DruidProperties.java
- cache : CacheProperties.java
- 下面以 web 未例
```
@Data
@Component
@ConfigurationProperties(prefix = "sesame.framework.web")
public class WebProperties implements InitializingBean {

    private String userHome = System.getProperty("user.home");
    private int sesaionTime = 30;
    private int userCacheTime = 60;// 单位 : 分钟
    private int defaultPageSize = 10;

    private boolean urlrewriteEnabled = false; // 是否启动 java 伪静态
    private String urlrewriteSuffix = "*.shtml"; // java伪静态的后缀名
    private String pageReplace = "~";

    /**
     * 项目路径,如果采用zuul 后,这个要显示改变
     */
    private String basePath = "";

    /**
     * 外部 javascript,css 等框架的地址
     */
    private String resource = "http://www.sesame.kim/zy";

    /**
     * 文件,图片映射路径,
     * 使用场景 : 当数据库保存的是相对路径时
     */
    private String fileMapping = "";

    private boolean interceptorUser = false;
    private boolean interceptorLogin = false;
    private boolean interceptorAuth = false;

    @Override
    public void afterPropertiesSet() throws Exception {
        GPage.DEFAULT_PAGE_SIZE = this.defaultPageSize;
    }
}
```
1. 修改session的过期时间
```
sesame.framework.web.sesaion-time=0
```
2. 开启java伪静态
```
sesame.framework.web.urlrewrite-enabled=true
```
```
比如说
localhost/xxx/xx.do?id=1&name=aaa
可以写成
localhost/xxx/xx/id_1/name_aaa.shtml
```