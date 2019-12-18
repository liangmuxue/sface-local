package com.ss.sdk.socket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientCache {
    public static ConcurrentMap<String ,NioClient> clientMap=new  ConcurrentHashMap<String,NioClient>();
}