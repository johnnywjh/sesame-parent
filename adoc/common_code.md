#### 返回给前端Long=>String
```java
// 方式一
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JsonSerialize(using = ToStringSerializer.class)
private Long id;
```

```java
// 方式二
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
private Long id;
```
