/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valtest.concurrenttest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Val
 */
public class ThrottledPublisher implements Runnable {

    public ThrottledPublisher(String publisherName, int interval, int maxWeight, Queue<MyEntity> masterOutputQueue, ScheduledExecutorService registry) {
        this.publisherName = publisherName;
        this.interval = interval;
        this.maxWeight = maxWeight;
        this.masterOutputQueue = masterOutputQueue;
        
        Random blah = new Random(4534654);
        int randomDelay = blah.nextInt(500);
        
        registry.scheduleAtFixedRate(this, randomDelay, interval, TimeUnit.MILLISECONDS);
    }
    
    private String publisherName;
    private Queue<MyEntity> masterOutputQueue;
    private Queue<MyEntity> myQueue = new LinkedList<MyEntity>(); 
    private int interval;
    private int maxWeight;
    private volatile int currentWeight = 0;
    
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS");
    
    public void publish(MyEntity e) {
        if(currentWeight >= maxWeight) {
            synchronized(this) {
                if(currentWeight >= maxWeight) {
                    myQueue.add(e);
                }
            }
        } else {
            publisherInner(e);
        }
        
        currentWeight++;
    }
    
    public void publisherInner(MyEntity e) {
        System.out.println(publisherName + "says " + e + " at " + formatter.format(new Date()));
    }

    public void run() {
        synchronized(this) {
            // 1. Do not dequeue more than the max weight.
            // 2. Deduct current weight for every dequeue operation.
            // 3. If there are no elements in the list, break.
            int counter = 0;
            while(counter < maxWeight) {
                MyEntity entity = myQueue.poll();
                if(entity != null) {
                    masterOutputQueue.add(entity);
                } else {
                    break;
                }                
            }
            currentWeight = 0;
        }
    }

}
