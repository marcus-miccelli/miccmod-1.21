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
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;


public class MiccModClient implements ClientModInitializer {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final KeyBinding zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.miccmod.zoom",
            GLFW.GLFW_KEY_Z,
            "category.miccmod"
    ));

    private static boolean zoomed = false;
    private static final int ZOOM_FOV = 30;
    private static int defaultFov;

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
        String coordinates = String.format("X: %.3f, Y: %.3f, Z: %.3f, %s", pos.x, pos.y, pos.z, getString(client.player.getYaw()  % 360));
        Text coordinatesText = Text.of(coordinates);
        TextRenderer textRenderer = client.textRenderer;
        int color = 0xFFFFFF; // White color
        context.drawText(textRenderer, coordinatesText, 2, 2, color, true);
    }

    @Nullable
    private static String getString(float yaw) {
        String direction = null;

        if (157.5 < yaw && yaw < 202.5) {
            direction = "-Z";
        } else if (202.5 <= yaw && yaw <= 247.5) {
            direction = "+X,-Z";
        } else if (247.5 < yaw && yaw < 292.5) {
            direction = "+X";
        } else if (292.5 <= yaw && yaw <= 337.5) {
            direction = "+X, +Z";
        } else if (337.5 < yaw || yaw < 22.5) {
            direction = "+Z";
        } else if (22.5 <= yaw && yaw <= 67.5) {
            direction = "-X, +Z";
        } else if (67.5 < yaw && yaw < 112.5) {
            direction = "-X";
        } else if (112.5 < yaw && yaw <= 157.5) {
            direction = "-X, -Z";
        }
        return direction;
    }
}