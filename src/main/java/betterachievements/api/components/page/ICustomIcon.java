package betterachievements.api.components.page;

import net.minecraft.item.ItemStack;

public interface ICustomIcon {
    /**
     * Get the {@link ItemStack} to be displayed on the tab
     *
     * @return if null one will be picked for the page
     */
    ItemStack getPageIcon();
}
