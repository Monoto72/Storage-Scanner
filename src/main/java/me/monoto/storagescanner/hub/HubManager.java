package me.monoto.storagescanner.hub;

import lombok.Getter;
import lombok.Setter;
import me.monoto.storagescanner.StorageScanner;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class HubManager {

    @Setter @Getter private ArrayList<HubClass> storageHubs = new ArrayList<>();
    @Getter private final ItemStack hubItem = createHub();

    public HubClass getStorageHub(Location location) {
        for (HubClass hubClass : storageHubs) {
            if (hubClass.getLocation().equals(location)) return hubClass;
        }
        return null;
    }

    public HubClass getStorageHub(Inventory inventory) {
        for (HubClass hubClass : storageHubs) {
            if (hubClass.getMenu().equals(inventory)) return hubClass;
        }
        return null;
    }

    public ItemStack createHub() {
        ItemStack item = new ItemStack(Material.BLACK_SHULKER_BOX);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text("Storage Scanner").decoration(TextDecoration.ITALIC, false));

            PersistentDataContainer container = meta.getPersistentDataContainer();

            if (!container.has(StorageScanner.getInstance().getKey(), PersistentDataType.STRING)) {
                container.set(StorageScanner.getInstance().getKey(), PersistentDataType.STRING, "SCANNER");
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public void setAttachedChests(HubClass hubClass) {
        List<Chest> chests = new ArrayList<>();

        int radius = 12; // 25 blocks radius
        Block block = hubClass.getLocation().getBlock();


        for (int x = -radius; x <= radius; x++) { // o(N^3)
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block relativeBlock = block.getRelative(x, y, z);
                    if (relativeBlock.getType() == Material.CHEST) {
                        chests.add((Chest) relativeBlock.getState());
                    }
                }
            }
        }

        hubClass.setChestsAttached(chests);
    }
}
