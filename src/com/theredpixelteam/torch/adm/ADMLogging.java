package com.theredpixelteam.torch.adm;

import java.lang.annotation.*;

/**
 * Methods annotated will provide further information, if any, when
 * Advanced Debug Mode is enabled.
 */
@Documented
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ADMLogging {
}
