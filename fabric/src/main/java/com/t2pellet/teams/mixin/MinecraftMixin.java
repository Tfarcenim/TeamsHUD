package com.t2pellet.teams.mixin;

import com.t2pellet.teams.client.TeamsHUDClientFabric;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "<init>",at = @At("RETURN"))
    private void clientInit(GameConfig gameConfig, CallbackInfo ci) {
        TeamsHUDClientFabric.clientInit();
    }
}
