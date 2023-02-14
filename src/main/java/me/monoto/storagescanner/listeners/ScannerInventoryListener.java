package me.monoto.storagescanner.listeners;

import me.monoto.storagescanner.StorageScanner;
import me.monoto.storagescanner.hub.HubClass;
import me.monoto.storagescanner.hub.HubManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ScannerInventoryListener implements Listener {

    private final StorageScanner plugin;

    public ScannerInventoryListener(@NotNull StorageScanner _plugin) {
        _plugin.getServer().getPluginManager().registerEvents(this, _plugin);
        plugin = _plugin;
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        HubManager hubManager = StorageScanner.getInstance().getHubManager();
        HubClass hub = hubManager.getStorageHub(inventory);

        if (hub != null) {
            event.setCancelled(true);

            if (hub.getMenuManager().getChestEntries().get(event.getSlot()) != null) {
                Chest chest = hub.getMenuManager().getChestEntries().get(event.getSlot());

                player.openInventory(chest.getInventory());
            }

            ItemStack item = event.getCurrentItem();
            if (item != null && item.getType().equals(Material.MAGENTA_GLAZED_TERRACOTTA)) {
                Bukkit.getScheduler().runTask(plugin, () -> hubManager.setAttachedChests(hub));
                hub.getMenuManager().updateInventory();
                player.openInventory(hub.getMenu());
            }
        }
    }

}
