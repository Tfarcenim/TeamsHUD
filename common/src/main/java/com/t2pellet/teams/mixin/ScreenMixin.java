package com.t2pellet.teams.mixin;

import com.t2pellet.teams.ScreenDuck;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin implements ScreenDuck {

    @Shadow @Final private List<NarratableEntry> narratables;

    @Shadow @Final private List<Widget> renderables;

    @Shadow @Final private List<GuiEventListener> children;

    @Override
    public Button $addButton(Button button) {
        narratables.add(button);
        renderables.add(button);
        children.add(button);
        return button;
    }
}
