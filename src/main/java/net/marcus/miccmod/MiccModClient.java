package net.marcus.miccmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;



public class MiccModClient implements ClientModInitializer {
    private static final KeyBinding zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.miccmod.zoom",
            GLFW.GLFW_KEY_Z,
            "category.miccmod"
    ));

    private static boolean zoomed = false;
    private static final int ZOOM_FOV = 30; // Adjust the FOV for zoom effect
    private static int defaultFov; // Adjust as per default FOV

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        registerZoomKey();
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
}