/*
 * A George software product.
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved..
 */
package com.georgeinfo.summary.hc.utils;

import java.util.HashMap;
import org.apache.http.client.HttpClient;

/**
 * Http Client对象单例容器
 *
 * @author George <Georgeinfo@163.com>
 */
public class HttpClientHolder {

    private final HashMap<String, HttpClient> httpClientMap;

    private HttpClientHolder() {
        httpClientMap = new HashMap<String, HttpClient>();
    }

    // 线程安全的惰性加载单例模式 开始
    private static class HttpClientHolderHolder {

        private static final HttpClientHolder INSTANCE = new HttpClientHolder();
    }

    public static HttpClientHolder getInstance() {
        return HttpClientHolderHolder.INSTANCE;
    }
    // 线程安全的惰性加载单例模式 结束

    public void removeHttpClient(String userAccount) {
        httpClientMap.remove(userAccount);
    }

    public void addHttpClient(String userAccount, HttpClient client) {
        httpClientMap.put(userAccount, client);
    }

    public HttpClient getClient(String userAccount) {
        if (userAccount == null || userAccount.trim().isEmpty()) {
            return null;
        } else {
            return httpClientMap.get(userAccount);
        }
    }

}
