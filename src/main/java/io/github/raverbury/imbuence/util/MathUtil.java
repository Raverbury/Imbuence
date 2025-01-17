package io.github.raverbury.imbuence.util;

public final class MathUtil {
    private static final int MAX_VANILLA_COST = 30;
    private static final int MAX_MODDED_COST = 199;
    private static final int UNACHIEVABLE_COST = 100000;

    public static int getModdedAwareMinCost(int levelOneCost,
                                            int currentLevel,
                                            int maxVanillaLevel,
                                            int maxModdedLevel) {
        return getModdedAwareMinCost(levelOneCost, currentLevel,
                maxVanillaLevel,
                maxModdedLevel,
                MAX_MODDED_COST);
    }

    public static int getModdedAwareMinCost(int levelOneCost,
                                            int currentLevel,
                                            int maxVanillaLevel,
                                            int maxModdedLevel,
                                            int maxModdedCost) {
        if (maxVanillaLevel == 1) {
            return levelOneCost;
        }
        // with vanilla levels, use linear scaling
        if (currentLevel <= maxVanillaLevel) {
            if (levelOneCost > MAX_VANILLA_COST) {
                return MAX_VANILLA_COST;
            }
            int costGap = MAX_VANILLA_COST - levelOneCost;
            int costGrowth = (int) ((float) costGap / (maxVanillaLevel - 1));
            return levelOneCost + (currentLevel - 1) * costGrowth;
        }
        if (currentLevel > maxModdedLevel) {
            return UNACHIEVABLE_COST;
        }
        // by now, it's established that maxVanillaLevel < currentLevel <=
        // maxModdedLevel
        if (levelOneCost > maxModdedCost) {
            return maxModdedCost;
        }
        // with modded levels, scale kinda linearly, read on to know more
        int linearCostGap = MAX_VANILLA_COST - levelOneCost;
        int linearCostGrowth =
                (int) ((float) linearCostGap / (maxVanillaLevel - 1));
        int costGap = maxModdedCost - MAX_VANILLA_COST;
        int levelGap = currentLevel - maxVanillaLevel;
        // compensate for apotheosis's extra cost after maxVanillaLevel
        costGap -= linearCostGrowth * (int) Math.pow(levelGap, 1.6);
        // the scaling for this is inspired from league of legends growth stats
        // ie growth over X levels (starting at 1) will be growth * X
        // but smaller levels will contribute less and higher levels more
        // each level provides stat equal to that level / sum of all levels
        // from 1 to highest level, so lv3 alone out of max level of 7 will
        // provide 3 / sum(1,7) of total stat
        return (int) (MAX_VANILLA_COST + (float) costGap * ((float) sumFrom0To(
                currentLevel - maxVanillaLevel) / sumFrom0To(
                maxModdedLevel - maxVanillaLevel)));
    }

    private static int sumFrom0To(int num) {
        int sum = 0;
        while (num > 0) {
            sum += num--;
        }
        return sum;
    }
}
