package net.minecraft.mangrove.core.inventory.utils;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.tileentity.TileEntityChest;

public class InventoryUtils {

    /**
     * Ensures that the given inventory is the full inventory, i.e. takes double
     * chests into account.
     *
     * @param inv
     * @return Modified inventory if double chest, unmodified otherwise.
     */
    public static IInventory getInventory(IInventory inv) {
        if (inv instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) inv;

            TileEntityChest adjacent = null;

            if (chest.adjacentChestXNeg != null) {
                adjacent = chest.adjacentChestXNeg;
            }

            if (chest.adjacentChestXPos != null) {
                adjacent = chest.adjacentChestXPos;
            }

            if (chest.adjacentChestZNeg != null) {
                adjacent = chest.adjacentChestZNeg;
            }

            if (chest.adjacentChestZPos != null) {
                adjacent = chest.adjacentChestZPos;
            }

            if (adjacent != null) {
                return new InventoryLargeChest("", chest, adjacent);
            }
            return inv;
        }
        return inv;
    }
}
