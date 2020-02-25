package com.study.mike.mybatis.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标识接口为Mapper的注解
 * 
 * @author Mike老师 QQ:3266399810
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Mapper {

}
