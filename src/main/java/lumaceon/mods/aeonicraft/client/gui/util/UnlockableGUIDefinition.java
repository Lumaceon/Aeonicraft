package lumaceon.mods.aeonicraft.client.gui.util;

import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockable;
import lumaceon.mods.aeonicraft.capability.CapabilityAeonicraftProgression;
import lumaceon.mods.aeonicraft.network.PacketHandler;
import lumaceon.mods.aeonicraft.network.message.MessageGainUnlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class UnlockableGUIDefinition
{
    public HourglassUnlockable unlockable;
    public EntityPlayer player;
    public boolean isUnlocked;

    private boolean isInBasePosition = true;

    // Base position is the position in their default category strip
    private int basePosLeft = 0;
    private int basePosTop = 0;

    public UnlockableGUIDefinition(HourglassUnlockable unlockable, EntityPlayer player) {
        this.unlockable = unlockable;
        this.player = player;
        CapabilityAeonicraftProgression.IAeonicraftProgressionHandler cap = player.getCapability(CapabilityAeonicraftProgression.AEONICRAFT_PROGRESSION_CAPABILITY, null);
        if(cap != null)
            isUnlocked = cap.isUnlocked(unlockable);
        else
            isUnlocked = false;
    }

    public void setBaseX(int left) {
        this.basePosLeft = left;
    }

    public void setBaseY(int top) {
        this.basePosTop  = top;
    }

    public int getLeft() {
        return basePosLeft;
    }

    public int getTop() {
        return basePosTop;
    }

    public void onClicked(Minecraft mc)
    {
        EntityPlayer player = mc.player;
        if(player != null)
        {
            CapabilityAeonicraftProgression.IAeonicraftProgressionHandler cap = player.getCapability(CapabilityAeonicraftProgression.AEONICRAFT_PROGRESSION_CAPABILITY, null);
            if(cap != null)
            {
                if(!cap.isUnlocked(unlockable))
                {
                    if(cap.unlock(unlockable))
                    {
                        PacketHandler.INSTANCE.sendToServer(new MessageGainUnlock(unlockable));
                        this.isUnlocked = true;
                    }
                }
            }
        }
    }
}
