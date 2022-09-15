package com.ggggght.misc.pointcut;

import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class EchoServiceMethodInterceptor implements MethodInterceptor {
    /**
     * Implement this method to perform extra treatments before and
     * after the invocation. Polite implementations would certainly
     * like to invoke {@link Joinpoint#proceed()}.
     *
     * @param invocation the method invocation joinpoint
     * @return the result of the call to {@link Joinpoint#proceed()};
     * might be intercepted by the interceptor
     * @throws Throwable if the interceptors or the target object
     *                   throws an exception
     */
    @Override public Object invoke(MethodInvocation invocation) throws Throwable {
        var method = invocation.getMethod();
        System.out.println("[Echo]:" + method.getName());
        return invocation.proceed();
    }
}
