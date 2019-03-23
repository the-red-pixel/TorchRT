package com.theredpixelteam.cocoabean.annotation;

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

    /**
     * Whether this entity is read-only. This flag will only take
     * effect when annotating a <b>value-type</b> entity. If read-only
     * flag is set, any setter declared under this element will be
     * ignored or rejected.
     *
     * @return Whether read-only, false for default.
     */
    public boolean readonly() default false;
}
