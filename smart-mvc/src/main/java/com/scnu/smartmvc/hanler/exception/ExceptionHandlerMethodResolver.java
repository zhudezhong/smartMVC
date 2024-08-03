package com.scnu.smartmvc.hanler.exception;


import com.scnu.smartmvc.annotation.ExceptionHandler;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 当找出了所有被`ControllerAdvice`标注的类之后，
 * 我们还需要解析出这些类中哪些方法被注解`ExceptionHandler`标注过，
 * `ExceptionHandlerMethodResolver`就是来做这个事的。
 */
public class ExceptionHandlerMethodResolver {

    /**
     * EXCEPTION_HANDLER_METHODS -> 判断方法是否有注解`ExceptionHandler`
     */
    public static final ReflectionUtils.MethodFilter EXCEPTION_HANDLER_METHODS = method ->
            AnnotatedElementUtils.hasAnnotation(method, ExceptionHandler.class);


    private final Map<Class<? extends Throwable>, Method> mappedMethods = new ConcurrentReferenceHashMap<>(16);

    /**
     * 构造方法中传入了Bean的类型，使用`MethodIntrospector.selectMethods`
     * 过滤出所有被`ExceptionHandler`标注的类（在HanderMapping的初始化也使用过同样的方法），
     * 保存异常类型和方法的对应关系
     *
     * @param handlerType
     */
    public ExceptionHandlerMethodResolver(Class<?> handlerType) {

        for (Method method : MethodIntrospector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS)) {
            for (Class<? extends Throwable> exceptionType : detectExceptionMappings(method)) {
                this.mappedMethods.put(exceptionType, method);
            }
        }

    }

    /**
     * 解析出方法上`ExceptionHandler`配置的所有异常
     *
     * @param method
     * @return
     */
    private List<Class<? extends Throwable>> detectExceptionMappings(Method method) {

        ExceptionHandler annotation = AnnotatedElementUtils.findMergedAnnotation(method, ExceptionHandler.class);
        Assert.state(annotation != null, "No ExceptionHandler annotation");

        return Arrays.asList(annotation.value());
    }

    public Map<Class<? extends Throwable>, Method> getMappedMethods() {
        return mappedMethods;
    }

    public boolean hasExceptionMappings() {
        return !this.mappedMethods.isEmpty();
    }

    /**
     * 根据异常类型获取解析异常的方法
     * @param exception
     * @return
     */
    public Method resolveMethod(Exception exception) {
        Method method = resolveMethodByExceptionType(exception.getClass());
        if (method == null) {
            Throwable cause = exception.getCause();
            if (cause != null) {
                method = resolveMethodByExceptionType(cause.getClass());
            }
        }

        return method;
    }

    /**
     * 通过异常类型找出对应的方法
     *
     * @param exceptionType
     * @return
     */
    private Method resolveMethodByExceptionType(Class<? extends Throwable> exceptionType) {
        return this.mappedMethods.get(exceptionType);
    }


}
