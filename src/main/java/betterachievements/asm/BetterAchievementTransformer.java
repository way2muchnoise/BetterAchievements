package betterachievements.asm;

import betterachievements.asm.data.ClassTransformers;
import net.minecraft.launchwrapper.IClassTransformer;
import thevault.asm.Transformer;
import thevault.asm.obfuscation.ASMString;

import java.util.HashMap;
import java.util.Map;

public class BetterAchievementTransformer implements IClassTransformer
{
    private static Map<String, Transformer.ClassTransformer> classMap = new HashMap<String, Transformer.ClassTransformer>();

    static
    {
        for (ClassTransformers classTransformer : ClassTransformers.values())
            classMap.put(classTransformer.getClassName(), classTransformer.getTransformer());
        Transformer.log.info((ASMString.OBFUSCATED ? "O" : "Deo") + "bfuscated environment detected");
    }

    @Override
    public byte[] transform(String className, String className2, byte[] bytes)
    {
        Transformer.ClassTransformer clazz = classMap.get(className);
        if (clazz != null)
        {
            bytes = clazz.transform(bytes);
            classMap.remove(className);
        }
        return bytes;
    }
}
