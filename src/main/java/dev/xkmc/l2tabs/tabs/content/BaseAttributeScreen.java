package dev.xkmc.l2tabs.tabs.content;

import dev.xkmc.l2tabs.data.AttributeDisplayConfig;
import dev.xkmc.l2tabs.data.L2TabsConfig;
import dev.xkmc.l2tabs.data.L2TabsLangData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.item.ItemStack.MODIFIER_FORMAT;

public abstract class BaseAttributeScreen extends BaseTextScreen
{
    private static int getSize()
    {
        return L2TabsConfig.client().attributeLinePerPage;
    }

    private final int page;

    protected BaseAttributeScreen(Text title, int page)
    {
        super(title, new Identifier("l2tabs:textures/gui/empty.png"));
        this.page = page;
    }

    public abstract LivingEntity getEntity();

    @Override
    public void init()
    {
        super.init();
        int w = 10;
        int h = 11;
        int size = AttributeDisplayConfig.get(getEntity()).size();
        int totalPage = (size - 1) / getSize() + 1;
        int x = (this.width + this.imageWidth) / 2 - 16;
        int y = (this.height - this.imageHeight) / 2 + 4;
        if (page > 0)
            addDrawableChild(ButtonWidget.builder(Text.literal("<"), e -> click(-1)).position(x - w - 1, y).size(w, h).build());
        if (page < totalPage - 1)
            addDrawableChild(ButtonWidget.builder(Text.literal(">"), e -> click(1)).position(x, y).size(w, h).build());
    }

    protected abstract void click(int nextPage);

    @Override
    public void render(DrawContext g, int mx, int my, float tick)
    {
        super.render(g, mx, my, tick);
        render(g, mx, my, tick, getEntity(), AttributeDisplayConfig.get(getEntity()));
    }

    public void render(DrawContext g, int mx, int my, float tick, LivingEntity player, List<AttributeEntry> list)
    {
        int x = leftPos + 8;
        int y = topPos + 6;
        AttributeEntry focus = null;
        int count = 0;
        for (AttributeEntry entry : list)
        {
            count++;
            if (count <= page * getSize() || count > (page + 1) * getSize()) continue;
            double val = player.getAttributeValue(entry.attr());
            Text comp = Text.translatable(
                    "attribute.modifier.equals." + (entry.usePercent() ? 1 : 0),
                    MODIFIER_FORMAT.format(entry.usePercent() ? val * 100 : val),
                    Text.translatable(entry.attr().getTranslationKey()));
            g.drawText(textRenderer, comp, x, y, 0, false);
            if (mx > x && mx < x + textRenderer.getWidth(comp) && my > y && my < y + 10) focus = entry;
            y += 10;
        }
        if (focus != null)
            g.drawTooltip(textRenderer, getAttributeDetail(player, focus), mx, my);
    }

    public List<Text> getAttributeDetail(LivingEntity entity, AttributeEntry entry)
    {
        var ans = getAttributeDetail(entity, entry.attr());
        if (entry.intrinsic() != 0)
            ans.add(L2TabsLangData.INTRINSIC.get(number("%s", entry.intrinsic())).formatted(Formatting.BLUE));
        return ans;
    }

    public List<Text> getAttributeDetail(LivingEntity entity, EntityAttribute attr)
    {
        EntityAttributeInstance ins = entity.getAttributeInstance(attr);
        if (ins == null) return List.of();
        var adds = ins.getModifiers(EntityAttributeModifier.Operation.ADDITION);
        var m0s = ins.getModifiers(EntityAttributeModifier.Operation.MULTIPLY_BASE);
        var m1s = ins.getModifiers(EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        double base = ins.getBaseValue();
        double addv = 0;
        double m0v = 0;
        double m1v = 1;
        for (var e : adds) addv += e.getValue();
        for (var e : m0s) m0v += e.getValue();
        for (var e : m1s) m1v *= 1 + e.getValue();
        double total = (base + addv) * (1 + m0v) * m1v;
        List<Text> ans = new ArrayList<>();
        ans.add(Text.translatable(attr.getTranslationKey()).formatted(Formatting.GOLD));
        boolean shift = Screen.hasShiftDown();
        ans.add(L2TabsLangData.BASE.get(number("%s", base)).formatted(Formatting.BLUE));
        ans.add(L2TabsLangData.ADD.get(numberSigned("%s", addv)).formatted(Formatting.BLUE));
        if (shift)
            for (var e : adds)
                ans.add(numberSigned("%s", e.getValue()).append(name(e)));
        ans.add(L2TabsLangData.MULT_BASE.get(numberSigned("%s%%", m0v * 100)).formatted(Formatting.BLUE));
        if (shift)
            for (var e : m0s)
                ans.add(numberSigned("%s%%", e.getValue() * 100).append(name(e)));
        ans.add(L2TabsLangData.MULT_TOTAL.get(number("x%s", m1v)).formatted(Formatting.BLUE));
        if (shift)
            for (var e : m1s)
                ans.add(number("x%s", 1 + e.getValue()).append(name(e)));
        ans.add(L2TabsLangData.FORMAT.get(
                number("%s", base),
                numberSigned("%s", addv),
                numberSigned("%s", m0v),
                number("%s", m1v),
                number("%s", total)));
        if (!shift) ans.add(L2TabsLangData.DETAIL.get().formatted(Formatting.GRAY));
        return ans;
    }

    private static MutableText number(String base, double value)
    {
        return Text.literal(String.format(base, MODIFIER_FORMAT.format(value))).formatted(Formatting.GREEN);
    }

    private static MutableText numberSigned(String base, double value)
    {
        if (value >= 0)
            return Text.literal(String.format("+" + base, MODIFIER_FORMAT.format(value))).formatted(Formatting.GREEN);
        return Text.literal(String.format("-" + base, MODIFIER_FORMAT.format(-value))).formatted(Formatting.RED);
    }

    private static MutableText name(EntityAttributeModifier e)
    {
        if (e.getName().equals("Unknown synced attribute modifier")) return Text.empty();
        return Text.literal("  (" + e.getName() + ")").formatted(Formatting.DARK_GRAY);
    }
}