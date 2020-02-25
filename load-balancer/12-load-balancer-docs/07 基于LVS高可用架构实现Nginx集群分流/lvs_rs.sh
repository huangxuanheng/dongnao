#!/bin/sh
#虚拟ip，根据需求修改
VIP=192.168.101.100
if [ -n "$2" ] ; then
    VIP="$2"
fi
#绑定到回环设备
VCARD="lo:0"
if [ -n "$3" ] ; then
    VCARD="$3"
fi

. /etc/rc.d/init.d/functions
case $1 in
        start)
            echo "$VCARD port starting"
            # 为了相应lvs调度器转发过来的包,需在本地lo接口上绑定vip
            ifconfig $VCARD $VIP broadcast $VIP netmask 255.255.255.255 up
            # 限制arp请求
            echo "1" > /proc/sys/net/ipv4/conf/lo/arp_ignore
            echo "2" > /proc/sys/net/ipv4/conf/lo/arp_announce
            echo "1" > /proc/sys/net/ipv4/conf/all/arp_ignore
            echo "2" > /proc/sys/net/ipv4/conf/all/arp_announce
            ifconfig
            ;;
        stop)
            if [ -n "$2" ] ; then
                VCARD="$2"
            fi
            echo "$VCARD port closing"
            ifconfig $VCARD down
            echo "0" > /proc/sys/net/ipv4/conf/lo/arp_ignore
            echo "0" > /proc/sys/net/ipv4/conf/lo/arp_announce
            echo "0" > /proc/sys/net/ipv4/conf/all/arp_ignore
            echo "0" > /proc/sys/net/ipv4/conf/all/arp_announce
            ifconfig
            ;;
        *)
            echo "Usage: $0 {start ¦ stop}"
            echo "Usage: $0 start vip loopback"
            echo "Usage: $0 stop loopback"
            exit 1
esac