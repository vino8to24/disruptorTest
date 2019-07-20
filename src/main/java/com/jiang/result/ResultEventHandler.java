package com.jiang.result;

import com.jiang.common.Common;
import com.lmax.disruptor.EventHandler;

public class ResultEventHandler implements EventHandler<ResultEvent> {
    @Override
    public void onEvent(ResultEvent event, long sequence, boolean endOfBatch) throws Exception {

        System.out.println(sequence);
        if(sequence == Common.ITERATION*10-1) {
            Common.END = System.nanoTime();
            long cost = Common.END - Common.START;
            double average = (double)cost/Common.ITERATION/10/1000;
            System.out.println(String.format("seqquence %d, cost %dns in total, average %.2fus", sequence, cost, average));
        }
    }
}
