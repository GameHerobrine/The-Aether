package com.gildedgames.aether.gui;
import org.lwjgl.opengl.GL11;

import com.gildedgames.aether.entity.tile.TileEntityFreezer;
import com.gildedgames.aether.gui.container.ContainerFreezer;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.client.gui.screen.container.ContainerBase;

public class GuiFreezer extends ContainerBase {
    private TileEntityFreezer freezerInventory;
    
    public GuiFreezer(final PlayerInventory inventoryplayer, final TileEntityFreezer tileentityFreezer) {
        super(new ContainerFreezer(inventoryplayer, tileentityFreezer));
        this.freezerInventory = tileentityFreezer;
    }
    
    @Override
    protected void renderForeground() {
        this.textManager.drawText("Freezer", 60, 6, 4210752);
        this.textManager.drawText("Inventory", 8, this.containerHeight - 96 + 2, 4210752);
    }
    
    @Override
    protected void renderContainerBackground(final float tickDelta) {
        final int i = this.minecraft.textureManager.getTextureId("/assets/aether/stationapi/textures/gui/enchanter.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.textureManager.bindTexture(i);
        final int j = (this.width - this.containerWidth) / 2;
        final int k = (this.height - this.containerHeight) / 2;
        this.blit(j, k, 0, 0, this.containerWidth, this.containerHeight);
        if (this.freezerInventory.isBurning()) {
            final int l = this.freezerInventory.getBurnTimeRemainingScaled(12);
            this.blit(j + 57, k + 47 - l, 176, 12 - l, 14, l + 2);
        }
        final int i2 = this.freezerInventory.getCookProgressScaled(24);
        this.blit(j + 79, k + 35, 176, 14, i2 + 1, 16);
    }
}
