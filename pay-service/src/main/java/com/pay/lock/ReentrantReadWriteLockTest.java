package com.pay.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author guoyiguang
 *
 *  重入锁的概念： 这个锁允许重入，抢到锁后，执行方法的时候又去调用 另一个 加锁的代码，这个时候不需要再次抢占锁，只是增加充入次数
 *               一般来说，锁都是重入的
 * @description $
 * @date 2021/12/26$
 */
public class ReentrantReadWriteLockTest {


    private static ReentrantReadWriteLock rwl = new  ReentrantReadWriteLock();

    // 读写锁 start (如何保证hashMap线程安全的另一个思路)
    static Map<String,String> cacheMap = new HashMap<>();

    public static String getValueFromMap(String key){

        ReentrantReadWriteLock.ReadLock readLock = rwl.readLock();
        // 读锁（有其他线程对 cacheMap 进行写入操作的时候，会陷入阻塞？？？原因）
        // Thead A 先 获得读锁，Thead B  就不能获得写锁，对数据进行修改
        // 反过来 Thread B 先获得写锁，Thead A 就不能获取读锁读数据（所以是互斥锁）
        readLock.lock();
        try {
           return cacheMap.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 手动释放锁
            readLock.unlock();
        }
        return "";
    }

    public static String setValueFromMap(String key,String value){
        ReentrantReadWriteLock rwl = new  ReentrantReadWriteLock();
        ReentrantReadWriteLock.WriteLock writeLock = rwl.writeLock();
        // 写锁
        writeLock.lock();
        try {
            return cacheMap.put(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 手动释放锁
            writeLock.unlock();
        }
        return "";
    }


    // 读读不互斥，读写互斥，写写互斥，通过互斥保证数据的准确性

    public static void main(String[] args) {


    }


}
