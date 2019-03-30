package com.theredpixelteam.torch.cocoabean;

import com.theredpixelteam.torch.ASMUtil;
import com.theredpixelteam.torch.adm.ADMLogging;
import com.theredpixelteam.torch.cocoabean.annotation.Entity;
import com.theredpixelteam.torch.exception.ShouldNotReachHere;
import com.theredpixelteam.torch.runtime.plugin.java.PluginClassTransformer;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

/**
 * Torch CocoaBean service class transformer.
 */
public class TorchCocoaBeanClassTransformer implements PluginClassTransformer {
    /**
     * @see PluginClassTransformer#accept(byte[])
     *
     * @param byts Class bytecode
     * @return Class bytecode
     * @throws Exception Exception
     */
    @ADMLogging
    @Override
    public @Nonnull byte[] accept(@Nonnull byte[] byts) throws Exception
    {
        ClassNode classNode = ASMUtil.asNode(byts);
        Optional<AnnotationNode> annotation = ASMUtil.getAnnotation(classNode, Entity.class);

        if (!annotation.isPresent())
            return byts;

        Map<String, Object> values = ASMUtil.getAnnotationValues(annotation.get());

        try {
            String namespaceName = (String) values.get("namespace");
            String identity = (String) values.get("value");

            // TODO
        } catch (Exception e) {
            throw new ShouldNotReachHere(e);
        }

        return byts;
    }
}
