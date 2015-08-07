package thevault.asm;

import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.Comparator;

public class InsnComparator implements Comparator<AbstractInsnNode>
{
    /*
    Again, credit to squeek502 for the idea of making a proper comparator out of this crap
    But at least the methods are completely mine this time
     */
    private static final String WILDCARD_STRING = "HilburnIsAwesome";
    private static final int WILDCARD_INT = -42;
    private static final Object[] WILDCARD_ARRAY = new Object[]{WILDCARD_STRING, WILDCARD_INT};

    @Override
    public int compare(AbstractInsnNode a, AbstractInsnNode b)
    {
        return areNodesEqual(a, b) ? 0 : 1;
    }

    public static boolean areNodesEqual(AbstractInsnNode a, AbstractInsnNode b)
    {
        if (a == null || b == null) return false;
        if (a.equals(b)) return true;
        if (a.getType() == b.getType() && a.getOpcode() == b.getOpcode())
        {
            switch (a.getType())
            {
                case AbstractInsnNode.FIELD_INSN:
                    return areFieldInsnNodesEqual((FieldInsnNode)a, (FieldInsnNode)b);
                case AbstractInsnNode.IINC_INSN:
                    return areIincInsnNodesEqual((IincInsnNode)a, (IincInsnNode)b);
                case AbstractInsnNode.INT_INSN:
                    return areIntInsnNodesEqual((IntInsnNode)a, (IntInsnNode)b);
                case AbstractInsnNode.INVOKE_DYNAMIC_INSN:
                    return areInvokeDynamicInsnNodesEqual((InvokeDynamicInsnNode)a, (InvokeDynamicInsnNode)b);
                case AbstractInsnNode.LDC_INSN:
                    return areLdcInsnNodesEqual((LdcInsnNode)a, (LdcInsnNode)b);
                case AbstractInsnNode.METHOD_INSN:
                    return areMethodInsnNodesEqual((MethodInsnNode)a, (MethodInsnNode)b);
                case AbstractInsnNode.MULTIANEWARRAY_INSN:
                    return areMultiANewArrayInsnNodesEqual((MultiANewArrayInsnNode)a, (MultiANewArrayInsnNode)b);
                case AbstractInsnNode.TYPE_INSN:
                    return areTypeInsnNodesEqual((TypeInsnNode)a, (TypeInsnNode)b);
                case AbstractInsnNode.VAR_INSN:
                    return areVarInsnNodesEqual((VarInsnNode)a, (VarInsnNode)b);
            }
        }
        return false;
    }

    private static boolean areFieldInsnNodesEqual(FieldInsnNode a, FieldInsnNode b)
    {
        return objectMatch(a.name, b.name) && objectMatch(a.desc, b.desc);
    }

    private static boolean areIincInsnNodesEqual(IincInsnNode a, IincInsnNode b)
    {
        return intMatch(a.incr, b.incr) && intMatch(a.var, b.var);
    }

    private static boolean areIntInsnNodesEqual(IntInsnNode a, IntInsnNode b)
    {
        return intMatch(a.operand, b.operand);
    }

    private static boolean areInvokeDynamicInsnNodesEqual(InvokeDynamicInsnNode a, InvokeDynamicInsnNode b)
    {
        return objectMatch(a.name, b.name) && objectMatch(a.desc, b.desc) && arrayMatch(a.bsmArgs, b.bsmArgs);
    }

    private static boolean areLdcInsnNodesEqual(LdcInsnNode a, LdcInsnNode b)
    {
        return objectMatch(a.cst, b.cst);
    }

    private static boolean areMethodInsnNodesEqual(MethodInsnNode a, MethodInsnNode b)
    {
        return objectMatch(a.name, b.name) && a.itf == b.itf && objectMatch(a.desc, b.desc);
    }

    private static boolean areMultiANewArrayInsnNodesEqual(MultiANewArrayInsnNode a, MultiANewArrayInsnNode b)
    {
        return intMatch(a.dims, b.dims) && objectMatch(a.desc, b.desc);
    }

    private static boolean areTypeInsnNodesEqual(TypeInsnNode a, TypeInsnNode b)
    {
        return objectMatch(a.desc, b.desc);
    }

    private static boolean areVarInsnNodesEqual(VarInsnNode a, VarInsnNode b)
    {
        return intMatch(a.var, b.var);
    }

    private static boolean intMatch(int a, int b)
    {
        return a == b || a == WILDCARD_INT || b == WILDCARD_INT;
    }

    private static boolean objectMatch(Object a, Object b)
    {
        return a.equals(b) || a.equals(WILDCARD_STRING) || b.equals(WILDCARD_STRING);
    }

    private static boolean arrayMatch(Object[] a, Object[] b)
    {
        return Arrays.deepEquals(a, b) || Arrays.deepEquals(a, WILDCARD_ARRAY) || Arrays.deepEquals(b, WILDCARD_ARRAY);
    }
}
