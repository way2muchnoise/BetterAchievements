package betterachievements.registry;

import betterachievements.api.components.page.ICustomIcon;
import betterachievements.util.LogHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.AchievementPage;

import java.util.*;
import java.util.stream.Collectors;

public final class AchievementRegistry
{
    private static AchievementRegistry instance;
    public static final AchievementPage mcPage = new AchievementPage("Minecraft");
    private List<Achievement> mcAchievements;
    private Map<String, Achievement> statIdMap;
    private Map<String, ItemStack> iconMap;
    private Map<String, ItemStack> userSetIcons;
    private Map<String, List<String>> childernMap;
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
        this.mcAchievements = new LinkedList<>();
        this.iconMap = new LinkedHashMap<>();
        this.statIdMap = new LinkedHashMap<>();
        this.userSetIcons = new LinkedHashMap<>();
        this.childernMap = new LinkedHashMap<>();
    }

    private void init()
    {
        for (Achievement achievement : AchievementList.ACHIEVEMENTS)
        {
            this.statIdMap.put(achievement.statId, achievement);
            if (!AchievementPage.isAchievementInPages(achievement))
               this.mcAchievements.add(achievement);
            if (achievement.parentAchievement != null)
            {
                List<String> children = this.childernMap.get(achievement.parentAchievement.statId);
                if (children == null) children = new LinkedList<>();
                children.add(achievement.statId);
                this.childernMap.put(achievement.parentAchievement.statId, children);
            }
        }
        this.iconMap.put(mcPage.getName(), new ItemStack(Blocks.GRASS));
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
        List<AchievementPage> pages = new LinkedList<>();
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
        if (this.firstLoad) init();
        return this.statIdMap.get(statId);
    }

    public List<Achievement> getAllChildren(Achievement achievement)
    {
        return toAchievements(getAllChildren(achievement.statId));
    }

    public List<String> getAllChildren(String statId)
    {
        List<String> children = new LinkedList<>();
        List<String> directChildren = getDirectChildren(statId);
        while(!directChildren.isEmpty())
        {
            children.addAll(directChildren);
            directChildren = directChildren.stream().map(this::getDirectChildren).flatMap(List::stream).collect(Collectors.toList());
        }
        return children;
    }

    public List<Achievement> getDirectChildren(Achievement achievement)
    {
        return toAchievements(getDirectChildren(achievement.statId));
    }

    public List<String> getDirectChildren(String statId)
    {
        if (this.firstLoad) init();
        return this.childernMap.containsKey(statId) ? this.childernMap.get(statId) : new ArrayList<>();
    }

    public List<Achievement> toAchievements(List<String> statIds)
    {
        return statIds.stream().map(this::getAchievement).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public String[] dumpUserSetIcons()
    {
        List<String> list = new LinkedList<>();
        for (Map.Entry<String, ItemStack> entry : this.userSetIcons.entrySet())
        {
            String pageName = entry.getKey();
            ItemStack itemStack = entry.getValue();
            String itemName = itemStack.getItem().getRegistryName().toString();
            String nbtCompoundTag = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "";
            list.add(pageName + "->" + itemName + ":" + entry.getValue().getItemDamage() + ":" + nbtCompoundTag);
        }
        return list.toArray(new String[list.size()]);
    }

    public void setUserSetIcons(String[] array)
    {
        int i = 0;
        for (String entry : array)
        {
            String[] split = entry.split("->");
            if (split.length != 2) continue;
            String[] itemSplit = split[1].split(":", 4);
            if (itemSplit.length < 2) continue;
            Item item = Item.REGISTRY.getObject(new ResourceLocation(itemSplit[0], itemSplit[1]));
            int meta = 0;
            try
            {
                meta = itemSplit.length > 2 ? Integer.parseInt(itemSplit[2]) : 0;
            } catch (NumberFormatException e)
            {
                LogHelper.instance().error(e, "Invalid input for meta data on entry " + i);
            }
            NBTTagCompound nbtTag = null;
            try
            {
                nbtTag = itemSplit.length > 3 && !itemSplit[3].equals("") ? JsonToNBT.getTagFromJson(itemSplit[3]) : null;
            } catch (NBTException e)
            {
                LogHelper.instance().error(e, "Invalid input for nbt data on entry " + i);
            }
            ItemStack itemStack = null;
            if (item != null)
                itemStack = new ItemStack(item, 0, meta);
            if (itemStack != null)
            {
                if (nbtTag != null)
                    itemStack.setTagCompound(nbtTag);
                this.userSetIcons.put(split[0], itemStack);
            }
            i++;
        }
    }
}
