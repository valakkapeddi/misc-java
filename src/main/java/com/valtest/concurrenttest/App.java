package com.valtest.concurrenttest;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
         ScheduledExecutorService jobsService = new ScheduledThreadPoolExecutor(2);
         Queue<MyEntity> concurrentQueue = new ConcurrentLinkedQueue<MyEntity>();
         
         ThrottledPublisher publisherOne = new ThrottledPublisher("Publisher1", 1000, 2, concurrentQueue, jobsService);
         ThrottledPublisher publisherTwo = new ThrottledPublisher("Publisher2", 1000, 2, concurrentQueue, jobsService);
         ThrottledPublisher publisherThree = new ThrottledPublisher("Publisher3", 10000, 1, concurrentQueue, jobsService);
       
         
         for(int a =0; a < 100; a++ ) {
             
             while(true) {
                MyEntity value = concurrentQueue.poll();
                if(value != null) {
                    System.out.println("Spillover: " + value);
                } else {
                    break;
                }
            }
             
             publisherOne.publish(new MyEntity("1", "Hello", a));
             publisherTwo.publish(new MyEntity("2", "Hello", a));
             
             Thread.sleep(500);
             if(a % 2 == 0) {
                 publisherOne.publish(new MyEntity("1", "Hello", a * 10));
             }
             
             if(a % 3 == 0) {
                 publisherTwo.publish(new MyEntity("2", "Hello", a * 10));
             }
             
             if(a % 5 == 0) {
                 publisherThree.publish(new MyEntity("3", "Hello", a));
             }
             
             if(a % 6 == 0 ) {
                 publisherOne.publish(new MyEntity("1", "Hello", a * 10 + 1));
                 publisherTwo.publish(new MyEntity("2", "Hello", a * 10 + 1));
             }
             
          
             
             Thread.sleep(500);
         }
         
         
         
         /*
         publisherOne.publish(new MyEntity("1", "Hello", 1));
         publisherTwo.publish(new MyEntity("2", "Hello", 1));
         
         publisherOne.publish(new MyEntity("1", "Hello", 2));
         publisherTwo.publish(new MyEntity("2", "Hello", 2));
         
         Thread.sleep(200);
         
         publisherOne.publish(new MyEntity("1", "Hello", 3));
         publisherTwo.publish(new MyEntity("2", "Hello", 3));
         
         Thread.sleep(400);
         publisherOne.publish(new MyEntity("1", "Hello", 4));
         publisherTwo.publish(new MyEntity("2", "Hello", 4));
         
         publisherOne.publish(new MyEntity("1", "Hello", 5));
         publisherTwo.publish(new MyEntity("2", "Hello", 5));
       
         
        System.out.println( "Hello World!" );
        
        Thread.sleep(15000);
        
        while(true) {
            MyEntity value = concurrentQueue.poll();
            if(value != null) {
                System.out.println(value);
            } else {
                break;
            }
        }
         */
        System.out.println( "Set up system.  Sleeping until done." );
        
        
        System.out.println( "Done" );
    }
}
