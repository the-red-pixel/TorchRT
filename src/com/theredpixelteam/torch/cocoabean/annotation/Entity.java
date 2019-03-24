package com.theredpixelteam.torch.cocoabean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation of CocoaBean entity.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
    /**
     * Represents the identity of this entity.
     *
     * @return Identity of entity
     */
    public String identity();
}
