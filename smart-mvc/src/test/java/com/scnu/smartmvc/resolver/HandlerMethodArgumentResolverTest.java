package com.scnu.smartmvc.resolver;

import com.alibaba.fastjson.JSON;
import com.scnu.smartmvc.BaseJunit4Test;
import com.scnu.smartmvc.controller.TestController;
import com.scnu.smartmvc.controller.TestReturnValueController;
import com.scnu.smartmvc.hanler.HandlerMethod;
import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import com.scnu.smartmvc.hanler.argument.HandlerMethodArgumentResolverComposite;
import com.scnu.smartmvc.hanler.argument.RequestBodyMethodArgumentResolver;
import com.scnu.smartmvc.hanler.argument.RequestParamMethodArgumentResolver;
import com.scnu.smartmvc.hanler.argument.ServletRequestMethodArgumentResolver;
import com.scnu.smartmvc.hanler.returnvalue.*;
import com.scnu.smartmvc.view.View;
import com.scnu.smartmvc.vo.UserVO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


public class HandlerMethodArgumentResolverTest extends BaseJunit4Test {


    @Test
    public void test1() throws NoSuchMethodException {

        TestController testController = new TestController();

        Method method = testController.getClass().getMethod("test4", String.class, Integer.class, Date.class, HttpServletRequest.class);

        // 构建HandlerMethod对象
        HandlerMethod handlerMethod = new HandlerMethod(testController, method);

        // 构建模拟请求request
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "zhangsan");
        request.setParameter("age", "25");
        request.setParameter("birthday", "2000-11-12 13:00:00");

        // 添加支持的解析器
        HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();
        resolverComposite.addResolver(new RequestParamMethodArgumentResolver());
        resolverComposite.addResolver(new ServletRequestMethodArgumentResolver());

        // 定义转换器 DefaultFormattingConversionService是Spring中的一个数据转换器服务，默认已经添加了很多转换器
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);

        MockHttpServletResponse response = new MockHttpServletResponse();

        // DefaultParameterNameDiscoverer 是用于查找参数名的类
        // 用于查找方法参数名
        DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.getParameters().forEach(methodParameter -> {
            methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);

            try {
                Object value = resolverComposite.resolveArgument(methodParameter, request, response, null, conversionService);
                System.out.println(methodParameter.getParameterName() + " : " + value + "   type: " + value.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    public void test2() throws NoSuchMethodException {

        TestController testController = new TestController();

        Method method = testController.getClass().getMethod("user", UserVO.class);

        // 构建HandlerMethod对象
        HandlerMethod handlerMethod = new HandlerMethod(testController, method);

        UserVO userVO = new UserVO();
        userVO.setName("Bob");
        userVO.setAge(28);
        userVO.setBirthday(new Date());

        // 构建模拟请求request
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(JSON.toJSONString(userVO).getBytes()); // 模拟JSON参数

        // 添加支持的解析器
        HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();
        resolverComposite.addResolver(new RequestBodyMethodArgumentResolver());

        MockHttpServletResponse response = new MockHttpServletResponse();

        // DefaultParameterNameDiscoverer 是用于查找参数名的类
        // 用于查找方法参数名
        DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.getParameters().forEach(methodParameter -> {
            methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);

            try {
                Object value = resolverComposite.resolveArgument(methodParameter, request, response, null, null);
                System.out.println(methodParameter.getParameterName() + " : " + value + "   type: " + value.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    @Test
    public void test3() throws Exception {
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        composite.addReturnValueHandler(new MapMethodReturnValueHandler());
        composite.addReturnValueHandler(new ModelMethodReturnValueHandler());
        composite.addReturnValueHandler(new ResponseBodyMethodReturnValueHandler());
        composite.addReturnValueHandler(new ViewMethodReturnValueHandler());
        composite.addReturnValueHandler(new ViewNameMethodReturnValueHandler());

        TestReturnValueController controller = new TestReturnValueController();
        ModelAndViewContainer mvContainer = new ModelAndViewContainer();


        // 测试testViewName
        Method viewNameMethod = controller.getClass().getMethod("testViewName");
        MethodParameter viewNameMethodParameter = new MethodParameter(viewNameMethod, -1);
        composite.handleReturnValue(controller.testViewName(), viewNameMethodParameter, mvContainer, null, null);
        Assert.assertEquals(mvContainer.getView(), "/jsp/index.jsp");

        // 测试testView
        Method view = controller.getClass().getMethod("testView");
        MethodParameter viewMethodParameter = new MethodParameter(view, -1);
        composite.handleReturnValue(controller.testView(), viewMethodParameter, mvContainer, null, null);
        Assert.assertTrue(mvContainer.getView() instanceof View);

        // 测试testResponseBody
        Method responseBodyMethod = controller.getClass().getMethod("testResponseBody");
        MethodParameter responseBodyMethodParameter = new MethodParameter(responseBodyMethod, -1);
        MockHttpServletResponse response = new MockHttpServletResponse();
        composite.handleReturnValue(controller.testResponseBody(), responseBodyMethodParameter, mvContainer, null, response);
        System.out.println(response.getContentAsString()); // 打印出Controller中返回得JSON字符串

        // 测试testModel
        Method modelMethod = controller.getClass().getMethod("testModel", Model.class);
        MethodParameter modelMethodParameter = new MethodParameter(modelMethod, -1);
        composite.handleReturnValue(controller.testModel(mvContainer.getModel()), modelMethodParameter, mvContainer, null, null);
        Assert.assertEquals(mvContainer.getModel().getAttribute("testModel"), "zhudezhong");


        // 测试testMap
        Method mapMethod = controller.getClass().getMethod("testMap");
        MethodParameter mapMethodParameter = new MethodParameter(mapMethod, -1);
        composite.handleReturnValue(controller.testMap(), mapMethodParameter, mvContainer, null, null);
        Assert.assertEquals(mvContainer.getModel().getAttribute("testMap"), "zhudezhong");

    }


}
