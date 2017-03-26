package com.sunx.rmi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sun
 * @version
 * @since JDK 1.7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Adaptor {

	String value() default "";

	AdaptorScope scope() default AdaptorScope.singleton;

}
