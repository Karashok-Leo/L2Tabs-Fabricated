package dev.xkmc.l2tabs.data;

import dev.xkmc.l2tabs.L2Tabs;
import karashokleo.leobrary.data.AbstractDataProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;

public class AttributeConfigProvider extends AbstractDataProvider
{
    public AttributeConfigProvider(FabricDataOutput output)
    {
        super(output, L2Tabs.ATTRIBUTE_CONFIG_PATH);
    }

    @Override
    public void add()
    {
        add(new Identifier(L2Tabs.MOD_ID, "vanilla"),
                new AttributeDisplayConfig()
                        .add(EntityAttributes.GENERIC_MAX_HEALTH, 1000)
                        .add(EntityAttributes.GENERIC_ARMOR, 2000)
                        .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 3000)
                        .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 4000)
                        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 5000)
                        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6000)
                        .add(EntityAttributes.GENERIC_ATTACK_SPEED, 7000)
                        .add(EntityAttributes.GENERIC_LUCK, 10000)
        );
    }

    @Override
    public String getName()
    {
        return "L2Tabs Attribute Config Provider";
    }
}
