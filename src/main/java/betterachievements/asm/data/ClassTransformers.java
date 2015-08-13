package betterachievements.asm.data;

import thevault.asm.Transformer;

public enum ClassTransformers
{
    /**
     * Replace instantiations of {@link net.minecraft.client.gui.achievement.GuiAchievements}
     * To Transform classes
     * {@link net.minecraft.client.gui.inventory.GuiInventory} line 130
     * {@link net.minecraft.client.gui.inventory.GuiContainerCreative} line 979
     * {@link net.minecraft.client.gui.GuiIngameMenu} line 66
     * TODO: look at byte code and replace instantiations with own GuiAchievements
     * Also take a look at {@link net.minecraft.client.gui.achievement.GuiAchievement}
     * if that needs replacement
     */
    GUI_INVENTORY(null),
    GUI_CONTAINER_CREATIVE(null),
    GUI_INGAME_MENU(null);

    private Transformer.ClassTransformer transformer;

    ClassTransformers(Transformer.ClassTransformer transformer)
    {
        this.transformer = transformer;
    }

    public Transformer.ClassTransformer getTransformer()
    {
        return null;
    }

    public String getClassName()
    {
        return transformer.getClassName();
    }
}
