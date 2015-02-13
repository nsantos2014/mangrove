/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package net.minecraft.mangrove.core.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

public class GuiSlider extends GuiButton {
	// Slider value
		public float sliderValue;

		public boolean dragging = false;

		/**
		* Sets up a slider.
		*/
		public GuiSlider(int buttonID, int x, int y, String label, float initialValue) {
			super(buttonID, x, y, 120, 20, label);		
			sliderValue = initialValue;
		}

		/**
		 * Fired when the mouse button is dragged. Equivalent of
		 * MouseListener.mouseDragged(MouseEvent e).
		 */
		protected void mouseDragged(Minecraft par1Minecraft, int x, int y) {
			if (!enabled) {
				return;
			}

			if (dragging) {
				sliderValue = limitToRange((float) (x - (xPosition + 4)) / (float) (width - 8));
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexturedModalRect(xPosition + (int) (sliderValue * (float) (width - 8)), yPosition, 0, 66, 4, 20);
			drawTexturedModalRect(xPosition + (int) (sliderValue * (float) (width - 8)) + 4, yPosition, 196, 66, 4, 20);
		}

		/**
		 * Returns true if the mouse has been pressed on this control. Equivalent of
		 * MouseListener.mousePressed(MouseEvent e).
		 */
		public boolean mousePressed(Minecraft mc, int x, int y) {
			if (super.mousePressed(mc, x, y)) {
				sliderValue = limitToRange((float) (x - (xPosition + 4)) / (float) (width - 8));
				
				dragging = true;
				return true;
			} else {
				return false;
			}
		}

		/**
		 * Fired when the mouse button is released. Equivalent of
		 * MouseListener.mouseReleased(MouseEvent e).
		 */
		public void mouseReleased(int par1, int par2) {
			dragging = false;
		}

		/**
		 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over
		 * this button and 2 if it IS hovering over this button.
		 */
		public int getHoverState(boolean par1) {
			return 0;
		}
		
		private float limitToRange (float value) {
			if (value < 0) {
				return 0;
			} else if (value > 1) {
				return 1;
			} else {
				return value;
			}
		}
}