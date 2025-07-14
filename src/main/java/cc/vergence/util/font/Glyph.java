package cc.vergence.util.font;

public record Glyph(int u, int v, int width, int height, char value, GlyphMap owner) {
}