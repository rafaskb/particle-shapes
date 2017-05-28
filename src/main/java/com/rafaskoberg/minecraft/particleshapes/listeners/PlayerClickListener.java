package com.rafaskoberg.minecraft.particleshapes.listeners;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.rafaskoberg.minecraft.particleshapes.ParticleShapes;
import com.rafaskoberg.minecraft.particleshapes.ShapeContainer;
import com.rafaskoberg.minecraft.particleshapes.Utils;

/** Listens to player click events and changes the shape container accordingly. */
public class PlayerClickListener implements Listener {
    private static final int     CLICK_DISTANCE = 100;
    private final ParticleShapes plugin;
    
    public PlayerClickListener(ParticleShapes plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Make sure event is coming from the main hand
        if(event.getHand() == null || event.getHand() != EquipmentSlot.HAND) return;
        
        // Make sure it was a left click
        Action action = event.getAction();
        if(action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) return;
        
        // Get shape container
        Player player = event.getPlayer();
        ShapeContainer container = plugin.getShapeContainer(player.getUniqueId());
        
        // If shape was already complete, reset it
        if(container.getSize() == ParticleShapes.MAX_VERTICES) {
            container.setSize(0);
            Utils.sendMessage(player, "Shape cleared.");
            return;
        }
        
        // Add new vertex to shape container
        Location targetLocation = player.getTargetBlock((Set<Material>) null, CLICK_DISTANCE).getLocation();
        double[] vertices = container.getVertices();
        double x = targetLocation.getBlockX() + 0.5;
        double z = targetLocation.getBlockZ() + 0.5;
        vertices[container.getSize() * 2] = x;
        vertices[container.getSize() * 2 + 1] = z;
        container.setSize(container.getSize() + 1);
        
        // Spawn interact particles
        player.spawnParticle(Particle.CLOUD, targetLocation, 15, 0, 1, 0);
        
        // Message player
        Utils.sendMessage(player, String.format("Added vertex at [x: %d, z: %d]", (int) x, (int) z));
    }
    
}
