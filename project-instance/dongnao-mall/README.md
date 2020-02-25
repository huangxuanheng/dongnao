## 电商项目
> 基于SOA架构的大型开源分布式电商B2C购物商城，前后端开发分离，前台基于Vue全家桶开发。<br>
> 后台完成所有下单支付流程，后台主要使用技术有Dubbo/SSM/Elasticsearch/Redis/MySQL/ActiveMQ/Shiro/Zookeeper等技术

----------

### 动脑学院公开课源码

- [Tony老师公开课源码](http://code.dongnaoedu.com/2048209527/public-lession) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[找Tony老师聊聊人生](http://wpa.qq.com/msgrd?v=3&uin=2048209527&site=qq&menu=yes "Tony QQ")

- [Mike老师公开课源码](http://code.dongnaoedu.com/3266399810/public-lessions)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[找笑容甜美的Mike老师](http://wpa.qq.com/msgrd?v=3&uin=3266399810&site=qq&menu=yes "Mike QQ")

- [Allen老师公开课源码](http://code.dongnaoedu.com/allen/public-lessons)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[找Allen老师聊聊](http://wpa.qq.com/msgrd?v=3&uin=3266399810&site=qq&menu=yes "Mike QQ")

----------

### 前台页面为基于Vue [mall-front](http://code.dongnaoedu.com/allen/mall-front) 项目仓库查看

使用技术:

* 后台
	* Spring Boot 2.x 
	* Maven构建项目
	* Jenkins持续集成
	* Dubbo RPC框架
	* Apollo统一配置中心
	* Spring + Spring MVC + MyBatis SSM框架
	* druid数据库连接池
	* MySQL 关系数据库和 Redis nosql
	* FastDFS分布式文件服务器
	* Elasticsearch基于Lucene分布式搜索引擎
	* Swagger2生成 RESTful Apis文档
	* Nginx负载均衡、keepalived实现高可用
	* tomcat8 Servlet 容器
	* Spring Scheduled 任务调度
	* RabbitMQ消息中间件
	* Shiro权限管理
	* Zookeeper分布式应用程序协调服务
	* Docker容器
	* TCC分布式事务
	* Mycat数据库分库分表中间件
	* 行为验证码
	* Snowflake分布式id生成算法
	* Mybatis Generator 代码生成器
* 前台
	* [H-ui](http://www.h-ui.net/)、[FlatLab](https://themeforest.net/item/flatlab-bootstrap-3-responsive-admin-template/5902687/) 静态页面支持
    * [Ztree](http://www.treejs.cn/v3/main.php#_zTreeInfo)：jQuery树插件
    * [DataTables](http://www.datatables.club/)：jQuery表格插件
    * [Layer](http://layer.layui.com/)：web弹层组件
    * [Distpicker](https://github.com/fengyuanchen/distpicker)：中国省市区地址三级联动插件
    * [KindEditor](https://github.com/kindsoft/kindeditor)：富文本编辑器
    * [WebUploader](http://fex.baidu.com/webuploader/getting-started.html)：百度文件上传插件
    * [HighCharts](http://www.hcharts.cn/)：图表库
    * [不蒜子](http://busuanzi.ibruce.info/)：极简网页计数器
	* [技术栈]：Vue2 + Vuex + Vue Router + Element UI + ES6 + webpack + axios + Node.js


## spring boot & nodejs

| 名称            |IP               |
|:---------------:|:---------------:|
| mall-front      | localhost:9999  |
| mall-manager-web| localhost:8888  |
| mall-front-web  | localhost:7777  |
| mall-manager    | localhost:8080  |
| mall-content    | localhost:8081  |
| mall-search     | localhost:8082  |
| mall-sso        | localhost:8083  |
| mall-xxl-job    | localhost:8084  |
| mall-pay        | localhost:5555  |


## Dubbo服务地址

| 服务名称         |Dubbo服务端口     |rest服务端口|
|:---------------:|:---------------:|:---------------:|
| mall-manager    | localhost:20880 |          |
| mall-content    | localhost:20881 |          |
| mall-search     | localhost:20882 |          |
| mall-sso        | localhost:20883 |          |


## 项目架构及功能模块图

![](http://code.dongnaoedu.com/allen/dongnao-mall/raw/master/images/jiagou.png)

![](http://code.dongnaoedu.com/allen/dongnao-mall/raw/master/images/front-web.png)

![](http://code.dongnaoedu.com/allen/dongnao-mall/raw/master/images/manger-web.png)


## 运行截图
![](http://code.dongnaoedu.com/allen/dongnao-mall/raw/master/images/%E5%B1%95%E7%A4%BA1.png)
![](http://code.dongnaoedu.com/allen/dongnao-mall/raw/master/images/%E5%B1%95%E7%A4%BA2.png)
![](http://code.dongnaoedu.com/allen/dongnao-mall/raw/master/images/%E5%B1%95%E7%A4%BA3.png)
![](http://code.dongnaoedu.com/allen/dongnao-mall/raw/master/images/%E5%B1%95%E7%A4%BA4.png)


## Dubbox Admin 服务使用情况
![](http://code.dongnaoedu.com/allen/dongnao-mall/raw/master/images/dubbo1.png)
![](http://code.dongnaoedu.com/allen/dongnao-mall/raw/master/images/dubbo2.png)


## 任务管理器
![](http://code.dongnaoedu.com/allen/dongnao-mall/raw/master/images/%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86%E5%99%A8.png)

> 启动了 2 个 Tomcat 容器 ＋ 4 个 Dubbo service 服务 内存使用情况，算上虚拟机中安装的环境预计单机需要16G内存运行整个项目环境


## 本地开发运行部署
- 下载zip直接解压或安装git后执行克隆命令 `git clone http://code.dongnaoedu.com/allen/dongnao-mall.git`
- 安装各中间件并启动：ZooKeeper、Redis、ActiveMQ、Elasticsearch
- 修改各配置文件相应依赖IP配置(默认本地127.0.0.1)，XPay邮箱配置在 `manager-service与sso-service` 中
- Maven安装和在IDEA中配置，nexus3 配置
- 使用ide导入源码，第一次导入后等待下载jar包
- MySQL数据库新建数据库，运行sql文件，注意在有 `application.yml` 的模块中修改你的数据库连接配置
- 先在父项目中执行`mvn install`，以后对子项目的修改可以单独在子模块执行
- 项目需运行除 `mall-parent` `mall-common` 以外其它所有6个服务，都可以使用spring boot的启动方式启动项目
- 后端管理系统默认端口8888 http://localhost:8888 管理员账密admin|123456
- 前端项目接口默认端口7777 前台页面请启动基于Vue的 [mall-front](http://code.dongnaoedu.com/allen/mall-front.git) 项目，并修改其接口配置


## 常见问题

1. 编译失败

	先在dongnao-mall/pom.xml中执行 mvn install <br>
	编译不成功一般是缺少jar包 麻烦配置Nexus 然后更新整个项目去下载jar包 
	在继续编译 如还失败 **请查看本地maven仓库jar是否真正下载下来**
	
2. 编译成功启动失败

	请确保你先启动了zookeeper 并且配置对了zookeeper地址 需要连接数据的请配置好数据密码
	service服务有启动顺序 看看你需要启动的服务依赖那些服务 
	
3. 启动不了

	90%是你的jar问题 

