package com.gildedgames.aether.item;

import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.tool.TemplateSword;
import net.minecraft.item.tool.Sword;

public class ItemSwordGravitite extends TemplateSword {
    public ItemSwordGravitite(final @NotNull Identifier identifier, final ToolMaterial mat) {
        super(identifier, mat);
    }
    
    @Override
    public boolean postHit(final ItemInstance itemstack, final Living damageSource, final Living damageTarget) {
        if (damageTarget != null && damageTarget instanceof PlayerBase && (damageSource.hurtTime > 0 || damageSource.deathTime > 0)) {
            ++damageSource.velocityY;
            itemstack.applyDamage(1, damageTarget);
        }
        return true;
    }
}
