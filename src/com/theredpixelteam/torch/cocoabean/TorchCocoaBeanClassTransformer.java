package com.theredpixelteam.torch.cocoabean;

import com.theredpixelteam.redtea.util.Optional;
import com.theredpixelteam.torch.asm.ASMUtil;
import com.theredpixelteam.torch.adm.ADMLogging;
import com.theredpixelteam.torch.cocoabean.annotation.Blank;
import com.theredpixelteam.torch.cocoabean.annotation.Entity;
import com.theredpixelteam.torch.exception.ShouldNotReachHere;
import com.theredpixelteam.torch.runtime.plugin.java.PluginClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Torch CocoaBean service class transformer.
 */
public class TorchCocoaBeanClassTransformer implements PluginClassTransformer {
    public TorchCocoaBeanClassTransformer(@Nonnull TorchCocoaBeanService service)
    {
        this.service = Objects.requireNonNull(service, "service");
    }

    /**
     * @see PluginClassTransformer#accept(byte[])
     *
     * @param byts Class bytecode
     * @return Class bytecode
     * @throws Exception Exception
     */
    @ADMLogging
    @SuppressWarnings("unchecked")
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

            for (MethodNode methodNode : (List<MethodNode>) classNode.methods)
            {
                if (ASMUtil.hasAnnotation(methodNode, Blank.class))
                    continue;

                ASMUtil.insertCodeBeforeInsn(Opcodes.RETURN,
                        methodNode.instructions, generate(namespaceName, identity));
            }
        } catch (Exception e) {
            throw new ShouldNotReachHere(e);
        }

        TorchCocoaBeanService.CocoaBeanEntityContextProvider contextProvider =
                service.getContextProvider();

        if (contextProvider.supportPreload())
            contextProvider.preloadContext(classNode);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);

        byts = writer.toByteArray();

        return byts;
    }

    private static @Nonnull InsnList generate(@Nonnull String namespaceName,
                                              @Nonnull String identity)
    {
        MethodNode node = new MethodNode();

        node.visitMethodInsn(Opcodes.INVOKESTATIC,
                "com/theredpixelteam/torch/Torch",
                "getCocoaBeanService",
                "()Lcom/theredpixelteam/redtea/util/Optional;",
                false);

        node.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "com/theredpixelteam/redtea/util/Optional",
                "get",
                "()Ljava/lang/Object;",
                false);

        node.visitTypeInsn(Opcodes.CHECKCAST,
                "com/theredpixelteam/torch/cocoabean/TorchCocoaBeanService");

        node.visitLdcInsn(namespaceName);
        node.visitLdcInsn(identity);
        node.visitVarInsn(Opcodes.ALOAD, 0);

        node.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "com/theredpixelteam/torch/cocoabean/TorchCocoaBeanService",
                "register",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Z",
                false);

        return node.instructions;
    }

    private final TorchCocoaBeanService service;
}
