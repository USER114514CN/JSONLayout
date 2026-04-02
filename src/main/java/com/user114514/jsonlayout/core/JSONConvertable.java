package com.user114514.jsonlayout.core;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(java.lang.annotation.ElementType.TYPE)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)


public @interface JSONConvertable {
    // 这个注解用来标记那些可以被 JSON 转换器处理的类
    // 咕咕嘎嘎 （我已经疯了）
    // 艹我忘了这是一个类的注解， 表示这个类可以被 JSON 转换器处理， 也就是说这个类的实例可以通过 JSON 配置来创建和配置
}
