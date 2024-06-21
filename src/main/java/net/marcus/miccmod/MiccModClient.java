package net.marcus.miccmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;


public class MiccModClient implements ClientModInitializer {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final KeyBinding zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.miccmod.zoom",
            GLFW.GLFW_KEY_Z,
            "category.miccmod"
    ));

    private static boolean zoomed = false;
    private static final int ZOOM_FOV = 30; // Adjust the FOV for zoom effect
    private static int defaultFov; // Adjust as per default FOV

    private static final KeyBinding coordKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.miccmod.coordinates",
            GLFW.GLFW_KEY_P,
            "category.miccmod"
    ));
    private static boolean coordinates = true;

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        registerZoomKey();
        registerCoordinateKey();

        HudRenderCallback.EVENT.register(this::onHudRender);
    }

    public void registerZoomKey() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (zoomKey.isPressed()) {
                if (!zoomed) {
                    defaultFov = client.options.getFov().getValue();
                    client.options.getFov().setValue(ZOOM_FOV);
                    zoomed = true;
                    MiccMod.LOGGER.info("Zoomed in");
                }
            } else {
                if (zoomed) {
                    client.options.getFov().setValue(defaultFov);
                    zoomed = false;
                    MiccMod.LOGGER.info("Zoomed out");
                }
            }
        });
    }

    public void registerCoordinateKey() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (coordKey.wasPressed()) {
                coordinates = !coordinates;
                MiccMod.LOGGER.info("Toggled coordinates display: " + (coordinates ? "ON" : "OFF"));
            }
        });
    }

    private void onHudRender(DrawContext context, RenderTickCounter tickDelta) {
        if (!coordinates || client.player == null) {
            return;
        }
        Vec3d pos = client.player.getPos();

        String coordinates = String.format("X: %.3f, Y: %.3f, Z: %.3f", pos.x, pos.y, pos.z);
        Text coordinatesText = Text.of(coordinates);
        TextRenderer textRenderer = client.textRenderer;
        int color = 0xFFFFFF; // White color
        context.drawText(textRenderer, coordinatesText, 2, 2, color, true);
    }
}