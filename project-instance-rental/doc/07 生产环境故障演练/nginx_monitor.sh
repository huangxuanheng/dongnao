#!/bin/bash
# shell脚本监控：如果程序的进程存在，则认为没有问题
if [ "$(ps -ef | grep "nginx"| grep -v grep| grep -v keepalived )" == "" ]
  then
    # 输出日志
    echo "nginx ############# app down " >> /var/log/messages
    exit 1
  else
    # 一切正常
	exit 0
fi
