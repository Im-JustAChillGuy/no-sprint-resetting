package com.example;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {

    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Text.literal("No Sprint Resetting Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2 - 100;
        int startY = 40;
        int spacing = 25;

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Enabled: " + NoSprintResettingConfig.enabled),
                btn -> {
                    NoSprintResettingConfig.setEnabled(!NoSprintResettingConfig.enabled);
                    btn.setMessage(Text.literal("Enabled: " + NoSprintResettingConfig.enabled));
                }
            ).dimensions(centerX, startY, 200, 20).build()
        );

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Disable While Sneaking: " + NoSprintResettingConfig.disableWhileSneaking),
                btn -> {
                    NoSprintResettingConfig.disableWhileSneaking = !NoSprintResettingConfig.disableWhileSneaking;
                    NoSprintResettingConfig.save();
                    btn.setMessage(Text.literal("Disable While Sneaking: " + NoSprintResettingConfig.disableWhileSneaking));
                }
            ).dimensions(centerX, startY + spacing, 200, 20).build()
        );

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Disable In Water: " + NoSprintResettingConfig.disableInWater),
                btn -> {
                    NoSprintResettingConfig.disableInWater = !NoSprintResettingConfig.disableInWater;
                    NoSprintResettingConfig.save();
                    btn.setMessage(Text.literal("Disable In Water: " + NoSprintResettingConfig.disableInWater));
                }
            ).dimensions(centerX, startY + spacing * 2, 200, 20).build()
        );

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Disable While Taking Damage: " + NoSprintResettingConfig.disableWhileTakingDamage),
                btn -> {
                    NoSprintResettingConfig.disableWhileTakingDamage = !NoSprintResettingConfig.disableWhileTakingDamage;
                    NoSprintResettingConfig.save();
                    btn.setMessage(Text.literal("Disable While Taking Damage: " + NoSprintResettingConfig.disableWhileTakingDamage));
                }
            ).dimensions(centerX, startY + spacing * 3, 200, 20).build()
        );

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Disable While Eating: " + NoSprintResettingConfig.disableWhileEating),
                btn -> {
                    NoSprintResettingConfig.disableWhileEating = !NoSprintResettingConfig.disableWhileEating;
                    NoSprintResettingConfig.save();
                    btn.setMessage(Text.literal("Disable While Eating: " + NoSprintResettingConfig.disableWhileEating));
                }
            ).dimensions(centerX, startY + spacing * 4, 200, 20).build()
        );

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Min Hunger: " + NoSprintResettingConfig.minHungerThreshold),
                btn -> {
                    NoSprintResettingConfig.minHungerThreshold =
                        (NoSprintResettingConfig.minHungerThreshold + 1) % 11;
                    NoSprintResettingConfig.save();
                    btn.setMessage(Text.literal("Min Hunger: " + NoSprintResettingConfig.minHungerThreshold));
                }
            ).dimensions(centerX, startY + spacing * 5, 200, 20).build()
        );

        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Done"),
                btn -> this.close()
            ).dimensions(centerX, this.height - 30, 200, 20).build()
        );
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
