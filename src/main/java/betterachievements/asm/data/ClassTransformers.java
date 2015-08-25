package betterachievements.asm.data;

import thevault.asm.Transformer;

public enum ClassTransformers
{
    ACHIEVEMENT(new Transformer.ClassTransformer(ASMStrings.ACHIEVEMENT, MethodTransformers.registerStat)),
    GUI_INVENTORY(new Transformer.ClassTransformer(ASMStrings.GUI_INVENTORY, MethodTransformers.actionPerformed)),
    GUI_CONTAINER_CREATIVE(new Transformer.ClassTransformer(ASMStrings.GUI_CONTAINER_CREATIVE, MethodTransformers.actionPerformed)),
    GUI_INGAME_MENU(new Transformer.ClassTransformer(ASMStrings.GUI_INGAME_MENU, MethodTransformers.actionPerformed));
    // TODO: take a look at {@link net.minecraft.client.gui.achievement.GuiAchievement} if that needs replacement

    private Transformer.ClassTransformer transformer;

    ClassTransformers(Transformer.ClassTransformer transformer)
    {
        this.transformer = transformer;
    }

    public Transformer.ClassTransformer getTransformer()
    {
        return this.transformer;
    }

    public String getClassName()
    {
        return transformer.getClassName();
    }
}
