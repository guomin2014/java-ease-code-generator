spring:
  application:
    name: ${AppName}
  profiles:
    active: "dev"
  config:
    import: 
      - classpath:application-${spring.application.name}.yml
      - classpath:application-${spring.application.name}-${spring.profiles.active}.yml
info:
  name: ${spring.application.name}
  desc: "@project.description@"
  version: "@project.version@"
  ip: ${spring.cloud.client.ip-address}:${server.port}