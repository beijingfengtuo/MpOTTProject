package cn.cibnmp.ott.jni;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.CLASS)
public @interface JNIMethods {
	String j() default "";
	String c() default "";
}