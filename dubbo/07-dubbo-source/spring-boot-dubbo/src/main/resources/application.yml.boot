
spring:
  main: 
    allow-bean-definition-overriding: true

dubbo:
  application:
    name: service-app1
  registry:
    address: zookeeper://127.0.0.1:2181
  protocol:
    name: dubbo
    port: 20880
  scan: 
    base-packages: edu.dongnao.study.dubbo.provider