package com.scnu.smartmvc.handler.interceptor;

import com.scnu.smartmvc.BaseJunit4Test;
import com.scnu.smartmvc.controller.TestHandlerController;
import com.scnu.smartmvc.exception.NoHandlerFoundException;
import com.scnu.smartmvc.hanler.HandlerExecutionChain;
import com.scnu.smartmvc.hanler.HandlerMethod;
import com.scnu.smartmvc.hanler.interceptor.InterceptorRegistry;
import com.scnu.smartmvc.hanler.interceptor.MappedInterceptor;
import com.scnu.smartmvc.hanler.mapping.RequestMappingHandlerMapping;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

public class HandlerInterceptorTest extends BaseJunit4Test {

    @Autowired
    RequestMappingHandlerMapping requestMappingHandlerMapping;

    private InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

    /**
     * 测试拦截器 HandlerInterceptor
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();

        interceptorRegistry.addInterceptor(interceptor)
                .addExcludePatterns("/ex_test")
                .addIncludePatterns("/in_test");

        List<MappedInterceptor> interceptors = interceptorRegistry.getMappedInterceptors();

        Assert.assertEquals(interceptors.size(), 1);

        MappedInterceptor mappedInterceptor = interceptors.get(0);

        Assert.assertTrue(mappedInterceptor.matches("/in_test"));
        Assert.assertFalse(mappedInterceptor.matches("/ex_test"));

        mappedInterceptor.preHandle(null, null, null);
        mappedInterceptor.postHandle(null, null, null, null);
        mappedInterceptor.afterCompletion(null, null, null, null);

    }

    @Test
    public void testGetHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        // 测试拦截器TestHandlerInterceptor生效
        request.setRequestURI("/in_test");
        HandlerExecutionChain executionChain = requestMappingHandlerMapping.getHandler(request);

        HandlerMethod handlerMethod = executionChain.getHandler();
        Assert.assertTrue(handlerMethod.getBean() instanceof TestHandlerController);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0)).getInterceptor()
                instanceof TestHandlerInterceptor);

        // 测试拦截器TestHandlerInterceptor不生效
        request.setRequestURI("/ex_test");
        executionChain = requestMappingHandlerMapping.getHandler(request);
        Assert.assertNull(executionChain.getInterceptors());

        // 测试找不到Handler， 抛出异常
        request.setRequestURI("/in_test123");
        try {
            requestMappingHandlerMapping.getHandler(request);
        } catch (NoHandlerFoundException ex) {
            System.out.println("异常URL：" + ex.getRequestURL());
        }

        // 测试Test2HandlerInterceptor拦截器对in_test2、in_test3都生效
        request.setRequestURI("/in_test2");
        executionChain = requestMappingHandlerMapping.getHandler(request);
        Assert.assertEquals(executionChain.getInterceptors().size(), 1);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0)).getInterceptor()
                instanceof Test2HandlerInterceptor);

        request.setRequestURI("/in_test3");
        executionChain = requestMappingHandlerMapping.getHandler(request);
        Assert.assertEquals(executionChain.getInterceptors().size(), 1);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0)).getInterceptor()
                instanceof Test2HandlerInterceptor);

    }

}
