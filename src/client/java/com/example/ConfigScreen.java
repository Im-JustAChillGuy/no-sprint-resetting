package com.example;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {

    public ConfigScreen() {
        super(Text.literal("No Sprint Resetting"));
    }

    @Override
    protected void init() {

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Enabled: " +
                                (NoSprintResettingConfig.enabled ? "ON" : "OFF")),
                        button -> {
                            NoSprintResettingConfig.enabled =
                                    !NoSprintResettingConfig.enabled;

                            button.setMessage(
                                    Text.literal("Enabled: " +
                                            (NoSprintResettingConfig.enabled ? "ON" : "OFF"))
                            );
                        }
                ).dimensions(width / 2 - 100, 50, 200, 20).build()
        );

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Done"),
                        button -> close()
                ).dimensions(width / 2 - 100, height - 40, 200, 20).build()
        );
    }

    @Override
    public void close() {
        assert client != null;
        client.setScreen(null);
    }
}
