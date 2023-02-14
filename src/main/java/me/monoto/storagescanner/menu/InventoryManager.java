package me.monoto.storagescanner.menu;

import lombok.Getter;
import lombok.Setter;
import me.monoto.storagescanner.StorageScanner;
import me.monoto.storagescanner.hub.HubClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryManager {

    @Getter @Setter private int size = 36 - 1;
    @Getter @Setter private String title = "Storage Scanner";
    @Getter @Setter private Inventory menu;
    @Getter @Setter private HubClass hub;

    @Getter private final HashMap<Integer, Chest> chestEntries = new HashMap<>();

    public InventoryManager(Location location) {
        this.hub = StorageScanner.getInstance().getHubManager().getStorageHub(location);

        updateInventory();
    }

    public void reload() {
        // temp
        updateInventory();
    }

    public void updateInventory() {
        this.menu = Bukkit.createInventory(null, size + 1, Component.text(title));

        for (int slot = 0; slot < size - 9; slot++) {
            if (slot == hub.getChestsAttached().size()) break;
            this.menu.setItem(slot, scannedChest(hub.getChestsAttached().get(slot), slot));
            chestEntries.put(slot, hub.getChestsAttached().get(slot));
        }

        for (int slot = size - 8; slot <= size; slot++) {
            this.menu.setItem(slot, fillerItem());
        }

        this.menu.setItem(size - 4, refreshItem());

        hub.setMenu(this.menu);
    }

    private ItemStack scannedChest(Chest chest, int slot) {
        ItemStack item = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            List<Component> lore = new ArrayList<>();
            Component itemName = chest.customName() == null
                    ? Component.text("Storage - " + slot).decoration(TextDecoration.ITALIC, false)
                    : chest.customName();

            lore.add(Component.text(" "));
            lore.add(Component.text(
                    "X: " + chest.getLocation().getBlockX()
                            + " Y: " + chest.getLocation().getBlockY()
                            + " Z: " + chest.getLocation().getBlockZ()
            ).decoration(TextDecoration.ITALIC, false));

            meta.displayName(itemName);
            meta.lore(lore);

            item.setItemMeta(meta);
        }

        return item;
    }

    private ItemStack fillerItem() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) meta.displayName(Component.text(" "));
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack refreshItem() {
        ItemStack item = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) meta.displayName(Component.text("Update Chest").decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);

        return item;
    }

}
