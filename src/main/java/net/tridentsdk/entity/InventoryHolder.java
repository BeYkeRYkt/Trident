/*
 *     TridentSDK - A Minecraft Server API
 *     Copyright (C) 2014, The TridentSDK Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.tridentsdk.entity;

import net.tridentsdk.api.entity.*;
import net.tridentsdk.api.inventory.Inventory;
import net.tridentsdk.api.inventory.ItemStack;

/**
 * Represents an Entity that holds an Inventory
 *
 * @author TridentSDK Team
 */
public interface InventoryHolder extends net.tridentsdk.api.entity.Entity {
    /*
     * TODO: Convert the return types into a valid representation of their respective objects
     */

    /**
     * The Inventory that this entity holds
     *
     * @return the Inventory that this entity holds
     */
    Inventory getInventory();

    /**
     * The contents this slot
     *
     * @param slot the target slot
     * @return the contents of the specified slot
     */
    ItemStack getContent(int slot);
}