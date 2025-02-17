package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.TeamsHUD;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public abstract class TeamsInputScreen extends TeamsScreen {

    private static final ResourceLocation TEXTURE = new ResourceLocation(TeamsHUD.MODID, "textures/gui/smaller_background.png");
    private static final Component DEFAULT_TEXT = new TranslatableComponent("teams.menu.input");
    private static final Component GO_BACK_TEXT = new TranslatableComponent("teams.menu.return");
    private static final int WIDTH = 120;
    private static final int HEIGHT = 110;

    protected EditBox inputField;
    protected Button submitButton;
    private String prevInputText = "";

    public TeamsInputScreen(Screen parent, Component title) {
        super(parent, title);
    }

    @Override
    protected void init() {
        super.init();
        inputField = addRenderableWidget(new EditBox(minecraft.font, x + (getWidth() - 100) / 2, y + 10, 100, 20, DEFAULT_TEXT));
        submitButton = addRenderableWidget(new Button(x + (getWidth() - 100) / 2, y + HEIGHT - 55, 100, 20, getSubmitText(), this::onSubmit));
        submitButton.active = submitCondition();
        addRenderableWidget(new Button(x + (getWidth() - 100) / 2, y + HEIGHT - 30, 100, 20, GO_BACK_TEXT, button -> {
            minecraft.setScreen(parent);
        }));
    }

    @Override
    public void tick() {
        if (!prevInputText.equals(inputField.getValue())) {
            submitButton.active = submitCondition();
        }
    }

    @Override
    protected int getWidth() {
        return WIDTH;
    }

    @Override
    protected int getHeight() {
        return HEIGHT;
    }

    @Override
    protected ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }

    protected abstract Component getSubmitText();

    protected abstract void onSubmit(Button widget);

    protected abstract boolean submitCondition();

}
