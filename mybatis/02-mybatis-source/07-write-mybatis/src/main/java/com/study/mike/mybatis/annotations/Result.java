package com.study.mike.mybatis.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apache.ibatis.type.JdbcType;

import com.study.mike.mybatis.type.TypeHandler;
import com.study.mike.mybatis.type.UndefinedTypeHandler;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD })
public @interface Result {

	String column() default "";

	Class<?> javaType() default void.class;

	JdbcType jdbcType() default JdbcType.UNDEFINED;

	Class<? extends TypeHandler> typeHandler() default UndefinedTypeHandler.class;
}
