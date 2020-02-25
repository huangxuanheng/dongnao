# ElasticSearch Centos7 安装

## 准备工作

### 开放端口

关闭防火墙

```shell
systemctl stop firewalld
systemctl disable firewalld
```

或者开放对应的端口号，比如ElasticSearch的9300、9200，Kibana的5601

```shell
sudo firewall-cmd --zone=public --add-port=9300/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9200/tcp --permanent
sudo firewall-cmd --zone=public --add-port=5601/tcp --permanent
sudo firewall-cmd --reload
```



### 新建用户 

```shell
# 添加用户备用
useradd elastic
# 设置密码
passwd elastic
```



### 创建安装目录

```shell
mkdir /usr/local/elastic
```



## 下载安装包

下载安装包 elasticsearch、kibana、IK Analysis，这里下载的是7.5.1版本，大家选择自己需要的版本

```shell
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.5.1-linux-x86_64.tar.gz
wget https://artifacts.elastic.co/downloads/kibana/kibana-7.5.1-linux-x86_64.tar.gz
wget https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.5.1/elasticsearch-analysis-ik-7.5.1.zip
```



解压安装包到安装目录

```shell
tar -zxvf elasticsearch-xxxx.tar.gz -C /usr/local/elastic

tar -zxvf kibana-xxxx.tar.gz -C /usr/local/elastic
```



修改安装目录的所有者

```shell
chown -R elastic:elastic /usr/local/elastic
```



## 预先解决可能会遇到的问题

### 启动问题

**问题一**：max file descriptors [4096] for elasticsearch process likely too low, increase to at least [65536]
**问题二**：max number of threads [1024] for user [lish] likely too low, increase to at least [2048]

需要服务器增大文件、线程等的限制数

**解决：修改切换到root用户修改配置limits.conf 添加下面两行**
**命令:vi /etc/security/limits.conf**

```properties
*	hard	nofile	65536
*	soft    nofile  65536
*	hard 	nproc	4096
*	soft	nproc	4096
```

**问题三**：max virtual memory areas vm.max_map_count [65530] likely too low, increase to at least [262144]
解决：切换到root用户修改配置sysctl.conf
**vi /etc/sysctl.conf** 
添加下面配置：

```properties
vm.max_map_count=655360
```

并执行命令：

```shell
sysctl -p
```



### 调整内存

配置elasticsearch的运行堆大小，默认是1g，如果是学习用可调小，如果是生成用可根据资源情况调大。**注意：最大、最小堆大小需相同**

```shell
vi /usr/local/elastic/elasticsearch-xxx/config/jvm.options
```

```properties
################################################################
## IMPORTANT: JVM heap size
################################################################
##
## You should always set the min and max JVM heap
## size to the same value. For example, to set
## the heap to 4 GB, set:
##
## -Xms4g
## -Xmx4g
##
## See https://www.elastic.co/guide/en/elasticsearch/reference/current/heap-size.html
## for more information
##
################################################################

# Xms represents the initial size of total heap space
# Xmx represents the maximum size of total heap space

-Xms256m
-Xmx256m

```



## 启动elasticsearch

### 配置调整

```
vi /usr/local/elastic/elasticsearch-xxx/config/jvm.options
```

设置3个点：

​	node.name: node-1					 # 1 设置节点名称

​	network.host: 0.0.0.0				# 2 设置网络地址绑定

​	cluster.initial_master_nodes: ["node-1"]		# 3 设置初始主节点

```yaml
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
#       Before you set out to tweak and tune the configuration, make sure you
#       understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please consult the documentation for further information on configuration options:
# https://www.elastic.co/guide/en/elasticsearch/reference/index.html
#
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
#
#cluster.name: my-application
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
#
node.name: node-1					 # 1 设置节点名称
#
# Add custom attributes to the node:
#
#node.attr.rack: r1
#
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
#path.data: /path/to/data
#
# Path to log files:
#
#path.logs: /path/to/logs
#
# ----------------------------------- Memory -----------------------------------
#
# Lock the memory on startup:
#
#bootstrap.memory_lock: true
#
# Make sure that the heap size is set to about half the memory available
# on the system and that the owner of the process is allowed to use this
# limit.
#
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network -----------------------------------
#
# Set the bind address to a specific IP (IPv4 or IPv6):
#
network.host: 0.0.0.0				# 2 设置网络地址绑定
#
# Set a custom port for HTTP:
#
#http.port: 9200
#
# For more information, consult the network module documentation.
#
# --------------------------------- Discovery ----------------------------------
#
# Pass an initial list of hosts to perform discovery when this node is started:
# The default list of hosts is ["127.0.0.1", "[::1]"]
#
#discovery.seed_hosts: ["host1", "host2"]
#
# Bootstrap the cluster using an initial set of master-eligible nodes:
#
cluster.initial_master_nodes: ["node-1"]		# 3 设置初始主节点
#
# For more information, consult the discovery and cluster formation module documentation.
#
# ---------------------------------- Gateway -----------------------------------
#
# Block initial recovery after a full cluster restart until N nodes are started:
#
#gateway.recover_after_nodes: 3
#
# For more information, consult the gateway module documentation.
#
# ---------------------------------- Various -----------------------------------
#
# Require explicit names when deleting indices:
#
#action.destructive_requires_name: true

```



### 中文分词器

```shell
# 将前面下载的IK分词器，加入ElasticSearch插件目录
mv elasticsearch-analysis-ik-xxx.zip /usr/local/elastic/elasticsearch-xxxx/plugins
cd /usr/local/elastic/elasticsearch-xxxx/plugins
uzip elasticsearch-analysis-ik-xxx.zip
```



### 启动ElasticSearch

以elastic用户身份启动elasticsearch

```shell
su elastic

/usr/local/elastic/elasticsearch-xxx/bin/elasticsearch
```

如要后台运行 加 -d

```shell
/usr/local/elastic/elasticsearch-xxx/bin/elasticsearch -d
```

访问：

curl http://localhost:9200

浏览器访问：http://192.168.1.168:9200

看到以下响应内容：

```json
{
"name" : "node-1",
"cluster_name" : "elasticsearch",
"cluster_uuid" : "oARu8gMlTA-9Y332TxPVGQ",
"version" : {
"number" : "7.1.1",
"build_flavor" : "default",
"build_type" : "tar",
"build_hash" : "7a013de",
"build_date" : "2019-05-23T14:04:00.380842Z",
"build_snapshot" : false,
"lucene_version" : "8.0.0",
"minimum_wire_compatibility_version" : "6.8.0",
"minimum_index_compatibility_version" : "6.0.0-beta1"
},
"tagline" : "You Know, for Search"
}
```



## 启动Kibana

### 配置kibana

```shell
vi /usr/local/elastic/kibana-xxx/config/kibana.yml
```

配置kibana地址绑定

**server.host: 0.0.0.0			  # 1 设置网络地址绑定**

如果kibana要访问的elasticsearch 和它不在一台机器上，则需要配置elasticsearch 的访问地址：

**elasticsearch.hosts: ["http://localhost:9200"]** 

如果开启了安全认证，则需要配置访问elasticsearch的用户密码：

**elasticsearch.username: "user"**
**elasticsearch.password: "pass"**



```yaml
# Kibana is served by a back end server. This setting specifies the port to use.
#server.port: 5601

# Specifies the address to which the Kibana server will bind. IP addresses and host names are both valid values.
# The default is 'localhost', which usually means remote machines will not be able to connect.
# To allow connections from remote users, set this parameter to a non-loopback address.
#server.host: "localhost"
server.host: 0.0.0.0			  # 1 设置网络地址绑定

# Enables you to specify a path to mount Kibana at if you are running behind a proxy.
# Use the `server.rewriteBasePath` setting to tell Kibana if it should remove the basePath
# from requests it receives, and to prevent a deprecation warning at startup.
# This setting cannot end in a slash.
#server.basePath: ""

# Specifies whether Kibana should rewrite requests that are prefixed with
# `server.basePath` or require that they are rewritten by your reverse proxy.
# This setting was effectively always `false` before Kibana 6.3 and will
# default to `true` starting in Kibana 7.0.
#server.rewriteBasePath: false

# The maximum payload size in bytes for incoming server requests.
#server.maxPayloadBytes: 1048576

# The Kibana server's name.  This is used for display purposes.
#server.name: "your-hostname"

# The URLs of the Elasticsearch instances to use for all your queries.
#elasticsearch.hosts: ["http://localhost:9200"]    # 设置elasticsearch的访问地址，默认本机

# When this setting's value is true Kibana uses the hostname specified in the server.host
# setting. When the value of this setting is false, Kibana uses the hostname of the host
# that connects to this Kibana instance.
#elasticsearch.preserveHost: true

# Kibana uses an index in Elasticsearch to store saved searches, visualizations and
# dashboards. Kibana creates a new index if the index doesn't already exist.
#kibana.index: ".kibana"

# The default application to load.
#kibana.defaultAppId: "home"

# If your Elasticsearch is protected with basic authentication, these settings provide
# the username and password that the Kibana server uses to perform maintenance on the Kibana
# index at startup. Your Kibana users still need to authenticate with Elasticsearch, which
# is proxied through the Kibana server.
#elasticsearch.username: "user"
#elasticsearch.password: "pass"

# Enables SSL and paths to the PEM-format SSL certificate and SSL key files, respectively.
# These settings enable SSL for outgoing requests from the Kibana server to the browser.
#server.ssl.enabled: false
#server.ssl.certificate: /path/to/your/server.crt
#server.ssl.key: /path/to/your/server.key

# Optional settings that provide the paths to the PEM-format SSL certificate and key files.
# These files validate that your Elasticsearch backend uses the same key files.
#elasticsearch.ssl.certificate: /path/to/your/client.crt
#elasticsearch.ssl.key: /path/to/your/client.key

# Optional setting that enables you to specify a path to the PEM file for the certificate
# authority for your Elasticsearch instance.
#elasticsearch.ssl.certificateAuthorities: [ "/path/to/your/CA.pem" ]

# To disregard the validity of SSL certificates, change this setting's value to 'none'.
#elasticsearch.ssl.verificationMode: full

# Time in milliseconds to wait for Elasticsearch to respond to pings. Defaults to the value of
# the elasticsearch.requestTimeout setting.
#elasticsearch.pingTimeout: 1500

# Time in milliseconds to wait for responses from the back end or Elasticsearch. This value
# must be a positive integer.
#elasticsearch.requestTimeout: 30000

# List of Kibana client-side headers to send to Elasticsearch. To send *no* client-side
# headers, set this value to [] (an empty list).
#elasticsearch.requestHeadersWhitelist: [ authorization ]

# Header names and values that are sent to Elasticsearch. Any custom headers cannot be overwritten
# by client-side headers, regardless of the elasticsearch.requestHeadersWhitelist configuration.
#elasticsearch.customHeaders: {}

# Time in milliseconds for Elasticsearch to wait for responses from shards. Set to 0 to disable.
#elasticsearch.shardTimeout: 30000

# Time in milliseconds to wait for Elasticsearch at Kibana startup before retrying.
#elasticsearch.startupTimeout: 5000

# Logs queries sent to Elasticsearch. Requires logging.verbose set to true.
#elasticsearch.logQueries: false

# Specifies the path where Kibana creates the process ID file.
#pid.file: /var/run/kibana.pid

# Enables you specify a file where Kibana stores log output.
#logging.dest: stdout

# Set the value of this setting to true to suppress all logging output.
#logging.silent: false

# Set the value of this setting to true to suppress all logging output other than error messages.
#logging.quiet: false

# Set the value of this setting to true to log all events, including system usage information
# and all requests.
#logging.verbose: false

# Set the interval in milliseconds to sample system and process performance
# metrics. Minimum is 100ms. Defaults to 5000.
#ops.interval: 5000

# Specifies locale to be used for all localizable strings, dates and number formats.
#i18n.locale: "en"
```



### 启动并使用Kibana

以elastic用户启动 kibana

```
/usr/local/elastic/kibana-xxx/bin/kibana
```

浏览器访问：http://192.168.1.168:5601





