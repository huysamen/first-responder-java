package org.huysamen.firstresponder;

public class FirstResponderResult<T> {

    private final String uuid;
    private final T result;

    public FirstResponderResult(final String uuid, final T result) {
        this.uuid = uuid;
        this.result = result;
    }

    public String getUuid() {
        return uuid;
    }

    public T getResult() {
        return result;
    }
}
