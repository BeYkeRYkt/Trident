/*
 *     Trident - A Multithreaded Server Alternative
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
package net.tridentsdk.api.entity.living;

import net.tridentsdk.api.entity.OcelotType;
import net.tridentsdk.api.entity.Peaceful;
import net.tridentsdk.api.entity.Tameable;

/**
 * Represents an Ocelot
 *
 * @author TridentSDK Team
 */
public interface Ocelot extends Tameable, Peaceful {
    /**
     * The breed of this ocelot, represented as a {@link net.tridentsdk.api.entity.OcelotType}OcelotType
     *
     * @return the breed of this ocelot
     */
    OcelotType getBreed();
}
