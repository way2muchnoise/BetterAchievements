package thevault.asm.obfuscation;

public class ASMString
{
    public static boolean OBFUSCATED;
    private String text;
    private String obfText;

    public ASMString(String text)
    {
        this(text, text);
    }

    public ASMString(Class clazz)
    {
        this(clazz.getCanonicalName());
    }

    public ASMString(String text, String obfText)
    {
        this.text = text;
        this.obfText = obfText;
    }

    public String getText()
    {
        return OBFUSCATED ? obfText : text;
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
}
