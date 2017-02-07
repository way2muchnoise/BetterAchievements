package betterachievements.gui;


import betterachievements.api.components.achievement.ICustomTitle;
import betterachievements.api.util.ColourHelper;
import betterachievements.reference.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import java.util.*;

public class GuiBetterAchievement extends GuiAchievement
{
    public static boolean showModName = true;
    public static int modNameColour = ColourHelper.RGB("#5555FF");

    private Minecraft mc;
    private int width;
    private int height;
    private String achievementTitle;
    private String achievementDescription;
    private String achievementPageName;
    private Achievement theAchievement;
    private long notificationTime;
    private RenderItem renderItem;
    private boolean permanentNotification;

    private Queue<Achievement> queue;

    public GuiBetterAchievement(Minecraft mc) {
        super(mc);
        this.mc = mc;
        this.renderItem = mc.getRenderItem();
        this.queue = new LinkedList<>();
    }

    @Override
    public void displayAchievement(Achievement achievement)
    {
        if (notificationTime == 0 || permanentNotification)
        {
            achievementTitle = achievement instanceof ICustomTitle ? ((ICustomTitle) achievement).getTitle() : I18n.format("achievement.get");
            achievementDescription = achievement.getStatName().getUnformattedText();
            notificationTime = Minecraft.getSystemTime();
            theAchievement = achievement;
            AchievementPage page = getPageOfAchievement(achievement);
            achievementPageName = page != null ? page.getName(): "Minecraft";
            permanentNotification = false;
        }
        else
        {
            queue.add(achievement);
        }
    }

    @Override
    public void displayUnformattedAchievement(Achievement achievement)
    {
        achievementTitle = achievement.getStatName().getUnformattedText();
        achievementDescription = achievement.getDescription();
        notificationTime = Minecraft.getSystemTime() + 2500L;
        theAchievement = achievement;
        permanentNotification = true;
    }

    private void updateAchievementWindowScale()
    {
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        width = mc.displayWidth;
        height = mc.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        width = scaledresolution.getScaledWidth();
        height = scaledresolution.getScaledHeight();
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, (double)width, (double)height, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
    }

    @Override
    public void updateAchievementWindow()
    {
        if (theAchievement != null && notificationTime != 0L && Minecraft.getMinecraft().player != null)
        {
            // When there is more then 1 achievement show them faster
            double d0 = (double)(Minecraft.getSystemTime() - notificationTime) / (queue.isEmpty() ? 3000.0D: 2000.0D);

            if (!permanentNotification)
            {
                if (d0 < 0.0D || d0 > 1.0D)
                {
                    if (!queue.isEmpty())
                    {
                        Achievement achievement = queue.poll();
                        achievementTitle = I18n.format("achievement.get");
                        achievementDescription = achievement.getStatName().getUnformattedText();
                        notificationTime = Minecraft.getSystemTime();
                        theAchievement = achievement;
                        AchievementPage page = getPageOfAchievement(achievement);
                        achievementPageName = page != null ? page.getName(): "Minecraft";
                        permanentNotification = false;
                    }
                    else
                    {
                        notificationTime = 0L;
                    }
                    return;
                }
            }
            else if (d0 > 0.5D)
            {
                d0 = 0.5D;
            }

            updateAchievementWindowScale();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            double d1 = d0 * 2.0D;

            if (d1 > 1.0D) d1 = 2.0D - d1;

            d1 = d1 * 4.0D;
            d1 = 1.0D - d1;

            if (d1 < 0.0D) d1 = 0.0D;

            d1 = d1 * d1;
            d1 = d1 * d1;
            int i = width - 160;
            int j = 0 - (int)(d1 * (permanentNotification ? 36.0D: 43.0D));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableTexture2D();
            mc.getTextureManager().bindTexture(Resources.GUI.SPRITES);
            GlStateManager.disableLighting();

            if (permanentNotification)
            {
                drawTexturedModalRect(i, j, 96, 202, 160, 32);
                mc.fontRendererObj.drawSplitString(achievementDescription, i + 30, j + 7, 120, -1);
            }
            else
            {
                drawTexturedModalRect(i, j, 96, 202, 160, 28);
                if (GuiBetterAchievement.showModName)
                    drawTexturedModalRect(i, j + 28, 96, 207, 160, 11);
                drawTexturedModalRect(i, j + (GuiBetterAchievement.showModName ? 39 : 28), 96, 230, 160, 4);
                mc.fontRendererObj.drawString(achievementTitle, i + 30, j + 7, -256);
                mc.fontRendererObj.drawString(achievementDescription, i + 30, j + 18, -1);
                if (GuiBetterAchievement.showModName) {
                    String s = mc.fontRendererObj.getStringWidth(achievementPageName) > 120 ? mc.fontRendererObj.trimStringToWidth(achievementPageName, 120) + "..." : achievementPageName;
                    mc.fontRendererObj.drawString(s, i + 30, j + 29, GuiBetterAchievement.modNameColour);
                }
            }


            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            renderItem.renderItemAndEffectIntoGUI(theAchievement.theItemStack, i + 8, j + (permanentNotification ? 8: 14) - (GuiBetterAchievement.showModName ? 0 : 6));
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
        }
    }

    public AchievementPage getPageOfAchievement(Achievement achievement)
    {
        for (AchievementPage page : AchievementPage.getAchievementPages())
            if (page.getAchievements().contains(achievement))
                return page;
        return null;
    }
}
