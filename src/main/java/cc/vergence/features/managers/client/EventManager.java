package cc.vergence.features.managers.client;

import cc.vergence.Vergence;
import cc.vergence.features.enums.MouseButtons;
import cc.vergence.features.event.eventbus.EventHandler;
import cc.vergence.features.event.events.*;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.managers.feature.ModuleManager;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BindOption;
import cc.vergence.features.screens.ClickGuiScreen;
import cc.vergence.modules.Module;
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
import org.lwjgl.glfw.GLFW;

public class EventManager implements Wrapper {
    public EventManager() {
        Vergence.EVENTBUS.subscribe(this);
    }

    @EventHandler
    public void onClickBlockEvent(ClickBlockEvent event) {
        BlockPos pos = event.getBlockPos();
        for (Module module : ModuleManager.modules) {
            module.onClickBlockEventAlways(event, pos);
            if (module.getStatus()) {
                module.onClickBlockEvent(event, pos);
            }
        }
    }

    @EventHandler
    public void onDeathEvent(DeathEvent event) {
        PlayerEntity player = event.getPlayer();
        for (Module module : ModuleManager.modules) {
            module.onDeathEventAlways(event, player);
            if (module.getStatus()) {
                module.onDeathEvent(event, player);
            }
        }
    }

    @EventHandler
    public void onHeldItemRendererEvent(HeldItemRendererEvent event) {
        Hand hand = event.getHand();
        ItemStack item = event.getItem();
        float equipProgress = event.getEp();
        MatrixStack stack = event.getStack();
        for (Module module : ModuleManager.modules) {
            module.onHeldItemRendererEventAlways(event, hand, item, equipProgress, stack);
            if (module.getStatus()) {
                module.onHeldItemRendererEvent(event, hand, item, equipProgress, stack);
            }
        }
    }

    @EventHandler
    public void onMoveEvent(MoveEvent event) {
        double x = event.getX();
        double y = event.getY();
        double z = event.getZ();
        for (Module module : ModuleManager.modules) {
            module.onMoveEventAlways(event, x, y, z);
            if (module.getStatus()) {
                module.onMoveEvent(event, x, y, z);
            }
        }
    }

    @EventHandler
    public void onReceivePacket(PacketEvent.Receive event) {
        Packet<?> packet = event.getPacket();
        for (Module module : ModuleManager.modules) {
            module.onReceivePacketAlways(event, packet);
            if (module.getStatus()) {
                module.onReceivePacket(event, packet);
            }
        }
    }

    @EventHandler
    public void onSendPacket(PacketEvent.Send event) {
        Packet<?> packet = event.getPacket();
        for (Module module : ModuleManager.modules) {
            module.onSendPacketAlways(event, packet);
            if (module.getStatus()) {
                module.onSendPacket(event, packet);
            }
        }
    }

    @EventHandler
    public void onParticle(ParticleEvent.AddParticle event) {
        Particle particle = event.getParticle();
        for (Module module : ModuleManager.modules) {
            module.onParticleAlways(event, particle);
            if (module.getStatus()) {
                module.onParticle(event, particle);
            }
        }
    }

    @EventHandler
    public void onEmmiter(ParticleEvent.AddEmmiter event) {
        ParticleEffect emmiter = event.getEmmiter();
        for (Module module : ModuleManager.modules) {
            module.onEmmiterAlways(event, emmiter);
            if (module.getStatus()) {
                module.onEmmiter(event, emmiter);
            }
        }
    }

    @EventHandler
    public void onPlaceBlockEvent(PlaceBlockEvent event) {
        BlockPos blockPos = event.blockPos;
        Block block = event.block;
        for (Module module : ModuleManager.modules) {
            module.onPlaceBlockEventAlways(event, blockPos, block);
            if (module.getStatus()) {
                module.onPlaceBlockEvent(event, blockPos, block);
            }
        }
    }

    @EventHandler
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        for (Module module : ModuleManager.modules) {
            module.onPlayerUpdateEventAlways(event);
            if (module.getStatus()) {
                module.onPlayerUpdateEvent(event);
            }
        }
    }

    @EventHandler
    public void onRenderSkyEvent(RenderSkyEvent event) {
        for (Module module : ModuleManager.modules) {
            module.onRenderSkyEventAlways(event);
            if (module.getStatus()) {
                module.onRenderSkyEvent(event);
            }
        }
    }

    @EventHandler
    public void onRotateEvent(RotateEvent event) {
        float yaw = event.getYaw();
        float pitch = event.getPitch();
        for (Module module : ModuleManager.modules) {
            module.onRotateEventAlways(event, yaw, pitch);
            if (module.getStatus()) {
                module.onRotateEvent(event, yaw, pitch);
            }
        }
    }

    @EventHandler
    public void onSendMessageEvent(SendMessageEvent event) {
        String message = event.message;
        for (Module module : ModuleManager.modules) {
            module.onSendMessageEventAlways(event, message);
            if (module.getStatus()) {
                module.onSendMessageEvent(event, message);
            }
        }
    }

    @EventHandler
    public void onStepEvent(StepEvent event) {
        Box axisAlignedBB = event.getAxisAlignedBB();
        float height = event.getHeight();
        for (Module module : ModuleManager.modules) {
            module.onStepEventAlways(event, axisAlignedBB, height);
            if (module.getStatus()) {
                module.onStepEvent(event, axisAlignedBB, height);
            }
        }
    }

    @EventHandler
    public void onTotemEvent(TotemEvent event) {
        PlayerEntity player = event.getPlayer();
        for (Module module : ModuleManager.modules) {
            module.onTotemEventAlways(event, player);
            if (module.getStatus()) {
                module.onTotemEvent(event, player);
            }
        }
    }

    @EventHandler
    public void onTotemParticleEvent(ParticleEvent event) {
        for (Module module : ModuleManager.modules) {
            module.onTotemParticleEventAlways(event);
            if (module.getStatus()) {
                module.onTotemParticleEvent(event);
            }
        }
    }

    @EventHandler
    public void onTravelEvent(TravelEvent event) {
        PlayerEntity entity = event.getEntity();
        for (Module module : ModuleManager.modules) {
            module.onTravelEventAlways(event, entity);
            if (module.getStatus()) {
                module.onTravelEvent(event, entity);
            }
        }
    }

    @EventHandler
    public void onUpdateWalkingEvent(UpdateWalkingEvent event) {
        for (Module module : ModuleManager.modules) {
            module.onUpdateWalkingEventAlways(event);
            if (module.getStatus()) {
                module.onUpdateWalkingEvent(event);
            }
        }
    }

    @EventHandler
    public void onWorldBreakEvent(WorldBreakEvent event) {
        BlockPos pos = event.getPos();
        int id = event.getId();
        for (Module module : ModuleManager.modules) {
            module.onWorldBreakEventAlways(event, pos, id);
            if (module.getStatus()) {
                module.onWorldBreakEvent(event, pos, id);
            }
        }
    }

    @EventHandler
    public void onSyncEvent(SyncEvent event) {
        float pitch = event.getPitch();
        float yaw = event.getYaw();
        for (Module module : ModuleManager.modules) {
            module.onSyncEventAlways(event, pitch, yaw);
            if (module.getStatus()) {
                module.onSyncEvent(event, pitch, yaw);
            }
        }
    }

    @EventHandler
    public void onRenderEntity(RenderEntityEvent event) {
        Entity entity = event.getEntity();
        VertexConsumerProvider vertexConsumerProvider = event.getVertexConsumers();
        for (Module module : ModuleManager.modules) {
            module.onRenderEntityEventAlways(event, entity, vertexConsumerProvider);
            if (module.getStatus()) {
                module.onRenderEntityEvent(event, entity, vertexConsumerProvider);
            }
        }
    }

    @EventHandler
    public void onKeyboardInputTick(KeyboardTickEvent event) {
        for (Module module : ModuleManager.modules) {
            module.onKeyboardInputTickAlways();
            if (module.getStatus()) {
                module.onKeyboardInputTick();
            }
        }
    }

    @EventHandler
    public void onOptionValueUpdate(OptionValueUpdateEvent event) {
        for (Module module : ModuleManager.modules) {
            module.onOptionValueUpdateAlways();
            if (module.getStatus()) {
                module.onOptionValueUpdate();
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        for (Module module : ModuleManager.modules) {
            module.onEntitySpawnAlways(event, entity);
            if (module.getStatus()) {
                module.onEntitySpawn(event, entity);
            }
        }
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveEvent event) {
        Entity entity = event.getEntity();
        for (Module module : ModuleManager.modules) {
            module.onEntityRemoveAlways(event, entity);
            if (module.getStatus()) {
                module.onEntityRemove(event, entity);
            }
        }
    }

    @EventHandler
    public void onDisconnect(DisconnectEvent event) {
        String reason = event.getReason();
        for (Module module : ModuleManager.modules) {
            module.onDisconnectAlways(event, reason);
            if (module.getStatus()) {
                module.onDisconnect(event, reason);
            }
        }
    }

    public void onMouseActive(int button, int action) {
        int bindCode = -100 - button;
        boolean shiftPressed = InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) || InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT);
        for (Module module : ModuleManager.modules) {
            module.onMouseActiveAlways(button, action);
            BindOption bind = module.getBind();
            if (bind.getValue() == bindCode && bind.isNeedShift() == shiftPressed && (mc.currentScreen instanceof ClickGuiScreen || mc.currentScreen == null)) {
                if (action == GLFW.GLFW_PRESS) {
                    if (bind.getBindType() == BindOption.BindType.Click) {
                        module.toggle();
                        for (Option<?> option : module.getOptions()) {
                            if (option instanceof BindOption bindOption) {
                                bindOption.setStatus(!bindOption.getStatus());
                            }
                        }
                    } else {
                        module.enable();
                        for (Option<?> option : module.getOptions()) {
                            if (option instanceof BindOption bindOption) {
                                bindOption.setStatus(true);
                            }
                        }
                    }
                } else if (action == GLFW.GLFW_RELEASE) {
                    if (bind.getBindType() == BindOption.BindType.Press) {
                        module.disable();
                        for (Option<?> option : module.getOptions()) {
                            if (option instanceof BindOption bindOption) {
                                bindOption.setStatus(false);
                            }
                        }
                    }
                }
            }
            if (module.getStatus()) {
                module.onMouseActive(button, action);
            }
        }
    }


    public void onMouseMoveInClickGuiScreen(DrawContext context, int mouseX, int mouseY) {
        Vergence.GUI.onMouseMoveInClickGuiScreen(context, mouseX, mouseY);
        for (Module module : ModuleManager.modules) {
            module.onMouseMoveInClickGuiScreenAlways(mouseX, mouseY);
            if (module.getStatus()) {
                module.onMouseMoveInClickGuiScreen(mouseX, mouseY);
            }
        }
    }

    public void onMouseMoveInHudEditorScreen(DrawContext context, int mouseX, int mouseY) {
        Vergence.HUD.onMouseMoveInHudEditorScreen(context, mouseX, mouseY);
        for (Module module : ModuleManager.modules) {
            module.onMouseMoveInHudEditorScreenAlways(mouseX, mouseY);
            if (module.getStatus()) {
                module.onMouseMoveInHudEditorScreen(mouseX, mouseY);
            }
        }
    }

    public void onKeyboardActive(int key, int action) {
        if (key == -1 || key == -2) {
            return;
        }

        boolean shiftPressed = InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT);

        for (Module module : ModuleManager.modules) {
            module.onKeyboardActiveAlways(key, action);

            BindOption bind = module.getBind();
            int bindKey = bind.getValue();

            if (key == bindKey && (mc.currentScreen instanceof ClickGuiScreen || mc.currentScreen == null)) {
                if (bind.isNeedShift() && !shiftPressed) continue;
                if (action == GLFW.GLFW_PRESS) {
                    if (bind.getBindType() == BindOption.BindType.Click) {
                        module.toggle();
                        for (Option<?> option : module.getOptions()) {
                            if (option instanceof BindOption bindOption) {
                                bindOption.setStatus(!bindOption.getStatus());
                            }
                        }
                    } else {
                        module.enable();
                        for (Option<?> option : module.getOptions()) {
                            if (option instanceof BindOption bindOption) {
                                bindOption.setStatus(true);
                            }
                        }
                    }
                } else if (action == GLFW.GLFW_RELEASE && bind.getBindType() == BindOption.BindType.Press) {
                    module.disable();
                    for (Option<?> option : module.getOptions()) {
                        if (option instanceof BindOption bindOption) {
                            bindOption.setStatus(false);
                        }
                    }
                }
            }
            if (module.getStatus()) {
                module.onKeyboardActive(key, action);
            }
        }
    }


    public void onTick() {
        for (Module module : ModuleManager.modules) {
            module.onTickAlways();
            if (module.getStatus()) {
                module.onTick();
            }
        }
        Vergence.POP.onTick();
        Vergence.ROTATE.onTick();
        if (!GuiManager.isClickGuiInited && mc.getWindow() != null) {
            Vergence.CONSOLE.logInfo("[UI] precalc ui position ...");
            GuiManager.updateClickGui();
            GuiManager.isClickGuiInited = true;
            Vergence.CONSOLE.logInfo("[UI] UI was loaded!");
        }
        NotifyManager.onTick();
    }

    public void onDraw2D(DrawContext context, float tickDelta) {
        for (Module module : ModuleManager.modules) {
            if (module.getStatus()) {
                module.onDraw2D(context, tickDelta);
            }
        }
        NotifyManager.onDraw2D(context, tickDelta);
    }

    public void onDraw3D(MatrixStack matrixStack,  float tickDelta) {
        for (Module module : ModuleManager.modules) {
            if (module.getStatus()) {
                module.onDraw3D(matrixStack, tickDelta);
            }
        }
    }

    public void onMouseClick(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        Vergence.GUI.onMouseClick(mouseX, mouseY, screen, button);
        Vergence.HUD.onMouseClick(mouseX, mouseY, screen, button);
    }

    public void onMouseRelease(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        Vergence.GUI.onMouseRelease(mouseX, mouseY, screen, button);
        Vergence.HUD.onMouseRelease(mouseX, mouseY, screen, button);
    }

    @EventHandler
    public void onClickSlot(ClickSlotEvent event) {
        SlotActionType slotActionType = event.getSlotActionType();
        int slot = event.getSlot();
        int button = event.getButton();
        int id = event.getId();
        for (Module module : ModuleManager.modules) {
            module.onClickSlotAlways(slotActionType, slot, button, id);
            if (module.getStatus()) {
                module.onClickSlot(slotActionType, slot, button, id);
            }
        }
    }

    public void onRenderClickGui(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        for (Module module : ModuleManager.modules) {
            module.onRenderClickGui(context, mouseX, mouseY, partialTicks);
            if (module.getStatus()) {
                module.onRenderClickGuiAlways(context, mouseX, mouseY, partialTicks);
            }
        }
        if (Vergence.GUI != null) {
            Vergence.GUI.onRenderClickGui(context, mouseX, mouseY, partialTicks);
        }
    }

    public void onRenderHudEditor(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        for (Module module : ModuleManager.modules) {
            module.onRenderHudEditor(context, mouseX, mouseY, partialTicks);
            if (module.getStatus()) {
                module.onRenderHudEditorAlways(context, mouseX, mouseY, partialTicks);
            }
        }
        if (Vergence.HUD != null) {
            Vergence.HUD.onRenderHudEditor(context, mouseX, mouseY, partialTicks);
        }
    }
}
