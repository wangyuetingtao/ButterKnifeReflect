package com.wyttlb.butter_knife_annotation;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Elements mElementsUtils;
//    private Types mTypes;
//    private Filer mFilter;
    private Map<String, ClassCreatorProxy> mProxyMap = new HashMap<>();
    /**
     * 初始化，得到ProcessingEnvironment，ProcessingEnvironment中提供很多有用的
     * 工具类Elements, Type, Filer
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        mElementsUtils = processingEnvironment.getElementUtils();
    }

    /**
     * 制定解析哪个注解
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(BindView.class.getCanonicalName());
        return supportTypes;
    }

    /**
     * 指定使用的java版本
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 扫描，评估和处理注解的代码，生成java文件
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {


        //获取所有注解element
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);

        if (elements.size() <= 0) {
            return false;
        }

        mMessager.printMessage(Diagnostic.Kind.NOTE, "Butter Knife processing");
        mProxyMap.clear();

        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            //获取注解element所在的类Element
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            //获取注解element所在的类Element的完整类名
            String fullClassName = classElement.getQualifiedName().toString();
            //该类名是否存在过
            ClassCreatorProxy proxy = mProxyMap.get(fullClassName);
            //如果不存在，新建key，value
            if (proxy == null) {
                proxy = new ClassCreatorProxy(mElementsUtils, classElement);
                mProxyMap.put(fullClassName, proxy);
            }

            BindView bindView = variableElement.getAnnotation(BindView.class);
            //拿到element绑定的组件id
            int id = bindView.value();
            //将注解element放到同一个类element下
            proxy.putElement(id, variableElement);
        }

        //通过遍历mProxyMap,创建java文件
        for (String className : mProxyMap.keySet()) {
            ClassCreatorProxy proxyInfo = mProxyMap.get(className);
            try{
                mMessager.printMessage(Diagnostic.Kind.NOTE, "--> create " + proxyInfo.getProxyClassFullName());
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(proxyInfo.getProxyClassFullName(), proxyInfo.getTypeElement());
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.genJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "--> create " + proxyInfo.getProxyClassFullName() + " error");
            }
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "Butter Knife process finish");
        return true;
    }
}
