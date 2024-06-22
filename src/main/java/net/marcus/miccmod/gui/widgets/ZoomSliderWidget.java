package net.marcus.miccmod.gui.widgets;

import net.marcus.miccmod.MiccMod;
import net.marcus.miccmod.MiccModClient;
import net.minecraft.text.Text;

public class ZoomSliderWidget extends CustomSliderWidget {

    public ZoomSliderWidget(int x, int y, int width, int height, Text optionText, double minValue, double maxValue, double initialValue) {
        super(x, y, width, height, optionText, minValue, maxValue, initialValue);
    }

    @Override
    protected void applyValue() {
        int value = (int)(this.minValue + (this.maxValue - this.minValue) * this.value);
        MiccModClient.setZoomFov(value);
        MiccMod.LOGGER.info(String.format("Set Zoom FOV: %d", MiccModClient.getZoomFov()));
    }
}
