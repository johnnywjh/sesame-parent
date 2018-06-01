##### 在 resources 里面下新建文件 logback-spring.xml
```
<configuration debug="fasle" scan="true" scanPeriod="30 seconds">
	<property name="FILE_PATTERN" value="%d [%t] %5p %c - %m%n" />
	<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<Pattern>%d{yy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{35} - %msg %n</Pattern>
		</encoder>
	</appender>
	<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">

		<File>${user.home}/sesame_space/logs/sso/current-log.txt</File>

		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">

			<FileNamePattern>${user.home}/sesame_space/logs/sso/log.%d{yyyy-MM-dd}.log</FileNamePattern>

			<maxHistory>90</maxHistory>
		</rollingPolicy>
		<encoder>
			<Pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{35} - %msg %n</Pattern>
		</encoder>
	</appender>
	<root level="info">
		<appender-ref ref="STDOUT" />
		<appender-ref ref="FILE" />
	</root>

</configuration> 
```
> 说明
- 里面有两个 sso , 这个要修改下,改成你的项目名
- user.home 是你的机器当前用户名的地址
- 所以当项目以 jar运行时, 你的日志 是放在user.home 里的.
- 如果在想看到sql的打印, 可以调整局部包的日志等级,例如
```
logging.level.com.sesame=debug
```