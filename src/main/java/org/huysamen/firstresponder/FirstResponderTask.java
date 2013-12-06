package org.huysamen.firstresponder;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class FirstResponderTask<T> {

    private final Callable<? extends T> task;
    private final BlockingQueue<FirstResponderResult> resultQueue;

    public FirstResponderTask(Callable<? extends T> task, final BlockingQueue<FirstResponderResult> resultQueue) {
        this.task = task;
        this.resultQueue = resultQueue;
    }

    protected void runTask(final FirstResponderManager manager) {
        final String uuid = UUID.randomUUID().toString();
        final Thread runner = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    resultQueue.put(new FirstResponderResult<T>(uuid, task.call()));
                } catch (final Exception e) {
                    /* Do nothing, simply cancel the thread execution */
                }
            }
        });

        manager.addTaskThread(uuid, runner);
        runner.start();
    }
}
