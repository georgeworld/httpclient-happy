/*
 * A George software product.
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved..
 */
package com.georgeinfo.summary.hc.chain;

import org.apache.http.client.HttpClient;

/**
 * 责任链上下文定义类
 *
 * @author George <Georgeinfo@163.com>
 */
public class RequestNodeContext {

    private boolean success;
    private String message;
    private String loginActionUrl;
    private HttpClient client;
    private String requestId;

    public boolean isSuccess() {
        return success;
    }

    public RequestNodeContext setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RequestNodeContext setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getLoginActionUrl() {
        return loginActionUrl;
    }

    public void setLoginActionUrl(String loginActionUrl) {
        this.loginActionUrl = loginActionUrl;
    }

    public HttpClient getClient() {
        return client;
    }

    public void setClient(HttpClient client) {
        this.client = client;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

}
