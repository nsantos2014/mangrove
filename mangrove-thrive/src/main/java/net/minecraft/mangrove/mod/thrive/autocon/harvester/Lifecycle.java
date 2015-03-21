package net.minecraft.mangrove.mod.thrive.autocon.harvester;

public enum Lifecycle {
	Off,
    Init,
	RenderScene,
	Execute,
	RenderSceneOut,
	Commit,
	RenderCooldown,
	Rollback,
	RenderFailScene;

	public static Lifecycle ofString(String name) {
		if(name==null || name.isEmpty()){
			return Off;
		}
		return valueOf(name);
	}

	public static Lifecycle ofInt(int i) {
		return values()[i];
	}
}
