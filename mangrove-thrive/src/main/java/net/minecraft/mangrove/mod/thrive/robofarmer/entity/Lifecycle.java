package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

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
}
