package cc.vergence.util.font;

import cc.vergence.util.interfaces.Wrapper;
import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.Closeable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FontRenderer implements Closeable, Wrapper {
    private static final Char2IntArrayMap colorCodes = new Char2IntArrayMap() {{
        put('0', 0x000000);
        put('1', 0x0000AA);
        put('2', 0x00AA00);
        put('3', 0x00AAAA);
        put('4', 0xAA0000);
        put('5', 0xAA00AA);
        put('6', 0xFFAA00);
        put('7', 0xAAAAAA);
        put('8', 0x555555);
        put('9', 0x5555FF);
        put('A', 0x55FF55);
        put('B', 0x55FFFF);
        put('C', 0xFF5555);
        put('D', 0xFF55FF);
        put('E', 0xFFFF55);
        put('F', 0xFFFFFF);
    }};

    private static final int BLOCK_SIZE = 256;
    private static final Object2ObjectArrayMap<Identifier, ObjectList<DrawEntry>> GLYPH_PAGE_CACHE = new Object2ObjectArrayMap<>();
    private final float originalSize;
    private final ObjectList<GlyphMap> maps = new ObjectArrayList<>();
    private final Char2ObjectArrayMap<Glyph> allGlyphs = new Char2ObjectArrayMap<>();
    private int scaleMul = 0;
    private Font[] fonts;
    private int previousGameScale = -1;
    private static final char RND_START = 'a';
    private static final char RND_END = 'z';
    private static final Random RND = new Random();

    public FontRenderer(Font @NotNull [] fonts, float sizePx) {
        Preconditions.checkArgument(fonts.length > 0, "fonts.length == 0");
        this.originalSize = sizePx;
        init(fonts, sizePx);
    }

    private static int floorNearestMulN(int x) {
        return FontRenderer.BLOCK_SIZE * (int) Math.floor((double) x / (double) FontRenderer.BLOCK_SIZE);
    }

    public static @NotNull String stripControlCodes(@NotNull String text) {
        char[] chars = text.toCharArray();
        StringBuilder f = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '§') {
                i++;
                continue;
            }
            f.append(c);
        }
        return f.toString();
    }

    private void sizeCheck() {
        int gs = getGuiScale();
        if (gs != this.previousGameScale) {
            close();
            init(this.fonts, this.originalSize);
        }
    }

    private void init(Font @NotNull [] fonts, float sizePx) {
        this.previousGameScale = getGuiScale();
        this.scaleMul = this.previousGameScale;
        this.fonts = new Font[fonts.length];
        for (int i = 0; i < fonts.length; i++) {
            this.fonts[i] = fonts[i].deriveFont(sizePx * this.scaleMul);
        }
    }

    private @NotNull GlyphMap generateMap(char from, char to) {
        GlyphMap gm = new GlyphMap(from, to, this.fonts, randomIdentifier());
        maps.add(gm);
        return gm;
    }

    private Glyph locateGlyph0(char glyph) {
        for (GlyphMap map : maps) {
            if (map.contains(glyph)) {
                return map.getGlyph(glyph);
            }
        }
        int base = floorNearestMulN(glyph);
        GlyphMap glyphMap = generateMap((char) base, (char) (base + BLOCK_SIZE));
        return glyphMap.getGlyph(glyph);
    }

    private Glyph locateGlyph1(char glyph) {
        return allGlyphs.computeIfAbsent(glyph, this::locateGlyph0);
    }

    public void drawStringWithShadow(@NotNull MatrixStack stack, @NotNull String s, float x, float y, float r, float g, float b, float a) {
        drawString(stack, s, x + 1, y + 1, 0, 0, 0, a, true);
        drawString(stack, s, x, y, r, g, b, a, false);
    }

    public void drawString(MatrixStack stack, String s, float x, float y, float r, float g, float b, float a, boolean shadow) {
        sizeCheck();
        float r2 = r, g2 = g, b2 = b;
        stack.push();
        stack.translate(x, y, 0);
        stack.scale(1f / this.scaleMul, 1f / this.scaleMul, 1f);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        Matrix4f mat = stack.peek().getPositionMatrix();
        char[] chars = s.toCharArray();
        float xOffset = 0, yOffset = 0;
        boolean inSel = false;
        int lineStart = 0;

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (inSel) {
                inSel = false;
                char c1 = Character.toUpperCase(c);
                if (colorCodes.containsKey(c1) && !shadow) {
                    int col = colorCodes.get(c1);
                    int[] rgb = RGBIntToRGB(col);
                    r2 = rgb[0] / 255f;
                    g2 = rgb[1] / 255f;
                    b2 = rgb[2] / 255f;
                } else if (c1 == 'R') {
                    r2 = r;
                    g2 = g;
                    b2 = b;
                }
                continue;
            }
            if (c == '§') {
                inSel = true;
                continue;
            } else if (c == '\n') {
                yOffset += getStringHeight(s.substring(lineStart, i)) * scaleMul;
                xOffset = 0;
                lineStart = i + 1;
                continue;
            }

            Glyph glyph = locateGlyph1(c);
            if (glyph != null && glyph.value() != ' ') {
                Identifier tex = glyph.owner().bindToTexture;
                DrawEntry entry = new DrawEntry(xOffset, yOffset, r2, g2, b2, glyph);
                GLYPH_PAGE_CACHE.computeIfAbsent(tex, k -> new ObjectArrayList<>()).add(entry);
            }
            if (glyph != null) {
                xOffset += glyph.width();
            }
        }

        for (Identifier identifier : GLYPH_PAGE_CACHE.keySet()) {
            List<DrawEntry> entries = GLYPH_PAGE_CACHE.get(identifier);
            if (entries == null || entries.isEmpty()) continue;

            RenderSystem.setShaderTexture(0, identifier);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder bb = tess.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            for (DrawEntry entry : entries) {
                float xo = entry.atX;
                float yo = entry.atY;
                float cr = entry.r;
                float cg = entry.g;
                float cb = entry.b;
                Glyph glyph = entry.toDraw;
                GlyphMap owner = glyph.owner();
                float w = glyph.width();
                float h = glyph.height();
                float u1 = (float) glyph.u() / owner.width;
                float v1 = (float) glyph.v() / owner.height;
                float u2 = (float) (glyph.u() + w) / owner.width;
                float v2 = (float) (glyph.v() + h) / owner.height;

                bb.vertex(mat, xo + 0, yo + h, 0).texture(u1, v2).color(cr, cg, cb, a);
                bb.vertex(mat, xo + w, yo + h, 0).texture(u2, v2).color(cr, cg, cb, a);
                bb.vertex(mat, xo + w, yo + 0, 0).texture(u2, v1).color(cr, cg, cb, a);
                bb.vertex(mat, xo + 0, yo + 0, 0).texture(u1, v1).color(cr, cg, cb, a);
            }

            BufferRenderer.drawWithGlobalProgram(bb.end());
        }

        stack.pop();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        GLYPH_PAGE_CACHE.clear();
    }

    @Override
    public void close() {
        for (GlyphMap map : maps) {
            map.destroy();
        }
        maps.clear();
        allGlyphs.clear();
    }

    public static int getGuiScale() {
        return (int) mc.getWindow().getScaleFactor();
    }

    public static int @NotNull [] RGBIntToRGB(int in) {
        int red = in >> 8 * 2 & 0xFF;
        int green = in >> 8 & 0xFF;
        int blue = in & 0xFF;
        return new int[]{red, green, blue};
    }

    public void drawCenteredString(MatrixStack stack, String s, float x, float y, float r, float g, float b, float a, boolean shadow) {
        drawString(stack, s, x - getStringWidth(s) / 2f, y, r, g, b, a, shadow);
    }

    public float getStringWidth(String text) {
        char[] c = stripControlCodes(text).toCharArray();
        float currentLine = 0;
        float maxPreviousLines = 0;
        for (char c1 : c) {
            if (c1 == '\n') {
                maxPreviousLines = Math.max(currentLine, maxPreviousLines);
                currentLine = 0;
                continue;
            }
            Glyph glyph = locateGlyph1(c1);

            float gWidth = glyph == null ? 1f : glyph.width();

            currentLine += gWidth / (float) this.scaleMul;
        }
        return Math.max(currentLine, maxPreviousLines);
    }

    public float getStringHeight(String text) {
        char[] c = stripControlCodes(text).toCharArray();
        if (c.length == 0) {
            c = new char[]{' '};
        }
        float currentLine = 0;
        float previous = 0;
        for (char c1 : c) {
            if (c1 == '\n') {
                if (currentLine == 0) {
                    currentLine = locateGlyph1(' ').height() / (float) this.scaleMul;
                }
                previous += currentLine;
                currentLine = 0;
                continue;
            }
            Glyph glyph = locateGlyph1(c1);
            float gHeight = glyph != null ? glyph.height() : 1f;
            currentLine = Math.max(gHeight / (float) this.scaleMul, currentLine);
        }
        return currentLine + previous;
    }

    public void drawGradientString(@NotNull MatrixStack stack, @NotNull String s, float x, float y) {
        sizeCheck();
        stack.push();
        stack.translate(x, y, 0);
        stack.scale(1f / this.scaleMul, 1f / this.scaleMul, 1f);

//        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        Matrix4f mat = stack.peek().getPositionMatrix();
        char[] chars = s.toCharArray();
        float xOffset = 0;
        float yOffset = 0;
        int lineStart = 0;
        float a = 1f;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            Color color = new Color(0, 0, 0);
            a = color.getAlpha() / 255f;

            if (c == '\n') {
                yOffset += getStringHeight(s.substring(lineStart, i)) * scaleMul;
                xOffset = 0;
                lineStart = i + 1;
                continue;
            }
            Glyph glyph = locateGlyph1(c);
            if (glyph.value() != ' ') {
                Identifier i1 = glyph.owner().bindToTexture;
                DrawEntry entry = new DrawEntry(xOffset, yOffset, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, glyph);
                GLYPH_PAGE_CACHE.computeIfAbsent(i1, integer -> new ObjectArrayList<>()).add(entry);
            }
            xOffset += glyph.width();
        }
        for (Identifier identifier : GLYPH_PAGE_CACHE.keySet()) {
            RenderSystem.setShaderTexture(0, identifier);
            List<DrawEntry> objects = GLYPH_PAGE_CACHE.get(identifier);

            tess.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            for (DrawEntry object : objects) {
                float xo = object.atX;
                float yo = object.atY;
                float cr = object.r;
                float cg = object.g;
                float cb = object.b;
                Glyph glyph = object.toDraw;
                GlyphMap owner = glyph.owner();
                float w = glyph.width();
                float h = glyph.height();
                float u1 = (float) glyph.u() / owner.width;
                float v1 = (float) glyph.v() / owner.height;
                float u2 = (float) (glyph.u() + glyph.width()) / owner.width;
                float v2 = (float) (glyph.v() + glyph.height()) / owner.height;

                bb.vertex(mat, xo + 0, yo + h, 0).texture(u1, v2).color(cr, cg, cb, a);
                bb.vertex(mat, xo + w, yo + h, 0).texture(u2, v2).color(cr, cg, cb, a);
                bb.vertex(mat, xo + w, yo + 0, 0).texture(u2, v1).color(cr, cg, cb, a);
                bb.vertex(mat, xo + 0, yo + 0, 0).texture(u1, v1).color(cr, cg, cb, a);
            }
            BufferRenderer.drawWithGlobalProgram(bb.end());
        }

        stack.pop();
        GLYPH_PAGE_CACHE.clear();
    }

    @Contract(value = "-> new", pure = true)
    public static @NotNull Identifier randomIdentifier() {
        return Identifier.of("vergence", "temp/" + randomString(48));
    }

    private static String randomString(int length) {
        return IntStream.range(0, length)
                .mapToObj(operand -> String.valueOf((char) RND.nextInt(RND_START, RND_END + 1)))
                .collect(Collectors.joining());
    }

    record DrawEntry(float atX, float atY, float r, float g, float b, Glyph toDraw) {
    }
}
