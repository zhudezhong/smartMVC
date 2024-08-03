package com.scnu.smartmvc.hanler;

import com.scnu.smartmvc.hanler.argument.HandlerMethodArgumentResolverComposite;
import com.scnu.smartmvc.hanler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * `InvocableHandlerMethod`是对`HandlerMethod`的扩展，
 * 基于一组`HandlerMethodArgumentResolver`从请求上下文中解析出控制器方法的参数值，然后调用控制器方法。
 * <p>
 * 用于处理用户的请求调用控制器方法，包装处理所需的各种参数和执行处理逻辑
 */
public class InvocableHandlerMethod extends HandlerMethod {

    // 用来查找方法名
    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private HandlerMethodArgumentResolverComposite argumentResolver;

    private HandlerMethodReturnValueHandlerComposite returnValueHandler;


    // 用于数据的转换
    private ConversionService conversionService;

    public InvocableHandlerMethod(HandlerMethod handlerMethod,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(handlerMethod);
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }

    public InvocableHandlerMethod(Object bean, Method method,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(bean, method);
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }

    public void invokeAndHandle(HttpServletRequest request,
                                HttpServletResponse response,
                                ModelAndViewContainer mvContainer,
                                Object... providedArgs) throws Exception {
        // 1、获取方法的参数
        List<Object> args = this.getMethodArgumentValues(request, response, mvContainer, providedArgs);

        // 2、通过反射调用Controller中的方法
        Object resultValue = doInvoke(args);


        // 3、执行完成后判断返回值是否为空，
        if (Objects.isNull(resultValue)) {
            // 如果为空需要判断当前的response是否已经提交（有可能用户直接在控制的方法中使用response输出内容到前端），
            if (response.isCommitted()) {
                // 已提交标记本次请求已经处理完成` mavContainer.setRequestHandled(true);`
                mvContainer.setRequestHandled(true);
                return;
            } else {
                throw new IllegalStateException("Controller handler return value is null");
            }
        }

        // 4、如果返回值不为空，使用条件返回值处理器处理返回值
        mvContainer.setRequestHandled(false);
        Assert.notNull(returnValueHandler, "HandlerMethodReturnValueHandler can not null");

        MethodParameter returnType = new MethodParameter(this.getMethod(), -1); // -1表示方法的返回值
        this.returnValueHandler.handleReturnValue(resultValue, returnType, mvContainer, request, response);
    }


    /**
     * 通过反射调用Controller中的方法
     *
     * @param args
     * @return
     */
    private Object doInvoke(List<Object> args) throws InvocationTargetException, IllegalAccessException {
        return this.getMethod().invoke(this.getBean(), args.toArray());
    }


    /**
     * 获取到请求Controller的方法的参数
     *
     * @param request
     * @param response
     * @param mvContainer
     * @return
     * @throws Exception
     */
    private List<Object> getMethodArgumentValues(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 ModelAndViewContainer mvContainer,
                                                 Object... providedArgs) throws Exception {

        Assert.notNull(argumentResolver, "HandlerMethodArgumentResolver can not null");
        List<MethodParameter> parameters = this.getParameters();
        ArrayList<Object> args = new ArrayList<>(parameters.size());

        //遍历方法所有的参数，处理每个参数之前需要先调用`initParameterNameDiscovery`，
        // 然后在通过参数解析器去找到想要的参数
        for (MethodParameter parameter : parameters) {
            parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);

            Object arg = findProvidedArgument(parameter, providedArgs);

            if (Objects.nonNull(arg)) {
                args.add(arg);
                continue;
            }

            args.add(argumentResolver.resolveArgument(parameter, request, response, mvContainer, conversionService));
        }

        return args;
    }

    private Object findProvidedArgument(MethodParameter parameter, Object... providedArgs) {

        if (!ObjectUtils.isEmpty(providedArgs)) {
            for (Object providedArg : providedArgs) {
                if (parameter.getParameterType().isInstance(providedArg)) {
                    return providedArg;
                }
            }
        }

        return null;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }
}
