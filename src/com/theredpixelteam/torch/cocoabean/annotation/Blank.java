package com.theredpixelteam.torch.cocoabean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Constructors annotated with this annotation will be ignored
 * by the class transformer of the CocoaBean service, which means
 * that when this constructor is called, this instance will not
 * be registered as a CocoaBean entity.
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface Blank {
}
