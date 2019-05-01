package com.theredpixelteam.torch.asm;

import com.theredpixelteam.torch.exception.ShouldNotReachHere;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.*;

import com.theredpixelteam.redtea.util.Optional;

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

    /**
     * Get annotation list in a ClassNode.
     *
     * @param node ClassNode instance
     * @return AnnotationNode list
     */
    @SuppressWarnings("unchecked")
    public static @Nonnull List<AnnotationNode> getAnnotations(@Nonnull ClassNode node)
    {
        return node.visibleAnnotations;
    }

    /**
     * Whether the class is annotated with specified annotation.
     *
     * @param annotationClass Annotation type
     * @return Result
     */
    public static boolean hasAnnotation(@Nonnull ClassNode node,
                                        @Nonnull Class<? extends Annotation> annotationClass)
    {
        return getAnnotation(node, annotationClass).isPresent();
    }

    /**
     * Get the specified annotation of this class.
     *
     * @param node ClassNode instance
     * @param annotationClass Annotation type
     * @return AnnotationNode instance, if any
     */
    public static Optional<AnnotationNode> getAnnotation(@Nonnull ClassNode node,
                                                         @Nonnull Class<? extends Annotation> annotationClass)
    {
        List<AnnotationNode> annotations = getAnnotations(node);
        String descriptor = Type.getDescriptor(annotationClass);

        for (AnnotationNode annotation : annotations)
            if (descriptor.equals(annotation.desc))
                return Optional.of(annotation);

        return Optional.empty();
    }

    /**
     * Get all values in an annotation node, if any.
     *
     * @param node AnnotationNode instance
     * @return Name-Value map
     */
    @SuppressWarnings("unchecked")
    public static @Nonnull Map<String, Object> getAnnotationValues(@Nonnull AnnotationNode node)
    {
        List<Object> values = node.values;

        if (values == null)
            return Collections.emptyMap();

        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < values.size(); i++) try {
            map.put((String) values.get(i++), values.get(i++));
        } catch (Exception e) {
            throw new ShouldNotReachHere(e);
        }

        return map;
    }
}
