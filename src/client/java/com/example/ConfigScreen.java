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
        int centerX = this.width / 2 - 100;
        int startY = 40;
        int spacing = 25;

        this.addRenderableWidget(
            Button.builder(
                Component.literal("Enabled: " + NoSprintResettingConfig.enabled),
                btn -> {
                    NoSprintResettingConfig.setEnabled(!NoSprintResettingConfig.enabled);
                    btn.setMessage(Component.literal("Enabled: " + NoSprintResettingConfig.enabled));
                }
            ).bounds(centerX, startY, 200, 20).build()
        );

        this.addRenderableWidget(
            Button.builder(
                Component.literal("Disable While Sneaking: " + NoSprintResettingConfig.disableWhileSneaking),
                btn -> {
                    NoSprintResettingConfig.disableWhileSneaking = !NoSprintResettingConfig.disableWhileSneaking;
                    NoSprintResettingConfig.save();
                    btn.setMessage(Component.literal("Disable While Sneaking: " + NoSprintResettingConfig.disableWhileSneaking));
                }
            ).bounds(centerX, startY + spacing, 200, 20).build()
        );

        this.addRenderableWidget(
            Button.builder(
                Component.literal("Disable In Water: " + NoSprintResettingConfig.disableInWater),
                btn -> {
                    NoSprintResettingConfig.disableInWater = !NoSprintResettingConfig.disableInWater;
                    NoSprintResettingConfig.save();
                    btn.setMessage(Component.literal("Disable In Water: " + NoSprintResettingConfig.disableInWater));
                }
            ).bounds(centerX, startY + spacing * 2, 200, 20).build()
        );

        this.addRenderableWidget(
            Button.builder(
                Component.literal("Disable While Taking Damage: " + NoSprintResettingConfig.disableWhileTakingDamage),
                btn -> {
                    NoSprintResettingConfig.disableWhileTakingDamage = !NoSprintResettingConfig.disableWhileTakingDamage;
                    NoSprintResettingConfig.save();
                    btn.setMessage(Component.literal("Disable While Taking Damage: " + NoSprintResettingConfig.disableWhileTakingDamage));
                }
            ).bounds(centerX, startY + spacing * 3, 200, 20).build()
        );

        this.addRenderableWidget(
            Button.builder(
                Component.literal("Disable While Eating: " + NoSprintResettingConfig.disableWhileEating),
                btn -> {
                    NoSprintResettingConfig.disableWhileEating = !NoSprintResettingConfig.disableWhileEating;
                    NoSprintResettingConfig.save();
                    btn.setMessage(Component.literal("Disable While Eating: " + NoSprintResettingConfig.disableWhileEating));
                }
            ).bounds(centerX, startY + spacing * 4, 200, 20).build()
        );

        this.addRenderableWidget(
            Button.builder(
                Component.literal("Min Hunger: " + NoSprintResettingConfig.minHungerThreshold),
                btn -> {
                    // Cycle through 0-10 on click
                    NoSprintResettingConfig.minHungerThreshold =
                        (NoSprintResettingConfig.minHungerThreshold + 1) % 11;
                    NoSprintResettingConfig.save();
                    btn.setMessage(Component.literal("Min Hunger: " + NoSprintResettingConfig.minHungerThreshold));
                }
            ).bounds(centerX, startY + spacing * 5, 200, 20).build()
        );

        this.addRenderableWidget(
            Button.builder(
                Component.literal("Done"),
                btn -> this.onClose()
            ).bounds(centerX, this.height - 30, 200, 20).build()
        );
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}
