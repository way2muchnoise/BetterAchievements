package betterachievements.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class RenderHelper
{
    public static TextureAtlasSprite getIcon(Block block)
    {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block.getDefaultState());
    }

    public static RenderItem getRenderItem()
    {
        return Minecraft.getMinecraft().getRenderItem();
    }
}
