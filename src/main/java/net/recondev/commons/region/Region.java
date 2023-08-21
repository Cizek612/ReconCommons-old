package net.recondev.commons.region;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;


public class Region {

    @Getter @Setter private int minX, minY, minZ, maxX, maxY, maxZ;
    private String world;


    public Region(final World world, final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
        this.world = world.getName();
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public void setWorld(final World world) {
        this.world = world.getName();
    }

    public World getWorld() {
        return Bukkit.getWorld(world);
    }
}
