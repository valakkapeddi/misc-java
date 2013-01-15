/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valtest.concurrenttest;

/**
 *
 * @author Val
 */
public class MyEntity {
    private String publisherName;
    private String message;
    private int sequence;

    public MyEntity(String publisherName, String message, int Sequence) {
        this.publisherName = publisherName;
        this.message = message;
        this.sequence = Sequence;
    }
    
    @Override
    public String toString() {
        return String.format("%s:%s:%d", publisherName, message, sequence);
    }
    
}
