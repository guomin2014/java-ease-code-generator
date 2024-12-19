spring:
  http:
    encoding:
      charset: UTF-8
      enabled: true
      force: true
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 100MB
server:
  tomcat:
    uri-encoding: UTF-8
javaeaseframe:
  interceptor:
    login:
      enable: false
    auth:
      enable: false
    log:
      enable: true
      fetchFromNativeRequestEnable: true
      printRequestUser: true
      printRequestBody: true
      printResponseBody: true
      printPosition: beforeAndAfter
  web:
    cors:
      enable: true
    cacheBody:
      enable: false
  mybatis:
    root-path: ${RootPackage}
    type-aliases-package: ${RootPackage}.**.model
    mapper-locations: classpath*:sqlmap/**/*.xml
  transaction:
    enable: true
    pointcut: 
      - "* ${RootPackage}..*Service.*(..)"
    methods:
      - name: "save*"
        propagation: "REQUIRED"
      - name: "update*"
        propagation: "REQUIRED"
      - name: "remove*"
        propagation: "REQUIRED"
      - name: "*NewTx"
        propagation: "REQUIRES_NEW"
      - name: "*NonTx"
        propagation: "NOT_SUPPORTED"
      - name: "*"
        propagation: "SUPPORTS"
        readOnly: true
  knife4j:
    enable: true
    basic:
      enable: true
      username: admin
      password: 123456
    openapi:
      title: JavaEaseFrame框架-demo项目
      description: "这是应用的描述信息"
      email: guomin@javaeaseframe.com
      concat: guomin
      group:
        group1:
          group-name: 分组名称
          api-rule: package
          api-rule-resources:
            - ${RootPackage}