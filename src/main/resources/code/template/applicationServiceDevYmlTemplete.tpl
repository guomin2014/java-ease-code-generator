application:
  log:
    level: INFO
    path: /export/logs/www.javaeasecode.com
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${DataSourceUrl}
    username: ${DataSourceUserName}
    password: ${DataSourcePassword}
    druid:
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 60000
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      validation-query: SELECT 1 FROM DUAL
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
      pool-prepared-statements: false
      max-pool-prepared-statement-per-connection-size: 20
      filters: stat,wall
      connection-properties: 'druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000'