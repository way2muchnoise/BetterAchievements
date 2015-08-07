package thevault.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.*;

public class ASMHelper
{
    /*
    Pretty much a straight-up copy of https://github.com/squeek502/ASMHelper/blob/master/java/squeek/asmhelper/ASMHelper.java
    Made a few changes and will probably add more stuff as it becomes necessary
    Had written a bunch of similar methods already and then I found that repo
     */
    private static InsnComparator insnComparator = new InsnComparator();

    public static ClassNode readClassFromBytes(byte[] bytes)
    {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        return classNode;
    }

    public static byte[] writeClassToBytes(ClassNode classNode)
    {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public static byte[] writeClassToBytesNoDeobfSkipFrames(ClassNode classNode)
    {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public static boolean isLabelOrLineNumber(AbstractInsnNode insn)
    {
        return insn.getType() == AbstractInsnNode.LABEL || insn.getType() == AbstractInsnNode.LINE;
    }

    public static AbstractInsnNode getOrFindInstructionOfType(AbstractInsnNode firstInsnToCheck, int type)
    {
        return getOrFindInstructionWithOpcode(firstInsnToCheck, type, false);
    }

    public static AbstractInsnNode getOrFindInstructionOfType(AbstractInsnNode firstInsnToCheck, int type, boolean reverseDirection)
    {
        for (AbstractInsnNode instruction = firstInsnToCheck; instruction != null; instruction = reverseDirection ? instruction.getPrevious() : instruction.getNext())
        {
            if (instruction.getType() == type)
                return instruction;
        }
        return null;
    }

    public static AbstractInsnNode getOrFindInstructionWithOpcode(AbstractInsnNode firstInsnToCheck, int opcode)
    {
        return getOrFindInstructionWithOpcode(firstInsnToCheck, opcode, false);
    }

    public static AbstractInsnNode getOrFindInstructionWithOpcode(AbstractInsnNode firstInsnToCheck, int opcode, boolean reverseDirection)
    {
        for (AbstractInsnNode instruction = firstInsnToCheck; instruction != null; instruction = reverseDirection ? instruction.getPrevious() : instruction.getNext())
        {
            if (instruction.getOpcode() == opcode)
                return instruction;
        }
        return null;
    }

    public static AbstractInsnNode getOrFindLabelOrLineNumber(AbstractInsnNode firstInsnToCheck)
    {
        return getOrFindInstruction(firstInsnToCheck, false);
    }

    public static AbstractInsnNode getOrFindLabelOrLineNumber(AbstractInsnNode firstInsnToCheck, boolean reverseDirection)
    {
        for (AbstractInsnNode instruction = firstInsnToCheck; instruction != null; instruction = reverseDirection ? instruction.getPrevious() : instruction.getNext())
        {
            if (isLabelOrLineNumber(instruction))
                return instruction;
        }
        return null;
    }

    public static AbstractInsnNode getOrFindInstruction(AbstractInsnNode firstInsnToCheck)
    {
        return getOrFindInstruction(firstInsnToCheck, false);
    }

    public static AbstractInsnNode getOrFindInstruction(AbstractInsnNode firstInsnToCheck, boolean reverseDirection)
    {
        for (AbstractInsnNode instruction = firstInsnToCheck; instruction != null; instruction = reverseDirection ? instruction.getPrevious() : instruction.getNext())
        {
            if (!isLabelOrLineNumber(instruction))
                return instruction;
        }
        return null;
    }

    public static AbstractInsnNode findFirstInstruction(MethodNode method)
    {
        return getOrFindInstruction(method.instructions.getFirst());
    }

    public static AbstractInsnNode findFirstInstructionWithOpcode(MethodNode method, int opcode)
    {
        return getOrFindInstructionWithOpcode(method.instructions.getFirst(), opcode);
    }

    public static AbstractInsnNode findLastInstructionWithOpcode(MethodNode method, int opcode)
    {
        return getOrFindInstructionWithOpcode(method.instructions.getLast(), opcode, true);
    }

    public static AbstractInsnNode findNextInstruction(AbstractInsnNode instruction)
    {
        return getOrFindInstruction(instruction.getNext());
    }

    public static AbstractInsnNode findNextInstructionWithOpcode(AbstractInsnNode instruction, int opcode)
    {
        return getOrFindInstructionWithOpcode(instruction.getNext(), opcode);
    }

    public static AbstractInsnNode findNextLabelOrLineNumber(AbstractInsnNode instruction)
    {
        return getOrFindLabelOrLineNumber(instruction.getNext());
    }

    public static AbstractInsnNode findPreviousInstruction(AbstractInsnNode instruction)
    {
        return getOrFindInstruction(instruction.getPrevious(), true);
    }

    public static AbstractInsnNode findPreviousInstructionWithOpcode(AbstractInsnNode instruction, int opcode)
    {
        return getOrFindInstructionWithOpcode(instruction.getPrevious(), opcode, true);
    }

    public static AbstractInsnNode findPreviousLabelOrLineNumber(AbstractInsnNode instruction)
    {
        return getOrFindLabelOrLineNumber(instruction.getPrevious(), true);
    }

    public static MethodNode getMethodByName(ClassNode classNode, String name, String args)
    {
        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(name) && method.desc.equals(args))
            {
                return method;
            }
        }
        return null;
    }

    public static LabelNode findEndLabel(MethodNode method)
    {
        for (AbstractInsnNode instruction = method.instructions.getLast(); instruction != null; instruction = instruction.getPrevious())
        {
            if (instruction instanceof LabelNode)
                return (LabelNode)instruction;
        }
        return null;
    }

    public static int removeFromInsnListUntil(InsnList insnList, AbstractInsnNode startInclusive, AbstractInsnNode endNotInclusive)
    {
        AbstractInsnNode insnToRemove = startInclusive;
        int numDeleted = 0;
        while (insnToRemove != null && insnToRemove != endNotInclusive)
        {
            numDeleted++;
            insnToRemove = insnToRemove.getNext();
            insnList.remove(insnToRemove.getPrevious());
        }
        return numDeleted;
    }

    public static AbstractInsnNode move(AbstractInsnNode start, int distance)
    {
        AbstractInsnNode movedTo = start;
        for (int i = 0; i < Math.abs(distance) && movedTo != null; i++)
        {
            movedTo = distance > 0 ? movedTo.getNext() : movedTo.getPrevious();
        }
        return movedTo;
    }

    public static boolean instructionsMatch(AbstractInsnNode first, AbstractInsnNode second)
    {
        return insnComparator.areNodesEqual(first, second);
    }

    public static boolean patternMatches(InsnList checkFor, AbstractInsnNode checkAgainst)
    {
        for (AbstractInsnNode instruction = checkFor.getFirst(); instruction != null; )
        {
            if (checkAgainst == null)
                return false;

            if (isLabelOrLineNumber(instruction))
            {
                instruction = instruction.getNext();
                continue;
            }

            if (isLabelOrLineNumber(checkAgainst))
            {
                checkAgainst = checkAgainst.getNext();
                continue;
            }

            if (!instructionsMatch(instruction, checkAgainst))
                return false;

            instruction = instruction.getNext();
            checkAgainst = checkAgainst.getNext();
        }
        return true;
    }

    public static AbstractInsnNode find(InsnList haystack, InsnList needle)
    {
        return find(haystack.getFirst(), needle);
    }

    public static AbstractInsnNode find(AbstractInsnNode haystackStart, InsnList needle)
    {
        if (needle.getFirst() == null)
            return null;

        int needleStartOpcode = needle.getFirst().getOpcode();
        AbstractInsnNode checkAgainstStart = getOrFindInstructionWithOpcode(haystackStart, needleStartOpcode);
        while (checkAgainstStart != null)
        {
            if (patternMatches(needle, checkAgainstStart))
                return checkAgainstStart;

            checkAgainstStart = findNextInstructionWithOpcode(checkAgainstStart, needleStartOpcode);
        }
        return null;
    }

    public static AbstractInsnNode find(InsnList haystack, AbstractInsnNode needle)
    {
        return find(haystack.getFirst(), needle);
    }

    public static AbstractInsnNode find(AbstractInsnNode haystackStart, AbstractInsnNode needle)
    {
        InsnList insnList = new InsnList();
        insnList.add(needle);
        return find(haystackStart, insnList);
    }

    public static AbstractInsnNode findAndReplace(InsnList haystack, InsnList needle, InsnList replacement)
    {
        return findAndReplace(haystack, needle, replacement, haystack.getFirst());
    }

    public static AbstractInsnNode findAndReplace(InsnList haystack, InsnList needle, InsnList replacement, AbstractInsnNode haystackStart)
    {
        AbstractInsnNode foundStart = find(haystackStart, needle);
        if (foundStart != null)
        {
            haystack.insertBefore(foundStart, cloneInsnList(replacement));
            AbstractInsnNode afterNeedle = move(foundStart, needle.size());
            removeFromInsnListUntil(haystack, foundStart, afterNeedle);
            return afterNeedle;
        }
        return null;
    }

    public static int findAndReplaceAll(InsnList haystack, InsnList needle, InsnList replacement)
    {
        return findAndReplaceAll(haystack, needle, replacement, haystack.getFirst());
    }

    public static int findAndReplaceAll(InsnList haystack, InsnList needle, InsnList replacement, AbstractInsnNode haystackStart)
    {
        int numReplaced = 0;
        while ((haystackStart = findAndReplace(haystack, needle, replacement, haystackStart)) != null)
        {
            numReplaced++;
        }
        return numReplaced;
    }

    public static InsnList cloneInsnList(InsnList source)
    {
        InsnList clone = new InsnList();

        // used to map the old labels to their cloned counterpart
        Map<LabelNode, LabelNode> labelMap = new HashMap<LabelNode, LabelNode>();

        // build the label map
        for (AbstractInsnNode instruction = source.getFirst(); instruction != null; instruction = instruction.getNext())
        {
            if (instruction instanceof LabelNode)
            {
                labelMap.put(((LabelNode)instruction), new LabelNode());
            }
        }

        for (AbstractInsnNode instruction = source.getFirst(); instruction != null; instruction = instruction.getNext())
        {
            clone.add(instruction.clone(labelMap));
        }

        return clone;
    }

    public static LocalVariableNode findLocalVariableOfMethod(MethodNode method, String varName, String varDesc)
    {
        for (LocalVariableNode localVar : method.localVariables)
        {
            if (localVar.name.equals(varName) && localVar.desc.equals(varDesc))
            {
                return localVar;
            }
        }
        return null;
    }
}
