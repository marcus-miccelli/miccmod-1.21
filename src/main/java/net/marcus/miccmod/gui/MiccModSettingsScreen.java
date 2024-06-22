package net.marcus.miccmod.gui;

import net.marcus.miccmod.MiccModClient;
import net.marcus.miccmod.gui.widgets.ZoomSliderWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;


public class MiccModSettingsScreen extends Screen {
    private final Screen parent;

    public MiccModSettingsScreen(Screen parent) {
        super(Text.literal("MiccMod Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Add a back button to return to the previous screen
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Done"),
                button -> this.client.setScreen(this.parent))
                .position(this.width / 2 - 100, this.height / 4 + 144)
                .size(200, 20)
            .build()
        );

        this.addDrawableChild(new ZoomSliderWidget(
                this.width / 2 - 100,
                this.height / 4,
                200,
                20,
                Text.literal("Zoom FOV"),
                30, MinecraftClient.getInstance().options.getFov().getValue()-1,
                MiccModClient.getZoomFov()
        ));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);
        super.render(context, mouseX, mouseY, delta);
    }
}
