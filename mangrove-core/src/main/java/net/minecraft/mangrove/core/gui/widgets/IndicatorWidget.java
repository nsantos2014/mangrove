/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package net.minecraft.mangrove.core.gui.widgets;

import net.minecraft.mangrove.core.gui.MGGui;
import net.minecraft.mangrove.core.gui.tooltip.ToolTip;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IndicatorWidget extends Widget {

    public final IIndicatorController controller;

    public IndicatorWidget(IIndicatorController controller, int x, int y, int u, int v, int w, int h) {
        super(x, y, u, v, w, h);
        this.controller = controller;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(MGGui gui, int guiX, int guiY, int mouseX, int mouseY) {
        int scale = controller.getScaledLevel(h);
        gui.drawTexturedModalRect(guiX + x, guiY + y + h - scale, u, v + h - scale, w, scale);
    }

    @Override
    public ToolTip getToolTip() {
        return controller.getToolTip();
    }

}
