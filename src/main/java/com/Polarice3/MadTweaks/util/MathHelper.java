package com.Polarice3.MadTweaks.util;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class MathHelper extends Mth {
    public static int secondsToTicks(int pSeconds){
        return pSeconds * 20;
    }

    public static float secondsToTicks(float pSeconds){
        return pSeconds * 20;
    }

    public static int minutesToTicks(int pMinutes){
        return secondsToTicks(pMinutes * 60);
    }

    public static float minutesToTicks(float pMinutes){
        return secondsToTicks(pMinutes * 60);
    }

    public static int minecraftDayToTicks(int pDay){
        return pDay * 24000;
    }

    public static float minecraftDayToTicks(float pDay){
        return pDay * 24000;
    }

    public static float modelDegrees(float degree){
        return (float) ((degree * Math.PI)/180.0F); /* For opposite, it's (answer * 180) / PI*/
    }

    public static double rgbToSpeed(double colorCode){
        return colorCode/255.0D;
    }

    /* Test this */
    public static double[] rgbParticle(int colorCode){
        return new double[]{((colorCode >> 16) & 0xff) / 255F, ((colorCode >> 8) & 0xff) / 255f, (colorCode & 0xff) / 255f};
    }

    public static long setDayNumberAndTime(long day, long time){
        return day * 24000 + time;
    }

    public static long getNextDaysTime(Level world, long timeOfDay) {
        long dayTime = world.getDayTime();
        long newTime = (dayTime + 24000);
        newTime -= newTime % 24000;
        return newTime + timeOfDay;
    }

    public static double getSkyDarken(Level world) {
        double d0 = 1.0D - (double)(world.getRainLevel(1.0F) * 5.0F) / 16.0D;
        double d1 = 1.0D - (double)(world.getThunderLevel(1.0F) * 5.0F) / 16.0D;
        double d2 = 0.5D + 2.0D * Mth.clamp((double)Mth.cos(world.getTimeOfDay(1.0F) * ((float)Math.PI * 2F)), -0.25D, 0.25D);
        return (int)((1.0D - d2 * d0 * d1) * 11.0D);
    }

    public static double getSkyDarkenTime(Level world) {
        double d2 = 0.5D + 2.0D * Mth.clamp((double)Mth.cos(world.getTimeOfDay(1.0F) * ((float)Math.PI * 2F)), -0.25D, 0.25D);
        return (int)((1.0D - d2) * 11.0D);
    }
}
