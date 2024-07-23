package dev.xkmc.l2tabs.data;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public enum L2TabsLangData
{
    INVENTORY("menu.tabs.inventory", "Inventory", "背包", 0),
    ATTRIBUTE("menu.tabs.attribute", "Attributes", "数值", 0),
    CURIOS("menu.tabs.trinkets", "Trinkets", "饰品", 0),
    DETAIL("menu.tabs.attribute.detail", "Press Shift to show details", "按Shift显示细节", 0),
    BASE("menu.tabs.attribute.base", "Base value: %s", "实体基础数值: %s", 1),
    ADD("menu.tabs.attribute.add", "Addition: %s", "加法乘区: %s", 1),
    MULT_BASE("menu.tabs.attribute.mult_base", "Multiply Base: %s", "乘法乘区: %s", 1),
    MULT_TOTAL("menu.tabs.attribute.mult_all", "Multiply total: %s", "独立乘法乘区: %s", 1),
    FORMAT("menu.tabs.attribute.format", "(%s%s)x(1%s)x%s=%s", "(%s%s)x(1%s)x%s=%s", 5),
    INTRINSIC("menu.tabs.attribute.intrinsic", "Intrinsic Value: %s", "数值基值: %s", 1);

    public final String key, defEn, defZh;
    private final int arg;

    L2TabsLangData(String key, String defEn, String defZh, int arg)
    {
        this.key = key;
        this.defEn = defEn;
        this.defZh = defZh;
        this.arg = arg;
    }

    public MutableText get(Object... args)
    {
        if (args.length != arg)
            throw new IllegalArgumentException("for " + name() + ": expect " + arg + " parameters, got " + args.length);
        return Text.translatable(key, args);
    }
}
