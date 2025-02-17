package com.t2pellet.teams.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface InventoryScreenAccessor {

    @Accessor("leftPos")
    int getX();
    @Accessor("topPos")
    int getY();
    @Accessor("imageWidth")
    int getBackgroundWidth();

}
