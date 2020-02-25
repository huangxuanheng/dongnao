#!/bin/bash
# description:  MySQL buckup shell script
# author:       Daniel
# web site:     http://home.ustc.edu.cn/~danewang/blog/

st=$(date +%s)
USER="lovecoins"
PASSWORD="111"#数据库密码
DATABASE="root" #数据库用户
MAIL="xxx@qq.com"
BACKUP_DIR=/data/backup/mysql_xxx_db_bak/ #备份文件存储路径
LOGFILE=/data/backup/mysql_xxx_db_bak/backup.log #日志文件路径
 #用日期格式作为文件名
DATE=`date +%Y%m%d-%H%M`
DUMPFILE=$DATE.sql
ARCHIVE=$DATE.sql.tar.gz
OPTIONS="-u$USER $DATABASE -p$PASSWORD"

#判断备份文件存储目录是否存在，否则创建该目录
if [ ! -d $BACKUP_DIR ]
then
        mkdir -p "$BACKUP_DIR"
fi

#开始备份之前，将备份信息头写入日记文件
echo "    ">> $LOGFILE
echo "--------------------" >> $LOGFILE
echo "BACKUP DATE:" $(date +"%y-%m-%d %H:%M:%S") >> $LOGFILE
echo "-------------------" >> $LOGFILE

#切换至备份目录
cd $BACKUP_DIR
mysqldump $OPTIONS > $DUMPFILE
#判断数据库备份是否成功
if [[ $? == 0 ]]
then
        tar czvf $ARCHIVE $DUMPFILE >> $LOGFILE 2>&1
    echo "[$ARCHIVE] Backup Successful!" >> $LOGFILE
    rm -f $DUMPFILE #删除原始备份文件,只需保留备份压缩包
else
    echo "Database Backup Fail!" >> $LOGFILE
#备份失败后向管理者发送邮件提醒
# mail -s "database:$DATABASE Daily Backup Fail!" $MAIL
fi
echo "Backup Process Done"
#删除3天以上的备份文件
#Cleaning
find $BACKUP_DIR  -type f -mtime +7 -name "*.tar.gz" -exec rm -f {} \;
