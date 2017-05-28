package com.rafaskoberg.minecraft.particleshapes.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.rafaskoberg.minecraft.particleshapes.ParticleShapes;
import com.rafaskoberg.minecraft.particleshapes.Utils;

/** Removes the vertices entry from the main plugin once a player switches to another world. */
public class PlayerChangedWorldListener implements Listener {
    private final ParticleShapes plugin;
    
    public PlayerChangedWorldListener(ParticleShapes plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        // Remove shape container
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        plugin.removeShapeContainer(uuid);
        
        // Message player
        Utils.sendMessage(player, "Shape cleared.");
    }
    
}
