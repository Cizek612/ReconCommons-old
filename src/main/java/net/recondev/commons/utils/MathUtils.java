package net.recondev.commons.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class MathUtils {

    public static Integer getRandom(final List<Integer> ints) {
        return ints.get(ThreadLocalRandom.current().nextInt(0, ints.size()));
    }

    public static Integer getMinMaxInt(final int min, final int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static Double getMinMaxDouble(final double min, final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static double round(final double num) {
        return (double) ((int) (num * 100.0)) / 100.0;
    }

    public static int getPercent(final int percent, final  int of) {
        return (int) ((float) percent / (float) of * 100.0f);
    }

    public static String getProgressBar(int current, final int max, final int totalBars, final String symbol, final String completedColor, final String notCompletedColor) {
        int i;
        if (current > max) {
            current = max;
        }
        final float percent = (float) current / (float) max;
        final int progressBars = (int) ((float) totalBars * percent);
        final int leftOver = totalBars - progressBars;
        final StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.translateAlternateColorCodes('&', completedColor));
        for (i = 0; i < progressBars; ++i) {
            sb.append(symbol);
        }
        sb.append(ChatColor.translateAlternateColorCodes('&', notCompletedColor));
        for (i = 0; i < leftOver; ++i) {
            sb.append(symbol);
        }
        return sb.toString();
    }

    public static List<Block> createRow(final Location center, final int radius, final World world) {

        final Location top = center.add(radius, 0, 0).clone();
        final Location bottom = center.add(-radius, 0, 0).clone();

        return getBlocks(top, bottom, world);
    }

    public static List<Block> createColumn(final Location center, final int radius, final World world) {


        final Location top = center.add(0, 0, radius).clone();
        final Location bottom = center.add(0, 0, -radius).clone();

        return getBlocks(top, bottom, world);
    }

    public static List<Block> createPlus(final Location center, final int radius, final World world) {

        final List<Block> blocks = new ArrayList<>();

        blocks.addAll(createColumn(center, radius, world));
        blocks.addAll(createColumn(center, -radius, world));
        blocks.addAll(createRow(center, radius, world));
        blocks.addAll(createRow(center, -radius, world));

        return blocks;
    }

    public static List<Block> getBlocks(final Location loc1, final Location loc2, final World world) {
        final List<Block> blocks = new ArrayList<>();

        final int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        final int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        final int minY = Math.min(loc1.getBlockY(),loc2.getBlockY());
        final int maxY = Math.max(loc1.getBlockY(),loc2.getBlockY());
        final int minZ = Math.min(loc1.getBlockZ(),loc2.getBlockZ());
        final int maxZ = Math.max(loc1.getBlockZ(),loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    final Block block = world.getBlockAt(x,y,z);
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public static boolean isInteger(final String args) {
        try {
            Integer.parseInt(args);
            return true;
        } catch (final Exception ignored) {
            return false;
        }
    }

    public static boolean isLong(final String args) {
        try {
            Long.parseLong(args);
            return true;
        } catch (final Exception ignored) {
            return false;
        }
    }

    public static boolean isDouble(String args) {
        try {
            Double.parseDouble(args);
            return true;
        } catch (Exception ignored) {
            return false;

        }
    }


}