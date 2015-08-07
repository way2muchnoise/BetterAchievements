package thevault.asm;

import org.objectweb.asm.tree.ClassNode;

public interface ITransformer
{
    boolean transform(ClassNode classNode);
}
