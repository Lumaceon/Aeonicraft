package lumaceon.mods.aeonicraft.tile.temporalmachine;

import lumaceon.mods.aeonicraft.api.clockwork.EnergyStorageModular;
import lumaceon.mods.aeonicraft.machine.Machine;
import lumaceon.mods.aeonicraft.machine.TemporalMachine;
import lumaceon.mods.aeonicraft.util.MachineActionBurstConfig;
import lumaceon.mods.aeonicraft.util.TickInterval;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class TileTemporalGenerator extends TileTemporalMachineInventory
{
    private int internalFuel = 0;
    private static final int RF_PER_TICK = 40;

    public TileTemporalGenerator() {
        super("Temporal Generator", 2);
        this.temporalMachine = new TemporalMachine(
                new EnergyStorageModular(1000000),
                new TickInterval(20),
                1.0F,
                1.0F,
                0.0F,
                this::getMaxActions,
                this::takeActions,
                new MachineActionBurstConfig()
        );
    }

    @Override
    public int getMaxActions(Machine machine)
    {
        EnergyStorageModular energy = this.temporalMachine.getEnergyStorage();
        if(energy.getEnergyStored() >= energy.getMaxEnergyStored())
            return 0;

        ItemStack fuel = this.getStackInSlot(0);
        if(fuel.equals(ItemStack.EMPTY))
            return internalFuel;

        int burnTimePerItem = TileEntityFurnace.getItemBurnTime(fuel);

        // TODO Special case for items with containers.

        return internalFuel + burnTimePerItem * fuel.getCount();
    }

    @Override
    public int takeActions(int max)
    {
        ItemStack fuel = this.getStackInSlot(0);
        Item fuelItem = fuel.getItem();
        int burnTimePerItem = TileEntityFurnace.getItemBurnTime(fuel);

        if(burnTimePerItem <= 0)
            return 0;

        if(max <= internalFuel)
        {
            internalFuel -= max;
            this.temporalMachine.getEnergyStorage().receiveEnergy(max * RF_PER_TICK, false);
            return max;
        }

        int itemsToEat = max / burnTimePerItem;
        fuel.shrink(itemsToEat);
        internalFuel += itemsToEat * burnTimePerItem;

        // TODO Special case for items with containers.
        //fuelItem.getContainerItem(fuel);

        internalFuel -= max;
        this.temporalMachine.getEnergyStorage().receiveEnergy(max * RF_PER_TICK, false);

        return max;
    }
}
