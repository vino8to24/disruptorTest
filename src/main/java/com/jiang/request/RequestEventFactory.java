package com.jiang.request;

import com.lmax.disruptor.EventFactory;

public class RequestEventFactory implements EventFactory<RequestEvent> {

    public RequestEvent newInstance() {

        return new RequestEvent();
    }
}
