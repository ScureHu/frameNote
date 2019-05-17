/**
 * 懒汉式(线程安全)
 * 因为这种方式在getInstance()方法上加了同步锁，所以在多线程情况下会造成线程阻塞，
 * 把大量的线程锁在外面，只有一个线程执行完毕才会执行下一个线程。
 */
public class Singleton2 {
    private static Singleton2 instance;
    private Singleton2(){};

    private static synchronized Singleton2 getInstance(){
        if(instance == null){
            instance = new Singleton2();
        }
        return instance;
    }
}
