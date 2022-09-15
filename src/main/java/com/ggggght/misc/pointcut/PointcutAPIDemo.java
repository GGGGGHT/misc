package com.ggggght.misc.pointcut;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class PointcutAPIDemo {
    public static void main(String[] args) {
        EchoServicePointCut servicePointCut = new EchoServicePointCut("echo", EchoService.class);
        DefaultPointcutAdvisor advisor =
            new DefaultPointcutAdvisor(servicePointCut, new EchoServiceMethodInterceptor());
        DefaultEchoService echoService = new DefaultEchoService();
        ProxyFactory proxyFactory = new ProxyFactory(echoService);

        proxyFactory.addAdvisor(advisor);
        EchoService service = (EchoService) proxyFactory.getProxy();
        System.out.println(service.echo("msg"));
    }
}
