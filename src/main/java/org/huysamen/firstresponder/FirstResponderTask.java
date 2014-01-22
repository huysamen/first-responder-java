/*
* FirstResponderTask.java
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

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * The FirstResponderTask encapsulates the callable task that is to be executed in parallel with other tasks.
 *
 * @param <T>
 */
public class FirstResponderTask<T> {

    private final Callable<? extends T> task;
    private final BlockingQueue<FirstResponderResult> resultQueue;

    protected FirstResponderTask(Callable<? extends T> task, final BlockingQueue<FirstResponderResult> resultQueue) {
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
