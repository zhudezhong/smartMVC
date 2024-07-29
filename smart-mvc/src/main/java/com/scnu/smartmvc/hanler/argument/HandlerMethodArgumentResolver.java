package com.scnu.smartmvc.hanler.argument;

import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 策略接口，作用是把请求中的数据解析为Controller中方法的参数值
 * 有了它才能会让Spring MVC处理入参显得那么高级、那么自动化。
 */
public interface HandlerMethodArgumentResolver {

    /**
     *  此方法判断当前的参数解析器是否支持传入的参数，返回true表示支持
     * @param parameter
     * @return
     */
    boolean supportParameter(MethodParameter parameter);

    /**
     * 从request对象中解析出parameter需要的值，除了`MethodParameter`和`HttpServletRequest`参数外，
     * 还传入了`ConversionService`，用于在把request中取出的值需要转换成`MethodParameter`参数的类型。
     *
     * 这个方法的参数定义和SpringMVC中的方法稍有不同，主要是为了简化数据转换的过程
     * @param parameter
     * @param request
     * @param response
     * @param container
     * @param conversionService
     * @return
     * @throws Exception
     */
    Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response,
                           ModelAndViewContainer container, ConversionService conversionService) throws Exception;
}
