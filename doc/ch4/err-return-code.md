#### 有三种大的异常
- 业务异常 BusinessException.java
- 交互异常 GeneralException.java
- 系统异常
##### 这里主要讲解一下业务异常,根据特定的业务一般自己定义一个异常,继承BusinessException 就好
```
package com.sesame.student.exception;

import kim.sesame.framework.exception.BusinessException;

public class StudentException extends BusinessException {

    private static final long serialVersionUID = 3827413256427200111L;

    public static final String NOT_NAME = "姓名不能为空";
    public static final String ORDER_NON = "单号不存在";

    public StudentException() {
    }

    public StudentException(String msg) {
        super(msg);
    }

    public StudentException(String code, String msg) {
        super(msg);
        this.errCode = code;
    }

}

```
##### 然后
![输入图片说明](https://gitee.com/uploads/images/2017/1225/105431_644082ee_1599674.png "屏幕截图.png")
结果
![输入图片说明](https://gitee.com/uploads/images/2017/1225/105700_131ed3ba_1599674.png "屏幕截图.png")