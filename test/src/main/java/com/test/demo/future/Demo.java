package com.test.demo.future;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.*;

public class Demo {

    private Integer aa = 1;
    public String ss = "sss";

    public static void main(String[] args) throws Exception {

//        ExecutorService executor = Executors.newCachedThreadPool();
//        Task task = new Task();
//        FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
//        executor.submit(futureTask);
//        executor.shutdown();
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
//
//        System.out.println("主线程在执行任务");
//
//        try {
//            System.out.println("task运行结果"+futureTask.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("所有任务执行完毕");


//        synchronized (Demo.class) {
//            System.out.println("1212");
//            aa();
//        }

        ThreadLocal threadLocal = new ThreadLocal();
        threadLocal.set("11");

        threadLocal.remove();

        // 对单向链表 1->2->3, 在不借助其他任何数据结构的情况下反向打印出 321，注意不能修改输入，不能复制输入
        Node node3 = new Node(null,"3");
        Node node2 = new Node(node3,"2");
        Node node1 = new Node(node2, "1");
//        System.out.println(node1.str+", "+ node1.getNext().getStr() + ","+ node1.getNext().getNext().getStr());


//        Node node = test(node1);

        Date date = new Date(1445876601L);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

//        while (true) {
//            if (node == null) {
//                break;
//            }
//
//            if (node.getNext() != null && StringUtils.isNotBlank(node.getStr())) {
//                if (nodeTemp == null) {
//                    nodeTemp = new Node(null, node.getStr());
//                } else {
//                    nodeTemp.setNext(new Node(node, node.getStr()));
//                }
//            }
//            node = node.getNext();
//
//        }

//        System.out.println(node.str+", "+ node.getNext().getStr() + ","+ node.getNext().getNext().getStr());
    }

    public static Node test(Node head) {
        if (head == null) {
            return null;
        }
        Node p0 = null;
        Node p1 = head;
        Node p2 = head.next;
        while (p1 != null) {
            p1.setNext(p0);;

            p0 = p1;
            p1 = p2;
            if (p2 != null) {
                p2 = p2.getNext();
            }
        }
        return p0;
    }

}

class Node {

    Node next;

    String str;

    public Node(Node next, String str) {
        this.next = next;
        this.str = str;
    }

    public Node getNext() {
        return next;
    }

    public String getStr() {
        return str;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setStr(String str) {
        this.str = str;
    }
}



class Task implements Callable<Integer>{
    
    @Override
    public Integer call() throws Exception {
        System.out.println("子线程在进行计算");
        Thread.sleep(3000);
        int sum = 0;
        for(int i=0;i<4;i++) {
            sum += i;
        }
        
        return sum;
    }

}

