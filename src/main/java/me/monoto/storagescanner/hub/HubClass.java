package me.monoto.storagescanner.hub;

import lombok.Getter;
import lombok.Setter;
import me.monoto.storagescanner.menu.InventoryManager;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class HubClass {

    @Getter private final String placer;
    @Getter private final Location location;
    @Getter @Setter private InventoryManager menuManager;
    @Getter @Setter private Inventory menu;

    @Getter @Setter private List<Chest> chestsAttached = new ArrayList<>();

    public HubClass(String name, Location location) {
        this.placer = name;
        this.location = location;
    }
}
