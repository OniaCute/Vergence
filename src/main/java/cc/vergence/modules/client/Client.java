package cc.vergence.modules.client;

import cc.vergence.Vergence;
import cc.vergence.features.enums.AntiCheats;
import cc.vergence.features.enums.Fonts;
import cc.vergence.features.enums.Languages;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.managers.AnimationManager;
import cc.vergence.features.managers.GuiManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.features.screens.ClickGuiScreen;
import cc.vergence.injections.accessors.CustomPayloadC2SPacketAccessor;
import cc.vergence.modules.Module;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.ui.gui.impl.*;
import cc.vergence.ui.gui.impl.TextComponent;
import cc.vergence.util.animations.ColorAnimation;
import cc.vergence.util.render.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.packet.BrandCustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

import java.awt.*;

public class Client extends Module {
    public static Client INSTANCE;
    private static Languages lastLanguage = Languages.English;

    public Client() {
        super("Client", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<Boolean> sync = addOption(new BooleanOption("Sync"));
    public Option<String> clientBrand = addOption(new TextOption("Brand", "{full_name}"));
    public Option<Enum<?>> UIScale = addOption(new EnumOption("UIScale", UIScales.X100));
    public Option<Enum<?>> language = addOption(new EnumOption("Language", Languages.English));
    public Option<Enum<?>> font = addOption(new EnumOption("Font", Fonts.Sans));

    @Override
    public String getDetails() {
        return language.getValue().name();
    }

    @Override
    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (packet instanceof CustomPayloadC2SPacket p) {
            if (!p.payload().getId().id().equals(BrandCustomPayload.ID.id())) {
                return;
            }
            ((CustomPayloadC2SPacketAccessor) (Object) p).setPayload(new BrandCustomPayload(clientBrand.getValue()));
        }
    }

    @Override
    public void onTickAlways() {
        if (!language.getValue().equals(lastLanguage)) {
            lastLanguage = (Languages) language.getValue();
            for (GuiComponent component : GuiManager.rootComponents) {
                component.setDisplayName(Vergence.TEXT.get("Module.Category." + ((CategoryComponent) component).getCategory().name() + ".name"));
                for (GuiComponent component1 : component.getSubComponents()) {
                    if (component1 instanceof ModuleComponent moduleComponent) {
                        moduleComponent.getModule().setDisplayName(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".name"));
                        moduleComponent.getModule().setDescription(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".desc"));
                        for (GuiComponent component2 : component1.getSubComponents()) {
                            moduleComponent.getModule().setDescription(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".description"));
                            for (GuiComponent component3 : component2.getSubComponents()) {
                                if (component3 instanceof BooleanComponent) {
                                    if (((BooleanComponent) component3).getOption().getName().equals("_DRAW_")) {
                                        ((BooleanComponent) component3).getOption().setDisplayName(Vergence.TEXT.get("Module.Special.ModuleDraw.name"));
                                        ((BooleanComponent) component3).getOption().setDescription(Vergence.TEXT.get("Module.Special.ModuleDraw.description"));
                                        continue;
                                    }
                                    ((BooleanComponent) component3).getOption().setDisplayName(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.BooleanOption." + ((BooleanComponent) component3).getOption().getName() + ".name"));
                                    ((BooleanComponent) component3).getOption().setDescription(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.BooleanOption." + ((BooleanComponent) component3).getOption().getName() + ".description"));
                                }
                                else if (component3 instanceof TextComponent) {
                                    ((TextComponent) component3).getOption().setDisplayName(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.TextOption." + ((TextComponent) component3).getOption().getName() + ".name"));
                                    ((TextComponent) component3).getOption().setDescription(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.TextOption." + ((TextComponent) component3).getOption().getName() + ".description"));
                                }
                                else if (component3 instanceof EnumComponent) {
                                    ((EnumComponent) component3).getOption().setDisplayName(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.EnumOption." + ((EnumComponent) component3).getOption().getName() + ".name"));
                                    ((EnumComponent) component3).getOption().setDescription(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.EnumOption." + ((EnumComponent) component3).getOption().getName() + ".description"));
                                }
                                else if (component3 instanceof MultipleComponent) {
                                    ((MultipleComponent) component3).getOption().setDisplayName(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.MultipleOption." + ((MultipleComponent) component3).getOption().getName() + ".name"));
                                    ((MultipleComponent) component3).getOption().setDescription(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.MultipleOption." + ((MultipleComponent) component3).getOption().getName() + ".description"));
                                }
                                else if (component3 instanceof DoubleComponent) {
                                    ((DoubleComponent) component3).getOption().setDisplayName(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.DoubleOption." + ((DoubleComponent) component3).getOption().getName() + ".name"));
                                    ((DoubleComponent) component3).getOption().setDescription(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.DoubleOption." + ((DoubleComponent) component3).getOption().getName() + ".description"));
                                }
                                else if (component3 instanceof ColorComponent) {
                                    ((ColorComponent) component3).getOption().setDisplayName(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.ColorOption." + ((ColorComponent) component3).getOption().getName() + ".name"));
                                    ((ColorComponent) component3).getOption().setDescription(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.ColorOption." + ((ColorComponent) component3).getOption().getName() + ".description"));
                                }
                                else if (component3 instanceof BindComponent) {
                                    if (((BindComponent) component3).getOption().getName().equals("_BIND_")) {
                                        ((BindComponent) component3).getOption().setDisplayName(Vergence.TEXT.get("Module.Special.ModuleBind.name"));
                                        ((BindComponent) component3).getOption().setDescription(Vergence.TEXT.get("Module.Special.ModuleBind.description"));
                                        continue;
                                    }
                                    ((BindComponent) component3).getOption().setDisplayName(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.BindOption." + ((BindComponent) component3).getOption().getName() + ".name"));
                                    ((BindComponent) component3).getOption().setDescription(Vergence.TEXT.get("Module.Modules." + moduleComponent.getModule().getName() + ".Options.BindOption." + ((BindComponent) component3).getOption().getName() + ".description"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public enum UIScales {
        X50,
        X100,
        X150,
        X200
    }
}
