package net.marcus.miccmod.gui.widgets;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public abstract class CustomSliderWidget extends SliderWidget {
    protected final Text optionText;
    protected final double minValue;
    protected final double maxValue;

    public CustomSliderWidget(int x, int y, int width, int height, Text optionText, double minValue, double maxValue, double initialValue) {
        super(x, y, width, height, optionText, initialValue);
        this.optionText = optionText;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    public double getValue() {
        return this.minValue + (this.maxValue - this.minValue) * this.value;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.literal(String.format("%s: %d", optionText.getString(), (int)getValue())));
    }
}
