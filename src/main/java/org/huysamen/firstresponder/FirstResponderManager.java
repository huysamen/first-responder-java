package org.huysamen.firstresponder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class FirstResponderManager {

    private Map<String, Thread> taskRunners;

    @SuppressWarnings({"unchecked"})
    public <T> T submitTasks(final Collection<Callable<T>> tasks) {
        final BlockingQueue<FirstResponderResult> resultQueue = new ArrayBlockingQueue<FirstResponderResult>(tasks.size());
        taskRunners = new HashMap<String, Thread>();

        for (final Callable<T> task : tasks) {
            new FirstResponderTask<T>(task, resultQueue).runTask(this);
        }

        while (true) {
            try {
                final FirstResponderResult<T> result = resultQueue.take();

                taskRunners.remove(result.getUuid());

                if (result.getResult() != null) {
                    terminateRemainingTasks();
                    return result.getResult();
                }

                if (taskRunners.isEmpty()) {
                    return null;
                }
            } catch (final InterruptedException e) {
                return null;
            } finally {
                terminateRemainingTasks();
            }
        }
    }

    protected void addTaskThread(final String uuid, final Thread thread) {
        if (taskRunners != null) {
            taskRunners.put(uuid, thread);
        }
    }

    private void terminateRemainingTasks() {
        final Iterator<Map.Entry<String, Thread>> taskIterator = taskRunners.entrySet().iterator();

        while (taskIterator.hasNext()) {
            taskIterator.next().getValue().interrupt();
            taskIterator.remove();
        }
    }
}
