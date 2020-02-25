# memcached单机到集群完整搭建过程

## 安装memcached

参考安装手册

```
yum install libevent‐devel
wget https://memcached.org/latest
mv latest memcached‐1.5.12.tar.gz
tar ‐zxf memcached‐1.5.12.tar.gz
cd memcached‐1.5.12
./configure ‐‐prefix=/usr/local/memcached
make && sudo make install
./memcached -m 64 -p 11211 -u root -vvvv
‐p <num> 监听的TCP端口(默认: 11211)
‐U <num> 监听的UDP端口(默认: 11211, 0表示不监听)
‐l <ip_addr> 监听的IP地址。（默认：INADDR_ANY，所有地址）
‐d 作为守护进程来运行
‐u <username> 设定进程所属用户（仅root用户可以使用）
‐m <num> 所有slab class可用内存的上限（默认：64MB）
‐v 提示信息（在事件循环中打印错误/警告信息。）
‐vv 详细信息（还打印客户端命令/响应）
-vvv 超级详细信息
```

### 配置

`memcached -h`或`man memcached`获取最新文档

第一次设置memcached的，你要注意`-m`，`-d`和`-v`

如果是通过软件包管理系统安装的memcached，/etc/sysconfig/memcached配置脚本文件。

如果是自己编译构建的memcached，源码文件中的'scripts /'目录包含几个init脚本示例。

检查运行是配置

```
$ echo "stats settings" | nc localhost 11211
```

### 启动

```
./memcached -m 64 -p 11211 -u root -vvvv
# 其他更多参数可以通过下面的命令查看
./memcached -h
```

### 连接

telnet连接上11211端口就可以作为客户端给服务端发送指令了。

```
# telnet memcached_server_ip port例如：
telnet localhost 11211
```



## Memcahed集群搭建

### twemproxy（nutcracker）概述

twemproxy（nutcracker） 是 Twitter开源的轻量级 memcached / redis 代理服务器，本质就是一个集群管理工具，主要用来弥补 Redis和 Memcached对集群管理的不足，其完成的最大功劳就是通过在后端减少同缓存服务器的连接数从而增加吞吐量。

### 环境准备(伪集群/真集群都一样)

准备三个节点：

```
    节点 | 端口 | 名字
    ‐‐‐|‐‐‐ |‐‐‐
    127.0.0.1 | 11220 | node1
    127.0.0.1 | 11221 | node2
    127.0.0.1 | 22122 | node3
```

备注：这里我们是用的同一台机器，可以根据自己的情况配置相应 ip 和 端口

### 部署twemproxy

#### 安装依赖

```shell
yum install autoconf
yum install automake
yum install libtool
```

#### 安装twemproxy代理

```shell
wget https://github.com/twitter/twemproxy/archive/master.zip
unzip master.zip
cd twemproxy
autoreconf -fvi
./configure --enable-debug=full
make
src/nutcracker -h
```

#### 查看 twemproxy帮助

```
src/nutcracker -h
```

#### 准备 twemproxy配置文件

vim /usr/local/twemproxy/conf/nutcracker.yml

```yaml
‐ 修改配置文件
memcached:
- 如果指定了ip地址，则只会允许对应的IP地址连接，要允许外网地址则使用0.0.0.0代替127.0.0.1
  listen: 127.0.0.1:22124
  hash: fnv1a_64
  distribution: ketama
  timeout: 100
  auto_eject_hosts: true
  server_retry_timeout: 2000
  server_failure_limit: 3
  servers:
   - 127.0.0.1:11220:1
   - 127.0.0.1:11221:1
   - 127.0.0.1:11222:1
```

检查yml语法

```shell
/usr/local/twemproxy/src/nutcracker -t
```

#### 启动twemproxy

后台启动加参数-d

```shell
/usr/local/twemproxy/src/nutcracker -d
```

#### 连接使用

测试twemproxy

```shell
telnet localhost 22124
Trying ::1...
telnet: connect to address ::1: Connection refused
Trying 127.0.0.1...
Connected to localhost.
Escape character is '^]'.
set h 0 0 5
12345
STORED
get h
VALUE h 0 5
12345
END
```

分别去各个memcached实例查看刚刚写入的数据，发现存在11222实例上。

#### 故障模拟

查看进程端口

```shell
ps -ef|grep memcached
wesley    9981     1  0 7月22 ?       00:00:22 ./memcached -m 64 -p 11211 -u root -vvv
wesley   29530     1  0 19:09 ?        00:00:00 memcached/memcached -p 11220 -d
wesley   29541     1  0 19:09 ?        00:00:00 memcached/memcached -p 11221 -d
wesley   29557     1  0 19:09 ?        00:00:00 memcached/memcached -p 11222 -d
wesley   29720 21039  0 19:18 pts/3    00:00:00 grep --color=auto memcached
```

kill掉11222实例

```shell
kill 29557
```

插入新的Key，正常，查询key h，已经连接不上了。

```shell
telnet localhost 22124
Trying ::1...
telnet: connect to address ::1: Connection refused
Trying 127.0.0.1...
Connected to localhost.
Escape character is '^]'.
set key 0 0 6
123456
STORED
set h 0 0 3
123
SERVER_ERROR Connection refused
```

重新启动11222实例

```shell
telnet localhost 22124
Trying ::1...
telnet: connect to address ::1: Connection refused
Trying 127.0.0.1...
Connected to localhost.
Escape character is '^]'.
set key 0 0 6
123456
STORED
set h 0 0 3
123
SERVER_ERROR Connection refused
set h 0 0 3
123
STORED
```

**总结：上面这个实验过程可以看出，一台 memcached实例挂掉后，twemproxy 能自动移除之；而恢复后，twemproxy 能够自动识别并重新加入到 memcached 组中重新使用**



可以选择的key值的hash算法： one_at_a_time、md5、crc16、crc32 、crc32a、fnv1_64、fnv1a_64、fnv1_32、fnv1a_32、hsieh、murmur、jenkins ，如果没选择，默认是fnv1a_64。