package com.wuage.test.loading;

import com.taobao.hsf.lightapi.ServiceFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by hacke on 2017/5/11.
 */
public class InitHSF {
    private static InitHSF init=null;
    private InitHSF(){}
    private static  ApplicationContext applicationContext=null;
    private static final ServiceFactory factory =null;
    public synchronized static InitHSF getInstance(){
        if (init==null) {
            init = new InitHSF();
//            ServiceFactory.getInstanceWithPath(System.getenv( "HSF_HOME" ));
            //E:\wuage\tools\taobao-hsf.sar\taobao-hsf.sar
            //under $HSF_HOME path has to contain taobao-hsf.sar package, or .gz.
            ServiceFactory.getInstanceWithPath("E:\\wuage\\tools\\taobao-hsf.sar" );
            applicationContext = new ClassPathXmlApplicationContext("classpath:/hsf-consumer-beans.xml");
        }
        return init;
    }

//    public static void initApplicationContext(){
//        if (applicationContext == null){
//            throw new IllegalArgumentException("load hsf-consumer-beans.xml error!");
//        }
//    }

    public Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
}
