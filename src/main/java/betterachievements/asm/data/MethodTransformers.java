package betterachievements.asm.data;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import thevault.asm.Transformer;

public final class MethodTransformers
{
    public static final Transformer.MethodTransformer actionPerformed =
            new Transformer.MethodTransformer("actionPerformed", "a","(" + ASMStrings.GUI_BUTTON.getASMTypeName() + ")V")
            {
                @Override
                protected void modify(MethodNode node)
                {
                    AbstractInsnNode insnNode = node.instructions.getFirst();
                    AbstractInsnNode newNode = null;
                    while (insnNode != null)
                    {
                        if (insnNode instanceof TypeInsnNode && insnNode.getOpcode() == Opcodes.NEW)
                        {
                            if (((TypeInsnNode) insnNode).desc.equals(ASMStrings.GUI_ACHIEVEMENTS.getASMClassName()))
                                newNode = new TypeInsnNode(Opcodes.NEW, ASMStrings.GUI_BETTER_ACHIEVEMENTS.getASMClassName());
                        }
                        else if (insnNode instanceof MethodInsnNode && insnNode.getOpcode() == Opcodes.INVOKESPECIAL)
                        {
                            if (((MethodInsnNode) insnNode).owner.equals(ASMStrings.GUI_ACHIEVEMENTS.getASMClassName()))
                                newNode = new MethodInsnNode(Opcodes.INVOKESPECIAL, ASMStrings.GUI_BETTER_ACHIEVEMENTS.getASMClassName(), "<init>", "(" + ASMStrings.GUI_SCREEN.getASMTypeName() + ASMStrings.STAT_FILE_WRITER.getASMTypeName() + ")V", false);
                        }
                        if (newNode != null)
                        {
                            node.instructions.insertBefore(insnNode, newNode);
                            node.instructions.remove(insnNode);
                            insnNode = newNode;
                            newNode = null;
                        }
                        insnNode = insnNode.getNext();
                    }
                }
            };
}
