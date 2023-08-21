package net.recondev.commons.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ShapeUtil {

    public static List<Block> createSphere(final Location location, final int radius) {
        final List<Block> sphere = new ArrayList<>();
        final Block center = location.getBlock();
        for(int x = -radius; x <= radius; x++) {
            for(int y = -radius; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    final Block b = center.getRelative(x, y, z);
                    if(center.getLocation().distance(b.getLocation()) <= radius) {
                        sphere.add(b);
                    }
                }

            }
        }
        return sphere;
    }

    public static List<Block> createSquare(final Location location, final int radius) {
        final List<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List<Block> getBlocks(final Location loc1, final Location loc2) {
        final List<Block> blocks = new ArrayList<>();

        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));

        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));

        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

        for(int x = bottomBlockX; x <= topBlockX; x++) {
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for(int y = bottomBlockY; y <= topBlockY; y++)
                {
                    final Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    public static List<Block> createRow(final Location center, final int radius) {

        final Location top = center.add(radius, 0, 0).clone();
        final Location bottom = center.add(-radius, 0, 0).clone();

        return getBlocks(top, bottom);
    }

    public static List<Block> createColumn(final Location center,final int radius) {


        final Location top = center.add(0, 0, radius).clone();
        final Location bottom = center.add(0, 0, -radius).clone();

        return getBlocks(top, bottom);
    }

    public static List<Block> createPlus(final Location center, final int radius) {

        final List<Block> blocks = new ArrayList<>();

        blocks.addAll(createColumn(center, radius));
        blocks.addAll(createColumn(center, -radius));
        blocks.addAll(createRow(center, radius));
        blocks.addAll(createRow(center, -radius));

        return blocks;
    }

    public static List<Block> createLayer(final Location center, final long radius) {

        final Location top = center.clone().add((radius/2), 0, (radius/2));
        final Location bottom = center.clone().subtract((radius/2)-1, 0, (radius/2)-1);

        return getBlocks(top, bottom);
    }
}