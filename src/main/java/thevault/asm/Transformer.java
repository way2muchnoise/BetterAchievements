package thevault.asm;

import thevault.asm.obfuscation.ASMString;
import thevault.reference.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class Transformer<T> implements ITransformer, Opcodes
{
    public static enum Access
    {
        PUBLIC(ACC_PUBLIC), PRIVATE(ACC_PRIVATE), PROTECTED(ACC_PROTECTED);
        public static int clearAccess = ~(ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED);
        private int val;
        Access(int val)
        {
            this.val = val;
        }

        public int modifyAccess(int access)
        {
            return (access & clearAccess) ^ val;
        }
    }

    public static int NODE_MODIFY = 0x1;
    public static int NODE_ACCESS = 0x2;
    public static int NODE_DELETE = 0x4;
    public static int NODE_ADD = 0x8;

    public static Logger log = LogManager.getLogger(Reference.ID+"-ASM");

    protected Access access;
    protected int action;
    protected ClassNode classNode;
    protected ASMString name;

    protected Transformer()
    {
        this(NODE_MODIFY);
    }

    protected Transformer(int action)
    {
        this(action, Access.PUBLIC);
    }

    protected Transformer(int action, Access access)
    {
        this.action = action;
        this.access = access;
    }

    @Override
    public boolean transform(ClassNode classNode)
    {
        this.classNode = classNode;
        T node = find();
        if (node != null)
        {
            if ((action & NODE_MODIFY) > 0) modify(node);
            if ((action & NODE_ACCESS) > 0) changeAccess(node);
            if ((action & NODE_DELETE) > 0) delete(node);
            if ((action & NODE_ADD) > 0) add();
            return true;
        }
        return false;
    }

    protected abstract T find();

    protected abstract void changeAccess(T node);

    protected abstract void modify(T node);

    protected abstract void delete(T node);

    protected abstract void add();

    protected abstract T getNodeToAdd();

    protected abstract void log();

    public static class ClassTransformer extends Transformer<ClassNode>
    {
        private Transformer[] transformers;

        public ClassTransformer(String name, Transformer... transformers)
        {
            this(new ASMString(name), transformers);
        }

        public ClassTransformer(String name, String obfName, Transformer... transformers)
        {
            this(new ASMString.ASMObfString(name, obfName), transformers);
        }

        public ClassTransformer(ASMString name, Transformer... transformers)
        {
            super(NODE_MODIFY);
            this.name = name;
            this.transformers = transformers;
        }

        public String getClassName()
        {
            return name.getText();
        }

        @Override
        protected ClassNode find()
        {
            return classNode;
        }

        @Override
        protected void changeAccess(ClassNode node)
        {
            classNode.access = access.modifyAccess(classNode.access);
        }

        @Override
        protected void modify(ClassNode node)
        {
            for (Transformer transformer : transformers)
            {
                if (transformer.transform(node)) transformer.log();
            }
        }

        @Override
        protected void delete(ClassNode node)
        {
        }

        @Override
        protected void add()
        {
        }

        @Override
        protected ClassNode getNodeToAdd()
        {
            return null;
        }

        @Override
        protected void log()
        {
            log.info("Transforming Class: " + name.getReadableText());
        }
    }

    public static class FieldTransformer extends Transformer<FieldNode>
    {
        protected String fieldDesc;
        
        public FieldTransformer(String name, String desc)
        {
            this(new ASMString(name), desc);
        }
        
        public FieldTransformer(String name, String obfName, String desc)
        {
            this(new ASMString.ASMObfString(name, obfName), desc);
        }
        
        public FieldTransformer(ASMString name, String desc)
        {
            this(NODE_ACCESS, Access.PUBLIC, name, desc);
        }
        
        public FieldTransformer(int action, Access access, ASMString name, String desc)
        {
            super(action, access);
            this.name = name;
            this.fieldDesc = desc;
        }

        @Override
        protected FieldNode find()
        {
            String fieldName = this.name.getText();
            for (FieldNode field : classNode.fields)
            {
                if (field.name.equals(fieldName) && field.desc.equals(fieldDesc))
                {
                    return field;
                }
            }
            return null;
        }

        @Override
        protected void changeAccess(FieldNode node)
        {
            node.access = access.modifyAccess(node.access);
        }

        @Override
        protected void modify(FieldNode node)
        {
        }

        @Override
        protected void delete(FieldNode node)
        {
            classNode.fields.remove(node);
        }

        @Override
        protected void add()
        {
            classNode.fields.add(getNodeToAdd());
        }

        @Override
        protected FieldNode getNodeToAdd()
        {
            return null;
        }

        @Override
        protected void log()
        {
            log.info("Transformed Field " + name.getReadableText());
        }
    }
    
    public static class MethodTransformer extends Transformer<MethodNode>
    {
        protected String methodDesc;

        public MethodTransformer(String name, String desc)
        {
            this(new ASMString(name), desc);
        }

        public MethodTransformer(String name, String obfName, String desc)
        {
            this(new ASMString.ASMObfString(name, obfName), desc);
        }

        public MethodTransformer(ASMString name, String desc)
        {
            this(NODE_ACCESS, Access.PUBLIC, name, desc);
        }

        public MethodTransformer(int action, ASMString name, String desc)
        {
            this(action, Access.PUBLIC, name, desc);
        }

        public MethodTransformer(int action, Access access, ASMString name, String desc)
        {
            super(action, access);
            this.name = name;
            this.methodDesc = desc;
        }
        
        @Override
        protected MethodNode find()
        {
            String name = this.name.getText();
            for (MethodNode method : classNode.methods)
            {
                if (method.name.equals(name) && method.desc.equals(methodDesc))
                {
                    return method;
                }
            }
            return null;
        }

        @Override
        protected void changeAccess(MethodNode node)
        {
            node.access = access.modifyAccess(node.access);
        }

        @Override
        protected void modify(MethodNode node)
        {
        }

        @Override
        protected void delete(MethodNode node)
        {
            classNode.methods.remove(node);
        }

        @Override
        protected void add()
        {
            classNode.methods.add(getNodeToAdd());
        }

        @Override
        protected MethodNode getNodeToAdd()
        {
            return null;
        }

        @Override
        protected void log()
        {
            log.info("Transformed Method: " + name.getReadableText());
        }
    }

    public static class InnerClassTransformer extends Transformer<InnerClassNode>
    {
        @Override
        protected InnerClassNode find()
        {
            String name = classNode.name + "$" + this.name.getText();
            for (InnerClassNode inner : classNode.innerClasses)
            {
                if (name.equals(inner.name))
                {
                    return inner;
                }
            }
            return null;
        }

        @Override
        protected void changeAccess(InnerClassNode node)
        {
            node.access = access.modifyAccess(node.access);
        }

        @Override
        protected void modify(InnerClassNode node)
        {
        }

        @Override
        protected void delete(InnerClassNode node)
        {
            classNode.innerClasses.remove(node);
        }

        @Override
        protected void add()
        {
            classNode.innerClasses.add(getNodeToAdd());
        }

        @Override
        protected InnerClassNode getNodeToAdd()
        {
            return null;
        }

        @Override
        protected void log()
        {
            log.info("Transformed Inner Class: " + name.getReadableText());
        }
    }
}
