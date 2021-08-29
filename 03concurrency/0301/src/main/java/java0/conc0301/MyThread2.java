package main.java.java0.conc0301;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这
 * 个方法的返回值后，退出主线程
 */
public class MyThread2 implements Runnable {

    private int result;

    public static void main(String[] args) {
        MyThread2 myThread2 = new MyThread2();
        Thread thread = new Thread(myThread2, "runnable");
        thread.start();
        try {
            thread.join();
            System.out.println(thread.getName() + "子线程的返回值: " + myThread2.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        result = sum();
    }

    public int getResult(){
        return result;
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
