package com.session.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 自定义的注解别名，用于在网络请求时属性字段，如果使用gson解释建议使用gson自带的SerializedName */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldAlias {

	/** 属性的别名 */
	public String value() default "";

}
