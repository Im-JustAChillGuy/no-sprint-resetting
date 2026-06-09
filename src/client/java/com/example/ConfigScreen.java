package com.example;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.literal("No Sprint Resetting Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(
            Button.builder(
                Component.literal("Enabled: " + NoSprintResettingConfig.isEnabled()),
                btn -> {
                    NoSprintResettingConfig.setEnabled(!NoSprintResettingConfig.isEnabled());
                    btn.setMessage(Component.literal("Enabled: " + NoSprintResettingConfig.isEnabled()));
                }
            ).bounds(this.width / 2 - 100, 50, 200, 20).build()
        );

        this.addRenderableWidget(
            Button.builder(
                Component.literal("Done"),
                btn -> this.onClose()
            ).bounds(this.width / 2 - 100, this.height - 40, 200, 20).build()
        );
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}
