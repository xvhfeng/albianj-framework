package com.geek94.values;

import com.geek94.comment.Comments;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Comments("Albianj标注不再被使用")
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AblUselessAnno {
    String value() default "";
}
