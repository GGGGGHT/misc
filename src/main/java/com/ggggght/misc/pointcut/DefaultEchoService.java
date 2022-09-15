package com.ggggght.misc.pointcut;

public class DefaultEchoService implements EchoService {
    @Override public String echo(String msg) {
        return "echo " + msg;
    }
}
