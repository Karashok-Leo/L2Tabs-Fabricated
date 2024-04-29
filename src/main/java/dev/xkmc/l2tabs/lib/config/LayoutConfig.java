package dev.xkmc.l2tabs.lib.config;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2tabs.mixin.HandledScreenAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@SuppressWarnings("unused")
@SerialClass
public class LayoutConfig
{
    public static LayoutConfig of(Identifier id, int height)
    {
        return new LayoutConfig(id, height);
    }

    public static Rect rect(int x, int y, int w, int h, int rx, int ry)
    {
        Rect rect = new Rect();
        rect.x = x;
        rect.y = y;
        rect.w = w;
        rect.h = h;
        rect.rx = rx;
        rect.ry = ry;
        return rect;
    }

    protected Identifier id;
    @SerialClass.SerialField
    public int height;
    @SerialClass.SerialField
    public HashMap<String, Rect> side, comp;

    @Deprecated
    public LayoutConfig()
    {
    }

    public LayoutConfig(Identifier id, int height)
    {
        this.id = id;
        this.height = height;
        this.side = new HashMap<>();
        this.comp = new HashMap<>();
    }

    public Identifier getTexture()
    {
        return new Identifier(id.getNamespace(), "textures/gui/container/" + id.getPath() + ".png");
    }

    /**
     * get the location of the component on the GUI
     */
    public Rect getComp(String key)
    {
        return comp.getOrDefault(key, Rect.ZERO);
    }

    /**
     * Height of this GUI
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * The X position of the player inventory
     */
    public int getPlInvX()
    {
        return 8;
    }

    /**
     * The Y position of the player inventory
     */
    public int getPlInvY()
    {
        return height - 82;
    }

    @Environment(EnvType.CLIENT)
    public ScreenRenderer getRenderer(HandledScreen<?> gui)
    {
        return new ScreenRenderer(gui);
    }

    @Environment(EnvType.CLIENT)
    public ScreenRenderer getRenderer(Screen gui, int x, int y, int w, int h)
    {
        return new ScreenRenderer(gui, x, y, w, h);
    }

    /**
     * get the rectangle representing the sprite element on the sprite
     */
    public Rect getSide(String key)
    {
        return side.getOrDefault(key, Rect.ZERO);
    }

    /**
     * configure the coordinate of the slot
     */
    public <T extends Slot> void getSlot(String key, SlotFactory<T> fac, SlotAcceptor con)
    {
        Rect c = getComp(key);
        for (int j = 0; j < c.ry; j++)
            for (int i = 0; i < c.rx; i++)
            {
                var slot = fac.getSlot(c.x + i * c.w, c.y + j * c.h);
                if (slot != null)
                    con.addSlot(key, i, j, slot);
            }
    }

    public int getWidth()
    {
        return 176;
    }

    /**
     * return if the coordinate is within the rectangle represented by the key
     */
    public boolean within(String key, double x, double y)
    {
        Rect c = getComp(key);
        return x > c.x && x < c.x + c.w && y > c.y && y < c.y + c.h;
    }

    public interface SlotFactory<T extends Slot>
    {
        @Nullable
        T getSlot(int x, int y);
    }

    public interface SlotAcceptor
    {
        void addSlot(String name, int i, int j, Slot slot);
    }

    @SerialClass
    public static class Rect
    {

        public static final Rect ZERO = new Rect();

        @SerialClass.SerialField
        public int x, y, w, h, rx = 1, ry = 1;

        public Rect()
        {
        }

    }

    @Environment(EnvType.CLIENT)
    public class ScreenRenderer
    {

        private final int x, y, w, h;
        private final Screen scr;

        public ScreenRenderer(Screen gui, int x, int y, int w, int h)
        {
            this.scr = gui;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        private ScreenRenderer(HandledScreen<?> scrIn)
        {
            var accessor = ((HandledScreenAccessor) scrIn);
            this.x = accessor.getX();
            this.y = accessor.getY();
            this.w = accessor.getBackgroundWidth();
            this.h = accessor.getBackgroundHeight();
            this.scr = scrIn;
        }

        /**
         * Draw a side sprite on the location specified by the component
         */
        public void draw(DrawContext context, String c, String s)
        {
            Rect cr = getComp(c);
            Rect sr = getSide(s);
            context.drawTexture(getTexture(), x + cr.x, y + cr.y, sr.x, sr.y, sr.w, sr.h);
        }

        /**
         * Draw a side sprite on the location specified by the component with offsets
         */
        public void draw(DrawContext context, String c, String s, int xoff, int yoff)
        {
            Rect cr = getComp(c);
            Rect sr = getSide(s);
            context.drawTexture(getTexture(), x + cr.x + xoff, y + cr.y + yoff, sr.x, sr.y, sr.w, sr.h);
        }

        /**
         * Draw a side sprite on the location specified by the component. Draw partially
         * from bottom to top
         */
        public void drawBottomUp(DrawContext context, String c, String s, int prog, int max)
        {
            if (prog == 0 || max == 0)
                return;
            Rect cr = getComp(c);
            Rect sr = getSide(s);
            int dh = sr.h * prog / max;
            context.drawTexture(getTexture(), x + cr.x, y + cr.y + sr.h - dh, sr.x, sr.y + sr.h - dh, sr.w, dh);
        }

        /**
         * Draw a side sprite on the location specified by the component. Draw partially
         * from left to right
         */
        public void drawLeftRight(DrawContext context, String c, String s, int prog, int max)
        {
            if (prog == 0 || max == 0)
                return;
            Rect cr = getComp(c);
            Rect sr = getSide(s);
            int dw = sr.w * prog / max;
            context.drawTexture(getTexture(), x + cr.x, y + cr.y, sr.x, sr.y, dw, sr.h);
        }

        /**
         * fill an area with a sprite, repeat as tiles if not enough, start from lower
         * left corner
         */
        public void drawLiquid(DrawContext context, String c, double per, int height, int sw, int sh)
        {
            Rect cr = getComp(c);
            int base = cr.y + height;
            int h = (int) Math.round(per * height);
            circularBlit(context, x + cr.x, base - h, 0, -h, cr.w, h, sw, sh);
        }

        /**
         * bind texture, draw background color, and GUI background
         */
        public void start(DrawContext context)
        {
            scr.renderBackground(context);
            context.drawTexture(getTexture(), x, y, 0, 0, w, h);
        }

        private void circularBlit(DrawContext context, int sx, int sy, int ix, int iy, int w, int h, int iw, int ih)
        {
            int x0 = ix, yb = iy, x1 = w, x2 = sx;
            while (x0 < 0)
                x0 += iw;
            while (yb < ih)
                yb += ih;
            while (x1 > 0)
            {
                int dx = Math.min(x1, iw - x0);
                int y0 = yb, y1 = h, y2 = sy;
                while (y1 > 0)
                {
                    int dy = Math.min(y1, ih - y0);
                    context.drawTexture(getTexture(), x2, y2, x0, y0, x1, y1);
                    y1 -= dy;
                    y0 += dy;
                    y2 += dy;
                }
                x1 -= dx;
                x0 += dx;
                x2 += dx;
            }
        }
    }
}
