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
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.AchievementPage;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiBetterAchievements extends GuiScreen
{
    private static final int
            buttonDone = 1, buttonOld = 2,
            guiWidth = 252, guiHeight = 175,
            tabWidth = 28, tabHeight = 32,
            mouseOffsetX = 8, mouseOffsetY = 17;
    private static final float scaleJump = 0.25F;
    private GuiScreen prevScreen;
    private StatFileWriter statFileWriter;
    private int top, left;
    private float scale;
    private int columnWidth, rowHeight; // TODO: find out if these are really needed
    private boolean newDrag;
    private int prevMouseX, prevMouseY;

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
        this.buttonList.add(new GuiOptionButton(buttonDone, this.width / 2 + 24, this.height / 2 + 78, 80, 20, I18n.format("gui.done")));
        this.buttonList.add(new GuiButton(buttonOld, this.left + 24, this.height / 2 + 78, 125, 20, I18n.format("betterachievements.gui.old")));
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
        AchievementPage page = AchievementRegistry.mcPage;
        GL11.glEnable(GL11.GL_BLEND);
        this.drawUnselectedTabs(page);
        this.mc.getTextureManager().bindTexture(Resources.GUI.SPRITES);
        this.drawTexturedModalRect(this.left, this.top + tabHeight / 2, 0, 0, guiWidth, guiHeight);
        this.drawCurrentTab(page);
        this.handleMouseInput(mouseX, mouseY, page);
        this.drawAchievementsBackground(page);
        this.drawAchievements(AchievementRegistry.instance().getAchievements(page), mouseX, mouseY);
        this.fontRendererObj.drawString(page.getName() + " " + I18n.format("gui.achievements"), this.left + 15, this.top + tabHeight / 2 + 5, 4210752);
        super.drawScreen(mouseX, mouseY, renderPartialTicks);
    }

    private void handleMouseInput(int mouseX, int mouseY, AchievementPage page)
    {
        doDrag(mouseX, mouseY);
        doZoom(page);
    }

    private void drawUnselectedTabs(AchievementPage selected)
    {
        List<AchievementPage> pages = AchievementRegistry.instance().getAllPages();
        this.mc.getTextureManager().bindTexture(Resources.GUI.TABS);
        for (int i = 0; i < 9; i++)
        {
            AchievementPage page = pages.size() > i ? pages.get(i) : null;
            if (page == selected) continue;
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int j = i * tabWidth;
            this.drawTexturedModalRect(this.left + j, this.top - 12, j, 0, tabWidth, tabHeight);
            this.drawPageIcon(page, this.left + j, this.top - 12, false);
        }
    }

    private void drawCurrentTab(AchievementPage selected)
    {
        List<AchievementPage> pages = AchievementRegistry.instance().getAllPages();
        this.mc.getTextureManager().bindTexture(Resources.GUI.TABS);
        for (int i = 0; i < 9; i++)
        {
            AchievementPage page = pages.size() > i ? pages.get(i) : null;
            if (page != selected) continue;
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int j = i * tabWidth;
            this.drawTexturedModalRect(this.left + j, this.top - 12, j, 32, tabWidth, tabHeight);
            this.drawPageIcon(page, this.left + j, this.top - 12, true);
        }
    }

    private void drawPageIcon(AchievementPage page, int tabLeft, int tabTop, boolean selected)
    {
        ItemStack itemStack = AchievementRegistry.instance().getItemStack(page);
        if (itemStack!= null)
        {
            this.zLevel = 100.0F;
            itemRender.zLevel = 100.0F;
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemStack, tabLeft + 6, tabTop + 9);
            itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemStack, tabLeft + 6, tabTop + 9);
            itemRender.zLevel = 0.0F;
            GL11.glDisable(GL11.GL_LIGHTING);
            this.zLevel = 0.0F;
        }
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

    private void doZoom(AchievementPage page)
    {
        int dWheel = Mouse.getDWheel();
        float prevScale = this.scale;

        if (dWheel < 0)
            this.scale += scaleJump;
        else if (dWheel > 0)
            this.scale -= scaleJump;

        float minZoom = page instanceof IBetterAchievementPage ? ((IBetterAchievementPage) page).getMinZoom() : 1.0F;
        float maxZoom = page instanceof IBetterAchievementPage ? ((IBetterAchievementPage) page).getMaxZoom() : 2.0F;
        this.scale = MathHelper.clamp_float(this.scale, minZoom, maxZoom);

        if (this.scale != prevScale)
        {
            // Recalculate things here
        }
    }

    private void doDrag(int mouseX, int mouseY)
    {
        if (Mouse.isButtonDown(0))
        {
            if (mouseX > this.left + mouseOffsetX
                && mouseX < this.left + guiWidth - mouseOffsetX
                && mouseY >= this.top + mouseOffsetY
                && mouseY > this.top + guiHeight - mouseOffsetY)
            {
                if (this.newDrag)
                {
                    this.newDrag = false;
                }
                else
                {
                    // Handle drag
                }

                this.prevMouseX = mouseX;
                this.prevMouseY = mouseY;
            }
        }
        else
        {
            this.newDrag = true;
        }
    }
}
