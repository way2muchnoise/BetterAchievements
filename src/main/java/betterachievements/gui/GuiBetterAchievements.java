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
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.AchievementPage;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class GuiBetterAchievements extends GuiScreen
{
    private static final int blockSize = 16,
            buttonDone = 1, buttonOld = 2,
            buttonOffsetX = 24, buttonOffsetY = 92,
            guiWidth = 252, guiHeight = 202,
            tabWidth = 28, tabHeight = 32,
            borderWidthX = 8, borderWidthY = 17,
            tabOffsetX =  0, tabOffsetY = -12,
            innerWidth = 228, innerHeight = 158,
            minDisplayColumn = AchievementList.minDisplayColumn * blockSize - 10 * blockSize,
            minDisplayRow = AchievementList.minDisplayRow * blockSize - 10 * blockSize,
            maxDisplayColumn = AchievementList.maxDisplayColumn * blockSize + 10 * blockSize,
            maxDisplayRow = AchievementList.maxDisplayRow * blockSize + 10 * blockSize;
    private static final float scaleJump = 0.25F,
            minZoom = 1.0F, maxZoom = 2.0F;
    private static final Random random = new Random();
    private GuiScreen prevScreen;
    private StatFileWriter statFileWriter;
    private int top, left;
    private float scale;
    private boolean newDrag;
    private int prevMouseX, prevMouseY;
    private List<AchievementPage> pages;
    private int currentPage;
    private int xPos, yPos;

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
        this.buttonList.add(new GuiOptionButton(buttonDone, this.width / 2 + buttonOffsetX, this.height / 2 + buttonOffsetY, 80, 20, I18n.format("gui.done")));
        this.buttonList.add(new GuiButton(buttonOld, this.left + buttonOffsetX, this.height / 2 + buttonOffsetY, 125, 20, I18n.format("betterachievements.gui.old")));
        this.pages = AchievementRegistry.instance().getAllPages();
        this.currentPage = 0;
        this.xPos = 0;
        this.yPos = 0;
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
        AchievementPage page = this.pages.get(this.currentPage);
        this.handleMouseInput(mouseX, mouseY, page);
        this.drawUnselectedTabs(page);
        this.drawAchievementsBackground(page);
        GL11.glEnable(GL11.GL_BLEND);
        this.drawAchievements(page, mouseX, mouseY);
        this.mc.getTextureManager().bindTexture(Resources.GUI.SPRITES);
        this.drawTexturedModalRect(this.left, this.top + tabHeight / 2, 0, 0, guiWidth, guiHeight);
        this.drawCurrentTab(page);
        this.fontRendererObj.drawString(page.getName() + " " + I18n.format("gui.achievements"), this.left + 15, this.top + tabHeight / 2 + 5, 4210752);
        super.drawScreen(mouseX, mouseY, renderPartialTicks);
        this.drawMouseOverTab(mouseX, mouseY);
    }

    private void handleMouseInput(int mouseX, int mouseY, AchievementPage page)
    {
        doDrag(mouseX, mouseY);
        doZoom(page);
        if (this.xPos < minDisplayColumn)
            this.xPos = minDisplayColumn;
        if (this.xPos > maxDisplayColumn)
            this.xPos = maxDisplayColumn;
        if (this.yPos < minDisplayRow)
            this.yPos = minDisplayRow;
        if (this.yPos > maxDisplayRow)
            this.yPos = maxDisplayRow;
        if (Mouse.isButtonDown(0))
            handleMouseClick(mouseX, mouseY);
    }

    private void drawUnselectedTabs(AchievementPage selected)
    {
        for (int i = 0; i < 9 && this.pages.size() > i; i++)
        {
            AchievementPage page = this.pages.get(i);
            if (page == selected) continue;
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int j = i * tabWidth;
            this.mc.getTextureManager().bindTexture(Resources.GUI.TABS);
            this.drawTexturedModalRect(this.left + tabOffsetX + j, this.top + tabOffsetY, j, 0, tabWidth, tabHeight);
            this.drawPageIcon(page, this.left + tabOffsetX + j, this.top + tabOffsetY);
        }
    }

    private void drawCurrentTab(AchievementPage selected)
    {
        for (int i = 0; i < 9; i++)
        {
            AchievementPage page = this.pages.size() > i ? this.pages.get(i) : null;
            if (page != selected) continue;
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int j = i * tabWidth;
            this.mc.getTextureManager().bindTexture(Resources.GUI.TABS);
            this.drawTexturedModalRect(this.left + tabOffsetX + j, this.top + tabOffsetY, j, 32, tabWidth, tabHeight);
            this.drawPageIcon(page, this.left + tabOffsetX + j, this.top + tabOffsetY);
        }
    }

    private void drawPageIcon(AchievementPage page, int tabLeft, int tabTop)
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
            GL11.glEnable(GL11.GL_BLEND);
            this.zLevel = 0.0F;
        }
    }

    private void drawAchievementsBackground(AchievementPage page)
    {
        if (page instanceof IBetterAchievementPage && ((IBetterAchievementPage) page).hasCustomBackGround())
            ((IBetterAchievementPage) page).drawBackground(this.left + borderWidthX, this.top + borderWidthY, this.zLevel, this.scale);
        else
        {
            GL11.glDepthFunc(GL11.GL_GEQUAL);
            GL11.glPushMatrix();
            float scaleInverse = 1.0F / this.scale;
            GL11.glTranslatef(this.left, this.top + borderWidthY, -200.0F);
            GL11.glScalef(scaleInverse, scaleInverse, 1.0F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            float scale = blockSize / this.scale;
            int dragX = this.xPos - minDisplayColumn >> 4;
            int dragY = this.yPos - minDisplayRow >> 4;
            float darkness = 0.7F - dragY / 80.0F;
            GL11.glColor4f(darkness, darkness, darkness, 1.0F);
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            for (int x = 1; x * scale < innerWidth + borderWidthX; x++)
            {
                for (int y = 1; y * scale < innerHeight + borderWidthY * 2; y++)
                {
                    random.setSeed(this.mc.getSession().getPlayerID().hashCode() + dragY + y + (dragX + x) * 16);
                    int r = random.nextInt(1 + dragY + y) + (dragY + y) / 2;
                    IIcon block = Blocks.grass.getIcon(0, 0);
                    if (r == 40)
                    {
                        if (random.nextInt(3) == 0)
                            block = Blocks.diamond_ore.getIcon(0, 0);
                        else
                            block = Blocks.redstone_ore.getIcon(0, 0);
                    }
                    else if (r == 20)
                        block = Blocks.iron_ore.getIcon(0, 0);
                    else if (r == 12)
                        block = Blocks.coal_ore.getIcon(0, 0);
                    else if (r > 70)
                        block = Blocks.bedrock.getIcon(0, 0);
                    else if (r > 4)
                        block = Blocks.stone.getIcon(0, 0);
                    else if (r > 0)
                        block = Blocks.dirt.getIcon(0, 0);

                    this.drawTexturedModelRectFromIcon(x * blockSize, y * blockSize, block, blockSize, blockSize);
                }
            }
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    private void drawAchievements(AchievementPage page, int mouseX, int mouseY)
    {
        List<Achievement> achievements = AchievementRegistry.instance().getAchievements(page);
        for (Achievement achievement : achievements)
        {
            // Get colour for achievement background
            // Render icon
            drawMouseOverAchievement(achievement, mouseX, mouseY);
        }
    }

    private void drawMouseOverAchievement(Achievement achievement, int mouseX, int mouseY)
    {

    }

    private void drawMouseOverTab(int mouseX, int mouseY)
    {
        int onTab = onTab(mouseX, mouseY);
        if (onTab == -1 || this.pages.size() <= onTab) return;
        AchievementPage page = pages.get(onTab);
        List<String> tooltip = new LinkedList<String>();
        tooltip.add(page.getName());
        drawHoveringText(tooltip, mouseX, mouseY, this.fontRendererObj);
    }

    private void handleMouseClick(int mouseX, int mouseY)
    {
        int onTab = onTab(mouseX, mouseY);
        if (onTab == -1 || this.pages.size() <= onTab || this.currentPage == onTab) return;
        this.currentPage = onTab;
    }

    private void doZoom(AchievementPage page)
    {
        int dWheel = Mouse.getDWheel();
        float prevScale = this.scale;

        if (dWheel < 0)
            this.scale += scaleJump;
        else if (dWheel > 0)
            this.scale -= scaleJump;

        float minZoom = page instanceof IBetterAchievementPage ? ((IBetterAchievementPage) page).getMinZoom() : GuiBetterAchievements.minZoom;
        float maxZoom = page instanceof IBetterAchievementPage ? ((IBetterAchievementPage) page).getMaxZoom() : GuiBetterAchievements.maxZoom;
        this.scale = MathHelper.clamp_float(this.scale, minZoom, maxZoom);

        if (this.scale != prevScale)
        {
            float prevScaledWidth = prevScale * this.width;
            float prevScaledHeight = prevScale * this.height;
            float newScaledWidth = this.scale * this.width;
            float newScaledHeight = this.scale * this.height;
            this.xPos -= (newScaledWidth - prevScaledWidth) / 2;
            this.yPos -= (newScaledHeight - prevScaledHeight) / 2;
        }
    }

    private void doDrag(int mouseX, int mouseY)
    {
        if (Mouse.isButtonDown(0))
        {
            if (inInnerScreen(mouseX, mouseY))
            {
                if (this.newDrag)
                {
                    this.newDrag = false;
                }
                else
                {
                    this.xPos -= (mouseX - this.prevMouseX)*scale;
                    this.yPos -= (mouseY - this.prevMouseY)*scale;
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

    private boolean inInnerScreen(int mouseX, int mouseY)
    {
        return mouseX > this.left + borderWidthX
                && mouseX < this.left + guiWidth - borderWidthX
                && mouseY > this.top + borderWidthY
                && mouseY < this.top + guiHeight - borderWidthY;
    }

    /**
     * Get the index of the tab the mouse is on
     *
     * @param mouseX x coord of the mouse
     * @param mouseY y coord of the mouse
     * @return -1 if not on a tab otherwise the index
     */
    private int onTab(int mouseX, int mouseY)
    {
        if (mouseX > this.left + tabOffsetX
                && mouseX < this.left + guiWidth
                && mouseY > this.top + tabOffsetY
                && mouseY < this.top + tabOffsetY + tabHeight)
        {
            return (mouseX - (this.left + tabOffsetX)) / tabWidth;
        }
        return -1;
    }
}
