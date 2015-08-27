package betterachievements.gui;

import betterachievements.api.IBetterAchievementPage;
import betterachievements.reference.Resources;
import betterachievements.registry.AchievementRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatFileWriter;
import net.minecraftforge.common.AchievementPage;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiBetterAchievements extends GuiScreen
{
    private static final int buttonDone = 1, buttonOld = 2;
    private static final int guiWidth = 256, guiHeight = 202;
    private GuiScreen prevScreen;
    private StatFileWriter statFileWriter;
    private int top, left;
    private float scale;
    private int columnWidth, rowHeight; // TODO: find out if these are really needed

    public GuiBetterAchievements(GuiScreen currentScreen, StatFileWriter statFileWriter)
    {
        this.prevScreen = currentScreen;
        this.statFileWriter = statFileWriter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        this.left = (this.width - guiWidth) / 2;
        this.top = (this.height - guiHeight) / 2;
        this.scale = 1.0F;
        this.buttonList.clear();
        this.buttonList.add(new GuiOptionButton(buttonDone, this.width / 2 + 24, this.height / 2 + 74, 80, 20, I18n.format("gui.done")));
        this.buttonList.add(new GuiButton(buttonOld, this.left + 24, this.height / 2 + 74, 125, 20, I18n.format("betterachievements.gui.old")));
    }

    @Override
    protected void keyTyped(char c, int i)
    {
        if (i == this.mc.gameSettings.keyBindInventory.getKeyCode())
        {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }
        else
        {
            super.keyTyped(c, i);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case buttonOld:
                this.mc.displayGuiScreen(new GuiAchievements(this.prevScreen, this.statFileWriter));
                break;
            case buttonDone:
                this.mc.displayGuiScreen(this.prevScreen);
                break;
            default:
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks)
    {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(Resources.GUI.SPRITES);
        this.drawTexturedModalRect(this.left, this.top, 0, 0, guiWidth, guiHeight);
        this.drawTabs();
        AchievementPage page = AchievementRegistry.mcPage;
        this.drawAchievementsBackground(page);
        this.drawAchievements(AchievementRegistry.instance().getAchievements(page), mouseX, mouseY);
        this.fontRendererObj.drawString(I18n.format("gui.achievements"), this.left + 15, this.top + 5, 4210752);
        super.drawScreen(mouseX, mouseY, renderPartialTicks);
    }

    private void drawTabs()
    {
        //Draw all tabs
    }

    private void drawAchievementsBackground(AchievementPage page)
    {
        if (page instanceof IBetterAchievementPage)
            ((IBetterAchievementPage) page).drawBackground(this.columnWidth, this.rowHeight, this.zLevel, this.scale);
        else
        {
            // Do default background rendering
        }
    }

    private void drawAchievements(List<Achievement> achievements, int mouseX, int mouseY)
    {
        for (Achievement achievement : achievements)
        {
            // Get colour for achievement background
            // Render icon
            drawMouseOver(achievement, mouseX, mouseY);
        }
    }

    private void drawMouseOver(Achievement achievement, int mouseX, int mouseY)
    {

    }
}
