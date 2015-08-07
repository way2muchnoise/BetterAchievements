package thevault.asm.obfuscation;

public class ASMString
{
    public static boolean OBFUSCATED;
    private String text;

    public ASMString(String text)
    {
        this.text = text;
    }

    public ASMString(Class clazz)
    {
        this.text = clazz.getCanonicalName();
    }

    public String getText()
    {
        return text;
    }

    public String getReadableText()
    {
        return text;
    }

    public String getASMClassName()
    {
        return text.replaceAll("\\.","/");
    }

    public String getASMTypeName()
    {
        return "L" + getASMClassName() +";";
    }

    public static class ASMObfString extends ASMString
    {
        private String obfText;
        public ASMObfString(String text, String obfText)
        {
            super(text);
            this.obfText = obfText;
        }

        @Override
        public String getText()
        {
            return OBFUSCATED ? obfText : super.getText();
        }
    }
}
