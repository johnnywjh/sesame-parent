package kim.sesame.framework.web.controller;

/**
 * swagger的一个空实现,和示例写法
 * 知道 dev 环境才开启 : @Profile("test")
 */
public interface ISwagger {
    /*
# 在注册中心里可以直接点击跳转
eureka.instance.prefer-ip-address=true
eureka.instance.instance-id=${spring.cloud.client.ipAddress}:${server.port}
eureka.instance.status-page-url=http://${spring.cloud.client.ipAddress}:${server.port}/${server.context-path}/swagger-ui.html
     */
    /*
 @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sesame")) // 扫描的路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口文档标题")
                .description("描述文字描述文字描述文字描述文字")
                .termsOfServiceUrl("www.baidu.com")
                .contact(contact())
                .version("1.0")
                .build();
    }
    private Contact contact() {
        return new Contact("johnny", "www.sesame.kim", "654499437@qq.com");
    }
    */

    /*
     示例

    @ApiOperation(value = "获取用户列表", notes = "xxxxxxxxxxxxxxxx")
    @GetMapping("/list")
    public Response list() {

        List<User> list = Arrays.asList(
                new User("张三")
                , new User("李四")
        );
        return returnSuccess(list);
    }

    @ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @PostMapping("/postUser")
    public Response postUser(@RequestBody User user) {

        return returnSuccess(user);
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @PostMapping("/deleteUser/{id}")
    public Response getUser(@PathVariable Long id) {
        return returnSuccess(new User(id + ""));
    }

    @ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    })
    @PostMapping("/putUser/{id}")
    public Response putUser(@PathVariable Long id, @RequestBody User user) {
        return returnSuccess();
    }


    */
}
