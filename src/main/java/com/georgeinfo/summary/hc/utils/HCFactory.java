/*
 * A George software product.
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved..
 */
package com.georgeinfo.summary.hc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录受理action预处理节点
 *
 * @author George <Georgeinfo@163.com>
 */
public class HCFactory {

    private static final Logger LOG = LoggerFactory.getLogger(HCFactory.class);

    private static SSLContext getSSLContext() throws Exception {
//        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());  
        KeyStore ks = KeyStore.getInstance("PKCS12");
        InputStream inputStream = new FileInputStream(new File("证书文件的本地路径")); //如：/tmp/apiclient_cert.p12 

        try {
            ks.load(inputStream, "证书密码".toCharArray());//设置证书密码
        } catch (CertificateException | NoSuchAlgorithmException e) {
            LOG.error("## Error when load cert file.", e);
            throw new RuntimeException("## Error when load cert file.", e);
        } finally {
            inputStream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext context = org.apache.http.ssl.SSLContexts.custom()
                .loadKeyMaterial(ks, "证书密码".toCharArray())
                .build();

        return context;
    }

    /**
     * 使用本地证书文件，构建SSL http client对象
     */
    public static HttpClient getHttpsClientByCert() throws Exception {
        HttpClientBuilder builder = HttpClients.custom().setSSLContext(getSSLContext());
        CloseableHttpClient httpClient = builder.build();
        return httpClient;
    }

    /**
     * 创建可访问https的http client对象<br>
     * 一般正常的做法是getHttpsClientByCert()的方式，加载本地证书文件，构建SSL http client对象，
     * 下面这个方法访问https网址，通常会返回超时。
     */
    public static HttpClient getHttpsClient() {
        HttpClient httpClient = null;
        SSLContext context;
        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

            }}, new SecureRandom());

            HostnameVerifier verifier = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(context, verifier);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return httpClient;
    }

    /**
     * 创建普通http client对象
     */
    public static HttpClient getHttpClient() {
        HttpClient client = HttpClientBuilder.create().build();
        return client;
    }

}
