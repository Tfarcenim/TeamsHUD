package com.t2pellet.teams.mixin;

import com.t2pellet.teams.client.TeamsHUDClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerTabOverlay.class)
public class TabHudMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 9)
    private int onRenderTabList(int p) {
        if (TeamsHUDClient.compass.isShowing()) {
            float scaledHeight = minecraft.getWindow().getGuiScaledHeight();
            return (int) (scaledHeight * 0.01) + 12 + 16;
        }
        return p;
    }

}
