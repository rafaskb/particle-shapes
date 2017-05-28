package com.rafaskoberg.minecraft.particleshapes;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

/** Runnable class that renders the polygons of all players each time it's executed. */
public class ParticleRenderer extends BukkitRunnable {
    private static final Particle VERTEX_PARTICLE_TYPE = Particle.SMOKE_LARGE;
    private static final int      VERTEX_PARTICLES     = 15;
    private static final double   VERTEX_OFFSET        = 0.5;
    
    private static final Particle SIDE_PARTICLE_TYPE = Particle.SPELL_WITCH;
    private static final int      SIDE_PARTICLES     = 2;
    private static final double   SIDE_OFFSET        = 0;
    
    private static final Particle AREA_PARTICLE_TYPE = Particle.REDSTONE;
    private static final int      AREA_PARTICLES     = 1;
    private static final double   AREA_OFFSET        = 0;
    private static final Color    AREA_COLOR         = Color.RED;
    
    private static final double SIDE_STEP_DISTANCE = 0.25;
    private static final double FILL_STEP_DISTANCE = 0.50;
    
    private final ParticleShapes plugin;
    
    ParticleRenderer(ParticleShapes plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        // Get containers and abort tick in case it's empty
        Map<UUID, ShapeContainer> containers = plugin.getContainerMap();
        if(containers.isEmpty()) return;
        
        // Iterate through containers
        Set<Entry<UUID, ShapeContainer>> entries = containers.entrySet();
        Iterator<Entry<UUID, ShapeContainer>> it = entries.iterator();
        while(it.hasNext()) {
            Entry<UUID, ShapeContainer> entry = it.next();
            Player player = Bukkit.getPlayer(entry.getKey());
            ShapeContainer shape = entry.getValue();
            
            // Ignore if player or container are null for some reason
            if(player == null || shape == null) continue;
            
            // Get player's world
            World world = player.getWorld();
            
            // Render vertices
            if(shape.getSize() > 0) {
                renderVertices(shape, world);
            }
            
            // Render sides
            if(shape.getSize() > 1) {
                renderSides(shape, world);
            }
            
            // Render area
            if(shape.isFill() && shape.getSize() == ParticleShapes.MAX_VERTICES) {
                renderArea(shape, world);
            }
        }
    }
    
    /** Renders all valid vertices. */
    private void renderVertices(ShapeContainer shape, World world) {
        // Iterate through vertices
        double[] vertices = shape.getVertices();
        for(int i = 0, n = shape.getSize(); i < n; i++) {
            
            // Get location
            double x = vertices[i * 2];
            double z = vertices[i * 2 + 1];
            double y = world.getHighestBlockAt(NumberConversions.floor(x), NumberConversions.floor(z)).getY();
            
            // Create N particles at this point
            for(int j = 0; j < VERTEX_PARTICLES; j++) {
                Utils.spawnParticle(VERTEX_PARTICLE_TYPE, world, x, y, z, VERTEX_OFFSET, 0, 0, 0);
            }
        }
    }
    
    /** Renders the sides of the triangle, or just one side in case the triangle is not finished yet. */
    private void renderSides(ShapeContainer shape, World world) {
        // Iterate through sides
        double[] vertices = shape.getVertices();
        final int sides = shape.getSize() == 3 ? 3 : 1;
        for(int i = 0; i < sides; i++) {
            // Get vertices
            double x1 = vertices[i * 2];
            double z1 = vertices[i * 2 + 1];
            double x2 = vertices[(i * 2 + 2) % vertices.length];
            double z2 = vertices[(i * 2 + 3) % vertices.length];
            
            // Calculate side length and necessary steps to iterate through
            double len = Utils.distanceBetween(x1, z1, x2, z2);
            int steps = (int) Math.max(3, len / (double) SIDE_STEP_DISTANCE);
            
            // Iterate through side steps and calculate the highest Y coordinate
            for(int step = 0; step <= steps; step++) {
                double progress = (double) step / (double) steps;
                double x = Utils.lerp(x1, x2, progress);
                double z = Utils.lerp(z1, z2, progress);
                double y = world.getHighestBlockAt(NumberConversions.floor(x), NumberConversions.floor(z)).getY();
                
                // Create N particles at this point
                for(int j = 0; j < SIDE_PARTICLES; j++) {
                    Utils.spawnParticle(SIDE_PARTICLE_TYPE, world, x, y, z, SIDE_OFFSET, 0, 0, 0);
                }
            }
        }
    }
    
    /** Renders the area if the triangle by checking if the points in the area are contained within the shape. */
    private void renderArea(ShapeContainer shape, World world) {
        // Get color channels
        double r = Math.max(0.0001, AREA_COLOR.getRed() / 255.0);
        double g = AREA_COLOR.getGreen() / 255.0;
        double b = AREA_COLOR.getBlue() / 255.0;
        
        // Get boundaries
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;
        double[] vertices = shape.getVertices();
        for(int i = 0, n = shape.getSize(); i < n; i++) {
            double x = vertices[i * 2];
            double z = vertices[i * 2 + 1];
            if(x < minX) minX = x;
            if(x > maxX) maxX = x;
            if(z < minZ) minZ = z;
            if(z > maxZ) maxZ = z;
        }
        
        // Iterate through shape area
        for(double x = (int) minX; x <= maxX; x += FILL_STEP_DISTANCE) {
            for(double z = (int) minZ; z <= maxZ; z += FILL_STEP_DISTANCE) {
                
                // Make sure point is inside polygon
                if(!Utils.isPointInsidePolygon(x, z, vertices)) continue;
                
                // Get highest Y value
                double y = world.getHighestBlockAt(NumberConversions.floor(x), NumberConversions.floor(z)).getY();
                
                // Create N particles at this point
                for(int j = 0; j < AREA_PARTICLES; j++) {
                    Utils.spawnParticle(AREA_PARTICLE_TYPE, world, x, y, z, AREA_OFFSET, r, g, b);
                }
            }
        }
    }
    
}
