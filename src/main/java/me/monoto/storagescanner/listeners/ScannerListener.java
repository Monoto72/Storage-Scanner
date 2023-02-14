package me.monoto.storagescanner.listeners;

import me.monoto.storagescanner.StorageScanner;
import me.monoto.storagescanner.hub.HubClass;
import me.monoto.storagescanner.hub.HubManager;
import me.monoto.storagescanner.menu.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ScannerListener implements Listener {

    private final StorageScanner plugin;

    public ScannerListener(@NotNull StorageScanner _plugin) {
        _plugin.getServer().getPluginManager().registerEvents(this, _plugin);
        plugin = _plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        HubManager hubManager = StorageScanner.getInstance().getHubManager();

        if (block != null && Tag.SHULKER_BOXES.isTagged(block.getType())) {
            if (hubManager.getStorageHub(block.getLocation()) != null) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) return;
                event.setCancelled(true);

                HubClass hub = hubManager.getStorageHub(block.getLocation());

                if (hub.getMenu() == null) hub.setMenuManager(new InventoryManager(block.getLocation()));
                player.openInventory(hub.getMenu());
            } else {
                try {
                    TileState tileState = (TileState) block.getState();
                    PersistentDataContainer container = tileState.getPersistentDataContainer();

                    if (container.has(StorageScanner.getInstance().getKey(), PersistentDataType.STRING)) {
                        if (event.getAction() == Action.LEFT_CLICK_BLOCK) return;
                        event.setCancelled(true);

                        String value = container.get(StorageScanner.getInstance().getKey(), PersistentDataType.STRING);
                        if (value == null) return;

                        HubClass hub = new HubClass(player.getName(), block.getLocation());
                        hubManager.getStorageHubs().add(hub);

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            hubManager.setAttachedChests(hub);

                            if (hub.getMenu() == null) hub.setMenuManager(new InventoryManager(block.getLocation()));
                            player.openInventory(hub.getMenu());
                        });
                    }
                } catch (Exception e) {
                    player.sendMessage("Failed to open Storage Scanner");
                    e.printStackTrace();
                }
            }
        }
    }
}
