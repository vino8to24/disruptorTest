package com.jiang;

import com.jiang.common.Common;
import com.jiang.request.RequestEvent;
import com.jiang.request.RequestEventFactory;
import com.jiang.request.RequestEventHandler;
import com.jiang.request.RequestEventProducer;
import com.jiang.result.ResultEvent;
import com.jiang.result.ResultEventFactory;
import com.jiang.result.ResultEventHandler;
import com.jiang.result.ResultEventProducer;
import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        System.out.println("======start Main========");

        WaitStrategy strategy = new YieldingWaitStrategy();

        ResultEventFactory resultEventFactory= new ResultEventFactory();
        ResultEventHandler resultEventHandler = new ResultEventHandler();
        RingBuffer<ResultEvent> resultRingBuffer = RingBuffer.createSingleProducer(resultEventFactory, Common.BUFFER_SIZE, strategy);
        ResultEventProducer resultEventProducer = new ResultEventProducer(resultRingBuffer);

        RequestEventFactory requestEventFactory = new RequestEventFactory();
        RequestEventHandler requestEventHandler = new RequestEventHandler(resultEventProducer);
        RingBuffer<RequestEvent> requestRingBuffer = RingBuffer.createMultiProducer(requestEventFactory, Common.BUFFER_SIZE, strategy);
        RequestEventProducer requestEventProducer = new RequestEventProducer(requestRingBuffer);

        BatchEventProcessor<RequestEvent> requestProcessor = new BatchEventProcessor<>(requestRingBuffer, requestRingBuffer.newBarrier(),requestEventHandler);
        BatchEventProcessor<ResultEvent> resultProcessor = new BatchEventProcessor<>(resultRingBuffer, resultRingBuffer.newBarrier(),resultEventHandler);

        requestRingBuffer.addGatingSequences(requestProcessor.getSequence());
        resultRingBuffer.addGatingSequences(resultProcessor.getSequence());

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for(int i=0; i<10; ++i) {
            executorService.execute(new MyRunnable(requestEventProducer));
        }
        requestEventProducer.onData(1);


        Thread t1 = new Thread(requestProcessor);
        Thread t2 = new Thread(resultProcessor);
        t1.start();
        t2.start();
        executorService.shutdown();
    }
}

class MyRunnable implements Runnable {
    private RequestEventProducer producer;

    public MyRunnable(RequestEventProducer producer) {
        this.producer = producer;
    }

    @Override
    public void run() {
//        producer.onData(1);
        for(int i=0; i< Common.ITERATION; ++i) {
            final int seq = i;
            producer.onData(seq);
            System.out.println(seq);
        }
    }
}