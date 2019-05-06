package com.indev.scrapx.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jsoup.Connection;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Authentication {
    String loginURL();
    Connection.Method method() default Connection.Method.POST;
}
