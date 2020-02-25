package com.study.mike.mybatis.annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标识选用的构造方法
 */
@Documented
@Retention(RUNTIME)
@Target(CONSTRUCTOR)
public @interface MapConstructor {

}
