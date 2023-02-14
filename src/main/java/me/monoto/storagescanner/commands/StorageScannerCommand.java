package me.monoto.storagescanner.commands;

import me.monoto.storagescanner.StorageScanner;
import me.monoto.storagescanner.hub.HubManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StorageScannerCommand implements CommandExecutor, TabExecutor {

    private final List<String> subCommands = List.of("give", "help", "reload");

    public StorageScannerCommand(@NotNull final StorageScanner plugin) {
        plugin.getCommand("storagescanner").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        HubManager hubManager = StorageScanner.getInstance().getHubManager();

        if (args.length == 0) {
            sendHelpMessage(sender);
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage("Not implemented yet.");
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 1 && sender instanceof Player) {
                ((Player) sender).getInventory().addItem(hubManager.createHub());
                return true;
            } else if (args.length == 2) {
                Player receiver = Bukkit.getPlayer(args[1]);

                if (receiver != null) {
                    receiver.getInventory().addItem(hubManager.getHubItem());
                    return true;
                } else {
                    sender.sendMessage("Player not found.");
                    return false;
                }
            } else {
                sender.sendMessage("Usage: /storagescanner give [player]");
                return false;
            }
        }

        sendHelpMessage(sender);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabComplete = new ArrayList<>();

        if (args.length == 1) {
            tabComplete.addAll(subCommands);
        } else if (args.length == 2) {
            tabComplete.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        }

        return tabComplete;
    }

    public void sendHelpMessage(@NotNull final CommandSender sender) {
        subCommands.forEach(sender::sendMessage);
    }
}
