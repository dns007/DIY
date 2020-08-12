import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author dns007
 * @version 1.0
 * @date 2019/8/12 8:41
 */
public class MyThreadPool {

    /** 利用阻塞队列实现生产者-消费者模式 */
    BlockingQueue<Runnable> workQueue;

    /** 工作线程 */
    MyThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        for (int i = 0; i < poolSize; i++) {
            WorkThread workThread = new WorkThread();
            workThread.start();
        }
    }

    void execute(Runnable command) {
        // 放入任务，如果没有空间，则阻塞等待
        try {
            workQueue.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class WorkThread extends Thread {
        @Override
        public void run() {
            // 循环取任务并执行
            while (true) {
                Runnable task = null;
                // 获取阻塞队列的第一个任务，并删除
                // 如果没有元素，则会阻塞等待
                try {
                    task = workQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //打印当前线程
                System.out.print(this);
                task.run();
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(5);
        //初始化线程池
        MyThreadPool pool = new MyThreadPool(2, workQueue);
        for (int i = 0; i < 10; i++) {
            int num = i;
            //参数是是一个实现了Runnable接口的类的对象  是以匿名内部类的方式创建， 匿名内部类仅限于只实例化一次的内部类，优点就是简洁
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("任务 " + num + " 执行");
                }
            });
        }
    }

}

//
// 概述  一个线程池核心构成  一个阻塞队列 多个线程
// 一个任务队列作为生产者    一个线程列表作为消费者 列表中每个线程消费队列的任务，没有则阻塞


//        ThreadPoolExecutor pools = new ThreadPoolExecutor(5,);

//
//        new Thread (new Runnable(){
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();