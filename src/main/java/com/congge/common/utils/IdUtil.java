package com.congge.common.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class IdUtil {

    private static AtomicInteger ato = new AtomicInteger(1);

    public static int createId(){
        int i = ato.incrementAndGet();
        return i+1;
    }

}
