package com.t2pellet.teams.mixin;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.ui.menu.TeamsLonelyScreen;
import com.t2pellet.teams.client.ui.menu.TeamsMainScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin extends Screen {

    // TODO : Fix team button position not updating when the recipe book is opened / closed

    private static final ResourceLocation TEAMS_BUTTON_TEXTURE = new ResourceLocation(TeamsHUD.MODID, "textures/gui/buttonsmall.png");

    protected InventoryScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo info) {
        if (!minecraft.gameMode.hasInfiniteItems()) {
            InventoryScreenAccessor screen = ((InventoryScreenAccessor) this);
            addRenderableWidget(new ImageButton(screen.getX() + screen.getBackgroundWidth() - 19, screen.getY() + 4, 15, 14, 0, 0, 13, TEAMS_BUTTON_TEXTURE, (button) -> {
                if (ClientTeam.INSTANCE.isInTeam()) {
                    minecraft.setScreen(new TeamsMainScreen(minecraft.screen));

                } else {
                    minecraft.setScreen(new TeamsLonelyScreen(minecraft.screen));
                }
            }));
        }
    }
}
