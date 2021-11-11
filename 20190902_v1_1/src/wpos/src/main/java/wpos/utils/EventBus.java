package wpos.utils;

import javafx.application.Platform;
import wpos.model.MethodManager;
import wpos.listener.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBus {

    private static volatile EventBus instance;
    // 用来保存这些带注解的方法（订阅者的回调方法）
    private Map<Object, List<MethodManager>> cacheMap;
    private ExecutorService executorService;

    private EventBus() {
        cacheMap = new HashMap<Object, List<MethodManager>>();
        //创建一个缓存线程池
        executorService = Executors.newCachedThreadPool();
    }

    public static EventBus getDefault() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    // 找到Controller所有带符合注解的方法
    public void register(Object subscriber) {
        //获取Controller所有的方法
        List<MethodManager> methodList = cacheMap.get(subscriber);
        if (methodList == null) {    //不为空表示已经注册过了
            methodList = findAnnotationMethod(subscriber);
            cacheMap.put(subscriber, methodList);
        }
    }

    private List<MethodManager> findAnnotationMethod(Object getter) {
        List<MethodManager> methodList = new ArrayList<MethodManager>();
        //获取类
        Class<?> clazz = getter.getClass();
        //获取所有方法
        Method[] methods = clazz.getMethods();

        while (clazz != null) {
            //找出系统类，直接跳出，不添加cacheMap(因为不是订阅者)
            String clazzName = clazz.getName();
            if (clazzName.startsWith(".java") || clazzName.startsWith(".javax")) {
                break;
            }

            for (Method method : methods) {
                //获取方法的注解
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                //如果方法没有注解，则寻找下一个方法
                if (subscribe == null) {
                    continue;
                }

                //方法必须返回void
                Type returnType = method.getGenericReturnType();
                if (!"void".equals(returnType.toString())) {
                    throw new RuntimeException(method.getName() + "方法返回必须是void");
                }

                //方法参数必须有值
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException(method.getName() + "方法有且只有一个参数");
                }

                //符合以上检测要求，则放入MethodManager中
                MethodManager methodManager = new MethodManager(parameterTypes[0], subscribe.threadMode(), method);
                methodList.add(methodManager);
            }
            //不断循环找出父类含有订阅者（注解方法）的类；
//            clazz = clazz.getSuperclass();
            clazz = null;
        }

        return methodList;
    }

    public void post(final Object setter) {
        //订阅者已经登记，从登记表中找出
        Set<Object> set = cacheMap.keySet();
        //循环获取controller对象
        for (final Object getter : set) {
            List<MethodManager> methodList = cacheMap.get(getter);
            if (methodList != null) {
                //循环每个方法
                for (final MethodManager method : methodList) {
                    //有可能多个方法的参数一样，从而都接受到发送的消息
                    //判断是否匹配的上
                    if (method.getType().isAssignableFrom(setter.getClass())) {
                        switch (method.getThreadMode()) {
                            case POSTING:
                                invoke(method, getter, setter);
                                break;
                            case MAIN:
                                if (Platform.isFxApplicationThread()) {  //todo 判断当前是否在主线程，判断不知是否有误
                                    invoke(method, getter, setter);
                                } else {
                                    //子线程转化为主线程执行
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            invoke(method, getter, setter);
                                        }
                                    });
                                }
                                break;
                            case BACKGROUND:
                                if (Platform.isFxApplicationThread()) {  //todo 判断当前是否在主线程，判断不知是否有误
                                    //主线程上，需要转化为子线程执行
                                    executorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            invoke(method, getter, setter);
                                        }
                                    });
                                } else {
                                    invoke(method, getter, setter);
                                }
                                break;
                            case ASYNC: //不管当前是否在非主线程上，都另外开启一个线程
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(method, getter, setter);
                                    }
                                });
                                break;
                        }
                    }
                }
            }
        }
    }

    private void invoke(MethodManager method, Object getter, Object setter) {
        Method execute = method.getMethod();
        try {
            execute.invoke(getter, setter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void unregister(Object subscriber){
        cacheMap.remove(subscriber);
    }

}
