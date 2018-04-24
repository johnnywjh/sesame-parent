### 盘点一下 spring-boot从1.x升级到2.x 后踩过的坑
1. content-path 的写法变了
```
# 1.x的写法
#server.context-path=/web 
# 2.x的写法
server.servlet.context-path=/web
```
2. Redis客户端驱动现在由Jedis变为了Lettuce。 但仍然支持Jedis，那么exclude掉io.lettuce:lettuce-core
```
     <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>io.lettuce</groupId>
                    <artifactId>lettuce-core</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
        </dependency>
```

3. 大量的Servlet专属的server.* properties被移到了server.servlet下,例如
```
1.x 我之前的代码
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;

2.x 的代码
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;

```

4. 拦截器的修改
- 之前用的是 WebMvcConfigurerAdapter,然后发现这个类被废弃了,是应为java8 支持接口中写方法的实现,然后直接实现WebMvcConfigurer 接口,
- 另外如果配置了拦截器 就会拦截 public,static 文件里的静态资源,分析了下原因 ,跟踪源码查看原因，是因为spring boot 2.x依赖的spring 5.x版本，相对于spring boot 1.5.x依赖的spring 4.3.x版本而言，针对资源的拦截器初始化时有区别，具体源码在WebMvcConfigurationSupport中，spring 4.3.x源码如下：
```
/**
 * Return a handler mapping ordered at Integer.MAX_VALUE-1 with mapped
 * resource handlers. To configure resource handling, override
 * {@link #addResourceHandlers}.
 */
@Bean
public HandlerMapping resourceHandlerMapping() {
    ResourceHandlerRegistry registry = new ResourceHandlerRegistry(this.applicationContext,
				this.servletContext, mvcContentNegotiationManager());
    addResourceHandlers(registry);

    AbstractHandlerMapping handlerMapping = registry.getHandlerMapping();
    if (handlerMapping != null) {
        handlerMapping.setPathMatcher(mvcPathMatcher());
        handlerMapping.setUrlPathHelper(mvcUrlPathHelper());
        // 此处固定添加了一个Interceptor
        handlerMapping.setInterceptors(new ResourceUrlProviderExposingInterceptor(mvcResourceUrlProvider()));
        handlerMapping.setCorsConfigurations(getCorsConfigurations());
		}
    else {
        handlerMapping = new EmptyHandlerMapping();
    }
    return handlerMapping;
}
```
而spring 5.x的源码如下：
```
/**
 * Return a handler mapping ordered at Integer.MAX_VALUE-1 with mapped
 * resource handlers. To configure resource handling, override
 * {@link #addResourceHandlers}.
 */
@Bean
public HandlerMapping resourceHandlerMapping() {
    Assert.state(this.applicationContext != null, "No ApplicationContext set");
    Assert.state(this.servletContext != null, "No ServletContext set");

    ResourceHandlerRegistry registry = new ResourceHandlerRegistry(this.applicationContext,
				this.servletContext, mvcContentNegotiationManager(), mvcUrlPathHelper());
    addResourceHandlers(registry);

    AbstractHandlerMapping handlerMapping = registry.getHandlerMapping();
    if (handlerMapping != null) {
        handlerMapping.setPathMatcher(mvcPathMatcher());
        handlerMapping.setUrlPathHelper(mvcUrlPathHelper());
        // 此处是将所有的HandlerInterceptor都添加了（包含自定义的HandlerInterceptor）
        handlerMapping.setInterceptors(getInterceptors());
        handlerMapping.setCorsConfigurations(getCorsConfigurations());
    }
    else {
        handlerMapping = new EmptyHandlerMapping();
    }
    return handlerMapping;
}

/**
 * Provide access to the shared handler interceptors used to configure
 * {@link HandlerMapping} instances with. This method cannot be overridden,
 * use {@link #addInterceptors(InterceptorRegistry)} instead.
 */
protected final Object[] getInterceptors() {
    if (this.interceptors == null) {
        InterceptorRegistry registry = new InterceptorRegistry();
        // 此处传入新new的registry对象，在配置类当中设置自定义的HandlerInterceptor后即可获取到
        addInterceptors(registry);
        registry.addInterceptor(new ConversionServiceExposingInterceptor(mvcConversionService()));
        registry.addInterceptor(new ResourceUrlProviderExposingInterceptor(mvcResourceUrlProvider()));
        this.interceptors = registry.getInterceptors();
    }
    return this.interceptors.toArray();
}
```
从源码当中可以看出，使用spring 5.x时，静态资源也会执行自定义的拦截器.我找了一些排除静态资源的方法,但是没有找到,然后发现,当访问静态资源和controller里的方法,拦截方法里注入的 Object handler的类型不一样,然后就在每个拦截器的方法里加上了这样一段代码. (如果谁有更好的方法记得给我留意哈)
![输入图片说明](https://gitee.com/uploads/images/2018/0424/134301_e92c2af4_1599674.png "屏幕截图.png")
