/*
 * A George software product.
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved..
 */
package com.georgeinfo.summary.hc.chain;

import java.io.IOException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

/**
 * 登录网站的主处理节点
 *
 * @author George <Georgeinfo@163.com>
 */
public class MessagePageNode extends BaseNode {

    @Override
    public RequestNodeContext process(RequestNodeContext context) {
        HttpClient client = context.getClient();

        try {
            //登录后，立即访问系统消息页面
            HttpGet get = new HttpGet("http://www.discuz.net/home.php?mod=space&do=pm&filter=privatepm");
            HttpResponse responseMsgPage = client.execute(get);
            HttpEntity entityMsgPage = responseMsgPage.getEntity();
            String responseOfMsgPage = EntityUtils.toString(entityMsgPage, Consts.UTF_8);
            context.setSuccess(true).setMessage(responseOfMsgPage);
        } catch (IOException ex) {
            logger.error("### Exception when resolve response of login page.", ex);
            return context.setSuccess(false).setMessage("解析登录页返回结果时，出现异常.");
        }

        if (this.getNext() != null) {
            return this.getNext().process(context);
        } else {
            return context;
        }
    }

}
