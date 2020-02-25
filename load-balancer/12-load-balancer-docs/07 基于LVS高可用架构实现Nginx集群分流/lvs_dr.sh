#!/bin/sh
# 定义虚拟ip
VIP=192.168.120.200 #虚拟 ip根据需求修改
if [ -n "$2" ] ; then
    VIP="$2"
fi
# 定义提供服务的端口
VIP_PORT=80
if [ -n "$3" ] ; then
    VIP_PORT="$3"
fi
# 定义realserver,并已空格分开，根据需求修改
RIPS="192.168.120.103 192.168.120.127"
if [ -n "$4" ] ; then
    RIPS="$4"
fi
#绑定到的虚拟网卡
# 是否可以通过shell脚本拿到本地的虚拟网卡
VCARD="enp0s3:0"
if [ -n "$5" ] ; then
    VCARD="$5"
fi

# 调用init.d脚本的标准库
. /etc/rc.d/init.d/functions
case $1 in
        start)
            echo "Start LVS of DR Mode"
            # 开启ip转发
            echo "1" > /proc/sys/net/ipv4/ip_forward
            # 绑定虚拟ip
            ifconfig $VCARD $VIP broadcast $VIP netmask 255.255.255.255 up
            route add -host $VIP dev $VCARD
            # 清除lvs规则
            ipvsadm -C
            # 添加一条虚拟服务器记录
            # -p指定一定的时间内将相同的客户端分配到同一台后端服务器
            # 用于解决session的问题,测试时或有别的解决方案时建议去掉
            ipvsadm -A -t $VIP:$VIP_PORT -s rr

            # 添加真实服务器记录
            for RIP in $RIPS
            do
                echo $RIP:$VIP_PORT;
                ipvsadm -a -t $VIP:$VIP_PORT -r $RIP:$VIP_PORT -g
            done
            # 设置tcp tcpfin  udp的超时连接值
            # 1 2 1 用作测试轮询效果
            ipvsadm --set 1 2 1
            ipvsadm
            ipvsadm -ln
            ;;

        stop)
            if [ -n "$2" ] ; then
                VCARD="$2"
            fi
            echo "Stop LVS DR"
            ifconfig $VCARD down
            ipvsadm -C
            ;;

        *)
        echo "Usage: $0 {start ¦ stop}"
        echo "Usage: $0 start vip vip_port \"real_server_ips...\" virtual_net_card"
        echo "Usage: $0 stop virtual_net_card"
        exit 1
esac