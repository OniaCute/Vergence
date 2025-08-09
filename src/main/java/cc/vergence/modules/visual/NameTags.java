package cc.vergence.modules.visual;

import cc.vergence.Vergence;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.injections.accessors.render.ItemRenderStateAccessor;
import cc.vergence.injections.accessors.render.ItemRendererAccessor;
import cc.vergence.injections.accessors.render.LayerRenderStateAccessor;
import cc.vergence.modules.Module;
import cc.vergence.modules.misc.NameProtect;
import cc.vergence.util.color.ColorUtil;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import cc.vergence.util.render.utils.Render3DUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MatrixUtil;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.List;

public class NameTags extends Module {
    public static NameTags INSTANCE;
    private final ItemRenderState itemRenderState = new ItemRenderState();

    public NameTags() {
        super("NameTags", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Boolean> antiBot = addOption(new BooleanOption("AntiBot", true));
    public Option<Boolean> forMyself = addOption(new BooleanOption("Myself", false));
    public Option<Enum<?>> fontSize = addOption(new EnumOption("FonsSize", FontSize.SMALL));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(14, 14, 14)));
    public Option<Color> infoColor = addOption(new ColorOption("InfoColor", new Color(14, 14, 14)));
    public Option<EnumSet<Modes>> mode = addOption(new MultipleOption<Modes>("Modes", EnumSet.of(Modes.Fill, Modes.Outline)));
    public Option<Color> fillColor = addOption(new ColorOption("FillColor", new Color(234, 234, 234), v -> mode.getValue().contains(Modes.Fill)));
    public Option<Color> outlineColor = addOption(new ColorOption("OutlineColor", new Color(246, 246, 246), v -> mode.getValue().contains(Modes.Outline)));
    public Option<Double> outlineWidth = addOption(new DoubleOption("OutlineWidth", 0, 4, 1, v -> mode.getValue().contains(Modes.Outline)));
    public Option<Boolean> forFriend = addOption(new BooleanOption("Friend", false));
    public Option<Color> friendTextColor = addOption(new ColorOption("FriendTextColor", new Color(14, 14, 14), v -> forFriend.getValue()));
    public Option<Color> friendInfoColor = addOption(new ColorOption("FriendInfoColor", new Color(14, 14, 14), v -> forFriend.getValue()));
    public Option<Color> friendFillColor = addOption(new ColorOption("FriendFillColor", new Color(77, 160, 255), v -> mode.getValue().contains(Modes.Fill) && forFriend.getValue()));
    public Option<Color> friendOutlineColor = addOption(new ColorOption("FriendOutlineColor", new Color(65, 145, 255), v -> mode.getValue().contains(Modes.Outline) && forFriend.getValue()));
    public Option<Boolean> forEnemy = addOption(new BooleanOption("Enemy", false));
    public Option<Color> enemyTextColor = addOption(new ColorOption("EnemyTextColor", new Color(14, 14, 14), v -> forEnemy.getValue()));
    public Option<Color> enemyInfoColor = addOption(new ColorOption("EnemyInfoColor", new Color(14, 14, 14), v -> forEnemy.getValue()));
    public Option<Color> enemyFillColor = addOption(new ColorOption("EnemyFillColor", new Color(255, 39, 39, 215), v -> mode.getValue().contains(Modes.Fill) && forEnemy.getValue()));
    public Option<Color> enemyOutlineColor = addOption(new ColorOption("EnemyOutlineColor", new Color(227, 29, 29, 231), v -> mode.getValue().contains(Modes.Outline) && forEnemy.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true, v -> !mode.getValue().isEmpty()));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 3, v -> rounded.getValue() && !mode.getValue().isEmpty()));
    public Option<Boolean> withPing = addOption(new BooleanOption("Ping", true));
    public Option<Boolean> withHealth = addOption(new BooleanOption("Health", true));
    public Option<Boolean> withDistance = addOption(new BooleanOption("Distance", true));
    public Option<Boolean> withGameMode = addOption(new BooleanOption("GameMode", true));
    public Option<Boolean> withPop = addOption(new BooleanOption("Popped", true));
    public Option<Boolean> withItems = addOption(new BooleanOption("Items", false));
    public Option<Boolean> withItemName = addOption(new BooleanOption("ItemName", false));
    public Option<Boolean> withEnchantments = addOption(new BooleanOption("Enchantments", false));
    public Option<Boolean> withDurability = addOption(new BooleanOption("Durability", false));
    public Option<Double> heightOffset = addOption(new DoubleOption("Height", -2.0, 2.0, 0.0));
    public Option<Double> maxScale = addOption(new DoubleOption("MaxScale", 10, 100, 30));
    public Option<Double> minScale = addOption(new DoubleOption("MineScale", 0.01, 2, 0.0245));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (isNull()) {
            return;
        }

        VertexConsumerProvider.Immediate vertexConsumers = mc.getBufferBuilders().getEntityVertexConsumers();

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player && !forMyself.getValue()) {
                continue;
            }
            if (antiBot.getValue() && EntityUtil.isBot(player)) {
                continue;
            }
            if (!Render3DUtil.isFrustumVisible(player.getBoundingBox())) {
                continue;
            }

            double x = MathHelper.lerp(tickDelta, player.lastRenderX, player.getX());
            double y = MathHelper.lerp(tickDelta, player.lastRenderY, player.getY()) + (player.isSneaking() ? 1.9f : 2.1f) + heightOffset.getValue();
            double z = MathHelper.lerp(tickDelta, player.lastRenderZ, player.getZ());

            Vec3d vec3d = new Vec3d(x - mc.getEntityRenderDispatcher().camera.getPos().x, y - mc.getEntityRenderDispatcher().camera.getPos().y, z - mc.getEntityRenderDispatcher().camera.getPos().z);
            float distance = (float) Math.sqrt(mc.getEntityRenderDispatcher().camera.getPos().squaredDistanceTo(x, y, z));
            float scaling = 0.0018f + (maxScale.getValue().intValue() / 10000.0f) * distance;
            if (distance <= 6.0) {
                scaling = minScale.getValue().floatValue();
            }

            matrixStack.push();
            matrixStack.translate(vec3d.x, vec3d.y, vec3d.z);
            matrixStack.multiply(mc.getEntityRenderDispatcher().getRotation());
            matrixStack.scale(scaling, -scaling, scaling);

            String displayString =  player == mc.player ? (NameProtect.INSTANCE.getStatus() ? NameProtect.INSTANCE.nickname.getValue() : mc.player.getName().getString()) : player.getName().getString();
            if (withHealth.getValue()) {
                displayString = ColorUtil.getHealthColor(player.getHealth() + player.getAbsorptionAmount()) + new DecimalFormat("0.0").format(player.getHealth() + player.getAbsorptionAmount()) + Formatting.RESET + " " + displayString;
            }
            if (withGameMode.getValue()) {
                displayString = "[" + EntityUtil.getGameModeText(EntityUtil.getGameMode(player)) + "] " + displayString;
            }
            if (withPing.getValue()) {
                displayString += " " + ColorUtil.getPingColor(EntityUtil.getLatency(player)) + EntityUtil.getLatency(player) + "ms" + Formatting.RESET;
            }
            if (withDistance.getValue()) {
                displayString += " " + new DecimalFormat("0.0").format(EntityUtil.getDistance(player)) + "m ";
            }
            if (withPop.getValue()) {
                displayString += Vergence.POP.getPop(player.getName().getString()) != 0 ? Vergence.POP.getPop(player.getName().getString()) + "POP" : "";
            }

            boolean isFriend = forFriend.getValue() && Vergence.FRIEND.isFriend(player.getName().getString());
            boolean isEnemy = forEnemy.getValue() && Vergence.ENEMY.isEnemy(player.getName().getString());

            Color textColorToUse = textColor.getValue();
            Color infoColorToUse = infoColor.getValue();
            Color fillColorToUse = fillColor.getValue();
            Color outlineColorToUse = outlineColor.getValue();

            if (isFriend) {
                textColorToUse = friendTextColor.getValue();
                infoColorToUse = friendInfoColor.getValue();
                fillColorToUse = friendFillColor.getValue();
                outlineColorToUse = friendOutlineColor.getValue();
            } else if (isEnemy) {
                textColorToUse = enemyTextColor.getValue();
                infoColorToUse = enemyInfoColor.getValue();
                fillColorToUse = enemyFillColor.getValue();
                outlineColorToUse = enemyOutlineColor.getValue();
            }

            double fontWidth = FontUtil.getWidth((FontSize) fontSize.getValue(), displayString);
            float pad = outlineWidth.getValue().floatValue();
            float left = -(float) (fontWidth / 2.0f) - pad;
            float top = (float) (-FontUtil.getHeight((FontSize) fontSize.getValue()) - pad);
            float right = (float) (fontWidth / 2.0f) + pad;

            if (mode.getValue().contains(Modes.Fill) || mode.getValue().contains(Modes.Outline)) {
                float radiusF = radius.getValue().floatValue();
                float outline = mode.getValue().contains(Modes.Outline) ? outlineWidth.getValue().floatValue() : 0.0f;
                Render2DUtil.drawRoundedRectWithOutline(
                        left, top,
                        right - left, pad - top,
                        rounded.getValue() ? radiusF : 0.0f,
                        outline,
                        mode.getValue().contains(Modes.Fill) ? fillColorToUse : new Color(0, 0, 0, 0),
                        mode.getValue().contains(Modes.Outline) ? outlineColorToUse : new Color(0, 0, 0, 0)
                );
            }

            FontUtil.drawText(
                    displayString,
                    -fontWidth / 2,
                    -FontUtil.getHeight((FontSize) fontSize.getValue()) + 2,
                    textColorToUse,
                    (FontSize) fontSize.getValue()
            );

            boolean renderedDurability = false;
            boolean renderedItems = false;
            double maxEnchants = 0;

            if (withEnchantments.getValue()) {
                for (int i = 0; i < 6; i++) {
                    ItemStack stack = getItem(player, i);
                    ItemEnchantmentsComponent component = EnchantmentHelper.getEnchantments(stack);

                    if (!component.getEnchantments().isEmpty()) {
                        double height = (component.getEnchantments().size() * FontUtil.getHeight((FontSize) fontSize.getValue()) / 2) - 18;
                        if (height > 0 && (height + 1) > maxEnchants) maxEnchants = height + 1;
                    }
                }
            }

            // items and armors
            for (int i = 0; i < 6; i++) {
                ItemStack stack = getItem(player, i);
                if (stack.isEmpty()) continue;

                renderedItems = true;

                double stackX = -(108 / 2) + (i * 18) + 1;
                double stackY = -FontUtil.getHeight((FontSize) fontSize.getValue()) - 1 - (withItems.getValue() ? 18 + maxEnchants : 1);

                if (withItems.getValue()) {
                    ((ItemRendererAccessor) mc.getItemRenderer()).getItemModelManager().update(itemRenderState, stack, ModelTransformationMode.GUI, false, mc.world, mc.player, 0);

                    matrixStack.push();
                    matrixStack.translate(stackX + 8, stackY + 8, 0);
                    matrixStack.scale(16.0F, -16.0F, -0.001f);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    GL11.glDepthFunc(GL11.GL_ALWAYS);

                    for (int j = 0; j < ((ItemRenderStateAccessor) itemRenderState).getLayerCount(); j++) {
                        ItemRenderState.LayerRenderState layer = ((ItemRenderStateAccessor) itemRenderState).getLayers()[j];

                        matrixStack.push();
                        ((LayerRenderStateAccessor) layer).getModel().getTransformation().getTransformation(ModelTransformationMode.GUI).apply(false, matrixStack);
                        matrixStack.translate(-0.5F, -0.5F, -0.5F);

                        if (((LayerRenderStateAccessor) layer).getSpecialModelType() != null) {
                            ((LayerRenderStateAccessor) layer).getSpecialModelType().render(((LayerRenderStateAccessor) layer).getData(), ModelTransformationMode.GUI, matrixStack, vertexConsumers, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, ((LayerRenderStateAccessor) layer).getGlint() != ItemRenderState.Glint.NONE);
                        } else if (((LayerRenderStateAccessor) layer).getModel() != null) {
                            VertexConsumer vertexConsumer;
                            RenderLayer renderLayer = RenderLayer.getGuiTextured(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

                            if (((LayerRenderStateAccessor) layer).getGlint() == ItemRenderState.Glint.SPECIAL) {
                                MatrixStack.Entry entry = matrixStack.peek().copy();
                                MatrixUtil.scale(entry.getPositionMatrix(), 0.5F);

                                vertexConsumer = ItemRendererAccessor.invokeGetDynamicDisplayGlintConsumer(vertexConsumers, renderLayer, entry);
                            } else {
                                vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumers, renderLayer, true, ((LayerRenderStateAccessor) layer).getGlint() != ItemRenderState.Glint.NONE);
                            }

                            ItemRendererAccessor.inovkeRenderBakedItemModel(((LayerRenderStateAccessor) layer).getModel(), ((LayerRenderStateAccessor) layer).getTints(), LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumer);
                        }

                        matrixStack.pop();
                    }

                    vertexConsumers.draw();

                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                    RenderSystem.disableBlend();

                    matrixStack.pop();

                    if (stack.getCount() != 1) {
                        String count = stack.getCount() + "";
                        matrixStack.push();
                        matrixStack.translate(stackX + 10 - FontUtil.getWidth(((FontSize) fontSize.getValue()), count), stackY + 9, 0);
                        FontUtil.drawText(
                                count,
                                0,
                                0,
                                infoColorToUse,
                                (FontSize) fontSize.getValue()
                        );
                        matrixStack.pop();
                    }
                }

                if (withDurability.getValue() && stack.isDamageable()) {
                    float green = (stack.getMaxDamage() - stack.getDamage()) / (float) stack.getMaxDamage();
                    float red = 1.0f - green;

                    matrixStack.push();
                    matrixStack.translate(stackX, stackY - FontUtil.getHeight((FontSize) fontSize.getValue()) / 2f - 1, 0);
                    matrixStack.scale(0.72f, 0.72f, 1);
                    FontUtil.drawText(
                            Math.round(((stack.getMaxDamage() - stack.getDamage()) * 100.0f) / stack.getMaxDamage()) + "%",
                            0,
                            0,
                            new Color(red, green, 0),
                            (FontSize) fontSize.getValue()
                    );
                    matrixStack.pop();

                    renderedDurability = true;
                }

                if (withDurability.getValue() && withEnchantments.getValue() && stack.hasEnchantments()) {
                    ItemEnchantmentsComponent component = EnchantmentHelper.getEnchantments(stack);
                    Object2IntMap<RegistryEntry<Enchantment>> enchantments = new Object2IntOpenHashMap<>();
                    for (RegistryEntry<Enchantment> enchantment : component.getEnchantments()) {
                        enchantments.put(enchantment, component.getLevel(enchantment));
                    }

                    int height = 0;
                    for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : Object2IntMaps.fastIterable(enchantments)) {
                        String str = getEnchantmentName(entry.getKey().getIdAsString(), entry.getIntValue());

                        matrixStack.push();
                        matrixStack.translate(stackX, stackY + height, 0);
                        matrixStack.scale(0.5f, 0.5f, 1);
                        FontUtil.drawText(
                                str,
                                0,
                                0,
                                infoColorToUse,
                                (FontSize) fontSize.getValue()
                        );
                        matrixStack.pop();

                        height += FontUtil.getHeight((FontSize) fontSize.getValue()) / 2;
                    }
                }
            }

            if (withItemName.getValue() && !player.getMainHandStack().isEmpty()) {
                String itemText = player.getMainHandStack().getName().getString();

                matrixStack.push();
                matrixStack.translate(-FontUtil.getWidth(((FontSize) fontSize.getValue()), itemText) / 2f / 2f, -FontUtil.getHeight(((FontSize) fontSize.getValue())) - 1 - FontUtil.getHeight(((FontSize) fontSize.getValue())) / 2f - 1 - (renderedItems ? (withItems.getValue() ? 18 + maxEnchants : 1) + (withDurability.getValue() && renderedDurability ? FontUtil.getHeight(((FontSize) fontSize.getValue())) / 2.0f + 1 : 0) : 0), 0);
                matrixStack.scale(0.72f, 0.72f, 1);
                FontUtil.drawText(
                        itemText,
                        0,
                        0,
                        infoColorToUse,
                        ((FontSize) fontSize.getValue())
                );
                matrixStack.pop();
            }

            matrixStack.pop();
        }
    }

    private ItemStack getItem(PlayerEntity player, int index) {
        return switch (index) {
            case 0 -> player.getMainHandStack();
            case 1 -> player.getInventory().armor.get(3);
            case 2 -> player.getInventory().armor.get(2);
            case 3 -> player.getInventory().armor.get(1);
            case 4 -> player.getInventory().armor.get(0);
            case 5 -> player.getOffHandStack();
            default -> ItemStack.EMPTY;
        };
    }

    private String getEnchantmentName(String id, int level) {
        id = id.replace("minecraft:", "");
        id = level > 1 ? id.substring(0, 2) : id.substring(0, 3);
        return id.substring(0, 1).toUpperCase() + id.substring(1) + " " + (level > 1 ? level : "");
    }

    private record ItemElement(ItemStack stack, List<String> enchantments) { }

    public enum Modes {
        Fill,
        Outline
    }
}
