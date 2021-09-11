### 分布式生成序列号
#### 用法
配置
```
# 默认表名, 
kim.serial.table-name=t_bse_serial_number_rule
```

写一个类继承SerialNumberRule
```java

import kim.sesame.framework.serial.define.SerialNumberRule;

/**
 * 序列号配置类,
 * 里面的code要保证系统唯一
 */
public class SysSerialNumber extends SerialNumberRule {


    /**
     * 用户编号
     */
    public static final SerialNumberRule USER_ACCOUNT =new SysSerialNumber
            ("用户账号", "UCODE", false, false, "",
                    false, "", true, "J", true,
                    true, 6, false, "");
    

    private SysSerialNumber(String name, String code, boolean needParams, boolean needTime, String timeFormat, boolean needDelimiter, String delimiter, boolean needLetterPrefix, String letterPrefix, boolean needNumber, boolean fixedNumLen, int numLen, boolean needLetterSuffix, String letterSuffix) {
        super(name, code, needParams, needTime, timeFormat, needDelimiter, delimiter, needLetterPrefix, letterPrefix, needNumber, fixedNumLen, numLen, needLetterSuffix, letterSuffix);
    }
}
```

使用
```java
@Autowired
SerialNumberService serialNumberService;

String code = serialNumberService.generateSerialNumber(SysSerialNumber.USER_ACCOUNT);
```