package com.theredpixelteam.torch.cocoabean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents the readability of a CocoaBean value entity.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Readability {
    /**
     * Represents the identity of target value element,
     * ignored if this annotation is directly annotated
     * above the element.
     *
     * @return Identity of entity
     */
    public String identity() default "";

    /**
     * Readability flag, ignored if this annotation is not
     * directly annotated above the element.
     *
     * @return True if can read, false otherwise.
     */
    public boolean value() default true;
}
