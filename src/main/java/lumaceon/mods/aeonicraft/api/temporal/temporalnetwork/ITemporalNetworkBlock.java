package lumaceon.mods.aeonicraft.api.temporal.temporalnetwork;

import lumaceon.mods.aeonicraft.api.temporal.TC;
import net.minecraft.util.EnumFacing;

/**
 * A block to be considered part of a temporal network. Standard implementation provided in BlockTemporalNetwork.
 */
public interface ITemporalNetworkBlock
{
    /**
     * Gets the TC (milliseconds) that this block will contribute to the temporal network while unloaded every second.
     * This value is then saved and referenced. If you need a dynamic value, you'll have to get the TemporalNetwork and
     * update it there.
     *
     * A 0 value can be passed in to create a simple network connector. Most machines and other blocks that make use of
     * TC should conventionally pass in a negative value; -(TCToRealTime.SECOND) is a good baseline for simple machines.
     *
     * @return The TC this will generate each second when unloaded.
     */
    TC getTCGenerationPerSecond();

    /**
     * How much TC capacity to add to a temporal network. Conventionally, blocks with a high TC capacity should also
     * have a significant negative generation value, encouraging the player to keep storage loaded at all times.
     */
    TC getTCCapacity();

    /**
     * Queried when first added to the network. If you need more dynamic sides, you'll have to get the TemporalNetwork
     * and update it there.
     *
     * @return True of the given side of this block can connect to a network, false otherwise.
     */
    default boolean canConnectOnSide(EnumFacing mySide) {
        return true;
    }

    /**
     * Hourglasses can link relays to each other to force connectivity across distance (and even dimensions). It uses
     * the return value from this method to decide whether or not this block is a valid connection point.
     */
    default boolean isRelay() {
        return false;
    }
}
