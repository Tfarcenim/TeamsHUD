package com.t2pellet.teams.mixin;

import net.minecraft.advancements.Advancement;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(PlayerAdvancements.class)
public interface AdvancementAccessor {

    @Accessor("visible")
    Set<Advancement> getVisibleAdvancements();

}
