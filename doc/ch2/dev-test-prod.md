#### 1 项目里有三个环境属性 分别是 dev(默认),test,prod
![输入图片说明](https://gitee.com/uploads/images/2017/1224/174203_e51e42d6_1599674.png "屏幕截图.png")

#### 2 在resources/config 目录里有4个文件
![输入图片说明](https://gitee.com/uploads/images/2017/1224/174312_a0b297c1_1599674.png "屏幕截图.png")

#### 3 在 pom.xml 里有这样一段配置
![输入图片说明](https://gitee.com/uploads/images/2017/1224/174453_56b30eee_1599674.png "屏幕截图.png")

#### 4 默认环境是dev, 当项目编译完成后, 打开 target 目录 ,会发现
![输入图片说明](https://gitee.com/uploads/images/2017/1224/174617_3493763b_1599674.png "屏幕截图.png")
#### 是的,就只有两个文件,当然第3步也可以不要,但是这里会出现4个文件,但是并不会影响指定的环境配置失效, 因为在 application.properties 里有这个 'spring.profiles.active=@profileActive@' 编译后会变成 'spring.profiles.active=dev'

#### 结束