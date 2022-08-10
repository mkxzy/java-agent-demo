package org.example;

import java.lang.instrument.Instrumentation;
import java.util.LinkedList;
import java.util.List;

public class SimpleAgent {

    private static String className = "com.iblotus.ratelimiterdemo.MyService";
    private static String methodName = "hello";

    /**
     * jvm 参数形式启动，运行此方法
     *
     * @param agentArgs
     * @param instrumentation
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("premain");
        instrumentation.addTransformer(new TestTransformer(className, methodName));
    }

    /**
     * 动态 attach 方式启动，运行此方法
     *
     * @param agentArgs
     * @param instrumentation
     */
    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("agentmain");

        try {
            List<Class> needRetransFormClasses = new LinkedList<>();
            Class[] loadedClass = instrumentation.getAllLoadedClasses();
            for (int i = 0; i < loadedClass.length; i++) {
                if (loadedClass[i].getName().equals(className)) {
                    needRetransFormClasses.add(loadedClass[i]);
                }
            }

            instrumentation.addTransformer(new TestTransformer(className, methodName));
            instrumentation.retransformClasses(needRetransFormClasses.toArray(new Class[0]));
        } catch (Exception e) {

        }
    }
}
