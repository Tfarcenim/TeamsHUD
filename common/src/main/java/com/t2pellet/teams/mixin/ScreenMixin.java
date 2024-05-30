package com.t2pellet.teams.mixin;

import com.t2pellet.teams.ScreenDuck;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin implements ScreenDuck {

    @Shadow @Final private List<Renderable> renderables;
    @Shadow @Final private List<GuiEventListener> children;
    @Shadow @Final private List<NarratableEntry> narratables;

    @Override
    public Button $addButton(Button b) {
        this.renderables.add(b);
        this.children.add(b);
        this.narratables.add(b);
        return b;
    }
}
