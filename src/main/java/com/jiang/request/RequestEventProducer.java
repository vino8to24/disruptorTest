package com.jiang.request;

import com.jiang.common.Common;
import com.lmax.disruptor.RingBuffer;

public class RequestEventProducer {
    private RingBuffer<RequestEvent> ringBuffer;

    public RequestEventProducer(RingBuffer<RequestEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(int seq) {
        long sequence = ringBuffer.next();
        try {
            if(sequence == 0L) {
                Common.START = System.nanoTime();
            }
            RequestEvent event = ringBuffer.get(sequence);
            event.setSeq(seq);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
