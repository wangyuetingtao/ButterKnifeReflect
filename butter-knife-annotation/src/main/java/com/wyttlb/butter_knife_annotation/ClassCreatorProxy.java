package com.wyttlb.butter_knife_annotation;

import java.util.HashMap;
import java.util.Map;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class ClassCreatorProxy {
    private String mBindingClassName;
    private String mPackageName;
    private TypeElement mTypeElement;
    private Map<Integer, VariableElement> mVariableElementMap = new HashMap<>();

    public ClassCreatorProxy(Elements elementsUtils, TypeElement classElement) {
        this.mTypeElement = classElement;
        //获取注解element所在类的包element
        PackageElement packageElement = elementsUtils.getPackageOf(classElement);
        //获取包名
        String packageName = packageElement.getQualifiedName().toString();
        //获取类名
        String className = mTypeElement.getSimpleName().toString();
        this.mPackageName = packageName;
        this.mBindingClassName = className + "_ViewBinding";
    }

    public void putElement(int id, VariableElement element) {
        mVariableElementMap.put(id, element);
    }

    public String genJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(mPackageName).append(";\n\n")
        .append("\n")
        .append("public class " + mBindingClassName).append(" {\n");
        genMethods(builder);
        builder.append("\n");
        builder.append(" }\n");

        return builder.toString();
    }

    public void genMethods(StringBuilder sb) {
        sb.append("public void bind(" + mTypeElement.getQualifiedName() + " host ){\n");
        for (int id : mVariableElementMap.keySet()) {
            VariableElement element = mVariableElementMap.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            sb.append("host." + name).append(" = ");
            sb.append("(" + type + ")(((android.app.Activity)host).findViewById(" + id + "));\n");
        }
        sb.append(" }\n");
    }

    public String getProxyClassFullName() {
        return mPackageName + "." + mBindingClassName;
    }

    public TypeElement getTypeElement() {
        return mTypeElement;
    }
}
