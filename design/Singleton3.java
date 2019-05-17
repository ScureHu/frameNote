/**
 * 懒汉式双重校验锁（DCL）
 *
 * 注意此处使用的关键字 volatile，
 * 被volatile修饰的变量的值，将不会被本地线程缓存，
 * 所有对该变量的读写都是直接操作共享内存，从而确保多个线程能正确的处理该变量。
 * 第一次是为了不必要的同步，第二次是在singleton等于null的情况下才创建实例
 */
public class Singleton3 {
    private volatile static Singleton3 instance;

    private Singleton3(){};

    private static Singleton3 getInstance(){
        if(instance == null){
            synchronized (Singleton3.class){
                if(instance == null){
                    instance = new Singleton3();
                }
            }
        }
        return instance;
    }
}
