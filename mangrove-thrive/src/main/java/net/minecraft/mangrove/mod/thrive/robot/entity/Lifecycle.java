package net.minecraft.mangrove.mod.thrive.robot.entity;

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
