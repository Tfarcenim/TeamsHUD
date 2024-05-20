package com.t2pellet.teams.client;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.core.ClientTeamDB;
import com.t2pellet.teams.client.ui.hud.CompassOverlay;
import com.t2pellet.teams.client.ui.hud.StatusOverlay;
import com.t2pellet.teams.client.ui.menu.TeamsLonelyScreen;
import com.t2pellet.teams.client.ui.menu.TeamsMainScreen;
import com.t2pellet.teams.mixin.InventoryScreenAccessor;
import com.t2pellet.teams.mixin.ScreenAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

public class TeamsHUDClient {

    public static final StatusOverlay status = new StatusOverlay();
    public static final CompassOverlay compass = new CompassOverlay();

    public static void registerKeybinding(KeyMapping keyMapping) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
    }

    public static final ResourceLocation TEAMS_BUTTON_TEXTURE = new ResourceLocation(TeamsHUD.MODID, "textures/gui/buttonsmall.png");

    public static void registerKeybinds() {
        // Register keybinds
        for (TeamsKeys.TeamsKey key : TeamsKeys.KEYS) {
            key.register();
        }
    }

    public static void clientDisconnect() {
        ClientTeam.INSTANCE.reset();
        ClientTeamDB.INSTANCE.clear();
    }

    public static void afterScreenInit(Minecraft minecraft, Screen screen, int scaledWidth, int scaledHeight){
        if (screen instanceof InventoryScreen inventoryScreen && minecraft.gameMode != null && !minecraft.gameMode.hasInfiniteItems()) {
            InventoryScreenAccessor screenAccessor = ((InventoryScreenAccessor) screen);
            ((ScreenAccessor)inventoryScreen).addButton(new ImageButton(screenAccessor.getX() + screenAccessor.getBackgroundWidth() - 19, screenAccessor.getY() + 4, 15, 14, 0, 0, 13, TEAMS_BUTTON_TEXTURE, (button) -> {
                if (ClientTeam.INSTANCE.isInTeam()) {
                    minecraft.setScreen(new TeamsMainScreen(minecraft.screen));

                } else {
                    minecraft.setScreen(new TeamsLonelyScreen(minecraft.screen));
                }
            }));
        }
    }

    public static void endClientTick() {
        for (var key : TeamsKeys.KEYS) {
            if (key.keyBinding.consumeClick()) {
                key.onPress.execute(Minecraft.getInstance());
            }
        }
    }
}
