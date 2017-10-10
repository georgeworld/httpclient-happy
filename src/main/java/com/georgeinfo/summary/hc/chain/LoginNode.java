/*
 * A George software product.
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved..
 */
package com.georgeinfo.summary.hc.chain;

import com.georgeinfo.summary.hc.utils.HCFactory;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 登录受理action预处理节点
 *
 * @author George <Georgeinfo@163.com>
 */
public class LoginNode extends BaseNode {

    @Override
    public RequestNodeContext process(RequestNodeContext context) {
        //1、创建Cookie Store
        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
        cookie.setDomain("www.discuz.net");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);

        //2、创建HttpClient对象
        HttpClient client = HCFactory.getHttpClient();
        context.setClient(client);

        //3、从数据库里查询出通道方的用户配置
        String accountChn = "你的论坛用户名，请先在www.discuz.net论坛注册用户";
        String passwordChn = "你的论坛密码";

        //4、创建显示登录页post对象
        try {
            //添加向登录受理页发送的请求参数
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", accountChn));
            params.add(new BasicNameValuePair("password", passwordChn));
            params.add(new BasicNameValuePair("cookietime", "2592000"));
            params.add(new BasicNameValuePair("quickforward", "yes"));
            params.add(new BasicNameValuePair("handlekey", "ls"));

            HttpEntity requestEntity = EntityBuilder.create().setParameters(params).build();

            HttpPost loginActionPost = new HttpPost(context.getLoginActionUrl());
            loginActionPost.setEntity(requestEntity);

            HttpResponse response = client.execute(loginActionPost);
            HttpEntity responseEntity = response.getEntity();
            String responseOfLoginActionPage = EntityUtils.toString(responseEntity, Consts.UTF_8);

            //一般登录成功后，会是一个跳转页，所以登录成功后不会有任何响应内容返回
            logger.debug("### Response from login action:" + responseOfLoginActionPage);
        } catch (Exception ex) {
            logger.error("## Exception when post data to website.", ex);
            return context.setSuccess(false).setMessage("向登录受理方发送数据时出现异常");
        }

        if (this.getNext() != null) {
            return this.getNext().process(context);
        } else {
            return context;
        }
    }

}
