/*
* FirstResponderResult.java
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

/**
 * Encapsulation class representing a single result obtained from running a FirstResponderTask.
 *
 * @param <T> The type of the result.
 *
 * @author Nicolaas Frederick Huysamen
 * @version 1.0.0
 */
public class FirstResponderResult<T> {

    private final String uuid;
    private final T result;

    protected FirstResponderResult(final String uuid, final T result) {
        this.uuid = uuid;
        this.result = result;
    }

    /**
     * Returns the UUID assigned to this result (matches the UUID of the task).
     *
     * @return The UUID in a string representation.
     * @since 1.0.0
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Returns the result as calculated by the task.
     *
     * @return The result.
     * @since 1.0.0
     */
    public T getResult() {
        return result;
    }
}
