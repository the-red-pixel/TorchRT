package com.theredpixelteam.torch.cocoabean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation of a CocoaBean element in an entity.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Element {
    /**
     * Represents the identity of this entity.
     *
     * @return Identity of entity
     */
    public String identity();
}
