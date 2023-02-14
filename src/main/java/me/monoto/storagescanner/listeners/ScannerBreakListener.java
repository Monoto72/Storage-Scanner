package me.monoto.storagescanner.listeners;

import me.monoto.storagescanner.StorageScanner;
import me.monoto.storagescanner.hub.HubClass;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ScannerBreakListener implements Listener {

    private final StorageScanner plugin;

    public ScannerBreakListener(@NotNull StorageScanner _plugin) {
        _plugin.getServer().getPluginManager().registerEvents(this, _plugin);
        plugin = _plugin;
    }

    @EventHandler
    public void onBlockBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (Tag.SHULKER_BOXES.isTagged(block.getType())) {
            TileState state = (TileState) block.getState();
            PersistentDataContainer container = state.getPersistentDataContainer();

            if (container.has(plugin.getKey(), PersistentDataType.STRING)) {
                String value = container.get(plugin.getKey(), PersistentDataType.STRING);
                if (value == null) return;

                if (value.equals("SCANNER")) {
                    try {
                        event.setDropItems(true);
                        block.getWorld().dropItemNaturally(block.getLocation(), plugin.getHubManager().getHubItem());

                        HubClass hub = plugin.getHubManager().getStorageHub(block.getLocation());
                        plugin.getHubManager().getStorageHubs().remove(hub);
                    } catch (Exception e) {
                        player.sendMessage("Failed to break Storage Scanner");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
