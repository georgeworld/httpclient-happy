/*
 * A George software product.
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved..
 */
package com.georgeinfo.summary.hc.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 责任链基础节点定义
 * @author George <Georgeinfo@163.com>
 */
public abstract class BaseNode {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private BaseNode next;

    public BaseNode getNext() {
        return next;
    }

    public void setNext(BaseNode next) {
        this.next = next;
    }

    public abstract RequestNodeContext process(RequestNodeContext context);
}
