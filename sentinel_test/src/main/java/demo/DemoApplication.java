package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableDubbo(scanBasePackages = "com.test.demo.dubbo")
@SpringBootApplication
//@EnableApolloConfig
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
//        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("spring.xml");
//        Test test = app.getBean(Test.class);
//        test.test();
//
//        Service1Impl service1 = new Service1Impl();
//        Service service = (Service) Proxy.newProxyInstance(Service1Impl.class.getClassLoader(), Service1Impl.class.getInterfaces(), new InvocationHandler() {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                System.out.println("---proxy---");
//                return method.invoke(service1,args);
//            }
//        });
//
//        service.print();
//
//        app.registerShutdownHook();

    }
}
