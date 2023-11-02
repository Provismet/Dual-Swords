package com.provismet.dualswords.utility;

import net.minecraft.util.math.MathHelper;

public class MoreMath {
    public static double roundDownToMultiple (double value, double denominator) {
        return (double)MathHelper.floor(value / denominator) * denominator;
    }
}
