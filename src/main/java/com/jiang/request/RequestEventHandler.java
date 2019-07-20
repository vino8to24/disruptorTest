package com.jiang.request;

import com.jiang.result.ResultEvent;
import com.jiang.result.ResultEventProducer;
import com.lmax.disruptor.EventHandler;

public class RequestEventHandler implements EventHandler<RequestEvent> {

    private ResultEventProducer producer;

    public RequestEventHandler(ResultEventProducer producer) {
        this.producer = producer;
    }

    @Override
    public void onEvent(RequestEvent event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    public void onEvent(RequestEvent event) {

        long start = System.nanoTime();
        long end = 0L;
        do {
            end = System.nanoTime();
        } while(end-start < 10000);

        ResultEvent resultEvent = new ResultEvent();
        resultEvent.setSeq(event.getSeq()*event.getSeq());
        producer.onData(resultEvent);
    }
}
