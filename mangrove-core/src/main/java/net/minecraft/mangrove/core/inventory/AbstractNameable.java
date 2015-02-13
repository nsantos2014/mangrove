package net.minecraft.mangrove.core.inventory;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IWorldNameable;

public abstract class AbstractNameable implements IWorldNameable {
	private String name = null;
	private boolean customName = false;

	public AbstractNameable(String title, boolean customName) {
		this.name = title;
		this.customName = customName;
	}

	@Override
	public String getCommandSenderName() {
		return this.name;
	}

	// @Override
	// public String getName() {
	// return this.name;
	// }

	public void setCustomName(String name) {
		this.customName = true;
		this.name = name;
	}

	@Override
	public boolean hasCustomName() {
		return customName;
	}

	@Override
	public IChatComponent getDisplayName() {
		return (IChatComponent) (this.hasCustomName() ? new ChatComponentText(this.getCommandSenderName()) : new ChatComponentTranslation(this.getCommandSenderName(), new Object[0]));
	}

}
