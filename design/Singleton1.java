/**
 * 懒汉式(非线程安全)
 * 
 * 懒汉模式申明了一个静态对象，在用户第一次调用时初始化，虽然节约了资源，但第一次加载时需要实例化，
 * 反映稍慢一些，而且在多线程不能正常工作。在多线程访问的时候，很可能会造成多次实例化，就不再是单例了。
 * 「懒汉式」与「饿汉式」的最大区别就是将单例的初始化操作，延迟到需要的时候才进行，这样做在某些场合中有很大用处。
 * 比如某个单例用的次数不是很多，但是这个单例提供的功能又非常复杂，而且加载和初始化要消耗大量的资源，这个时候使用「懒汉式」就是非常不错的选择。
 */
public class Singleton1 {

    private static Singleton1 instance;

    private Singleton1(){};

    public static Singleton1 getInstance(){
        if(instance == null){
            instance = new Singleton1();
        }
        return instance;
    }
}
