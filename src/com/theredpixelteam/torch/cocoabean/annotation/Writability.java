package com.theredpixelteam.torch.cocoabean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents the writability of a CocoaBean value element.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Writability {
    /**
     * Represents the identity of target value entity,
     * ignored if this annotation is directly annotated
     * above the element.
     *
     * @return Identity of entity
     */
    public String identity() default "";

    /**
     * Writability flag, ignored if this annotation is not
     * directly annotated above the element.
     *
     * @return True if can write, otherwise false.
     */
    public boolean value() default true;
}
