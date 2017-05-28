package com.rafaskoberg.minecraft.particleshapes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.rafaskoberg.minecraft.particleshapes.commands.FillCommand;
import com.rafaskoberg.minecraft.particleshapes.listeners.PlayerChangedWorldListener;
import com.rafaskoberg.minecraft.particleshapes.listeners.PlayerClickListener;
import com.rafaskoberg.minecraft.particleshapes.listeners.PlayerJoinListener;
import com.rafaskoberg.minecraft.particleshapes.listeners.PlayerQuitListener;

/** Entry point of the plugin. Handles the containers for each player (mapped by UUIDs). */
public class ParticleShapes extends JavaPlugin {
    public static final int  MAX_VERTICES      = 3;
    private static final int RENDERER_INTERVAL = 10;
    
    private final Map<UUID, ShapeContainer> containersByUuid = new HashMap<>();
    
    @Override
    public void onEnable() {
        // Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerQuitListener(this), this);
        pm.registerEvents(new PlayerClickListener(this), this);
        pm.registerEvents(new PlayerChangedWorldListener(this), this);
        
        // Register commands
        getCommand("fill").setExecutor(new FillCommand(this));
        
        // Register task
        ParticleRenderer renderer = new ParticleRenderer(this);
        renderer.runTaskTimer(this, RENDERER_INTERVAL, RENDERER_INTERVAL);
    }
    
    /** Returns the {@link ShapeContainer} instance associated with the given UUID, or creates a new one if necessary. Never
     * returns {@code null}. */
    public ShapeContainer getShapeContainer(UUID uuid) {
        // Try to get existing container for the given UUID
        ShapeContainer container = containersByUuid.get(uuid);
        
        // If the map has no such entry, create one
        if(container == null) {
            container = new ShapeContainer();
            containersByUuid.put(uuid, container);
        }
        
        return container;
    }
    
    /** Removes this {@link UUID}'s entry stored in the backed map, if any. */
    public void removeShapeContainer(UUID uuid) {
        containersByUuid.remove(uuid);
    }
    
    /** Returns the map of {@link ShapeContainer}s by {@link UUID}s handled by this plugin for read-only purposes. */
    Map<UUID, ShapeContainer> getContainerMap() {
        return containersByUuid;
    }
    
}
