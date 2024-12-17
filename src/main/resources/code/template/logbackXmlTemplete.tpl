<?xml version="1.0" encoding="utf-8"?>
<configuration scan="true" scanPeriod="60 seconds" debug="false">
    <springProperty scope="context" name="springApplicationName" source="spring.application.name"/>
    <springProperty scope="context" name="serverPort" source="server.port"/>
    <springProperty scope="context" name="logFilePath" source="application.log.path" defaultValue="/export/logs/www.javaeasecode.com" />
    <springProperty scope="context" name="logLevel" source="application.log.level" defaultValue="INFO" />
    <property name="logFilePath" value="${logFilePath:-/export/logs/www.javaeasecode.com}" />
	<property name="logLevel" value="${logLevel:-INFO}" />

    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%thread] [%.50c\(%L\)] - %msg%n</pattern>
        </encoder>
    </appender>
    <appender name="fileInfo" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%thread] [%.50c\(%L\)] - %msg%n</pattern>
        </encoder>
        <file>${logFilePath}/${springApplicationName:-default}/${serverPort:-default}-info.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logFilePath}/${springApplicationName:-default}/${serverPort:-default}-info.log.%d{yyyyMMdd}</fileNamePattern>
        </rollingPolicy>
    </appender>
    <appender name="fileError" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%thread] [%.50c\(%L\)] - %msg%n</pattern>
        </encoder>
        <file>${logFilePath}/${springApplicationName:-default}/${serverPort:-default}-error.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logFilePath}/${springApplicationName:-default}/${serverPort:-default}-error.log.%d{yyyyMMdd}</fileNamePattern>
        </rollingPolicy>
    </appender>
    <root level="${logLevel}">
        <appender-ref ref="console"/>
        <appender-ref ref="fileInfo"/>
        <appender-ref ref="fileError"/>
    </root>
</configuration>