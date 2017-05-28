package com.rafaskoberg.minecraft.particleshapes.listeners;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.rafaskoberg.minecraft.particleshapes.ParticleShapes;

/** Removes the vertices entry from the main plugin once a player leaves the server to avoid memory leaks. */
public class PlayerQuitListener implements Listener {
    private final ParticleShapes plugin;
    
    public PlayerQuitListener(ParticleShapes plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        plugin.removeShapeContainer(uuid);
    }
    
}
