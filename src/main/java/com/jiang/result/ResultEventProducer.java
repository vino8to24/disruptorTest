package com.jiang.result;

import com.lmax.disruptor.RingBuffer;

public class ResultEventProducer {
    RingBuffer<ResultEvent> ringBuffer;

    public ResultEventProducer(RingBuffer<ResultEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ResultEvent event) {
        long sequecne = ringBuffer.next();
        try {
            ResultEvent e = ringBuffer.get(sequecne);
            e.setSeq(event.getSeq());
        } finally {
            ringBuffer.publish(sequecne);
        }
    }
}
