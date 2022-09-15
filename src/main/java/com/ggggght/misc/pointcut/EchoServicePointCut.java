package com.ggggght.misc.pointcut;

import java.lang.reflect.Method;
import java.util.Objects;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

// @AllArgsConstructor
// @Data(staticConstructor = "of")
// @EqualsAndHashCode(callSuper = true)
public  class EchoServicePointCut extends StaticMethodMatcherPointcut {
    private  final String methodName;
    private  final Class<?> targetClass;

    /**
     * Perform static checking whether the given method matches.
     * <p>If this returns {@code false} or if the {@link #isRuntime()}
     * method returns {@code false}, no runtime check (i.e. no
     * {@link #matches(Method, Class, Object[])} call)
     * will be made.
     *
     * @param method      the candidate method
     * @param targetClass the target class
     * @return whether this method matches statically
     */
    @Override public boolean matches(Method method, Class<?> targetClass) {
        return Objects.equals(method.getName(), methodName) && this.targetClass.isAssignableFrom(
            targetClass);
    }

    public EchoServicePointCut(String methodName, Class<?> targetClass) {
        this.methodName = methodName;
        this.targetClass = targetClass;
    }
}
