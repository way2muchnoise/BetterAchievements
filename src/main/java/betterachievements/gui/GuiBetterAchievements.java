package betterachievements.gui;

import betterachievements.api.components.achievement.ICustomBackgroundColour;
import betterachievements.api.components.achievement.ICustomIconRenderer;
import betterachievements.api.components.achievement.ICustomTooltip;
import betterachievements.api.components.page.ICustomArrows;
import betterachievements.api.components.page.ICustomBackground;
import betterachievements.api.components.page.ICustomPosition;
import betterachievements.api.components.page.ICustomScale;
import betterachievements.api.util.ColourHelper;
import betterachievements.handler.MessageHandler;
import betterachievements.handler.message.AchievementUnlockMessage;
import betterachievements.reference.Resources;
import betterachievements.registry.AchievementRegistry;
import betterachievements.util.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.AchievementPage;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class GuiBetterAchievements extends GuiScreen
{
    private static final int blockSize = 16, maxTabs = 9,
            lineSize = 12, defaultTooltipWidth = 120,
            arrowHeadWidth = 11, arrowHeadHeight = 7, arrowOffset = 5,
            arrowRightX = 114, arrowRightY = 234,
            arrowLeftX = 107, arrowLeftY = 234,
            arrowDownX = 96, arrowDownY = 234,
            arrowUpX = 96, arrowUpY = 241,
            achievementX = 0, achievementY = 202,
            achievementTooltipOffset = 3,
            achievementTextureSize = 26, achievementOffset = 2,
            achievementSize = 24, achievementInnerSize = 22,
            buttonDone = 1, buttonOld = 2,
            buttonPrev = 3, buttonNext = 4,
            buttonOffsetX = 24, buttonOffsetY = 92,
            guiWidth = 252, guiHeight = 202,
            tabWidth = 28, tabHeight = 32,
            borderWidthX = 8, borderWidthY = 17,
            tabOffsetX =  0, tabOffsetY = -12,
            innerWidth = 228, innerHeight = 158,
            minDisplayColumn = AchievementList.minDisplayColumn * achievementSize - 10 * achievementSize,
            minDisplayRow = AchievementList.minDisplayRow * achievementSize - 10 * achievementSize,
            maxDisplayColumn = AchievementList.maxDisplayColumn * achievementSize,
            maxDisplayRow = AchievementList.maxDisplayRow * achievementSize;
    private static final float scaleJump = 0.25F,
            minZoom = 1.0F, maxZoom = 2.0F;
    private static final Random random = new Random();
    public static int colourUnlocked, colourCanUnlock, colourCantUnlock;
    public static boolean scrollButtons, iconReset, userColourOverride,
            colourUnlockedRainbow, colourCanUnlockRainbow, colourCantUnlockRainbow;
    public static float[] colourUnlockedRainbowSettings, colourCanUnlockRainbowSettings, colourCantUnlockRainbowSettings;
    private GuiScreen prevScreen;
    private StatFileWriter statFileWriter;
    private int top, left;
    private float scale;
    private boolean pause, newDrag;
    private int prevMouseX, prevMouseY;
    private List<AchievementPage> pages;
    private int currentPage, tabsOffset;
    private static int lastPage = 0;
    private int xPos, yPos;
    private Achievement hoveredAchievement;

    public GuiBetterAchievements(GuiScreen currentScreen, int page)
    {
        this.prevScreen = currentScreen;
        this.currentPage = page == 0 ? lastPage : page;
        this.statFileWriter = Minecraft.getMinecraft().thePlayer.getStatFileWriter();
        this.pause = true;
    }
    
    @Override
    public void initGui()
    {
        this.left = (this.width - guiWidth) / 2;
        this.top = (this.height - guiHeight) / 2;
        this.scale = 1.0F;
        this.xPos = achievementSize*3;
        this.yPos = achievementSize;

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(buttonDone, this.width / 2 + buttonOffsetX, this.height / 2 + buttonOffsetY, 80, 20, I18n.format("gui.done")));
        this.buttonList.add(new GuiButton(buttonOld, this.left + buttonOffsetX, this.height / 2 + buttonOffsetY, 125, 20, I18n.format("betterachievements.gui.old")));
        if (scrollButtons)
        {
            this.buttonList.add(new GuiButton(buttonPrev, this.left - 24, this.top - 5, 20, 20, "<"));
            this.buttonList.add(new GuiButton(buttonNext, this.left + 256, this.top - 5, 20, 20, ">"));
        }

        this.hoveredAchievement = null;
        this.pages = AchievementRegistry.instance().getAllPages();
        this.tabsOffset = this.currentPage < maxTabs/3*2 ? 0 : this.currentPage - maxTabs/3*2;
        if (this.tabsOffset < 0 )
            this.tabsOffset = 0;

        AchievementPage page = this.pages.get(this.currentPage);
        if (page instanceof ICustomScale)
            this.scale = ((ICustomScale) page).setScale();

        if (page instanceof ICustomPosition)
        {
            Achievement center = ((ICustomPosition) page).setPositionOnLoad();
            this.xPos = center.displayColumn * achievementSize + achievementSize * 3;
            this.yPos = center.displayRow * achievementSize + achievementSize;
        }
    }

    public void onGuiClosed()
    {
        lastPage = this.currentPage;
    }

    @Override
    protected void keyTyped(char c, int i) throws IOException
    {
        if (i == this.mc.gameSettings.keyBindInventory.getKeyCode())
        {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }
        else if (i == Keyboard.KEY_LEFT)
        {
            this.currentPage--;
            if (currentPage < 0)
            {
                this.currentPage = this.pages.size() - 1;
                this.tabsOffset += (this.pages.size() / maxTabs) * maxTabs;
            }
            if (this.currentPage - this.tabsOffset < 0)
                this.tabsOffset -= maxTabs;
            if(this.tabsOffset < 0)
                this.tabsOffset = 0;
        }
        else if (i == Keyboard.KEY_RIGHT)
        {
            this.currentPage++;
            if (this.currentPage >= this.pages.size())
            {
                this.currentPage = 0;
                this.tabsOffset = 0;
            }
            if (this.currentPage - this.tabsOffset >= maxTabs)
                this.tabsOffset += maxTabs;
            if (this.pages.size() <= this.tabsOffset)
                this.tabsOffset = this.pages.size() - 1;
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
                this.mc.displayGuiScreen(new GuiAchievementsOld(this.prevScreen, this.statFileWriter));
                break;
            case buttonDone:
                this.mc.displayGuiScreen(this.prevScreen);
                break;
            case buttonPrev:
                this.tabsOffset -= maxTabs;
                if (this.tabsOffset == -maxTabs) this.tabsOffset = this.pages.size() - maxTabs / 3 * 2;
                else if (this.tabsOffset < 0) this.tabsOffset = 0;
                break;
            case buttonNext:
                this.tabsOffset += maxTabs;
                if (this.tabsOffset > this.pages.size())
                    this.tabsOffset = 0;
                else if (this.tabsOffset > this.pages.size() - maxTabs / 3 * 2)
                    this.tabsOffset = this.pages.size() - maxTabs / 3 * 2;
                break;
            default:
                break;
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return this.pause;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks)
    {
        this.drawDefaultBackground();
        AchievementPage page = this.pages.get(this.currentPage);
        this.handleMouseInput(mouseX, mouseY, page);
        this.drawUnselectedTabs(page);
        GlStateManager.depthFunc(GL11.GL_GEQUAL);
        GlStateManager.pushMatrix();
        this.drawAchievementsBackground(page);
        this.drawAchievements(page, mouseX, mouseY);
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
        this.mc.getTextureManager().bindTexture(Resources.GUI.SPRITES);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.left, this.top + tabHeight / 2, 0, 0, guiWidth, guiHeight);
        this.drawCurrentTab(page);
        this.fontRendererObj.drawString(page.getName() + " " + I18n.format("gui.achievements"), this.left + 15, this.top + tabHeight / 2 + 5, 4210752);
        super.drawScreen(mouseX, mouseY, renderPartialTicks);
        this.drawMouseOverAchievement(mouseX, mouseY);
        this.drawMouseOverTab(mouseX, mouseY);
    }

    private void handleMouseInput(int mouseX, int mouseY, AchievementPage page)
    {
        doDrag(mouseX, mouseY);
        if (onTab(mouseX, mouseY) != -1)
            doTabScroll();
        else
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
        for (int i = this.tabsOffset; i < maxTabs + this.tabsOffset && this.pages.size() > i; i++)
        {
            AchievementPage page = this.pages.get(i);
            if (page == selected) continue;
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int j = (i - this.tabsOffset) * tabWidth;
            this.mc.getTextureManager().bindTexture(Resources.GUI.TABS);
            this.drawTexturedModalRect(this.left + tabOffsetX + j, this.top + tabOffsetY, j, 0, tabWidth, tabHeight);
            this.drawPageIcon(page, this.left + tabOffsetX + j, this.top + tabOffsetY);
        }
    }

    private void drawCurrentTab(AchievementPage selected)
    {
        for (int i = this.tabsOffset; i < maxTabs + this.tabsOffset && this.pages.size() > i; i++)
        {
            AchievementPage page = this.pages.get(i);
            if (page != selected) continue;
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int j = (i - this.tabsOffset) * tabWidth;
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
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            itemRender.renderItemAndEffectIntoGUI(itemStack, tabLeft + 6, tabTop + 9);
            itemRender.zLevel = 0.0F;
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.zLevel = 0.0F;
        }
    }

    private void drawAchievementsBackground(AchievementPage page)
    {
        GlStateManager.translate(this.left, this.top + borderWidthY, -200.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        if (page instanceof ICustomBackground)
        {
            GlStateManager.pushMatrix();
            ((ICustomBackground) page).drawBackground(this.left, this.top, innerWidth + borderWidthX, innerHeight + borderWidthY, this.zLevel, this.scale);
            GlStateManager.popMatrix();
        }
        else
        {
            GlStateManager.pushMatrix();
            float scaleInverse = 1.0F / this.scale;
            GlStateManager.scale(scaleInverse, scaleInverse, 1.0F);
            float scale = blockSize / this.scale;
            int dragX = this.xPos - minDisplayColumn >> 4;
            int dragY = this.yPos - minDisplayRow >> 4;
            int antiJumpX = (this.xPos - minDisplayColumn) % 16;
            int antiJumpY = (this.yPos - minDisplayRow) % 16;
            // TODO: some smarter background gen
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            for (int y = 1; y * scale - antiJumpY < innerHeight + borderWidthY; y++)
            {
                float darkness = 0.7F - (dragY + y) / 80.0F;
                GlStateManager.color(darkness, darkness, darkness, 1.0F);
                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                for (int x = 1; x * scale - antiJumpX < innerWidth + borderWidthX; x++)
                {
                    random.setSeed(this.mc.getSession().getPlayerID().hashCode() + dragY + y + (dragX + x) * 16);
                    int r = random.nextInt(1 + dragY + y) + (dragY + y) / 2;
                    TextureAtlasSprite icon = RenderHelper.getIcon(Blocks.grass);
                    if (r == 40)
                    {
                        if (random.nextInt(3) == 0)
                            icon = RenderHelper.getIcon(Blocks.diamond_ore);
                        else
                            icon = RenderHelper.getIcon(Blocks.redstone_ore);
                    }
                    else if (r == 20)
                        icon = RenderHelper.getIcon(Blocks.iron_ore);
                    else if (r == 12)
                        icon = RenderHelper.getIcon(Blocks.coal_ore);
                    else if (r > 60)
                        icon = RenderHelper.getIcon(Blocks.bedrock);
                    else if (r > 4)
                        icon = RenderHelper.getIcon(Blocks.stone);
                    else if (r > 0)
                        icon = RenderHelper.getIcon(Blocks.dirt);

                    this.drawTexturedModalRect(x * blockSize - antiJumpX, y * blockSize - antiJumpY, icon, blockSize, blockSize);
                }
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
    }

    private void drawArrow(Achievement achievement, int colourCantUnlock, int colourCanUnlock, int colourUnlocked)
    {
        int depth = this.statFileWriter.func_150874_c(achievement); // How far is the nearest unlocked parent

        if (depth < 5)
        {
            int achievementXPos = achievement.displayColumn * achievementSize - this.xPos + achievementInnerSize/2;
            int achievementYPos = achievement.displayRow * achievementSize - this.yPos + achievementInnerSize/2;
            int parentXPos = achievement.parentAchievement.displayColumn * achievementSize - this.xPos + achievementInnerSize/2;
            int parentYPos = achievement.parentAchievement.displayRow * achievementSize - this.yPos + achievementInnerSize/2;
            boolean unlocked = this.statFileWriter.hasAchievementUnlocked(achievement);
            boolean canUnlock = this.statFileWriter.canUnlockAchievement(achievement);
            int colour = colourCantUnlock;

            if (unlocked)
                colour = colourUnlocked;
            else if (canUnlock)
                colour = colourCanUnlock;

            this.drawHorizontalLine(achievementXPos, parentXPos, achievementYPos, colour);
            this.drawVerticalLine(parentXPos, achievementYPos, parentYPos, colour);

            this.mc.getTextureManager().bindTexture(Resources.GUI.SPRITES);
            GlStateManager.enableBlend();
            if (achievementXPos > parentXPos)
                this.drawTexturedModalRect(achievementXPos - achievementInnerSize/2 - arrowHeadHeight, achievementYPos - arrowOffset, arrowRightX, arrowRightY, arrowHeadHeight, arrowHeadWidth);
            else if (achievementXPos < parentXPos)
                this.drawTexturedModalRect(achievementXPos + achievementInnerSize/2, achievementYPos - arrowOffset, arrowLeftX, arrowLeftY, arrowHeadHeight, arrowHeadWidth);
            else if (achievementYPos > parentYPos)
                this.drawTexturedModalRect(achievementXPos - arrowOffset, achievementYPos - achievementInnerSize/2 - arrowHeadHeight, arrowDownX, arrowDownY, arrowHeadWidth, arrowHeadHeight);
            else if (achievementYPos < parentYPos)
                this.drawTexturedModalRect(achievementXPos - arrowOffset, achievementYPos + achievementInnerSize/2, arrowUpX, arrowUpY, arrowHeadWidth, arrowHeadHeight);
        }
    }

    private void drawAchievements(AchievementPage page, int mouseX, int mouseY)
    {
        List<Achievement> achievements = new LinkedList<Achievement>(AchievementRegistry.instance().getAchievements(page));
        boolean customColours = page instanceof ICustomArrows;
        int colourCantUnlock = !userColourOverride && customColours ? ((ICustomArrows) page).getColourForCantUnlockArrow() : (GuiBetterAchievements.colourCantUnlockRainbow ? ColourHelper.getRainbowColour(GuiBetterAchievements.colourCantUnlockRainbowSettings) : GuiBetterAchievements.colourCantUnlock);
        int colourCanUnlock = !userColourOverride && customColours ? ((ICustomArrows) page).getColourForCanUnlockArrow() : (GuiBetterAchievements.colourCanUnlockRainbow ? ColourHelper.getRainbowColour(GuiBetterAchievements.colourCanUnlockRainbowSettings) : GuiBetterAchievements.colourCanUnlock);
        int colourUnlocked = !userColourOverride && customColours ? ((ICustomArrows) page).getColourForUnlockedArrow() : (GuiBetterAchievements.colourUnlockedRainbow ? ColourHelper.getRainbowColour(GuiBetterAchievements.colourUnlockedRainbowSettings) : GuiBetterAchievements.colourUnlocked);
        Collections.reverse(achievements);
        GlStateManager.pushMatrix();
        float inverseScale = 1.0F / scale;
        GlStateManager.scale(inverseScale, inverseScale, 1.0F);
        for (Achievement achievement : achievements)
            if (achievement.parentAchievement != null && achievements.contains(achievement.parentAchievement))
                this.drawArrow(achievement, colourCantUnlock, colourCanUnlock, colourUnlocked);
        for (Achievement achievement : achievements)
        {
            drawAchievement(achievement);
            if (onAchievement(achievement, mouseX, mouseY))
                this.hoveredAchievement = achievement;
        }
        GlStateManager.popMatrix();
    }

    private void drawAchievement(Achievement achievement)
    {
        int achievementXPos = achievement.displayColumn * achievementSize - this.xPos;
        int achievementYPos = achievement.displayRow * achievementSize - this.yPos;

        if (!onScreen(achievementXPos, achievementYPos)) return;

        int depth = this.statFileWriter.func_150874_c(achievement);
        boolean unlocked = this.statFileWriter.hasAchievementUnlocked(achievement);
        boolean canUnlock = this.statFileWriter.canUnlockAchievement(achievement);
        boolean special = achievement.getSpecial();
        float brightness;

        if (unlocked)
            brightness = 0.75F;
        else if (canUnlock)
            brightness = 1.0F;
        else if (depth < 3)
            brightness = 0.3F;
        else if (depth < 4)
            brightness = 0.2F;
        else if (depth < 5)
            brightness = 0.1F;
        else
            return;

        if (achievement instanceof ICustomBackgroundColour)
        {
            int colour = ((ICustomBackgroundColour) achievement).recolourBackground(brightness);
            GlStateManager.color((colour >> 16 & 255) / 255.0F, (colour >> 8 & 255) / 255.0F, (colour & 255) / 255.0F, 1.0F);
        }
        else
            GlStateManager.color(brightness, brightness, brightness, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI.SPRITES);
        GlStateManager.enableBlend();
        if (special)
            this.drawTexturedModalRect(achievementXPos - achievementOffset, achievementYPos - achievementOffset, achievementX + achievementTextureSize, achievementY, achievementTextureSize, achievementTextureSize);
        else
            this.drawTexturedModalRect(achievementXPos - achievementOffset, achievementYPos - achievementOffset, achievementX, achievementY, achievementTextureSize, achievementTextureSize);

        if (achievement instanceof ICustomIconRenderer)
        {
            GlStateManager.pushMatrix();
            ((ICustomIconRenderer) achievement).renderIcon(achievementXPos, achievementYPos);
            GlStateManager.popMatrix();
        }
        else
        {
            RenderItem renderItem = RenderHelper.getRenderItem();
            if (!canUnlock)
            {
                GlStateManager.color(0.1F, 0.1F, 0.1F, 1.0F);
                renderItem.func_175039_a(false); // Render with colour
            }

            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableCull();
            renderItem.renderItemAndEffectIntoGUI(achievement.theItemStack, achievementXPos + 3, achievementYPos + 3);

            if (!canUnlock)
                renderItem.func_175039_a(true); // Render with colour
        }
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableLighting();
    }

    private void drawMouseOverAchievement(int mouseX, int mouseY)
    {
        if (this.hoveredAchievement == null || !inInnerScreen(mouseX, mouseY)) return;

        if (Mouse.isButtonDown(1))
        {
            this.pause = false;
            MessageHandler.INSTANCE.sendToServer(new AchievementUnlockMessage(this.hoveredAchievement));
        }
        else
        {
            this.pause = true;
        }

        if (iconReset && Mouse.isButtonDown(2))
        {
            AchievementRegistry.instance().registerIcon(this.pages.get(this.currentPage).getName(), this.hoveredAchievement.theItemStack, true);
        }

        int tooltipX = mouseX + 12;
        int tooltipY = mouseY - 4;

        if (this.hoveredAchievement instanceof ICustomTooltip)
            ((ICustomTooltip) this.hoveredAchievement).renderTooltip(mouseX, mouseY, this.statFileWriter);
        else
        {
            String title = this.hoveredAchievement.getStatName().getUnformattedText();
            String desc = this.hoveredAchievement.getDescription();

            int depth = this.statFileWriter.func_150874_c(this.hoveredAchievement);
            boolean unlocked = this.statFileWriter.hasAchievementUnlocked(this.hoveredAchievement);
            boolean canUnlock = this.statFileWriter.canUnlockAchievement(this.hoveredAchievement);
            boolean special = this.hoveredAchievement.getSpecial();
            int tooltipWidth = defaultTooltipWidth;

            if (!canUnlock)
            {
                if (depth > 3)
                    return;
                else
                    desc = this.getChatComponentTranslation("achievement.requires", this.hoveredAchievement.parentAchievement.getStatName());

                if (depth == 3)
                    title = I18n.format("achievement.unknown");
            }

            tooltipWidth = Math.max(this.fontRendererObj.getStringWidth(title), tooltipWidth);
            int tooltipHeight = this.fontRendererObj.splitStringWidth(desc, tooltipWidth);

            if (unlocked) tooltipHeight += lineSize;

            this.drawGradientRect(
                    tooltipX - achievementTooltipOffset,
                    tooltipY - achievementTooltipOffset,
                    tooltipX + tooltipWidth + achievementTooltipOffset,
                    tooltipY + tooltipHeight + achievementTooltipOffset + lineSize,
                    -1073741824, -1073741824);
            this.fontRendererObj.drawStringWithShadow(title, tooltipX, tooltipY, canUnlock ? (special ? -128 : -1) : (special ? -8355776 : -8355712));
            this.fontRendererObj.drawSplitString(desc, tooltipX, tooltipY + lineSize, tooltipWidth, -6250336);
            if (unlocked)
                this.fontRendererObj.drawStringWithShadow(I18n.format("achievement.taken"), tooltipX, tooltipY + tooltipHeight + 4, -7302913);
        }

        this.hoveredAchievement = null;
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
        AchievementPage page = this.pages.get(this.currentPage);
        if (page instanceof ICustomScale && ((ICustomScale) page).resetScaleOnLoad())
            this.scale = ((ICustomScale) page).setScale();

        if (page instanceof ICustomPosition)
        {
            Achievement center = ((ICustomPosition) page).setPositionOnLoad();
            this.xPos = center.displayColumn * achievementSize + achievementSize * 3;
            this.yPos = center.displayRow * achievementSize + achievementSize;
        }
    }

    private void doTabScroll()
    {
        int dWheel = Mouse.getDWheel();

        if (dWheel < 0)
            this.tabsOffset--;
        else if (dWheel > 0)
            this.tabsOffset++;

        if (this.tabsOffset > this.pages.size() - maxTabs / 3 * 2)
            this.tabsOffset = this.pages.size() - maxTabs / 3 * 2;
        if(this.tabsOffset < 0)
            this.tabsOffset = 0;
    }

    private void doZoom(AchievementPage page)
    {
        int dWheel = Mouse.getDWheel();
        float prevScale = this.scale;

        if (dWheel < 0)
            this.scale += scaleJump;
        else if (dWheel > 0)
            this.scale -= scaleJump;

        boolean customScale = page instanceof ICustomScale;
        float minZoom =  customScale ? ((ICustomScale) page).getMinScale() : GuiBetterAchievements.minZoom;
        float maxZoom = customScale ? ((ICustomScale) page).getMaxScale() : GuiBetterAchievements.maxZoom;
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
                    this.newDrag = false;
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

    private boolean onAchievement(Achievement achievement, int mouseX, int mouseY)
    {
        int achievementXPos = achievement.displayColumn * achievementSize - this.xPos;
        int achievementYPos = achievement.displayRow * achievementSize - this.yPos + achievementInnerSize;
        return mouseX > this.left + achievementXPos / scale
                && mouseX < this.left + (achievementXPos + achievementInnerSize) / scale
                && mouseY > this.top + achievementYPos / scale
                && mouseY < this.top + (achievementYPos + achievementInnerSize) / scale;
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
            return ((mouseX - (this.left + tabOffsetX)) / tabWidth) + tabsOffset;
        }
        return -1;
    }

    private boolean onScreen(int x, int y)
    {
        return x > 0
                && x < guiWidth * scale - achievementSize
                && y > 0
                && y < guiHeight * scale - achievementSize;
    }

    private String getChatComponentTranslation(String s, Object... objects)
    {
        return (new ChatComponentTranslation(s, objects)).getUnformattedTextForChat();
    }
}
