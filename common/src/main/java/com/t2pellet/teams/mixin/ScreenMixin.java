package com.t2pellet.teams.mixin;

import com.t2pellet.teams.ScreenDuck;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Screen.class)
public abstract class ScreenMixin implements ScreenDuck {

    @Shadow protected abstract <T extends Renderable> T addRenderableOnly(T pRenderable);

    @Override
    public Button $addButton(Button b) {
        return this.addRenderableOnly(b);
    }
}
