package com.lyx.frame.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Id
 * <p>
 * eg:
 *
 * @Id(id = R.id.tv)
 * private TextView textView;
 * <p>
 * author:  luoyingxing
 * date: 2017/10/16.
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    //id is view's resource id ,default -1.
    int id() default -1;

    //is true , this view will implements View.OnTouchListener
    boolean onTouch() default false;

    //is true , this view will implements View.OnClickListener
    boolean onClick() default false;
}