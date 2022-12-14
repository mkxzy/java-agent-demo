package org.example;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TestTransformer implements ClassFileTransformer {
    //目标类名称，  .分隔
    private String targetClassName;
    //目标类名称，  /分隔
    private String targetVMClassName;
    private String targetMethodName;


    public TestTransformer(String className,String methodName){
        this.targetVMClassName = new String(className).replaceAll("\\.","\\/");
        this.targetMethodName = methodName;
        this.targetClassName=className;
    }
    //类加载时会执行该函数，其中参数 classfileBuffer为类原始字节码，返回值为目标字节码，className为/分隔
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        //判断类名是否为目标类名
        if(!className.equals(targetVMClassName)){
            return classfileBuffer;
        }
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass cls = classPool.get(this.targetClassName);
            CtMethod ctMethod = cls.getDeclaredMethod(this.targetMethodName);
            ctMethod.insertBefore("{ logger.info(\"agent inject start\"); }");
            ctMethod.insertAfter("{ logger.info(\"agent inject end\"); }");
            return cls.toBytecode();
        } catch (Exception e) {

        }
        return classfileBuffer;
    }
}
