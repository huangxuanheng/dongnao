1、特殊分页
对于一个大表而言，mysql超过几十万行。分页就会有问题，此时先查询主键分页结果，再关联到表中的主要信息，会快很多。
Select * from big_table e
Inner join (select id from big_table limit 4100000, 10) a on a.id = e.id

2、join表时的特别处理
使用组合索引，小表的变形sql处理等，
不要使用子查询：这是错误的示例：select *, (select name from user where id = trade.uid) from trade where create_time > '2018-05-04'
改为：select t.*, u.name from trade t inner join user u on t.uid = u.id and t.create_time > '2018-05-04'
在更大的表时，改为：
select t.* from trade t inner join user u on t.uid = u.id 
inner join(select id from trade where create_time > '2018-05-04') as x on x.id = t.id

3、删除大表记录，可考虑线程池处理
while(true){

 　　//每次只做1000条
　　 “delete from logs where log_date <= ’2012-11-01’ limit 1000”;
 　　if(mysql_affected_rows == 0){

　　 　　//删除完成，退出！
　　 　　break；
　　}

        //每次暂停一段时间，释放表让其他进程/线程访问。
        Thread.sleep(5000L)
}
