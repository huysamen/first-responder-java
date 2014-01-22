/*
* FirstResponderManager.java
*
* Copyright (c) 2014, Nicolaas Frederick Huysamen. All rights reserved.
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 3 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA 02110-1301 USA
*/

package org.huysamen.firstresponder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * The FirstResponderManager is responsible for managing a collection of parallel tasks. A manager can be re-used in
 * the event that the previous collection of tasks have finished execution (or a response has been received, in which
 * case remaining tasks would have been terminated.
 *
 * @author Nicolaas Frederick Huysamen
 * @version 1.0.0
 */
public class FirstResponderManager {

    private Map<String, Thread> taskRunners;

    /**
     * Submits a collection of tasks to be executed in parallel.
     *
     * @param tasks The collection of tasks to be executed in parallel.
     * @param <T> The type of the result.
     * @return The result of the first successful response, otherwise <code>null</code>.
     * @since 1.0.0
     */
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
