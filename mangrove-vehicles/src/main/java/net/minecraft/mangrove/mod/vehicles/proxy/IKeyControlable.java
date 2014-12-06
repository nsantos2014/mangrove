package net.minecraft.mangrove.mod.vehicles.proxy;

import net.minecraft.entity.player.EntityPlayer;
public interface IKeyControlable {
    enum ID{
        ACCELERATE,DECELERATE,RIGHT,LEFT,SNEAK,JUMP, INVENTORY, FLOAT, COLLECT, INC_SPEED, DEC_SPEED,DIG  
    }
    void pressKey(ID id,EntityPlayer player);
}
