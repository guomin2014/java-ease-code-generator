spring:
  application:
    name: ${AppName}
  profiles:
    active: "dev"
  config:
    import: 
      - classpath:${spring.application.name}.yml
      - classpath:${spring.application.name}-${spring.profiles.active}.yml
info:
  name: ${spring.application.name}
  desc: "@project.description@"
  version: "@project.version@"
  ip: ${spring.cloud.client.ip-address}:${server.port}
application:
  log:
    level: "@profiles.log.level@"
    path: "@profiles.log.path@"