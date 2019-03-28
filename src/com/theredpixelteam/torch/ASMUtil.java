package com.theredpixelteam.torch;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Providing some common asm operations.
 */
public class ASMUtil {
    private ASMUtil()
    {
    }

    /**
     * Wrap the bytecode array as a ClassNode instance.
     *
     * @param byts Bytecode array
     * @return ClassNode instance
     */
    public static @Nonnull ClassNode asNode(@Nonnull byte[] byts)
    {
        Objects.requireNonNull(byts, "byts");

        ClassNode classNode = new ClassNode();
        ClassReader reader = new ClassReader(byts);

        reader.accept(classNode, 0);

        return classNode;
    }
}
