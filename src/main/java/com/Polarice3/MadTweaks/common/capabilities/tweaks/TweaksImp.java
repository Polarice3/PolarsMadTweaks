package com.Polarice3.MadTweaks.common.capabilities.tweaks;

public class TweaksImp implements ITweaks {
    private boolean init = false;
    private int arrowCount = 0;

    @Override
    public boolean init(){
        return this.init;
    }

    @Override
    public void setInit(boolean init){
        this.init = init;
    }

    @Override
    public int arrowCount() {
        return this.arrowCount;
    }

    @Override
    public void setArrowCount(int level) {
        this.arrowCount = level;
    }
}
