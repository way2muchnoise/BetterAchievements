package betterachievements.registry;

import betterachievements.api.components.page.ICustomIcon;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class AchievementRegistry
{
    private static AchievementRegistry instance;
    public static final AchievementPage mcPage = new AchievementPage("Minecraft");
    private List<Achievement> mcAchievements;
    private Map<String, Achievement> statIdMap;
    private Map<String, ItemStack> iconMap;
    private Map<String, ItemStack> userSetIcons;
    private boolean firstLoad;

    public static AchievementRegistry instance()
    {
        if (instance == null)
            instance = new AchievementRegistry();
        return instance;
    }

    private AchievementRegistry()
    {
        this.firstLoad = true;
        this.mcAchievements = new LinkedList<Achievement>();
        this.iconMap = new LinkedHashMap<String, ItemStack>();
        this.statIdMap = new LinkedHashMap<String, Achievement>();
        this.userSetIcons = new LinkedHashMap<String, ItemStack>();
    }

    private void init()
    {
        for (Object oa : AchievementList.achievementList)
        {
            Achievement achievement = (Achievement)oa;
            this.statIdMap.put(achievement.statId, achievement);
            if (!AchievementPage.isAchievementInPages(achievement))
               this.mcAchievements.add(achievement);
        }
        this.iconMap.put(mcPage.getName(), new ItemStack(Blocks.grass));
        this.iconMap.putAll(this.userSetIcons);
        this.firstLoad = false;
    }

    public List<Achievement> getAchievements(AchievementPage page)
    {
        if (this.firstLoad) init();
        return page == mcPage ? this.mcAchievements : page.getAchievements();
    }

    public List<AchievementPage> getAllPages()
    {
        if (this.firstLoad) init();
        List<AchievementPage> pages = new LinkedList<AchievementPage>();
        pages.add(mcPage);
        int size = AchievementPage.getAchievementPages().size();
        for (int i = 0; i < size; i++) // Make sure to get pages in same order
            pages.add(AchievementPage.getAchievementPage(i));
        return pages;
    }

    public ItemStack getItemStack(AchievementPage page)
    {
        if (page == null) return null;
        ItemStack itemStack = this.iconMap.get(page.getName());
        if (itemStack == null)
        {
            if (page instanceof ICustomIcon)
                itemStack = ((ICustomIcon) page).getPageIcon();
            if (itemStack == null)
            {
                for (Achievement achievement : page.getAchievements())
                {
                    if (achievement.parentAchievement == null)
                    {
                        itemStack = achievement.theItemStack;
                        this.iconMap.put(page.getName(), itemStack);
                        break;
                    }
                }
            }
        }
        return itemStack;
    }

    public void registerIcon(String pageName, ItemStack itemStack, boolean userSet)
    {
        this.iconMap.put(pageName, itemStack);
        if (userSet)
            this.userSetIcons.put(pageName, itemStack);
    }

    public Achievement getAchievement(String statId)
    {
        return this.statIdMap.get(statId);
    }

    public String[] dumpUserSetIcons()
    {
        List<String> list = new LinkedList<String>();
        for (Map.Entry<String, ItemStack> entry : this.userSetIcons.entrySet())
        {
            String pageName = entry.getKey();
            String itemName = GameRegistry.findUniqueIdentifierFor(entry.getValue().getItem()).toString();
            list.add(pageName + "->" + itemName + ":" + entry.getValue().getItemDamage());
        }
        return list.toArray(new String[list.size()]);
    }

    public void setUserSetIcons(String[] array)
    {
        for (String entry : array)
        {
            String[] split = entry.split("->");
            if (split.length != 2) continue;
            String[] itemSplit = split[1].split(":");
            if (itemSplit.length != 3) continue;
            Item item = GameRegistry.findItem(itemSplit[0], itemSplit[1]);
            Integer meta = Integer.valueOf(itemSplit[2]);
            if (item != null && meta != null)
                this.userSetIcons.put(split[0], new ItemStack(item, meta));
        }
    }
}
