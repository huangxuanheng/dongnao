package com.dongnao.concurrent.period6;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
// 缓存示例
   这是ReentrantReadWriteLock注释中给出的一个示例
   用于构建一个缓存，该缓存在读取并使用值的时候，不允许修改缓存值
   目前还没找到适用场景，有同学有适用场景的，可以推荐给老师
 */

public class Demo8_CacheData {

    public static void main(String args[]){
        System.out.println(TeacherInfoCache.get("Kody"));;
    }

}


class TeacherInfoCache {
    static volatile boolean cacheValid;
    static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    static Object get(String dataKey) {
        Object data = null;

        //读数据，加读锁
        rwl.readLock().lock();
        try {
            if (cacheValid){
                data = Redis.data.get(dataKey);
            }else{
                //data= DataBase.queryUserInfo();       //可能存在缓存雪崩的场景
                rwl.readLock().unlock();

                //加写锁之后，并不会马上获取到所，会等到所有的读锁释放
                rwl.writeLock().lock();
                try {
                    if (!cacheValid){
                        data = DataBase.queryUserInfo();
                        Redis.data.put(dataKey,data);

                        cacheValid = true;
                    }
                    //获取读锁，进行锁降级
                    rwl.readLock().lock();

                }finally {
                    rwl.writeLock().unlock();
                }

                //useData()....
            }

            return data;
        }finally {
            rwl.readLock().unlock();
        }



    }


}


class DataBase{
    static String queryUserInfo(){
        System.out.println("查询数据库。。。");
        return "name:Kody,age:40,gender:true,";
    }
}

class Redis{
    static Map<String, Object> data = new HashMap<>();
}



