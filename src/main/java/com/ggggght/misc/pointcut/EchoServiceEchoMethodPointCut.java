package com.ggggght.misc.pointcut;

import java.lang.reflect.Method;
import java.util.Objects;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

public class EchoServiceEchoMethodPointCut implements Pointcut {
    public static final EchoServiceEchoMethodPointCut INSTANCE = new EchoServiceEchoMethodPointCut();

    private EchoServiceEchoMethodPointCut() {
    }

    @Override public ClassFilter getClassFilter() {
        return new ClassFilter() {
            @Override public boolean matches(Class<?> clazz) {
                return EchoService.class.isAssignableFrom(clazz);
            }
        };
    }

    @Override public MethodMatcher getMethodMatcher() {
        return new MethodMatcher() {
            @Override public boolean matches(Method method, Class<?> targetClass) {
                return Objects.equals(method.getName(), "echo") &&
                    method.getParameters().length == 1 &&
                    Objects.equals(method.getParameterTypes()[0], String.class);
            }

            @Override public boolean isRuntime() {
                return false;
            }

            @Override public boolean matches(Method method, Class<?> targetClass, Object... args) {
                return false;
            }
        };
    }
}
