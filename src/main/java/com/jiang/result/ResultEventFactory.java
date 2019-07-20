package com.jiang.result;

import com.lmax.disruptor.EventFactory;

public class ResultEventFactory implements EventFactory<ResultEvent> {
    public ResultEvent newInstance() {
        return new ResultEvent();
    }
}
