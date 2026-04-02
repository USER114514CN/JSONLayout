package com.user114514.jsonlayout.core;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(java.lang.annotation.ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)

public @interface SetterInvokable {
    String name(); // 这个注解用来标记那些可以被 JSON 转换器调用的 setter 方法， name 属性指定了这个 setter 对应的 JSON 属性名
}