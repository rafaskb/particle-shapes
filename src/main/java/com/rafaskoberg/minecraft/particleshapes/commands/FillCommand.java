package com.rafaskoberg.minecraft.particleshapes.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rafaskoberg.minecraft.particleshapes.ParticleShapes;
import com.rafaskoberg.minecraft.particleshapes.ShapeContainer;
import com.rafaskoberg.minecraft.particleshapes.Utils;

/** Toggles the fill state of the player's shape container. */
public class FillCommand implements CommandExecutor {
    private final ParticleShapes plugin;
    
    public FillCommand(ParticleShapes plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Make sure the sender is a player
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "Command exclusive to players.");
            return true;
        }
        
        // Get shape container
        Player player = (Player) sender;
        ShapeContainer container = plugin.getShapeContainer(player.getUniqueId());
        
        // Toggle fill state
        container.setFill(!container.isFill());
        
        // Message player
        ChatColor flagColor = container.isFill() ? ChatColor.GREEN : ChatColor.RED;
        String flagName = container.isFill() ? "enabled" : "disabled";
        Utils.sendMessage(player, "Fill function is " + flagColor + flagName);
        
        // Return success
        return true;
    }
    
}
