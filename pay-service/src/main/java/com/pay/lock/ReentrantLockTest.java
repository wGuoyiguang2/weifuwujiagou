package com.pay.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author guoyiguang
 *
 *  重入锁的概念： 这个锁允许重入，抢到锁后，执行方法的时候又去调用 另一个 加锁的代码，这个时候不需要再次抢占锁，只是增加充入次数
 *               一般来说，锁都是重入的
 * @description $
 * @date 2021/12/26$
 */
public class ReentrantLockTest {

    private static int count = 0 ;

    static Lock lock = new ReentrantLock();




    public static void incremnet(){

        // 获取锁（是互斥锁）
        lock.lock();
        try {
            Thread.sleep(4);
            count++;
            // 重入测试: decremnet() 方法里 用的也是同一个锁对象，假如不是重入锁，Thead A 需要重新持有锁（重新抢占到锁） 才能 执行  decremnet（），此时呢 代码还没有 unlock
            //          导致死锁

            // 重入的概念：不需要重新抢占锁，如果自己持有锁，就不去抢占了，大部分的都是重入锁，非重入的锁基本没有见过
            decremnet();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 手动释放锁
            lock.unlock();
        }

    }

    public static void decremnet(){

        // 获取锁（是互斥锁）
        lock.lock();
        try {
            Thread.sleep(4);
            count--;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 手动释放锁
            lock.unlock();
        }

    }



    public static void reentrantLockTest(){

        for(int i = 0 ;i < 1000 ; i++){
            new Thread(ReentrantLockTest::incremnet).start();
        }


        // 主线程休眠，等待上面执行结果结束
        try {
            Thread.sleep(9000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(count);
        System.out.println(count);


    }

    // 读读不互斥，读写互斥，写写互斥，通过互斥保证数据的准确性

    public static void main(String[] args) {

        // 重入锁测试
        reentrantLockTest();

    }


}
