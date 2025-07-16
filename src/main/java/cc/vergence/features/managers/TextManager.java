package cc.vergence.features.managers;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Languages;
import cc.vergence.modules.client.Client;

import java.util.HashMap;
import java.util.Map;

public class TextManager {
    private final Map<String, String> englishLanguageBase = new HashMap<>();
    private final Map<String, String> chineseLanguageBase = new HashMap<>();
    private final Map<String, String> russianLanguageBase = new HashMap<>();

    public TextManager() {
        loadEnglish();
        Vergence.CONSOLE.logInfo("language \"English (English)\" was loaded!");
        loadChinese();
        Vergence.CONSOLE.logInfo("language \"简体中文 (Chinese)\" was loaded!");
        loadRussian();
        Vergence.CONSOLE.logInfo("language \"русский язык (Russian) \" was loaded!");
    }

    private void loadEnglish() {
        englishLanguageBase.put("COMMANDS.Aim.desc", "Aim to pos");
        englishLanguageBase.put("COMMANDS.Friend.desc", "Edit friend list");
        englishLanguageBase.put("COMMANDS.Enemy.desc", "Edit enemy list");
        englishLanguageBase.put("COMMANDS.Bind.desc", "Bind key");
        englishLanguageBase.put("COMMANDS.Prefix.desc", "Set command prefix");
        englishLanguageBase.put("COMMANDS.Toggle.desc", "Toggle module status");
        englishLanguageBase.put("COMMANDS.MESSAGE.COMMAND_NOT_FOUND", "No commands found about:");
        englishLanguageBase.put("COMMANDS.MESSAGE.INVALID_COMMAND", "§cInvalid Command! §fType§e h  elp§f for a list of commands.");
        englishLanguageBase.put("COMMANDS.MESSAGE.FRIEND.LIST_TITLE", "§bFriends:");
        englishLanguageBase.put("COMMANDS.MESSAGE.FRIEND.EMPTY", "§6Friends list is empty");
        englishLanguageBase.put("COMMANDS.MESSAGE.FRIEND.RESET", "§fFriends list got reset");
        englishLanguageBase.put("COMMANDS.MESSAGE.FRIEND.REMOVE", "§cFriend removed successfully");
        englishLanguageBase.put("COMMANDS.MESSAGE.FRIEND.ADD", "§bFriend added successfully");
        englishLanguageBase.put("COMMANDS.MESSAGE.FRIEND.IS_ENEMY", "§cThis player in your enemy list!");
        englishLanguageBase.put("COMMANDS.MESSAGE.ENEMY.LIST_TITLE", "§bEnemies:");
        englishLanguageBase.put("COMMANDS.MESSAGE.ENEMY.EMPTY", "§6Enemies list is empty");
        englishLanguageBase.put("COMMANDS.MESSAGE.ENEMY.RESET", "§fEnemies list got reset");
        englishLanguageBase.put("COMMANDS.MESSAGE.ENEMY.REMOVE", "§cEnemy removed successfully");
        englishLanguageBase.put("COMMANDS.MESSAGE.ENEMY.ADD", "§bEnemy added successfully");
        englishLanguageBase.put("COMMANDS.MESSAGE.ENEMY.IS_FRIEND", "§cThis player in your friend list!");
        englishLanguageBase.put("COMMANDS.MESSAGE.BIND.UNKNOWN_MODULE", "§cUnknown Module");
        englishLanguageBase.put("COMMANDS.MESSAGE.BIND.NO_KEY", "§6Please specify a key");
        englishLanguageBase.put("COMMANDS.MESSAGE.BIND.UNKNOWN_ERROR", "§cUnknown internal error.");
        englishLanguageBase.put("COMMANDS.MESSAGE.BIND.BIND_OK", "§bKey bind successfully");
        englishLanguageBase.put("COMMANDS.MESSAGE.PREFIX.NO_PREFIX", "§6Please specify a prefix (one char)");
        englishLanguageBase.put("COMMANDS.MESSAGE.PREFIX.OK", "§bNow prefix is");
        englishLanguageBase.put("COMMANDS.MESSAGE.TOGGLE.UNKNOWN_MODULE", "§cUnknown Module");

        englishLanguageBase.put("Module.Category.CLIENT.name", "Client");
        englishLanguageBase.put("Module.Category.COMBAT.name", "Combat");
        englishLanguageBase.put("Module.Category.MOVEMENT.name", "Movement");
        englishLanguageBase.put("Module.Category.PLAYER.name", "Player");
        englishLanguageBase.put("Module.Category.EXPLOIT.name", "Exploit");
        englishLanguageBase.put("Module.Category.VISUAL.name", "Visual");
        englishLanguageBase.put("Module.Category.MISC.name", "Misc");
        englishLanguageBase.put("Module.Category.HUD.name", "HUD");

        englishLanguageBase.put("Module.Special.Messages.Blocked", "Module {a} is blocked by {b}.");
        englishLanguageBase.put("Module.Special.ModuleBind.name", "Hotkey");
        englishLanguageBase.put("Module.Special.ModuleBind.description", "The hotkey of the module");
        englishLanguageBase.put("Module.Special.ModuleDraw.name", "Display");
        englishLanguageBase.put("Module.Special.ModuleDraw.description", "Display module in ArrayList");

        englishLanguageBase.put("Theme.DefaultTheme.name", "Default Theme");
        englishLanguageBase.put("Theme.DefaultTheme.description", "The default theme of vergence client");

        englishLanguageBase.put("Module.Modules.ClickGUI.name", "Click GUI");
        englishLanguageBase.put("Module.Modules.ClickGUI.description", "The options of click gui");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.TextOption.Title.name", "Title");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.TextOption.Title.description", "The title of client click gui");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.BooleanOption.ShowOptionDescription.name", "Show Option Description");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.BooleanOption.ShowOptionDescription.description", "display the options descriptions");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.BooleanOption.RainbowSync.name", "Rainbow SYNC");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.BooleanOption.RainbowSync.description", "Make all the colors of the rainbow the same");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowOffset.name", "Rainbow Offset");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowOffset.description", "The maximum offset between each of the rainbow colors");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowSpeed.name", "Rainbow Speed");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowSpeed.description", "The speed at which the colors of the rainbow color change");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.MainPageAnimationTime.name", "MainPage Animation Time");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.MainPageAnimationTime.description", "The duration of animation");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ModuleSpreadAnimationTime.name", "Module Animation Time");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ModuleSpreadAnimationTime.description", "The duration of animation");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.OptionsSpreadAnimationTime.name", "Options Animation Time");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.OptionsSpreadAnimationTime.description", "The duration of animation");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.DescriptionSpreadAnimationTime.name", "Description Animation Time");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.DescriptionSpreadAnimationTime.description", "The duration of animation");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollAnimationTime.name", "Scroll Animation Time");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollAnimationTime.description", "The duration of animation");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollScale.name", "Scroll Scale");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollScale.description", "The scale of the distance that the rollers roll");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ColorAnimationTime.name", "Color Animation Time");
        englishLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ColorAnimationTime.description", "The duration of animation");

        englishLanguageBase.put("Module.Modules.Client.name", "Client");
        englishLanguageBase.put("Module.Modules.Client.description", "Manage many settings of vergence client");
        englishLanguageBase.put("Module.Modules.Client.Options.BooleanOption.Sync.name", "SYNC");
        englishLanguageBase.put("Module.Modules.Client.Options.BooleanOption.Sync.description", "Share your Vergence user status so that you can be seen by other users who have enabled Sync and have their Vergence icons displayed.");
        englishLanguageBase.put("Module.Modules.Client.Options.EnumOption.UIScale.name", "UI Scale");
        englishLanguageBase.put("Module.Modules.Client.Options.EnumOption.UIScale.description", "The UI scale of client");
        englishLanguageBase.put("Module.Modules.Client.Options.EnumOption.Language.name", "Language");
        englishLanguageBase.put("Module.Modules.Client.Options.EnumOption.Language.description", "The language of client");
        englishLanguageBase.put("Module.Modules.Client.Options.EnumOption.Font.name", "Font");
        englishLanguageBase.put("Module.Modules.Client.Options.EnumOption.Font.description", "The font style of client");

        englishLanguageBase.put("Module.Modules.Notify.name", "Notify");
        englishLanguageBase.put("Module.Modules.Notify.description", "Setting about notifications");
        englishLanguageBase.put("Module.Modules.Notify.Options.BooleanOption.Rounded.name", "Rounded");
        englishLanguageBase.put("Module.Modules.Notify.Options.BooleanOption.Rounded.description", "Use rounded rect for the notifications");
        englishLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.Radius.name", "Radius");
        englishLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.Radius.description", "The radius of notification");
        englishLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.NotificationAliveTime.name", "Notification AliveTime");
        englishLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.NotificationAliveTime.description", "How long does the notification stay at the screen");
        englishLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.AnimationSpeed.name", "Animation Speed");
        englishLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.AnimationSpeed.description", "The speed of animation");

        englishLanguageBase.put("Module.Modules.HudEditor.name", "HUD Editor");
        englishLanguageBase.put("Module.Modules.HudEditor.description", "Edit something about HUDs");
        englishLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.TextColor.name", "Text Color");
        englishLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.TextColor.description", "The text color of hud in hud editor screen");
        englishLanguageBase.put("Module.Modules.HudEditor.Options.BooleanOption.Outline.name", "Outline");
        englishLanguageBase.put("Module.Modules.HudEditor.Options.BooleanOption.Outline.description", "Display the outline when the hud is selected");
        englishLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.OutlineColor.name", "Outline Color");
        englishLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.OutlineColor.description", "The outline color of hud");

        englishLanguageBase.put("Module.Modules.Watermark.name", "Watermark");
        englishLanguageBase.put("Module.Modules.Watermark.description", "Draw a watermark on the screen");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.CustomTitle.name", "Custom Title");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.CustomTitle.description", "Whether to customize the watermark title");
        englishLanguageBase.put("Module.Modules.Watermark.Options.TextOption.Title.name", "Title");
        englishLanguageBase.put("Module.Modules.Watermark.Options.TextOption.Title.description", "the watermark title");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Outline.name", "Outline");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Outline.description", "Display outline");
        englishLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.OutlineWidth.name", "Outline Width");
        englishLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.OutlineWidth.description", "The width of outline");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Rounded.name", "Rounded Rect");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Rounded.description", "Apply rounded rect to the watermark");
        englishLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.Radius.name", "Rounded Radius");
        englishLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.Radius.description", "The radius of watermark");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Split.name", "Split");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Split.description", "Display each part of the Watermark in separate sections");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedTime.name", "Include Time");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedTime.description", "Incorporate the time component into the watermark");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedFPS.name", "Include FPS");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedFPS.description", "Incorporate the fps component into the watermark");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedUser.name", "Include Username");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedUser.description", "Incorporate the username component into the watermark");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedConfig.name", "Include Config");
        englishLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedConfig.description", "Incorporate the config component into the watermark");
        englishLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.TextColor.name", "Text Color");
        englishLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.TextColor.description", "The text color of watermark");
        englishLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.BackgroundColor.name", "Background Color");
        englishLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.BackgroundColor.description", "The background color of watermark");
        englishLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.OutlineColor.name", "Outline Color");
        englishLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.OutlineColor.description", "The outline color of watermark");

        englishLanguageBase.put("Module.Modules.Spammer.name", "Spammer");
        englishLanguageBase.put("Module.Modules.Spammer.description", "Auto type some text to chat screen");
        englishLanguageBase.put("Module.Modules.Spammer.Options.DoubleOption.Cooldown.name", "Cool Down");
        englishLanguageBase.put("Module.Modules.Spammer.Options.DoubleOption.Cooldown.description", "The cooling time for sending messages");
        englishLanguageBase.put("Module.Modules.Spammer.Options.EnumOption.ListOrder.name", "List Order");
        englishLanguageBase.put("Module.Modules.Spammer.Options.EnumOption.ListOrder.description", "The order of the text list");
        englishLanguageBase.put("Module.Modules.Spammer.Options.TextOption.FileName.name", "File Name");
        englishLanguageBase.put("Module.Modules.Spammer.Options.TextOption.FileName.description", "The file name of text list");

        englishLanguageBase.put("Module.Modules.AutoSprint.name", "Auto Sprint");
        englishLanguageBase.put("Module.Modules.AutoSprint.description", "Always sprint when you move");
        englishLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.Attack.name", "Attack");
        englishLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.Attack.description", "The module will have an impact when you attack");
        englishLanguageBase.put("Module.Modules.AutoSprint.Options.DoubleOption.AttackCounteract.name", "Attack Counteract");
        englishLanguageBase.put("Module.Modules.AutoSprint.Options.DoubleOption.AttackCounteract.description", "The counteract scale of attack");
        englishLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.UseItem.name", "Using Items");
        englishLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.UseItem.description", "Keep sprint while you using items");

        englishLanguageBase.put("Module.Modules.FastUse.name", "Fast Use");
        englishLanguageBase.put("Module.Modules.FastUse.description", "Reduce the delay between actions");
        englishLanguageBase.put("Module.Modules.FastUse.Options.DoubleOption.Delay.name", "Delay");
        englishLanguageBase.put("Module.Modules.FastUse.Options.DoubleOption.Delay.description", "The delay of actions");
        englishLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Blocks.name", "Blocks");
        englishLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Blocks.description", "Reduce the spacing between blocks");
        englishLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Crystals.name", "Crystals");
        englishLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Crystals.description", "Reduce the spacing between crystals");
        englishLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.XP.name", "XP");
        englishLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.XP.description", "Reduce the intervals of discarding the XP bottles");

        englishLanguageBase.put("Module.Modules.ESP.name", "Player ESP");
        englishLanguageBase.put("Module.Modules.ESP.description", "Show info of players all the time");

        englishLanguageBase.put("Module.Modules.AntiHungry.name", "Anti Hungry");
        englishLanguageBase.put("Module.Modules.AntiHungry.description", "By using a reasonable method, you can reduce the feeling of hunger");
        englishLanguageBase.put("Module.Modules.AntiHungry.Options.EnumOption.AntiCheat.name", "Anti Cheat");
        englishLanguageBase.put("Module.Modules.AntiHungry.Options.EnumOption.AntiCheat.description", "Determine which functions need to be blocked by anti cheat mode");

        englishLanguageBase.put("Module.Modules.BetterChat.name", "Better Chat");
        englishLanguageBase.put("Module.Modules.BetterChat.description", "Allow you to customize the chat interface");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.Prefix.name", "Prefix");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.Prefix.description", "What style should the prefix of vergence client be");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PrefixColor.name", "Prefix Color");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PrefixColor.description", "The color of prefix");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomNameColor.name", "Custom Name Color");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomNameColor.description", "Enable custom color for your player name");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerNameColor.name", "Player Name Color");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerNameColor.description", "The color of your player name");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNamePrefix.name", "Name Prefix");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNamePrefix.description", "The prefix before your player name");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNameSuffix.name", "Name Suffix");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNameSuffix.description", "The suffix after your player name");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomChatColor.name", "Custom Chat Color");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomChatColor.description", "Enable custom color for your chat messages");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerChatColor.name", "Chat Text Color");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerChatColor.description", "The color of your chat messages");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerChatPrefix.name", "Chat Prefix");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerChatPrefix.description", "The prefix between name and message");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationTime.name", "Animation Time");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationTime.description", "The frequency of animation");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationOffset.name", "Animation Offset");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationOffset.description", "The offset of animation");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.AnimationQuadType.name", "Animation Type");
        englishLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.AnimationQuadType.description", "The computational style of animation");

        englishLanguageBase.put("Module.Modules.SafeMode.name", "Safe Mode");
        englishLanguageBase.put("Module.Modules.SafeMode.description", "Try your best to avoid being detected by anti cheat");
        englishLanguageBase.put("Module.Modules.SafeMode.Options.EnumOption.AntiCheatMode.name", "Anti Cheat Mode");
        englishLanguageBase.put("Module.Modules.SafeMode.Options.EnumOption.AntiCheatMode.description", "Determine which functions need to be blocked by anti cheat mode");
        englishLanguageBase.put("Module.Modules.SafeMode.Options.BooleanOption.AllowTimer.name", "Allow Timer");
        englishLanguageBase.put("Module.Modules.SafeMode.Options.BooleanOption.AllowTimer.description", "If it is enabled, it means that the Timer can be used. This might result in you being detected by anti cheat");


        englishLanguageBase.put("Module.Modules.KillAura.name", "Kill Aura");
        englishLanguageBase.put("Module.Modules.KillAura.description", "Auto attack objects within a certain range");
        englishLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.AntiCheat.name", "Anti Cheat");
        englishLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.AntiCheat.description", "Determine which functions need to be blocked by anti cheat mode");
        englishLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.ClickType.name", "Click Type");
        englishLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.ClickType.description", "Choose the click mode for auto attack");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Range.name", "Range");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Range.description", "Maximum distance to attack targets");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.FOV.name", "FOV");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.FOV.description", "Attack field of view angle");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MinCPS.name", "MinCPS");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MinCPS.description", "Minimum clicks per second");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MaxCPS.name", "MaxCPS");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MaxCPS.description", "Maximum clicks per second");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Delay.name", "Delay");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Delay.description", "Delay between each attack");
        englishLanguageBase.put("Module.Modules.KillAura.Options.BooleanOption.CrosshairLock.name", "CrosshairLock");
        englishLanguageBase.put("Module.Modules.KillAura.Options.BooleanOption.CrosshairLock.description", "No rotate when crosshair is on target");
        englishLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.RotateType.name", "RotateType");
        englishLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.RotateType.description", "Rotation type");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.RotateSpeed.name", "RotateSpeed");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.RotateSpeed.description", "Rotation speed");
        englishLanguageBase.put("Module.Modules.KillAura.Options.BooleanOption.SmoothRotate.name", "SmoothRotate");
        englishLanguageBase.put("Module.Modules.KillAura.Options.BooleanOption.SmoothRotate.description", "Enable smooth rotation");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.SmoothOffset.name", "SmoothOffset");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.SmoothOffset.description", "Smooth rotation offset");
        englishLanguageBase.put("Module.Modules.KillAura.Options.BooleanOption.RotateLock.name", "RotateLock");
        englishLanguageBase.put("Module.Modules.KillAura.Options.BooleanOption.RotateLock.description", "Lock rotation after attack");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.RotateLockTime.name", "RotateLockTime");
        englishLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.RotateLockTime.description", "Rotation lock duration");
        englishLanguageBase.put("Module.Modules.KillAura.Options.MultipleOption.Targets.name", "Targets");
        englishLanguageBase.put("Module.Modules.KillAura.Options.MultipleOption.Targets.description", "Attack targets");

        englishLanguageBase.put("Module.Modules.Scaffold.name", "Scaffold");
        englishLanguageBase.put("Module.Modules.Scaffold.description", "Auto place block on the ground when moving");
        englishLanguageBase.put("Module.Modules.Scaffold.Options.EnumOption.AntiCheat.name", "Anti Cheat");
        englishLanguageBase.put("Module.Modules.Scaffold.Options.EnumOption.AntiCheat.description", "Determine which functions need to be blocked by anti cheat mode");

        englishLanguageBase.put("Module.Modules.Reach.name", "Reach");
        englishLanguageBase.put("Module.Modules.Reach.description", "Change the range of interaction and attack");
        englishLanguageBase.put("Module.Modules.Reach.Options.DoubleOption.Range.name", "Range");
        englishLanguageBase.put("Module.Modules.Reach.Options.DoubleOption.Range.description", "The range of interaction and attack");

        englishLanguageBase.put("Module.Modules.FakePlayer.name", "FakePlayer");
        englishLanguageBase.put("Module.Modules.FakePlayer.description", "Create a fake player");
        englishLanguageBase.put("Module.Modules.FakePlayer.Options.TextOption.PlayerName.name", "Player Name");
        englishLanguageBase.put("Module.Modules.FakePlayer.Options.TextOption.PlayerName.description", "The name of the fake player to be displayed");
        englishLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.GoldenApple.name", "Golden Apple");
        englishLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.GoldenApple.description", "Enable golden apple effects on the fake player");
        englishLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.AutoTotem.name", "Auto Totem");
        englishLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.AutoTotem.description", "Auto use a totem when the fake player dies");

        englishLanguageBase.put("Module.Modules.SafeWalk.name", "SafeWalk");
        englishLanguageBase.put("Module.Modules.SafeWalk.description", "Enable you to walk safely along the edges of the blocks");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoInject.name", "Inject Input");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoInject.description", "Use an unsafe method so that you won't fall");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoShift.name", "Auto Sneak");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoShift.description", "Auto sneak when near edge");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.RandomThreshold.name", "Random Threshold");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.RandomThreshold.description", "Randomize sneak edge threshold");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.Threshold.name", "Edge Threshold");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.Threshold.description", "The fixed distance to trigger sneaking");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MaxThreshold.name", "Max Threshold");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MaxThreshold.description", "Maximum random edge threshold");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MinThreshold.name", "Min Threshold");
        englishLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MinThreshold.description", "Minimum random edge threshold");

        englishLanguageBase.put("Module.Modules.FOVModifier.name", "FOVModifier");
        englishLanguageBase.put("Module.Modules.FOVModifier.description", "Change the fov of the players");
        englishLanguageBase.put("Module.Modules.FOVModifier.Options.DoubleOption.FOV.name", "FOV");
        englishLanguageBase.put("Module.Modules.FOVModifier.Options.DoubleOption.FOV.description", "The fov");
        englishLanguageBase.put("Module.Modules.FOVModifier.Options.BooleanOption.Items.name", "Items");
        englishLanguageBase.put("Module.Modules.FOVModifier.Options.BooleanOption.Items.description", "Apply the effect to the items");

        englishLanguageBase.put("Module.Modules.Disabler.name", "Disabler");
        englishLanguageBase.put("Module.Modules.Disabler.description", "Attempt to disable certain anti cheat behaviors");

        englishLanguageBase.put("Module.Modules.InventoryMove.name", "Inventory Move");
        englishLanguageBase.put("Module.Modules.InventoryMove.description", "Enable you to be able to move when opening certain GUI");
        englishLanguageBase.put("Module.Modules.InventoryMove.Options.BooleanOption.HorizontalCollision.name", "Horizontal Collision");
        englishLanguageBase.put("Module.Modules.InventoryMove.Options.BooleanOption.HorizontalCollision.description", "Whether to enable Horizontal Collision when sending packets");


        englishLanguageBase.put("Module.Modules.AutoWalk.name", "Auto Walk");
        englishLanguageBase.put("Module.Modules.AutoWalk.description", "Move forward automatically");
        englishLanguageBase.put("Module.Modules.AutoWalk.Options.EnumOption.Mode.name", "Mode");
        englishLanguageBase.put("Module.Modules.AutoWalk.Options.EnumOption.Mode.description", "Baritone support");

        englishLanguageBase.put("Module.Modules.NoCooldown.name", "No Cooldown");
        englishLanguageBase.put("Module.Modules.NoCooldown.description", "Reduce the cooling time between operations");
        englishLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Attack.name", "Attack");
        englishLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Attack.description", "No attack cooldown");
        englishLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Jump.name", "Jump");
        englishLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Jump.description", "No jump cooldown");
        englishLanguageBase.put("Module.Modules.NoCooldown.Options.DoubleOption.JumpTicks.name", "Jump Ticks");
        englishLanguageBase.put("Module.Modules.NoCooldown.Options.DoubleOption.JumpTicks.description", "The amount of ticks that have to be waited for before jumping again.");

        englishLanguageBase.put("Module.Modules.PortalGod.name", "Portal God");
        englishLanguageBase.put("Module.Modules.PortalGod.description", "Allow you to operate the GUI within the portal");

        englishLanguageBase.put("Module.Modules.NoFall.name", "No Fall Damage");
        englishLanguageBase.put("Module.Modules.NoFall.description", "Prevent fall damage");
        englishLanguageBase.put("Module.Modules.NoFall.Options.EnumOption.AntiCheat.name", "Anti Cheat");
        englishLanguageBase.put("Module.Modules.NoFall.Options.EnumOption.AntiCheat.description", "Determine which functions need to be blocked by anti cheat mode");
        englishLanguageBase.put("Module.Modules.NoFall.Options.BooleanOption.HorizontalCollision.name", "HorizontalCollision");
        englishLanguageBase.put("Module.Modules.NoFall.Options.BooleanOption.HorizontalCollision.description", "Enable horizontal collision check for Grim");
        englishLanguageBase.put("Module.Modules.NoFall.Options.BooleanOption.AlwaysActive.name", "AlwaysActive");
        englishLanguageBase.put("Module.Modules.NoFall.Options.BooleanOption.AlwaysActive.description", "Always active regardless of falling state");

        englishLanguageBase.put("Module.Modules.Placeholder.name", "Placeholder Previewer");
        englishLanguageBase.put("Module.Modules.Placeholder.description", "Display client and player information placeholders");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Client_ID.name", "{id}");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Client_ID.description", "Client ID placeholder");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Client_Name.name", "{name}");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Client_Name.description", "Client name placeholder");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Client_Full_Name.name", "{full_name}");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Client_Full_Name.description", "Client full name placeholder");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Client_Version.name", "{version}");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Client_Version.description", "Client version placeholder");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Player.name", "{player}");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Player.description", "Player name placeholder");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_HP.name", "{hp}");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_HP.description", "Player health placeholder");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Max_HP.name", "{max_hp}");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Max_HP.description", "Player max health placeholder");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Armor.name", "{armor}");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Armor.description", "Player armor placeholder");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_OverWorld.name", "{world} (Overworld)");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_OverWorld.description", "Overworld dimension placeholder");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Nether.name", "{world} (Nether)");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_Nether.description", "Nether dimension placeholder");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_TheEnd.name", "{world} (TheEnd)");
        englishLanguageBase.put("Module.Modules.Placeholder.Options.TextOption.Placeholder_TheEnd.description", "The End dimension placeholder");

        englishLanguageBase.put("Module.Modules.Title.name", "Title");
        englishLanguageBase.put("Module.Modules.Title.description", "Customize and animate the window title");
        englishLanguageBase.put("Module.Modules.Title.Options.TextOption.Title.name", "Title");
        englishLanguageBase.put("Module.Modules.Title.Options.TextOption.Title.description", "Window title text");
        englishLanguageBase.put("Module.Modules.Title.Options.BooleanOption.Animation.name", "Animation");
        englishLanguageBase.put("Module.Modules.Title.Options.BooleanOption.Animation.description", "Enable title animation");
        englishLanguageBase.put("Module.Modules.Title.Options.BooleanOption.ReverseAnimation.name", "Reverse Animation");
        englishLanguageBase.put("Module.Modules.Title.Options.BooleanOption.ReverseAnimation.description", "Reverse the animation direction");
        englishLanguageBase.put("Module.Modules.Title.Options.DoubleOption.AnimationDelay.name", "Animation Delay");
        englishLanguageBase.put("Module.Modules.Title.Options.DoubleOption.AnimationDelay.description", "Delay between animation steps");

        englishLanguageBase.put("Module.Modules.BetterTab.name", "Better Tab");
        englishLanguageBase.put("Module.Modules.BetterTab.description", "Improve the player tab list with custom colors and limits");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.DoubleOption.PlayerLimit.name", "Player Limit");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.DoubleOption.PlayerLimit.description", "Maximum players shown in tab list");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.BooleanOption.Myself.name", "Myself");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.BooleanOption.Myself.description", "Highlight yourself in tab");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.ColorOption.MyColor.name", "My Color");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.ColorOption.MyColor.description", "Color for yourself");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.BooleanOption.Friends.name", "Friends");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.BooleanOption.Friends.description", "Highlight friends in tab");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.ColorOption.FriendColor.name", "Friend Color");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.ColorOption.FriendColor.description", "Color for friends");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.BooleanOption.Enemies.name", "Enemies");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.BooleanOption.Enemies.description", "Highlight enemies in tab");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.ColorOption.EnemyColor.name", "Enemy Color");
        englishLanguageBase.put("Module.Modules.BetterTab.Options.ColorOption.EnemyColor.description", "Color for enemies");

        englishLanguageBase.put("Module.Modules.ModuleList.name", "Module List");
        englishLanguageBase.put("Module.Modules.ModuleList.description", "Display a list of enabled modules on the HUD");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.EnumOption.Align.name", "Align");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.EnumOption.Align.description", "module list alignment");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.EnumOption.FontSize.name", "FontSize");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.EnumOption.FontSize.description", "Font size for module names");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.ColorOption.TextColor.name", "TextColor");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.ColorOption.TextColor.description", "Module name color");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.BooleanOption.Background.name", "Background");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.BooleanOption.Background.description", "Enable background");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.ColorOption.BackgroundColor.name", "Background Color");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.ColorOption.BackgroundColor.description", "Background color");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.BooleanOption.Rounded.name", "Rounded");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.BooleanOption.Rounded.description", "Round background corners");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.DoubleOption.Radius.name", "Radius");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.DoubleOption.Radius.description", "Corner radius");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.BooleanOption.Rect.name", "Rect");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.BooleanOption.Rect.description", "Enable rectangle behind text");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.ColorOption.RectColor.name", "Rect Color");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.ColorOption.RectColor.description", "Rectangle color");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.BooleanOption.RoundedRect.name", "Rounded Rect");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.BooleanOption.RoundedRect.description", "Round rectangle corners");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.DoubleOption.RadiusRect.name", "Radius Rect");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.DoubleOption.RadiusRect.description", "Rectangle corner radius");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.BooleanOption.Animation.name", "Animation");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.BooleanOption.Animation.description", "Enable fade-in animation");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.DoubleOption.AnimationTime.name", "Animation Time");
        englishLanguageBase.put("Module.Modules.ModuleList.Options.DoubleOption.AnimationTime.description", "Animation duration");
    }

    private void loadChinese() {
        chineseLanguageBase.put("COMMANDS.Aim.desc", "瞄准到指定位置");
        chineseLanguageBase.put("COMMANDS.Friend.desc", "编辑好友列表");
        chineseLanguageBase.put("COMMANDS.Enemy.desc", "编辑敌人列表");
        chineseLanguageBase.put("COMMANDS.Bind.desc", "绑定按键");
        chineseLanguageBase.put("COMMANDS.Prefix.desc", "设置命令前缀");
        chineseLanguageBase.put("COMMANDS.Toggle.desc", "切换模块状态");
        chineseLanguageBase.put("COMMANDS.MESSAGE.COMMAND_NOT_FOUND", "未找到相关命令:");
        chineseLanguageBase.put("COMMANDS.MESSAGE.INVALID_COMMAND", "§c无效命令! §f输入§e help§f查看命令列表.");
        chineseLanguageBase.put("COMMANDS.MESSAGE.FRIEND.LIST_TITLE", "§b好友列表:");
        chineseLanguageBase.put("COMMANDS.MESSAGE.FRIEND.EMPTY", "§6好友列表为空");
        chineseLanguageBase.put("COMMANDS.MESSAGE.FRIEND.RESET", "§f好友列表已重置");
        chineseLanguageBase.put("COMMANDS.MESSAGE.FRIEND.REMOVE", "§c好友移除成功");
        chineseLanguageBase.put("COMMANDS.MESSAGE.FRIEND.ADD", "§b好友添加成功");
        chineseLanguageBase.put("COMMANDS.MESSAGE.FRIEND.IS_ENEMY", "§c该玩家在敌人列表中!");
        chineseLanguageBase.put("COMMANDS.MESSAGE.ENEMY.LIST_TITLE", "§b敌人列表:");
        chineseLanguageBase.put("COMMANDS.MESSAGE.ENEMY.EMPTY", "§6敌人列表为空");
        chineseLanguageBase.put("COMMANDS.MESSAGE.ENEMY.RESET", "§f敌人列表已重置");
        chineseLanguageBase.put("COMMANDS.MESSAGE.ENEMY.REMOVE", "§c敌人移除成功");
        chineseLanguageBase.put("COMMANDS.MESSAGE.ENEMY.ADD", "§b敌人添加成功");
        chineseLanguageBase.put("COMMANDS.MESSAGE.ENEMY.IS_FRIEND", "§c该玩家在好友列表中!");
        chineseLanguageBase.put("COMMANDS.MESSAGE.BIND.UNKNOWN_MODULE", "§c未知模块");
        chineseLanguageBase.put("COMMANDS.MESSAGE.BIND.NO_KEY", "§6请指定按键");
        chineseLanguageBase.put("COMMANDS.MESSAGE.BIND.UNKNOWN_ERROR", "§c未知内部错误.");
        chineseLanguageBase.put("COMMANDS.MESSAGE.BIND.BIND_OK", "§b按键绑定成功");
        chineseLanguageBase.put("COMMANDS.MESSAGE.PREFIX.NO_PREFIX", "§6请指定前缀(单个字符)");
        chineseLanguageBase.put("COMMANDS.MESSAGE.PREFIX.OK", "§b当前前缀为");
        chineseLanguageBase.put("COMMANDS.MESSAGE.TOGGLE.UNKNOWN_MODULE", "§c未知模块");

        chineseLanguageBase.put("Module.Category.CLIENT.name", "客户端");
        chineseLanguageBase.put("Module.Category.COMBAT.name", "战斗类");
        chineseLanguageBase.put("Module.Category.MOVEMENT.name", "移动类");
        chineseLanguageBase.put("Module.Category.PLAYER.name", "玩家类");
        chineseLanguageBase.put("Module.Category.EXPLOIT.name", "漏洞类");
        chineseLanguageBase.put("Module.Category.VISUAL.name", "视觉类");
        chineseLanguageBase.put("Module.Category.MISC.name", "杂项");
        chineseLanguageBase.put("Module.Category.HUD.name", "HUD");

        chineseLanguageBase.put("Module.Special.Messages.Blocked", "模块{a}被{b}阻止了");
        chineseLanguageBase.put("Module.Special.ModuleBind.name", "快捷键");
        chineseLanguageBase.put("Module.Special.ModuleBind.description", "模块的快捷键");

        chineseLanguageBase.put("Theme.DefaultTheme.name", "默认主题");
        chineseLanguageBase.put("Theme.DefaultTheme.description", "Vergence客户端的默认主题");

        chineseLanguageBase.put("Theme.Error.Notify.AuthorIsNotExist.title", "主题错误");
        chineseLanguageBase.put("Theme.Error.Notify.AuthorIsNotExist.description", "主题作者不存在!");

        chineseLanguageBase.put("Module.Modules.ClickGUI.name", "点击界面");
        chineseLanguageBase.put("Module.Modules.ClickGUI.description", "点击界面的选项");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.TextOption.Title.name", "标题文字");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.TextOption.Title.description", "客户端点击界面的标题");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.BooleanOption.RainbowSync.name", "彩虹颜色同步");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.BooleanOption.RainbowSync.description", "使所有彩虹颜色保持一致");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowOffset.name", "彩虹颜色偏移");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowOffset.description", "彩虹颜色之间的最大偏移量");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowSpeed.name", "彩虹颜色速度");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowSpeed.description", "彩虹颜色变化的速度");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.MainPageAnimationTime.name", "主页面动画时间");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.MainPageAnimationTime.description", "动画的持续时间");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ModuleSpreadAnimationTime.name", "模块动画时间");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ModuleSpreadAnimationTime.description", "动画的持续时间");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.OptionsSpreadAnimationTime.name", "选项动画时间");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.OptionsSpreadAnimationTime.description", "动画的持续时间");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.DescriptionSpreadAnimationTime.name", "描述动画时间");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.DescriptionSpreadAnimationTime.description", "动画的持续时间");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollAnimationTime.name", "滚动动画时间");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollAnimationTime.description", "动画的持续时间");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollScale.name", "滚动比例");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollScale.description", "滚轮滚动的距离比例");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ColorAnimationTime.name", "颜色动画时间");
        chineseLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ColorAnimationTime.description", "动画的持续时间");

        chineseLanguageBase.put("Module.Modules.Client.name", "客户端");
        chineseLanguageBase.put("Module.Modules.Client.description", "管理Vergence客户端的许多设置");
        chineseLanguageBase.put("Module.Modules.Client.Options.BooleanOption.Sync.name", "同步共享");
        chineseLanguageBase.put("Module.Modules.Client.Options.BooleanOption.Sync.description", "共享你的Vergence用户状态，让其他启用了\"同步共享\"的用户可以看到你的Vergence图标");
        chineseLanguageBase.put("Module.Modules.Client.Options.EnumOption.UIScale.name", "UI缩放");
        chineseLanguageBase.put("Module.Modules.Client.Options.EnumOption.UIScale.description", "客户端的UI缩放");
        chineseLanguageBase.put("Module.Modules.Client.Options.EnumOption.Language.name", "语言");
        chineseLanguageBase.put("Module.Modules.Client.Options.EnumOption.Language.description", "客户端的语言");
        chineseLanguageBase.put("Module.Modules.Client.Options.EnumOption.Font.name", "字体");
        chineseLanguageBase.put("Module.Modules.Client.Options.EnumOption.Font.description", "客户端的字体样式");

        chineseLanguageBase.put("Module.Modules.Notify.name", "通知");
        chineseLanguageBase.put("Module.Modules.Notify.description", "关于通知的设置");
        chineseLanguageBase.put("Module.Modules.Notify.Options.BooleanOption.Rounded.name", "圆角");
        chineseLanguageBase.put("Module.Modules.Notify.Options.BooleanOption.Rounded.description", "为通知使用圆角矩形");
        chineseLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.Radius.name", "半径");
        chineseLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.Radius.description", "通知的圆角半径");
        chineseLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.NotificationAliveTime.name", "通知存活时间");
        chineseLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.NotificationAliveTime.description", "通知在屏幕上停留的时间");
        chineseLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.AnimationSpeed.name", "动画速度");
        chineseLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.AnimationSpeed.description", "动画的速度");

        chineseLanguageBase.put("Module.Modules.HudEditor.name", "HUD编辑器");
        chineseLanguageBase.put("Module.Modules.HudEditor.description", "编辑HUD相关内容");
        chineseLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.TextColor.name", "文本颜色");
        chineseLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.TextColor.description", "HUD编辑器中HUD的文本颜色");
        chineseLanguageBase.put("Module.Modules.HudEditor.Options.BooleanOption.Outline.name", "轮廓");
        chineseLanguageBase.put("Module.Modules.HudEditor.Options.BooleanOption.Outline.description", "选中HUD时显示轮廓");
        chineseLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.OutlineColor.name", "轮廓颜色");
        chineseLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.OutlineColor.description", "HUD的轮廓颜色");

        chineseLanguageBase.put("Module.Modules.AntiCheat.name", "反作弊设置");
        chineseLanguageBase.put("Module.Modules.AntiCheat.description", "管理功能模块的相关选项");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.AntiCheat.name", "反作弊模式");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.AntiCheat.description", "模块运行的反作弊模式");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.MultipleOption.AttackTargets.name", "目标");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.MultipleOption.AttackTargets.description", "战斗相关功能的攻击目标");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.BooleanOption.RotateSync.name", "旋转同步");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.BooleanOption.RotateSync.description", "将旋转与服务器同步");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.RotateMode.name", "转头模式");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.RotateMode.description", "模块转头的模式");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.DoubleOption.RotateTime.name", "转头时间");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.DoubleOption.RotateTime.description", "模块旋转的时间限制");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.SwingMode.name", "挥动模式");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.SwingMode.description", "模块挥动的模式");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.PlaceMode.name", "放置模式");
        chineseLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.PlaceMode.description", "模块放置的模式");

        chineseLanguageBase.put("Module.Modules.Watermark.name", "水印");
        chineseLanguageBase.put("Module.Modules.Watermark.description", "在屏幕上绘制水印");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.CustomTitle.name", "自定义标题");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.CustomTitle.description", "是否自定义水印标题");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.TextOption.Title.name", "标题");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.TextOption.Title.description", "水印标题");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Outline.name", "轮廓");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Outline.description", "显示轮廓");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.OutlineWidth.name", "轮廓宽度");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.OutlineWidth.description", "轮廓的宽度");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Rounded.name", "圆角矩形");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Rounded.description", "为水印应用圆角矩形");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.Radius.name", "圆角半径");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.Radius.description", "水印的圆角半径");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Split.name", "分割");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Split.description", "将水印各部分显示为独立区域");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedTime.name", "包含时间");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedTime.description", "将时间组件纳入水印");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedFPS.name", "包含FPS");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedFPS.description", "将FPS组件纳入水印");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedUser.name", "包含用户名");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedUser.description", "将用户名组件纳入水印");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedConfig.name", "包含配置");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedConfig.description", "将配置组件纳入水印");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.TextColor.name", "文本颜色");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.TextColor.description", "水印的文本颜色");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.BackgroundColor.name", "背景颜色");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.BackgroundColor.description", "水印的背景颜色");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.OutlineColor.name", "轮廓颜色");
        chineseLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.OutlineColor.description", "水印的轮廓颜色");

        chineseLanguageBase.put("Module.Modules.Spammer.name", "刷屏工具");
        chineseLanguageBase.put("Module.Modules.Spammer.description", "自动在聊天栏发送文本");
        chineseLanguageBase.put("Module.Modules.Spammer.Options.DoubleOption.Cooldown.name", "冷却时间");
        chineseLanguageBase.put("Module.Modules.Spammer.Options.DoubleOption.Cooldown.description", "发送消息的冷却时间");
        chineseLanguageBase.put("Module.Modules.Spammer.Options.EnumOption.ListOrder.name", "列表顺序");
        chineseLanguageBase.put("Module.Modules.Spammer.Options.EnumOption.ListOrder.description", "文本列表的顺序");
        chineseLanguageBase.put("Module.Modules.Spammer.Options.TextOption.FileName.name", "文件名");
        chineseLanguageBase.put("Module.Modules.Spammer.Options.TextOption.FileName.description", "文本列表的文件名");

        chineseLanguageBase.put("Module.Modules.AutoSprint.name", "自动疾跑");
        chineseLanguageBase.put("Module.Modules.AutoSprint.description", "移动时自动保持疾跑");
        chineseLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.Attack.name", "攻击");
        chineseLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.Attack.description", "攻击时模块会受到影响");
        chineseLanguageBase.put("Module.Modules.AutoSprint.Options.DoubleOption.AttackCounteract.name", "攻击抵消");
        chineseLanguageBase.put("Module.Modules.AutoSprint.Options.DoubleOption.AttackCounteract.description", "攻击的抵消比例");
        chineseLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.UseItem.name", "使用物品");
        chineseLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.UseItem.description", "使用物品时保持疾跑");

        chineseLanguageBase.put("Module.Modules.FastUse.name", "快速使用");
        chineseLanguageBase.put("Module.Modules.FastUse.description", "减少动作间的延迟");
        chineseLanguageBase.put("Module.Modules.FastUse.Options.DoubleOption.Delay.name", "延迟");
        chineseLanguageBase.put("Module.Modules.FastUse.Options.DoubleOption.Delay.description", "动作的延迟");
        chineseLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Blocks.name", "方块");
        chineseLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Blocks.description", "减少方块间的间隔");
        chineseLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Crystals.name", "水晶");
        chineseLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Crystals.description", "减少水晶间的间隔");
        chineseLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.XP.name", "经验");
        chineseLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.XP.description", "减少丢弃经验瓶的间隔");

        chineseLanguageBase.put("Module.Modules.ESP.name", "玩家透视");
        chineseLanguageBase.put("Module.Modules.ESP.description", "始终显示玩家信息");

        chineseLanguageBase.put("Module.Modules.AntiHungry.name", "保持饱腹");
        chineseLanguageBase.put("Module.Modules.AntiHungry.description", "通过合理方法降低饥饿感");
        chineseLanguageBase.put("Module.Modules.AntiHungry.Options.EnumOption.AntiCheat.name", "反作弊");
        chineseLanguageBase.put("Module.Modules.AntiHungry.Options.EnumOption.AntiCheat.description", "确定哪些功能需要被反作弊模式阻止");

        chineseLanguageBase.put("Module.Modules.BetterChat.name", "更好的聊天");
        chineseLanguageBase.put("Module.Modules.BetterChat.description", "允许自定义聊天界面");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.Prefix.name", "前缀");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.Prefix.description", "Vergence客户端前缀的样式");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PrefixColor.name", "前缀颜色");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PrefixColor.description", "前缀的颜色");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomNameColor.name", "自定义名称颜色");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomNameColor.description", "启用玩家名的自定义颜色");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerNameColor.name", "玩家名称颜色");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerNameColor.description", "玩家名称的颜色");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNamePrefix.name", "名称前缀");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNamePrefix.description", "玩家名称前的前缀");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNameSuffix.name", "名称后缀");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNameSuffix.description", "玩家名称后的后缀");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomChatColor.name", "自定义聊天颜色");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomChatColor.description", "启用聊天消息的自定义颜色");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerChatColor.name", "聊天文本颜色");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerChatColor.description", "聊天消息的颜色");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerChatPrefix.name", "聊天前缀");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerChatPrefix.description", "名称与消息之间的前缀");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationTime.name", "动画时间");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationTime.description", "动画的频率");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationOffset.name", "动画偏移");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationOffset.description", "动画的偏移量");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.AnimationQuadType.name", "动画类型");
        chineseLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.AnimationQuadType.description", "动画的计算方式");

        chineseLanguageBase.put("Module.Modules.SafeMode.name", "安全模式");
        chineseLanguageBase.put("Module.Modules.SafeMode.description", "尽力避免被反作弊检测");
        chineseLanguageBase.put("Module.Modules.SafeMode.Options.EnumOption.AntiCheatMode.name", "反作弊模式");
        chineseLanguageBase.put("Module.Modules.SafeMode.Options.EnumOption.AntiCheatMode.description", "确定哪些功能需要被反作弊模式阻止");
        chineseLanguageBase.put("Module.Modules.SafeMode.Options.BooleanOption.AllowTimer.name", "允许计时器");
        chineseLanguageBase.put("Module.Modules.SafeMode.Options.BooleanOption.AllowTimer.description", "启用后允许使用计时器，可能导致被反作弊检测");

        chineseLanguageBase.put("Module.Modules.KillAura.name", "杀戮光环");
        chineseLanguageBase.put("Module.Modules.KillAura.description", "自动攻击一定范围内的目标");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.AntiCheat.name", "反作弊");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.AntiCheat.description", "确定要绕过的反作弊策略");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.ClickType.name", "点击类型");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.ClickType.description", "选择自动攻击的点击模式");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Range.name", "范围");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Range.description", "攻击目标的最大距离");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.FOV.name", "视野");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.FOV.description", "攻击视野角度");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MinCPS.name", "最小CPS");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MinCPS.description", "每秒最小点击次数");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MaxCPS.name", "最大CPS");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MaxCPS.description", "每秒最大点击次数");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Delay.name", "点击延迟");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Delay.description", "每次点击间的延迟");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.RotateType.name", "旋转模式");
        chineseLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.RotateType.description", "攻击时使用的旋转模式");

        chineseLanguageBase.put("Module.Modules.Scaffold.name", "自动搭路");
        chineseLanguageBase.put("Module.Modules.Scaffold.description", "移动时自动在脚下放置方块");
        chineseLanguageBase.put("Module.Modules.Scaffold.Options.EnumOption.AntiCheat.name", "反作弊");
        chineseLanguageBase.put("Module.Modules.Scaffold.Options.EnumOption.AntiCheat.description", "确定哪些功能需要被反作弊模式阻止");

        chineseLanguageBase.put("Module.Modules.Reach.name", "更远交互");
        chineseLanguageBase.put("Module.Modules.Reach.description", "改变互动和攻击的范围");
        chineseLanguageBase.put("Module.Modules.Reach.Options.DoubleOption.Range.name", "范围");
        chineseLanguageBase.put("Module.Modules.Reach.Options.DoubleOption.Range.description", "互动和攻击的范围");

        chineseLanguageBase.put("Module.Modules.FakePlayer.name", "假人");
        chineseLanguageBase.put("Module.Modules.FakePlayer.description", "创建一个假人");
        chineseLanguageBase.put("Module.Modules.FakePlayer.Options.TextOption.PlayerName.name", "玩家名称");
        chineseLanguageBase.put("Module.Modules.FakePlayer.Options.TextOption.PlayerName.description", "要显示的假玩家名称");
        chineseLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.GoldenApple.name", "金苹果");
        chineseLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.GoldenApple.description", "为假玩家启用金苹果效果");
        chineseLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.AutoTotem.name", "自动图腾");
        chineseLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.AutoTotem.description", "假玩家死亡时自动使用图腾");

        chineseLanguageBase.put("Module.Modules.SafeWalk.name", "安全行走");
        chineseLanguageBase.put("Module.Modules.SafeWalk.description", "让你能安全地沿着方块边缘行走");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoInject.name", "注入输入");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoInject.description", "使用不安全的方法防止掉落");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoShift.name", "自动潜行");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoShift.description", "靠近边缘时自动潜行");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.RandomThreshold.name", "随机阈值");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.RandomThreshold.description", "随机化潜行边缘阈值");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.Threshold.name", "边缘阈值");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.Threshold.description", "触发潜行的固定距离");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MaxThreshold.name", "最大阈值");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MaxThreshold.description", "最大随机边缘阈值");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MinThreshold.name", "最小阈值");
        chineseLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MinThreshold.description", "最小随机边缘阈值");

        chineseLanguageBase.put("Module.Modules.FOVModifier.name", "视野修改器");
        chineseLanguageBase.put("Module.Modules.FOVModifier.description", "改变玩家的视野");
        chineseLanguageBase.put("Module.Modules.FOVModifier.Options.DoubleOption.FOV.name", "视野");
        chineseLanguageBase.put("Module.Modules.FOVModifier.Options.DoubleOption.FOV.description", "视野值");
        chineseLanguageBase.put("Module.Modules.FOVModifier.Options.BooleanOption.Items.name", "物品");
        chineseLanguageBase.put("Module.Modules.FOVModifier.Options.BooleanOption.Items.description", "将效果应用于物品");

        chineseLanguageBase.put("Module.Modules.Disabler.name", "禁用器");
        chineseLanguageBase.put("Module.Modules.Disabler.description", "尝试禁用某些反作弊行为");

        chineseLanguageBase.put("Module.Modules.InventoryMove.name", "背包移动");
        chineseLanguageBase.put("Module.Modules.InventoryMove.description", "打开某些GUI时可以移动");
        chineseLanguageBase.put("Module.Modules.InventoryMove.Options.BooleanOption.HorizontalCollision.name", "水平碰撞");
        chineseLanguageBase.put("Module.Modules.InventoryMove.Options.BooleanOption.HorizontalCollision.description", "发送数据包时是否启用水平碰撞");

        chineseLanguageBase.put("Module.Modules.AutoWalk.name", "自动行走");
        chineseLanguageBase.put("Module.Modules.AutoWalk.description", "自动向前移动");
        chineseLanguageBase.put("Module.Modules.AutoWalk.Options.EnumOption.Mode.name", "模式");
        chineseLanguageBase.put("Module.Modules.AutoWalk.Options.EnumOption.Mode.description", "Baritone支持");

        chineseLanguageBase.put("Module.Modules.NoCooldown.name", "无冷却");
        chineseLanguageBase.put("Module.Modules.NoCooldown.description", "减少操作间的冷却时间");
        chineseLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Attack.name", "攻击");
        chineseLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Attack.description", "无攻击冷却");
        chineseLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Jump.name", "跳跃");
        chineseLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Jump.description", "无跳跃冷却");
        chineseLanguageBase.put("Module.Modules.NoCooldown.Options.DoubleOption.JumpTicks.name", "跳跃刻");
        chineseLanguageBase.put("Module.Modules.NoCooldown.Options.DoubleOption.JumpTicks.description", "再次跳跃前需要等待的刻数");
    }

    private void loadRussian() {
        russianLanguageBase.put("COMMANDS.Aim.desc", "Прицелиться в позицию");
        russianLanguageBase.put("COMMANDS.Friend.desc", "Редактировать список друзей");
        russianLanguageBase.put("COMMANDS.Enemy.desc", "Редактировать список врагов");
        russianLanguageBase.put("COMMANDS.Bind.desc", "Привязать клавишу");
        russianLanguageBase.put("COMMANDS.Prefix.desc", "Установить префикс команды");
        russianLanguageBase.put("COMMANDS.Toggle.desc", "Переключить статус модуля");
        russianLanguageBase.put("COMMANDS.MESSAGE.COMMAND_NOT_FOUND", "Команды не найдены по:");
        russianLanguageBase.put("COMMANDS.MESSAGE.INVALID_COMMAND", "§cНеверная команда! §fВведите §ehelp§f для списка команд.");
        russianLanguageBase.put("COMMANDS.MESSAGE.FRIEND.LIST_TITLE", "§bДрузья:");
        russianLanguageBase.put("COMMANDS.MESSAGE.FRIEND.EMPTY", "§6Список друзей пуст");
        russianLanguageBase.put("COMMANDS.MESSAGE.FRIEND.RESET", "§fСписок друзей сброшен");
        russianLanguageBase.put("COMMANDS.MESSAGE.FRIEND.REMOVE", "§cДруг успешно удалён");
        russianLanguageBase.put("COMMANDS.MESSAGE.FRIEND.ADD", "§bДруг успешно добавлен");
        russianLanguageBase.put("COMMANDS.MESSAGE.FRIEND.IS_ENEMY", "§cЭтот игрок в списке врагов!");
        russianLanguageBase.put("COMMANDS.MESSAGE.ENEMY.LIST_TITLE", "§bВраги:");
        russianLanguageBase.put("COMMANDS.MESSAGE.ENEMY.EMPTY", "§6Список врагов пуст");
        russianLanguageBase.put("COMMANDS.MESSAGE.ENEMY.RESET", "§fСписок врагов сброшен");
        russianLanguageBase.put("COMMANDS.MESSAGE.ENEMY.REMOVE", "§cВраг успешно удалён");
        russianLanguageBase.put("COMMANDS.MESSAGE.ENEMY.ADD", "§bВраг успешно добавлен");
        russianLanguageBase.put("COMMANDS.MESSAGE.ENEMY.IS_FRIEND", "§cЭтот игрок в списке друзей!");
        russianLanguageBase.put("COMMANDS.MESSAGE.BIND.UNKNOWN_MODULE", "§cНеизвестный модуль");
        russianLanguageBase.put("COMMANDS.MESSAGE.BIND.NO_KEY", "§6Укажите клавишу");
        russianLanguageBase.put("COMMANDS.MESSAGE.BIND.UNKNOWN_ERROR", "§cНеизвестная внутренняя ошибка.");
        russianLanguageBase.put("COMMANDS.MESSAGE.BIND.BIND_OK", "§bКлавиша успешно привязана");
        russianLanguageBase.put("COMMANDS.MESSAGE.PREFIX.NO_PREFIX", "§6Укажите префикс (слово)");
        russianLanguageBase.put("COMMANDS.MESSAGE.PREFIX.OK", "§bТеперь префикс:");
        russianLanguageBase.put("COMMANDS.MESSAGE.TOGGLE.UNKNOWN_MODULE", "§cНеизвестный модуль");

        russianLanguageBase.put("Module.Category.CLIENT.name", "Клиент");
        russianLanguageBase.put("Module.Category.COMBAT.name", "Бой");
        russianLanguageBase.put("Module.Category.MOVEMENT.name", "Передвижение");
        russianLanguageBase.put("Module.Category.PLAYER.name", "Игрок");
        russianLanguageBase.put("Module.Category.EXPLOIT.name", "Эксплойт");
        russianLanguageBase.put("Module.Category.VISUAL.name", "Визуал");
        russianLanguageBase.put("Module.Category.MISC.name", "Разное");
        russianLanguageBase.put("Module.Category.HUD.name", "HUD");

        russianLanguageBase.put("Module.Special.Messages.Blocked", "Модуль {a} заблокирован {b}.");
        russianLanguageBase.put("Module.Special.ModuleBind.name", "Горячая клавиша");
        russianLanguageBase.put("Module.Special.ModuleBind.description", "Горячая клавиша модуля");

        russianLanguageBase.put("Theme.DefaultTheme.name", "Тема по умолчанию");
        russianLanguageBase.put("Theme.DefaultTheme.description", "Тема по умолчанию клиента Vergence");

        russianLanguageBase.put("Theme.Error.Notify.AuthorIsNotExist.title", "Ошибка темы");
        russianLanguageBase.put("Theme.Error.Notify.AuthorIsNotExist.description", "Автор темы не существует!");

        russianLanguageBase.put("Module.Modules.ClickGUI.name", "Клик-GUI");
        russianLanguageBase.put("Module.Modules.ClickGUI.description", "Настройки клик-GUI");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.TextOption.Title.name", "Заголовок");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.TextOption.Title.description", "Заголовок клиентского клик-GUI");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.BooleanOption.RainbowSync.name", "Синхронизация радуги");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.BooleanOption.RainbowSync.description", "Сделать все цвета радуги одинаковыми");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowOffset.name", "Смещение радуги");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowOffset.description", "Максимальное смещение между цветами радуги");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowSpeed.name", "Скорость радуги");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowSpeed.description", "Скорость изменения цветов радуги");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.MainPageAnimationTime.name", "Время анимации главной страницы");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.MainPageAnimationTime.description", "Продолжительность анимации");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ModuleSpreadAnimationTime.name", "Время анимации модуля");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ModuleSpreadAnimationTime.description", "Продолжительность анимации");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.OptionsSpreadAnimationTime.name", "Время анимации опций");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.OptionsSpreadAnimationTime.description", "Продолжительность анимации");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.DescriptionSpreadAnimationTime.name", "Опишите длину анимации");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.DescriptionSpreadAnimationTime.description", "Продолжительность анимации");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollAnimationTime.name", "Время анимации прокрутки");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollAnimationTime.description", "Продолжительность анимации");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollScale.name", "Масштаб прокрутки");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollScale.description", "Масштаб расстояния прокрутки колёсика");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ColorAnimationTime.name", "Время анимации цвета");
        russianLanguageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ColorAnimationTime.description", "Продолжительность анимации");

        russianLanguageBase.put("Module.Modules.Client.name", "Клиент");
        russianLanguageBase.put("Module.Modules.Client.description", "Управление многими настройками клиента Vergence");
        russianLanguageBase.put("Module.Modules.Client.Options.BooleanOption.Sync.name", "SYNC");
        russianLanguageBase.put("Module.Modules.Client.Options.BooleanOption.Sync.description", "Поделитесь статусом пользователя Vergence, чтобы вас могли видеть другие пользователи с включённым Sync.");
        russianLanguageBase.put("Module.Modules.Client.Options.EnumOption.UIScale.name", "Масштаб UI");
        russianLanguageBase.put("Module.Modules.Client.Options.EnumOption.UIScale.description", "Масштаб UI клиента");
        russianLanguageBase.put("Module.Modules.Client.Options.EnumOption.Language.name", "Язык");
        russianLanguageBase.put("Module.Modules.Client.Options.EnumOption.Language.description", "Язык клиента");
        russianLanguageBase.put("Module.Modules.Client.Options.EnumOption.Font.name", "Шрифт");
        russianLanguageBase.put("Module.Modules.Client.Options.EnumOption.Font.description", "Стиль шрифта клиента");

        russianLanguageBase.put("Module.Modules.Notify.name", "Уведомления");
        russianLanguageBase.put("Module.Modules.Notify.description", "Настройки уведомлений");
        russianLanguageBase.put("Module.Modules.Notify.Options.BooleanOption.Rounded.name", "Скруглённые");
        russianLanguageBase.put("Module.Modules.Notify.Options.BooleanOption.Rounded.description", "Использовать скруглённые прямоугольники для уведомлений");
        russianLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.Radius.name", "Радиус");
        russianLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.Radius.description", "Радиус уведомления");
        russianLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.NotificationAliveTime.name", "Время жизни уведомления");
        russianLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.NotificationAliveTime.description", "Время отображения уведомления на экране");
        russianLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.AnimationSpeed.name", "Скорость анимации");
        russianLanguageBase.put("Module.Modules.Notify.Options.DoubleOption.AnimationSpeed.description", "Скорость анимации");

        russianLanguageBase.put("Module.Modules.HudEditor.name", "Редактор HUD");
        russianLanguageBase.put("Module.Modules.HudEditor.description", "Редактирование элементов HUD");
        russianLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.TextColor.name", "Цвет текста");
        russianLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.TextColor.description", "Цвет текста HUD в редакторе");
        russianLanguageBase.put("Module.Modules.HudEditor.Options.BooleanOption.Outline.name", "Контур");
        russianLanguageBase.put("Module.Modules.HudEditor.Options.BooleanOption.Outline.description", "Отображать контур при выборе HUD");
        russianLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.OutlineColor.name", "Цвет контура");
        russianLanguageBase.put("Module.Modules.HudEditor.Options.ColorOption.OutlineColor.description", "Цвет контура HUD");

        russianLanguageBase.put("Module.Modules.AntiCheat.name", "Анти-чит");
        russianLanguageBase.put("Module.Modules.AntiCheat.description", "Управление параметрами боевых модулей");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.AntiCheat.name", "Режим анти-чита");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.AntiCheat.description", "Режим анти-чита для работы модуля");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.MultipleOption.AttackTargets.name", "Цели");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.MultipleOption.AttackTargets.description", "Цели для боевых функций");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.BooleanOption.RotateSync.name", "Синхронизация вращения");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.BooleanOption.RotateSync.description", "Синхронизировать вращение с сервером");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.RotateMode.name", "Режим вращения");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.RotateMode.description", "Режим вращения модуля");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.DoubleOption.RotateTime.name", "Время вращения");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.DoubleOption.RotateTime.description", "Ограничение времени вращения");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.SwingMode.name", "Режим замаха");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.SwingMode.description", "Режим замаха модуля");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.PlaceMode.name", "Режим размещения");
        russianLanguageBase.put("Module.Modules.AntiCheat.Options.EnumOption.PlaceMode.description", "Режим размещения модуля");

        russianLanguageBase.put("Module.Modules.Watermark.name", "Водяной знак");
        russianLanguageBase.put("Module.Modules.Watermark.description", "Отображает водяной знак на экране");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.CustomTitle.name", "Пользовательский заголовок");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.CustomTitle.description", "Использовать пользовательский заголовок водяного знака");
        russianLanguageBase.put("Module.Modules.Watermark.Options.TextOption.Title.name", "Заголовок");
        russianLanguageBase.put("Module.Modules.Watermark.Options.TextOption.Title.description", "Заголовок водяного знака");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Outline.name", "Контур");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Outline.description", "Отображать контур");
        russianLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.OutlineWidth.name", "Ширина контура");
        russianLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.OutlineWidth.description", "Ширина контура");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Rounded.name", "Скруглённый прямоугольник");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Rounded.description", "Применить скруглённый прямоугольник к водяному знаку");
        russianLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.Radius.name", "Радиус скругления");
        russianLanguageBase.put("Module.Modules.Watermark.Options.DoubleOption.Radius.description", "Радиус скругления водяного знака");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Split.name", "Разделение");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.Split.description", "Отображать каждую часть водяного знака отдельно");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedTime.name", "Включить время");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedTime.description", "Добавить компонент времени в водяной знак");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedFPS.name", "Включить FPS");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedFPS.description", "Добавить компонент FPS в водяной знак");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedUser.name", "Включить имя");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedUser.description", "Добавить имя пользователя в водяной знак");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedConfig.name", "Включить конфиг");
        russianLanguageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedConfig.description", "Добавить конфигурацию в водяной знак");
        russianLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.TextColor.name", "Цвет текста");
        russianLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.TextColor.description", "Цвет текста водяного знака");
        russianLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.BackgroundColor.name", "Цвет фона");
        russianLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.BackgroundColor.description", "Цвет фона водяного знака");
        russianLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.OutlineColor.name", "Цвет контура");
        russianLanguageBase.put("Module.Modules.Watermark.Options.ColorOption.OutlineColor.description", "Цвет контура водяного знака");

        russianLanguageBase.put("Module.Modules.Spammer.name", "Спаммер");
        russianLanguageBase.put("Module.Modules.Spammer.description", "Автоматически печатает текст в чат");
        russianLanguageBase.put("Module.Modules.Spammer.Options.DoubleOption.Cooldown.name", "Задержка");
        russianLanguageBase.put("Module.Modules.Spammer.Options.DoubleOption.Cooldown.description", "Время задержки между сообщениями");
        russianLanguageBase.put("Module.Modules.Spammer.Options.EnumOption.ListOrder.name", "Порядок списка");
        russianLanguageBase.put("Module.Modules.Spammer.Options.EnumOption.ListOrder.description", "Порядок текстового списка");
        russianLanguageBase.put("Module.Modules.Spammer.Options.TextOption.FileName.name", "Имя файла");
        russianLanguageBase.put("Module.Modules.Spammer.Options.TextOption.FileName.description", "Имя файла текстового списка");

        russianLanguageBase.put("Module.Modules.AutoSprint.name", "Авто-бег");
        russianLanguageBase.put("Module.Modules.AutoSprint.description", "Всегда бегать при движении");
        russianLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.Attack.name", "Атака");
        russianLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.Attack.description", "Модуль повлияет при атаке");
        russianLanguageBase.put("Module.Modules.AutoSprint.Options.DoubleOption.AttackCounteract.name", "Противодействие атаки");
        russianLanguageBase.put("Module.Modules.AutoSprint.Options.DoubleOption.AttackCounteract.description", "Масштаб противодействия атаки");
        russianLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.UseItem.name", "Использование предметов");
        russianLanguageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.UseItem.description", "Сохранять бег при использовании предметов");

        russianLanguageBase.put("Module.Modules.FastUse.name", "Быстрое использование");
        russianLanguageBase.put("Module.Modules.FastUse.description", "Уменьшить задержку между действиями");
        russianLanguageBase.put("Module.Modules.FastUse.Options.DoubleOption.Delay.name", "Задержка");
        russianLanguageBase.put("Module.Modules.FastUse.Options.DoubleOption.Delay.description", "Задержка между действиями");
        russianLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Blocks.name", "Блоки");
        russianLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Blocks.description", "Уменьшить интервал между блоками");
        russianLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Crystals.name", "Кристаллы");
        russianLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.Crystals.description", "Уменьшить интервал между кристаллами");
        russianLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.XP.name", "Опыт");
        russianLanguageBase.put("Module.Modules.FastUse.Options.BooleanOption.XP.description", "Уменьшить интервал выбрасывания бутыльков опыта");

        russianLanguageBase.put("Module.Modules.ESP.name", "ESP игрока");
        russianLanguageBase.put("Module.Modules.ESP.description", "Всегда отображать информацию об игроках");

        russianLanguageBase.put("Module.Modules.AntiHungry.name", "Анти-голод");
        russianLanguageBase.put("Module.Modules.AntiHungry.description", "Уменьшает чувство голода разумным способом");
        russianLanguageBase.put("Module.Modules.AntiHungry.Options.EnumOption.AntiCheat.name", "Анти-чит");
        russianLanguageBase.put("Module.Modules.AntiHungry.Options.EnumOption.AntiCheat.description", "Определяет, какие функции блокируются режимом анти-чита");

        russianLanguageBase.put("Module.Modules.BetterChat.name", "Улучшенный чат");
        russianLanguageBase.put("Module.Modules.BetterChat.description", "Позволяет настроить интерфейс чата");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.Prefix.name", "Префикс");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.Prefix.description", "Стиль префикса клиента Vergence");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PrefixColor.name", "Цвет префикса");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PrefixColor.description", "Цвет префикса");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomNameColor.name", "Пользовательский цвет имени");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomNameColor.description", "Включить пользовательский цвет имени игрока");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerNameColor.name", "Цвет имени игрока");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerNameColor.description", "Цвет имени игрока");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNamePrefix.name", "Префикс имени");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNamePrefix.description", "Префикс перед именем игрока");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNameSuffix.name", "Суффикс имени");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNameSuffix.description", "Суффикс после имени игрока");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomChatColor.name", "Пользовательский цвет чата");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomChatColor.description", "Включить пользовательский цвет сообщений в чате");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerChatColor.name", "Цвет текста чата");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerChatColor.description", "Цвет ваших сообщений в чате");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerChatPrefix.name", "Префикс чата");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerChatPrefix.description", "Префикс между именем и сообщением");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationTime.name", "Время анимации");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationTime.description", "Частота анимации");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationOffset.name", "Смещение анимации");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationOffset.description", "Смещение анимации");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.AnimationQuadType.name", "Тип анимации");
        russianLanguageBase.put("Module.Modules.BetterChat.Options.EnumOption.AnimationQuadType.description", "Стиль вычисления анимации");

        russianLanguageBase.put("Module.Modules.SafeMode.name", "Безопасный режим");
        russianLanguageBase.put("Module.Modules.SafeMode.description", "Максимально избегает обнаружения анти-читом");
        russianLanguageBase.put("Module.Modules.SafeMode.Options.EnumOption.AntiCheatMode.name", "Режим анти-чита");
        russianLanguageBase.put("Module.Modules.SafeMode.Options.EnumOption.AntiCheatMode.description", "Определяет, какие функции блокируются режимом анти-чита");
        russianLanguageBase.put("Module.Modules.SafeMode.Options.BooleanOption.AllowTimer.name", "Разрешить таймер");
        russianLanguageBase.put("Module.Modules.SafeMode.Options.BooleanOption.AllowTimer.description", "Если включено, таймер может быть использован. Это может привести к обнаружению анти-читом");

        russianLanguageBase.put("Module.Modules.KillAura.name", "Kill Aura");
        russianLanguageBase.put("Module.Modules.KillAura.description", "Автоматически атакует объекты в радиусе");
        russianLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.AntiCheat.name", "Анти-чит");
        russianLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.AntiCheat.description", "Определяет, какую стратегию анти-чита обходить");
        russianLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.ClickType.name", "Тип клика");
        russianLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.ClickType.description", "Выберите режим автоматической атаки");
        russianLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Range.name", "Радиус");
        russianLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Range.description", "Максимальная дистанция атаки");
        russianLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.FOV.name", "Поле зрения");
        russianLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.FOV.description", "Угол поля зрения атаки");
        russianLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MinCPS.name", "Мин CPS");
        russianLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MinCPS.description", "Минимальное количество кликов в секунду");
        russianLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MaxCPS.name", "Макс CPS");
        russianLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.MaxCPS.description", "Максимальное количество кликов в секунду");
        russianLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Delay.name", "Задержка клика");
        russianLanguageBase.put("Module.Modules.KillAura.Options.DoubleOption.Delay.description", "Задержка между кликами");
        russianLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.RotateType.name", "Режим вращения");
        russianLanguageBase.put("Module.Modules.KillAura.Options.EnumOption.RotateType.description", "Режим вращения при атаке");

        russianLanguageBase.put("Module.Modules.Scaffold.name", "Scaffold");
        russianLanguageBase.put("Module.Modules.Scaffold.description", "Автоматически размещает блоки под ногами при движении");
        russianLanguageBase.put("Module.Modules.Scaffold.Options.EnumOption.AntiCheat.name", "Анти-чит");
        russianLanguageBase.put("Module.Modules.Scaffold.Options.EnumOption.AntiCheat.description", "Определяет, какие функции блокируются режимом анти-чита");

        russianLanguageBase.put("Module.Modules.Reach.name", "Досягаемость");
        russianLanguageBase.put("Module.Modules.Reach.description", "Изменяет дальность взаимодействия и атаки");
        russianLanguageBase.put("Module.Modules.Reach.Options.DoubleOption.Range.name", "Дальность");
        russianLanguageBase.put("Module.Modules.Reach.Options.DoubleOption.Range.description", "Дальность взаимодействия и атаки");

        russianLanguageBase.put("Module.Modules.FakePlayer.name", "Фальшивый игрок");
        russianLanguageBase.put("Module.Modules.FakePlayer.description", "Создаёт фальшивого игрока");
        russianLanguageBase.put("Module.Modules.FakePlayer.Options.TextOption.PlayerName.name", "Имя игрока");
        russianLanguageBase.put("Module.Modules.FakePlayer.Options.TextOption.PlayerName.description", "Имя отображаемого фальшивого игрока");
        russianLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.GoldenApple.name", "Золотое яблоко");
        russianLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.GoldenApple.description", "Включить эффекты золотого яблока на фальшивого игрока");
        russianLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.AutoTotem.name", "Авто тотем");
        russianLanguageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.AutoTotem.description", "Автоматически использовать тотем при смерти фальшивого игрока");

        russianLanguageBase.put("Module.Modules.SafeWalk.name", "Безопасная ходьба");
        russianLanguageBase.put("Module.Modules.SafeWalk.description", "Позволяет безопасно ходить по краю блоков");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoInject.name", "Инъекция ввода");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoInject.description", "Использовать небезопасный метод, чтобы не упасть");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoShift.name", "Авто-приседание");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoShift.description", "Автоматически приседать у края");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.RandomThreshold.name", "Случайный порог");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.RandomThreshold.description", "Случайное значение порога приседания у края");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.Threshold.name", "Порог края");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.Threshold.description", "Фиксированное расстояние для активации приседания");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MaxThreshold.name", "Макс порог");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MaxThreshold.description", "Максимальный случайный порог края");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MinThreshold.name", "Мин порог");
        russianLanguageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MinThreshold.description", "Минимальный случайный порог края");

        russianLanguageBase.put("Module.Modules.FOVModifier.name", "Изменитель FOV");
        russianLanguageBase.put("Module.Modules.FOVModifier.description", "Изменяет поле зрения игрока");
        russianLanguageBase.put("Module.Modules.FOVModifier.Options.DoubleOption.FOV.name", "FOV");
        russianLanguageBase.put("Module.Modules.FOVModifier.Options.DoubleOption.FOV.description", "Поле зрения");
        russianLanguageBase.put("Module.Modules.FOVModifier.Options.BooleanOption.Items.name", "Предметы");
        russianLanguageBase.put("Module.Modules.FOVModifier.Options.BooleanOption.Items.description", "Применить эффект к предметам");

        russianLanguageBase.put("Module.Modules.Disabler.name", "Отключатель");
        russianLanguageBase.put("Module.Modules.Disabler.description", "Попытка отключить определённые поведения анти-чита");

        russianLanguageBase.put("Module.Modules.InventoryMove.name", "Движение в инвентаре");
        russianLanguageBase.put("Module.Modules.InventoryMove.description", "Позволяет двигаться при открытии определённых GUI");
        russianLanguageBase.put("Module.Modules.InventoryMove.Options.BooleanOption.HorizontalCollision.name", "Горизонтальное столкновение");
        russianLanguageBase.put("Module.Modules.InventoryMove.Options.BooleanOption.HorizontalCollision.description", "Включить горизонтальное столкновение при отправке пакетов");

        russianLanguageBase.put("Module.Modules.AutoWalk.name", "Авто-ходьба");
        russianLanguageBase.put("Module.Modules.AutoWalk.description", "Автоматически двигаться вперёд");
        russianLanguageBase.put("Module.Modules.AutoWalk.Options.EnumOption.Mode.name", "Режим");
        russianLanguageBase.put("Module.Modules.AutoWalk.Options.EnumOption.Mode.description", "Поддержка Baritone");

        russianLanguageBase.put("Module.Modules.NoCooldown.name", "Без кулдауна");
        russianLanguageBase.put("Module.Modules.NoCooldown.description", "Уменьшает время кулдауна между действиями");
        russianLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Attack.name", "Атака");
        russianLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Attack.description", "Отключить кулдаун атаки");
        russianLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Jump.name", "Прыжок");
        russianLanguageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Jump.description", "Отключить кулдаун прыжка");
        russianLanguageBase.put("Module.Modules.NoCooldown.Options.DoubleOption.JumpTicks.name", "Тики прыжка");
        russianLanguageBase.put("Module.Modules.NoCooldown.Options.DoubleOption.JumpTicks.description", "Количество тиков ожидания перед следующим прыжком");
    }

    public String get(String key) {
        String result;
        switch (Client.INSTANCE == null ? Languages.English : (Languages) Client.INSTANCE.language.getValue()) {
            case Chinese -> {
                result = chineseLanguageBase.get(key);
            }
            case Russian -> {
                result = russianLanguageBase.get(key);
            }
            default -> {
                result = englishLanguageBase.get(key);
            }
        }

        if (result == null) {
            Vergence.CONSOLE.logWarn("[Text Manager] Language Text not found, text key: " + key);
            return key;
        }

        return result;
    }
}
