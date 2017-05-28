package com.rafaskoberg.minecraft.particleshapes;

/** Container containing all vertices to form a polygon. */
public class ShapeContainer {
    private final double[] vertices = new double[ParticleShapes.MAX_VERTICES * 2];
    private int            size     = 0;
    private boolean        fill     = false;
    
    /** Returns how many vertices of this container are valid. */
    public int getSize() {
        return size;
    }
    
    /** Sets how many vertices of this container are valid. */
    public void setSize(int size) {
        this.size = size;
    }
    
    /** Returns an array containing the X and Z values of all three vertices handled by this container. */
    public double[] getVertices() {
        return vertices;
    }
    
    /** Returns whether this shape should be filled with particles. */
    public boolean isFill() {
        return fill;
    }
    
    /** Sets whether this shape should be filled with particles. */
    public void setFill(boolean fill) {
        this.fill = fill;
    }
    
}
