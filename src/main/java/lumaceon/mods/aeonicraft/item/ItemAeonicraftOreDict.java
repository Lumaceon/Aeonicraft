package lumaceon.mods.aeonicraft.item;

import lumaceon.mods.aeonicraft.util.IOreDict;

public class ItemAeonicraftOreDict extends ItemAeonicraft implements IOreDict
{
    private String oreDictionaryKey;

    public ItemAeonicraftOreDict(int maxStack, int maxDamage, String name, String oreDictionaryKey)
    {
        super(maxStack, maxDamage, name);
        this.oreDictionaryKey = oreDictionaryKey;
    }

    @Override
    public String getOreDictionaryString() {
        return this.oreDictionaryKey;
    }
}