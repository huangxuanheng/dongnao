
spring:
  main: 
    allow-bean-definition-overriding: true

server.port: 9000

dubbo:
  application:
    name: consumer-service-app1
  registry:
    address: zookeeper://127.0.0.1:2181
  consumer: 
    timeout: 3000
  scan: 
    base-packages: edu.dongnao.study.dubbo.consumer
