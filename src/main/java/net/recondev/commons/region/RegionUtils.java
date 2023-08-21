package net.recondev.commons.region;

import net.recondev.commons.region.Region;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public final class RegionUtils {

    public Set<Block> getBlocksWithinRegion(final Region region) {

        final Set<Block> blocks = new HashSet<>();

        for (int x = region.getMinX(); x <= region.getMaxX(); x++) {
            for (int z = region.getMinZ(); z <= region.getMaxZ(); z++) {
                for (int y = region.getMinY(); y <= region.getMaxY(); y++) {
                    Block block = region.getWorld().getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    public Set<Location> getEdgesOfRegion(final Region region) {

        final Set<Location> locations = new HashSet<>();


        final int startX = Math.min(region.getMinX(), region.getMaxX());
        final int startY = Math.min(region.getMinY(), region.getMaxY());
        final int startZ = Math.min(region.getMinZ(), region.getMaxZ());
        final int endX = Math.max(region.getMinX(), region.getMaxX());
        final int endY = Math.max(region.getMinY(), region.getMaxY());
        final int endZ = Math.max(region.getMinZ(), region.getMaxZ());

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {

                    final Location location = new Location(
                            region.getWorld(),
                            x,
                            y,
                            z
                    );

                    if (!isOutline(location, region)) continue;

                    locations.add(location);
                }
            }
        }

        return locations;
    }

    public boolean isOutline(final Location location, final Region region) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        if (x != region.getMinX() && x != region.getMaxX() && y != region.getMinY() && y != region.getMaxY()) return false;
        if (z != region.getMinZ() && z != region.getMaxZ() && y != region.getMinY() && y != region.getMaxY()) return false;
        if (x != region.getMinX() && z != region.getMinZ() && y == region.getMinY()
                && x != region.getMaxX() && z != region.getMaxZ() && y == region.getMinY()) return false;
        if (x != region.getMinX() && z != region.getMinZ() && y == region.getMaxY()
                && x != region.getMaxX() && z != region.getMaxZ() && y == region.getMaxY()) return false;

        return true;
    }
}