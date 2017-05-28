package com.rafaskoberg.minecraft.particleshapes.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/** Sends plugin instructions to the player once they join the server. */
public class PlayerJoinListener implements Listener {
    private final String instructions;
    
    public PlayerJoinListener() {
        instructions = createInstructions();
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        
        // Send player instructions
        event.getPlayer().sendMessage(instructions);
    }
    
    /** Creates the instructions that should be sent to each player that joins the server. */
    private String createInstructions() {
        ChatColor gray = ChatColor.DARK_GRAY;
        ChatColor purple = ChatColor.DARK_PURPLE;
        ChatColor pink = ChatColor.LIGHT_PURPLE;
        
        StringBuilder sb = new StringBuilder();
        sb.append(gray).append("===[ ").append(purple).append("ParticleShapes").append(gray).append(" ]===");
        sb.append(gray).append("\n- ").append(pink).append("Left click to add vertices to your shape.");
        sb.append(gray).append("\n- ").append(pink).append("Use the /fill command to fill the shape's area.");
        sb.append(gray).append("\n- ").append(pink).append("Enjoy! :)");
        sb.append("\n").append(gray).append("======================");
        
        return sb.toString();
    }
    
}
