package cc.vergence.modules;

import cc.vergence.Vergence;
import cc.vergence.features.enums.MouseButtons;
import cc.vergence.features.event.events.*;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BindOption;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.modules.client.SafeMode;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Module implements Wrapper {
    private String name;
    private String displayName;
    private String description;
    private Category category;
    private BindOption bind;
    private BooleanOption draw;
    private double x;
    private double y;
    private double width;
    private double height;
    private int priority = 0;
    private boolean status; // default disabled
    private boolean alwaysEnable = false;
    private String details;
    private ArrayList<Option<?>> options = new ArrayList<>();
    private HashMap<String, Option<?>> optionHashMap = new HashMap<>();
    public boolean isBlocked;

    public Module(String name, Category category) {
        this.name = name;
        this.displayName = Vergence.TEXT.get("Module.Modules." + name + ".name");
        this.description = Vergence.TEXT.get("Module.Modules." + name + ".description");
        this.category = category;

        this.bind = (BindOption) addOption(new BindOption("_BIND_", -1, BindOption.BindType.Click));
        this.draw = (BooleanOption) addOption(new BooleanOption("_DRAW_", true));
    }

    public Module(String name, Category category, int priority) {
        this.name = name;
        this.displayName = Vergence.TEXT.get("Module.Modules." + name + ".name");
        this.description = Vergence.TEXT.get("Module.Modules." + name + ".description");
        this.category = category;
        this.priority = priority;

        this.bind = (BindOption) addOption(new BindOption("_BIND_", -1, BindOption.BindType.Click));
        this.draw = (BooleanOption) addOption(new BooleanOption("_DRAW_", true));
    }

    public void setDraw(BooleanOption draw) {
        this.draw = draw;
    }

    public boolean shouldDraw() {
        return this.draw.getValue();
    }

    public static boolean isNull() {
        return mc.player == null || mc.world == null;
    }

    public void setAlwaysEnable(boolean alwaysEnable) {
        this.alwaysEnable = alwaysEnable;
        this.bind.setValue(-1);
        this.bind.setInvisibility(v -> false);
        this.status = true;
    }

    public boolean isAlwaysEnable() {
        return alwaysEnable;
    }

    public abstract String getDetails();

    public void enable() {
        if (this.getStatus() || SafeMode.INSTANCE == null) {
            return;
        }

        if (isBlocked && SafeMode.INSTANCE.getStatus()) {
            MessageManager.blockedMessage(this, SafeMode.INSTANCE);
        }

        MessageManager.newMessage(this, "§rwas §aenabled.");

        this.setStatus(true);
        this.onEnable();
    }

    public void disable() {
        if (!this.getStatus() || isAlwaysEnable()) {
            return;
        }

        MessageManager.newMessage(this, "§rwas §cdisabled.");

        this.setStatus(false);
        this.onDisable();
    }

    public void toggle() {
        if (this.getStatus()) {
            this.disable();
        } else {
            this.enable();
        }
    }

    public void block(Module module) {
        if (isAlwaysEnable()) {
            return;
        }

        MessageManager.newMessage(this, "§rwas §6blocked.");

        this.setStatus(false);
        this.onBlock(module);
    }

    public void unblock(boolean status) {
        if (isAlwaysEnable()) {
            return;
        }

        MessageManager.newMessage(this, "§rwas §aunblocked.");

        this.setStatus(status);
        this.onUnblock();
    }

    public <T> Option<T> addOption(Option<T> option) {
        options.add(option);
        optionHashMap.put(option.getName(), option);
        return option;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onBlock(Module module) {
    }

    public void onUnblock() {
    }

    public void onRegister() {
    }

    public void onTick() {
    }

    public void onTickAlways() {
    }

    public void onDraw2D(DrawContext context,  float tickDelta) {
    }

    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
    }

    public void onMouseMoveInClickGuiScreen(DrawContext context,  double mouseX, double mouseY) {
    }

    public void onMouseMoveInHudEditorScreen(DrawContext context,  double mouseX, double mouseY) {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public String getDebugInfo() {
        return "";
    }

    public void onClickBlockEvent(ClickBlockEvent event, BlockPos pos) {
    }

    public void onDeathEvent(DeathEvent event, PlayerEntity player) {
    }

    public void onHeldItemRendererEvent(HeldItemRendererEvent event, Hand hand, ItemStack item, float equipProgress, MatrixStack stack) {
    }

    public void onMoveEvent(MoveEvent event, double x, double y, double z) {
    }

    public void onReceivePacket(PacketEvent.Receive event, Packet<?> packet) {
    }

    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
    }

    public void onParticle(ParticleEvent event, Particle particle) {
    }

    public void onEmmiter(ParticleEvent.AddEmmiter event, ParticleEffect emmiter) {
    }

    public void onPlaceBlockEvent(PlaceBlockEvent event, BlockPos blockPos, Block block) {
    }

    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
    }

    public void onRenderSkyEvent(RenderSkyEvent event) {
    }

    public void onRotateEvent(RotateEvent event, float yaw, float pitch) {
    }

    public void onSendMessageEvent(SendMessageEvent event, String message) {
    }

    public void onStepEvent(StepEvent event, Box axisAlignedBB, float height) {
    }

    public void onTotemEvent(TotemEvent event, PlayerEntity player) {
    }

    public void onTotemParticleEvent(ParticleEvent event) {
    }

    public void onTravelEvent(TravelEvent event, PlayerEntity entity) {
    }

    public void onUpdateWalkingEvent(UpdateWalkingEvent event) {
    }

    public void onWorldBreakEvent(WorldBreakEvent event, BlockPos pos, int id) {
    }

    public void onMouseActive(int button, int action) {
    }

    public void onMouseMoveInClickGuiScreen(int mouseX, int mouseY) {
    }

    public void onMouseMoveInHudEditorScreen(int mouseX, int mouseY) {
    }

    public void onKeyboardActive(int key, int action) {
    }

    public void onSyncEvent(SyncEvent event, float pitch, float yaw) {
    }

    public void onRenderEntityEvent(RenderEntityEvent event, Entity entity, VertexConsumerProvider vertexConsumerProvider) {
    }

    public void onClickSlot(SlotActionType slotActionType, int slot, int button, int id) {
    }

    public void onClickBlockEventAlways(ClickBlockEvent event, BlockPos pos) {
    }

    public void onDeathEventAlways(DeathEvent event, PlayerEntity player) {
    }

    public void onHeldItemRendererEventAlways(HeldItemRendererEvent event, Hand hand, ItemStack item, float equipProgress, MatrixStack stack) {
    }

    public void onMoveEventAlways(MoveEvent event, double x, double y, double z) {
    }

    public void onReceivePacketAlways(PacketEvent.Receive event, Packet<?> packet) {
    }

    public void onSendPacketAlways(PacketEvent.Send event, Packet<?> packet) {
    }

    public void onParticleAlways(ParticleEvent event, Particle particle) {
    }

    public void onEmmiterAlways(ParticleEvent.AddEmmiter event, ParticleEffect emmiter) {
    }

    public void onPlaceBlockEventAlways(PlaceBlockEvent event, BlockPos blockPos, Block block) {
    }

    public void onPlayerUpdateEventAlways(PlayerUpdateEvent event) {
    }

    public void onRenderSkyEventAlways(RenderSkyEvent event) {
    }

    public void onRotateEventAlways(RotateEvent event, float yaw, float pitch) {
    }

    public void onSendMessageEventAlways(SendMessageEvent event, String message) {
    }

    public void onStepEventAlways(StepEvent event, Box axisAlignedBB, float height) {
    }

    public void onTotemEventAlways(TotemEvent event, PlayerEntity player) {
    }

    public void onTotemParticleEventAlways(ParticleEvent event) {
    }

    public void onTravelEventAlways(TravelEvent event, PlayerEntity entity) {
    }

    public void onUpdateWalkingEventAlways(UpdateWalkingEvent event) {
    }

    public void onWorldBreakEventAlways(WorldBreakEvent event, BlockPos pos, int id) {
    }

    public void onMouseActiveAlways(int button, int action) {
    }

    public void onMouseMoveInClickGuiScreenAlways(int mouseX, int mouseY) {
    }

    public void onMouseMoveInHudEditorScreenAlways(int mouseX, int mouseY) {
    }

    public void onKeyboardActiveAlways(int key, int action) {
    }

    public void onSyncEventAlways(SyncEvent event, float pitch, float yaw) {
    }

    public void onRenderEntityEventAlways(RenderEntityEvent event, Entity entity, VertexConsumerProvider vertexConsumerProvider) {
    }

    public void onMouseRelease(double mouseX, double mouseY, Screen screen, MouseButtons button) {
    }

    public void onMouseClicked(double mouseX, double mouseY, Screen screen, MouseButtons button) {
    }

    public void onClickSlotAlways(SlotActionType slotActionType, int slot, int button, int id) {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setBind(BindOption bind) {
        this.bind = bind;
    }

    public boolean setBindValue(String key) {
        if (key.equalsIgnoreCase("none")) {
            this.bind.setValue(-1);
            return true;
        }
        int intKey;
        try {
            intKey = InputUtil.fromTranslationKey("key.keyboard." + key.toLowerCase()).getCode();
        } catch (NumberFormatException e) {
            return false;
        }
        if (key.equalsIgnoreCase("none")) {
            intKey = -1;
        }
        if (intKey == 0) {
            return false;
        }
        this.bind.setValue(intKey);
        return true;
    }

    public BindOption getBind() {
        return bind;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void addPriority() {
        priority ++;
    }

    public void reducePriority() {
        priority --;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public Category getCategory() {
        return category;
    }

    public String getCategoryDisplayName(Category category) {
        return Vergence.TEXT.get("Module.Category." + name + ".name");
    }

    public HashMap<String, Option<?>> getOptionHashMap() {
        return optionHashMap;
    }

    public ArrayList<Option<?>> getOptions() {
        return options;
    }

    public static ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(Category.CLIENT);
        categories.add(Category.COMBAT);
        categories.add(Category.MISC);
        categories.add(Category.EXPLOIT);
        categories.add(Category.MOVEMENT);
        categories.add(Category.PLAYER);
        categories.add(Category.VISUAL);
        categories.add(Category.HUD);
        return categories;
    }

    public enum Category {
        CLIENT, COMBAT, MISC, EXPLOIT, MOVEMENT, PLAYER, VISUAL, HUD
    }
}
