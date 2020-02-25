package com.dongnao.concurrent.period6;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// 将hashmap 改造一个并发安全的
// 这是ReentrantReadWriteLock注释中给出的一个示例
public class Demo7_Map {
    private final Map<String, Object> m = new HashMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock r = lock.readLock();
    private final Lock w = lock.writeLock();

    public Object get(String key){
        r.lock();
        try {
            return m.get(key);
        }finally {
            r.unlock();
        }
    }

    public Object[] allKeys(){
        r.lock();
        try {
            return m.keySet().toArray();
        }finally {
            r.unlock();
        }
    }

    public Object put(String key, Object value){
        w.lock();
        try {
            return m.put(key, value);
        }finally {
            w.unlock();
        }
    }

    public void clear(){
        w.lock();
        try {
            m.clear();
        }finally {
            w.unlock();
        }

    }



}