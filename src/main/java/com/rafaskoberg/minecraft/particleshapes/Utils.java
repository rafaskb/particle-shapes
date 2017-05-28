package com.rafaskoberg.minecraft.particleshapes;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

/** Utility class. */
public class Utils {
    
    /** Sends a formatted message to the player. */
    public static void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.DARK_PURPLE + "[ParticleShapes] " + ChatColor.LIGHT_PURPLE + message);
    }
    
    /** Spawns a particle at the given location with the specified color. */
    public static void spawnParticle(Particle particle, Location location, double offset, Color color) {
        // Get color channels
        double r = color.getRed() / 255.0;
        double g = color.getGreen() / 255.0;
        double b = color.getBlue() / 255.0;
        
        // Get coordinates
        double x = location.getX() + ((Math.random() * 2.0 - 1.0) * offset);
        double y = location.getY();
        double z = location.getZ() + ((Math.random() * 2.0 - 1.0) * offset);
        
        // Spawn particle
        spawnParticle(particle, location.getWorld(), x, y, z, r, g, b);
    }
    
    /** Spawns a particle at the given location with the specified color. */
    public static void spawnParticle(Particle particle, World world, double x, double y, double z, double offset, double r,
        double g, double b) {
        // Apply offset to coordinates
        x += ((Math.random() * 2.0 - 1.0) * offset);
        z += ((Math.random() * 2.0 - 1.0) * offset);
        
        // Spawn particle
        spawnParticle(particle, world, x, y, z, r, g, b);
    }
    
    /** Spawns a particle at the given location with the specified color. */
    public static void spawnParticle(Particle particle, World world, double x, double y, double z, double r, double g, double b) {
        world.spawnParticle(particle, x, y, z, 0, r, g, b, 1);
    }
    
    /** Returns the distance between two points. */
    public static double distanceBetween(double x1, double y1, double x2, double y2) {
        double a = x2 - x1;
        double b = y2 - y1;
        return Math.sqrt(a * a + b * b);
    }
    
    /** Performs a linear interpolation from A to B at the given progress. */
    public static double lerp(double a, double b, double progress) {
        return a + (b - a) * progress;
    }
    
    /** Checks if the given point is inside the polygon vertices. */
    public static boolean isPointInsidePolygon(double x, double z, double[] vertices) {
        int intersections = 0;
        for(int i = 0, n = vertices.length; i < n; i += 2) {
            double x1 = vertices[i];
            double z1 = vertices[i + 1];
            double x2 = vertices[(i + 2) % n];
            double z2 = vertices[(i + 3) % n];
            if((z1 > z) != (z2 > z) && (x < (x2 - x1) * (z - z1) / (z2 - z1) + x1)) {
                intersections++;
            }
        }
        return (intersections % 2) == 1;
    }
    
}
