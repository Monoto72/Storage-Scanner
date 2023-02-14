package me.monoto.storagescanner.listeners;

import me.monoto.storagescanner.StorageScanner;
import me.monoto.storagescanner.hub.HubClass;
import me.monoto.storagescanner.hub.HubManager;
import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ScannerPlaceListener implements Listener {

    private final StorageScanner plugin;

    public ScannerPlaceListener(@NotNull StorageScanner _plugin) {
        _plugin.getServer().getPluginManager().registerEvents(this, _plugin);

        plugin = _plugin;
    }

    @EventHandler
    public void onScannerPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack item = event.getItemInHand();

        HubManager hubManager = plugin.getHubManager();

        if (Tag.SHULKER_BOXES.isTagged(block.getType())) {
            if (Tag.SHULKER_BOXES.isTagged(item.getType())) {
                if (!item.equals(hubManager.getHubItem())) return;

                ItemStack hubItem = hubManager.getHubItem();
                PersistentDataContainer data = hubItem.getItemMeta().getPersistentDataContainer();

                if (data.has(plugin.getKey(), PersistentDataType.STRING)) {
                    String value = data.get(plugin.getKey(), PersistentDataType.STRING);

                    if (value == null) return;

                    try {
                        TileState tileState = (TileState) block.getState();
                        PersistentDataContainer container = tileState.getPersistentDataContainer();

                        container.set(
                                plugin.getKey(),
                                PersistentDataType.STRING,
                                value
                        );
                        tileState.update();

                        HubClass hub = new HubClass(player.getName(), block.getLocation());
                        hubManager.getStorageHubs().add(hub);

                        Bukkit.getScheduler().runTask(plugin, () -> hubManager.setAttachedChests(hub));
                    } catch (Exception e) {
                        Bukkit.getConsoleSender().sendMessage("Failed to set TileState of Storage Scanner");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
