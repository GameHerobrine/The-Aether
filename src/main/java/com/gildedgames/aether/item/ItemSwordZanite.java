package com.gildedgames.aether.item;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.tool.TemplateSword;

public class ItemSwordZanite extends TemplateSword {
    public ItemSwordZanite(final @NotNull Identifier identifier, final ToolMaterial mat) {
        super(identifier, mat);
    }
    
    @Override
    public float getStrengthOnBlock(final ItemInstance item, final BlockBase tile) {
        return super.getStrengthOnBlock(item, tile) * (2.0f * item.getDamage() / item.getType().getDurability() + 0.5f);
    }
}
