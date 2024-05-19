package com.t2pellet.teams.mixin;

import com.t2pellet.teams.TeamsHUDFabric;
import com.t2pellet.teams.events.AdvancementEvents;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class AdvancementMixin {

    @Shadow private ServerPlayer player;

    @Inject(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerAdvancements;ensureVisibility(Lnet/minecraft/advancements/Advancement;)V"))
    public void advancementCompleted(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> ci) {
        if (!TeamsHUDFabric.getServer().overworld().isClientSide) {
            AdvancementEvents.ADVANCEMENT_GRANTED.invoker().onPlayerAdvancement(player, advancement);
        }
    }
}
