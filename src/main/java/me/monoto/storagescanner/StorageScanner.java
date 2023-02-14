package me.monoto.storagescanner;

import lombok.Getter;
import me.monoto.storagescanner.commands.StorageScannerCommand;
import me.monoto.storagescanner.hub.HubManager;
import me.monoto.storagescanner.listeners.ScannerBreakListener;
import me.monoto.storagescanner.listeners.ScannerInventoryListener;
import me.monoto.storagescanner.listeners.ScannerListener;
import me.monoto.storagescanner.listeners.ScannerPlaceListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class StorageScanner extends JavaPlugin {

    @Getter private static StorageScanner instance;
    @Getter private HubManager hubManager;
    @Getter private final NamespacedKey key = new NamespacedKey(this, "STORAGE_SCANNER");

    public StorageScanner() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        new StorageScannerCommand(this);

        new ScannerListener(this);
        new ScannerPlaceListener(this);
        new ScannerInventoryListener(this);
        new ScannerBreakListener(this);

        hubManager = new HubManager();

        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("storage_scanner"), hubManager.getHubItem());
        recipe.shape("XAX","DBD","XCX");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.CHEST);
        recipe.setIngredient('C', Material.REDSTONE);
        recipe.setIngredient('D', Material.IRON_BARS);

        Bukkit.getServer().addRecipe(recipe);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
