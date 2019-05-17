/**
 * 静态内部类单例设计模式
 * jvm在如下情况给我们提供了同步控制
 *
 * 在static {...}区块中初始化的数据
 * 访问final字段时
 *
 * 因为在JVM进行类加载的时候他会保证数据是同步的，我们可以这样实现：采用内部类，在这个内部类里面去创建对象实例。\
 * 这样的话，只要应用中不使用内部类 JVM 就不会去加载这个单例类，也就不会创建单例对象，从而实现「懒汉式」的延迟加载和线程安全。
 *
 * 第一次加载Singleton类时并不会初始化sInstance，只有第一次调用getInstance方法时虚拟机加载SingletonHolder 并初始化sInstance ，
 * 这样不仅能确保线程安全也能保证Singleton类的唯一性，所以推荐使用静态内部类单例模式。
 */
public class Singleton4 {

    private Singleton4(){};

    public static Singleton4 getInstance(){
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder{
        private static final Singleton4 sInstance = new Singleton4();
    }
}
