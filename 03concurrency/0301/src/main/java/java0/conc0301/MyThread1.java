package main.java.java0.conc0301;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这
 * 个方法的返回值后，退出主线程
 * 通过callable实现
 */
public class MyThread1 implements Callable {

    public static void main(String[] args) {
        //创建Callable的对象
        MyThread1 ca = new MyThread1();
        FutureTask<Integer> ft = new FutureTask<Integer>(ca);


        Thread thread = new Thread(ft, "Callable");
        thread.start();

        try {
            System.out.println(thread.getName() + "子线程的返回值: " + ft.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer call() throws Exception {
        return sum();
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
