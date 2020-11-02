package com.aether.item;

import com.aether.entity.projectile.LightningKnifeEntity;
import com.aether.util.AetherSoundEvents;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class LightningKnifeItem extends Item {

	public LightningKnifeItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
		ItemStack heldItem = playerIn.getHeldItem(hand);
		
		if (!playerIn.isCreative() && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, heldItem) == 0) {
			heldItem.shrink(1);
		}
		
		worldIn.playSound(null, playerIn.getPosition(), AetherSoundEvents.ENTITY_PROJECTILE_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
		
		if (!worldIn.isRemote) {
			LightningKnifeEntity lightningKnife = new LightningKnifeEntity(playerIn, worldIn);
			lightningKnife.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 0.8F, 1.0F);
			worldIn.addEntity(lightningKnife);
		}
		
		return ActionResult.resultSuccess(heldItem);
	}

}
