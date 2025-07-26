package cc.vergence.features.languages.impl;

import cc.vergence.Vergence;
import cc.vergence.features.languages.Language;
import cc.vergence.features.managers.other.TextManager;

public class RussianLanguage extends Language {
    public RussianLanguage() {
        load();
        Vergence.CONSOLE.logInfo("language \"русский язык (Russian) \" was loaded!");
    }

    @Override
    public void load() {
        languageBase.put("COMMANDS.Aim.desc", "Прицелиться в позицию");
        languageBase.put("COMMANDS.Friend.desc", "Редактировать список друзей");
        languageBase.put("COMMANDS.Enemy.desc", "Редактировать список врагов");
        languageBase.put("COMMANDS.Bind.desc", "Привязать клавишу");
        languageBase.put("COMMANDS.Prefix.desc", "Установить префикс команды");
        languageBase.put("COMMANDS.Toggle.desc", "Переключить статус модуля");
        languageBase.put("COMMANDS.MESSAGE.COMMAND_NOT_FOUND", "Команды не найдены по:");
        languageBase.put("COMMANDS.MESSAGE.INVALID_COMMAND", "§cНеверная команда! §fВведите §ehelp§f для списка команд");
        languageBase.put("COMMANDS.MESSAGE.FRIEND.LIST_TITLE", "§bДрузья:");
        languageBase.put("COMMANDS.MESSAGE.FRIEND.EMPTY", "§6Список друзей пуст");
        languageBase.put("COMMANDS.MESSAGE.FRIEND.RESET", "§fСписок друзей сброшен");
        languageBase.put("COMMANDS.MESSAGE.FRIEND.REMOVE", "§cДруг успешно удалён");
        languageBase.put("COMMANDS.MESSAGE.FRIEND.ADD", "§bДруг успешно добавлен");
        languageBase.put("COMMANDS.MESSAGE.FRIEND.IS_ENEMY", "§cЭтот игрок в списке врагов!");
        languageBase.put("COMMANDS.MESSAGE.ENEMY.LIST_TITLE", "§bВраги:");
        languageBase.put("COMMANDS.MESSAGE.ENEMY.EMPTY", "§6Список врагов пуст");
        languageBase.put("COMMANDS.MESSAGE.ENEMY.RESET", "§fСписок врагов сброшен");
        languageBase.put("COMMANDS.MESSAGE.ENEMY.REMOVE", "§cВраг успешно удалён");
        languageBase.put("COMMANDS.MESSAGE.ENEMY.ADD", "§bВраг успешно добавлен");
        languageBase.put("COMMANDS.MESSAGE.ENEMY.IS_FRIEND", "§cЭтот игрок в списке друзей!");
        languageBase.put("COMMANDS.MESSAGE.BIND.UNKNOWN_MODULE", "§cНеизвестный модуль");
        languageBase.put("COMMANDS.MESSAGE.BIND.NO_KEY", "§6Укажите клавишу");
        languageBase.put("COMMANDS.MESSAGE.BIND.UNKNOWN_ERROR", "§cНеизвестная внутренняя ошибка");
        languageBase.put("COMMANDS.MESSAGE.BIND.BIND_OK", "§bКлавиша успешно привязана");
        languageBase.put("COMMANDS.MESSAGE.PREFIX.NO_PREFIX", "§6Укажите префикс (слово)");
        languageBase.put("COMMANDS.MESSAGE.PREFIX.OK", "§bТеперь префикс:");
        languageBase.put("COMMANDS.MESSAGE.TOGGLE.UNKNOWN_MODULE", "§cНеизвестный модуль");

        languageBase.put("Module.Category.CLIENT.name", "Клиент");
        languageBase.put("Module.Category.COMBAT.name", "Бой");
        languageBase.put("Module.Category.MOVEMENT.name", "Передвижение");
        languageBase.put("Module.Category.PLAYER.name", "Игрок");
        languageBase.put("Module.Category.EXPLOIT.name", "Эксплойт");
        languageBase.put("Module.Category.VISUAL.name", "Визуал");
        languageBase.put("Module.Category.MISC.name", "Разное");
        languageBase.put("Module.Category.HUD.name", "HUD");

        languageBase.put("Module.Special.Messages.Blocked", "Модуль {a} заблокирован {b}");
        languageBase.put("Module.Special.ModuleBind.name", "Горячая клавиша");
        languageBase.put("Module.Special.ModuleBind.description", "Горячая клавиша модуля");

        languageBase.put("Theme.DefaultTheme.name", "Тема по умолчанию");
        languageBase.put("Theme.DefaultTheme.description", "Тема по умолчанию клиента Vergence");

        languageBase.put("Theme.Error.Notify.AuthorIsNotExist.title", "Ошибка темы");
        languageBase.put("Theme.Error.Notify.AuthorIsNotExist.description", "Автор темы не существует!");

        languageBase.put("Module.Modules.ClickGUI.name", "Клик-GUI");
        languageBase.put("Module.Modules.ClickGUI.description", "Настройки клик-GUI");
        languageBase.put("Module.Modules.ClickGUI.Options.TextOption.Title.name", "Заголовок");
        languageBase.put("Module.Modules.ClickGUI.Options.TextOption.Title.description", "Заголовок клиентского клик-GUI");
        languageBase.put("Module.Modules.ClickGUI.Options.BooleanOption.RainbowSync.name", "Синхронизация радуги");
        languageBase.put("Module.Modules.ClickGUI.Options.BooleanOption.RainbowSync.description", "Сделать все цвета радуги одинаковыми");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowOffset.name", "Смещение радуги");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowOffset.description", "Максимальное смещение между цветами радуги");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowSpeed.name", "Скорость радуги");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.RainbowSpeed.description", "Скорость изменения цветов радуги");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.MainPageAnimationTime.name", "Время анимации главной страницы");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.MainPageAnimationTime.description", "Продолжительность анимации");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ModuleSpreadAnimationTime.name", "Время анимации модуля");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ModuleSpreadAnimationTime.description", "Продолжительность анимации");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.OptionsSpreadAnimationTime.name", "Время анимации опций");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.OptionsSpreadAnimationTime.description", "Продолжительность анимации");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.DescriptionSpreadAnimationTime.name", "Опишите длину анимации");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.DescriptionSpreadAnimationTime.description", "Продолжительность анимации");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollAnimationTime.name", "Время анимации прокрутки");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollAnimationTime.description", "Продолжительность анимации");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollScale.name", "Масштаб прокрутки");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ScrollScale.description", "Масштаб расстояния прокрутки колёсика");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ColorAnimationTime.name", "Время анимации цвета");
        languageBase.put("Module.Modules.ClickGUI.Options.DoubleOption.ColorAnimationTime.description", "Продолжительность анимации");

        languageBase.put("Module.Modules.Client.name", "Клиент");
        languageBase.put("Module.Modules.Client.description", "Управление многими настройками клиента Vergence");
        languageBase.put("Module.Modules.Client.Options.BooleanOption.Sync.name", "SYNC");
        languageBase.put("Module.Modules.Client.Options.BooleanOption.Sync.description", "Поделитесь статусом пользователя Vergence, чтобы вас могли видеть другие пользователи с включённым Sync");
        languageBase.put("Module.Modules.Client.Options.EnumOption.UIScale.name", "Масштаб UI");
        languageBase.put("Module.Modules.Client.Options.EnumOption.UIScale.description", "Масштаб UI клиента");
        languageBase.put("Module.Modules.Client.Options.EnumOption.Language.name", "Язык");
        languageBase.put("Module.Modules.Client.Options.EnumOption.Language.description", "Язык клиента");
        languageBase.put("Module.Modules.Client.Options.EnumOption.Font.name", "Шрифт");
        languageBase.put("Module.Modules.Client.Options.EnumOption.Font.description", "Стиль шрифта клиента");

        languageBase.put("Module.Modules.Notify.name", "Уведомления");
        languageBase.put("Module.Modules.Notify.description", "Настройки уведомлений");
        languageBase.put("Module.Modules.Notify.Options.BooleanOption.Rounded.name", "Скруглённые");
        languageBase.put("Module.Modules.Notify.Options.BooleanOption.Rounded.description", "Использовать скруглённые прямоугольники для уведомлений");
        languageBase.put("Module.Modules.Notify.Options.DoubleOption.Radius.name", "Радиус");
        languageBase.put("Module.Modules.Notify.Options.DoubleOption.Radius.description", "Радиус уведомления");
        languageBase.put("Module.Modules.Notify.Options.DoubleOption.NotificationAliveTime.name", "Время жизни уведомления");
        languageBase.put("Module.Modules.Notify.Options.DoubleOption.NotificationAliveTime.description", "Время отображения уведомления на экране");
        languageBase.put("Module.Modules.Notify.Options.DoubleOption.AnimationSpeed.name", "Скорость анимации");
        languageBase.put("Module.Modules.Notify.Options.DoubleOption.AnimationSpeed.description", "Скорость анимации");

        languageBase.put("Module.Modules.HudEditor.name", "Редактор HUD");
        languageBase.put("Module.Modules.HudEditor.description", "Редактирование элементов HUD");
        languageBase.put("Module.Modules.HudEditor.Options.ColorOption.TextColor.name", "Цвет текста");
        languageBase.put("Module.Modules.HudEditor.Options.ColorOption.TextColor.description", "Цвет текста HUD в редакторе");
        languageBase.put("Module.Modules.HudEditor.Options.BooleanOption.Outline.name", "Контур");
        languageBase.put("Module.Modules.HudEditor.Options.BooleanOption.Outline.description", "Отображать контур при выборе HUD");
        languageBase.put("Module.Modules.HudEditor.Options.ColorOption.OutlineColor.name", "Цвет контура");
        languageBase.put("Module.Modules.HudEditor.Options.ColorOption.OutlineColor.description", "Цвет контура HUD");

        languageBase.put("Module.Modules.Watermark.name", "Водяной знак");
        languageBase.put("Module.Modules.Watermark.description", "Отображает водяной знак на экране");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.CustomTitle.name", "Пользовательский заголовок");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.CustomTitle.description", "Использовать пользовательский заголовок водяного знака");
        languageBase.put("Module.Modules.Watermark.Options.TextOption.Title.name", "Заголовок");
        languageBase.put("Module.Modules.Watermark.Options.TextOption.Title.description", "Заголовок водяного знака");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.Outline.name", "Контур");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.Outline.description", "Отображать контур");
        languageBase.put("Module.Modules.Watermark.Options.DoubleOption.OutlineWidth.name", "Ширина контура");
        languageBase.put("Module.Modules.Watermark.Options.DoubleOption.OutlineWidth.description", "Ширина контура");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.Rounded.name", "Скруглённый прямоугольник");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.Rounded.description", "Применить скруглённый прямоугольник к водяному знаку");
        languageBase.put("Module.Modules.Watermark.Options.DoubleOption.Radius.name", "Радиус скругления");
        languageBase.put("Module.Modules.Watermark.Options.DoubleOption.Radius.description", "Радиус скругления водяного знака");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.Split.name", "Разделение");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.Split.description", "Отображать каждую часть водяного знака отдельно");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedTime.name", "Включить время");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedTime.description", "Добавить компонент времени в водяной знак");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedFPS.name", "Включить FPS");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedFPS.description", "Добавить компонент FPS в водяной знак");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedUser.name", "Включить имя");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedUser.description", "Добавить имя пользователя в водяной знак");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedConfig.name", "Включить конфиг");
        languageBase.put("Module.Modules.Watermark.Options.BooleanOption.IncludedConfig.description", "Добавить конфигурацию в водяной знак");
        languageBase.put("Module.Modules.Watermark.Options.ColorOption.TextColor.name", "Цвет текста");
        languageBase.put("Module.Modules.Watermark.Options.ColorOption.TextColor.description", "Цвет текста водяного знака");
        languageBase.put("Module.Modules.Watermark.Options.ColorOption.BackgroundColor.name", "Цвет фона");
        languageBase.put("Module.Modules.Watermark.Options.ColorOption.BackgroundColor.description", "Цвет фона водяного знака");
        languageBase.put("Module.Modules.Watermark.Options.ColorOption.OutlineColor.name", "Цвет контура");
        languageBase.put("Module.Modules.Watermark.Options.ColorOption.OutlineColor.description", "Цвет контура водяного знака");

        languageBase.put("Module.Modules.Spammer.name", "Спаммер");
        languageBase.put("Module.Modules.Spammer.description", "Автоматически печатает текст в чат");
        languageBase.put("Module.Modules.Spammer.Options.DoubleOption.Cooldown.name", "Задержка");
        languageBase.put("Module.Modules.Spammer.Options.DoubleOption.Cooldown.description", "Время задержки между сообщениями");
        languageBase.put("Module.Modules.Spammer.Options.EnumOption.ListOrder.name", "Порядок списка");
        languageBase.put("Module.Modules.Spammer.Options.EnumOption.ListOrder.description", "Порядок текстового списка");
        languageBase.put("Module.Modules.Spammer.Options.TextOption.FileName.name", "Имя файла");
        languageBase.put("Module.Modules.Spammer.Options.TextOption.FileName.description", "Имя файла текстового списка");

        languageBase.put("Module.Modules.AutoSprint.name", "Авто-бег");
        languageBase.put("Module.Modules.AutoSprint.description", "Всегда бегать при движении");
        languageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.Attack.name", "Атака");
        languageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.Attack.description", "Модуль повлияет при атаке");
        languageBase.put("Module.Modules.AutoSprint.Options.DoubleOption.AttackCounteract.name", "Противодействие атаки");
        languageBase.put("Module.Modules.AutoSprint.Options.DoubleOption.AttackCounteract.description", "Масштаб противодействия атаки");
        languageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.UseItem.name", "Использование предметов");
        languageBase.put("Module.Modules.AutoSprint.Options.BooleanOption.UseItem.description", "Сохранять бег при использовании предметов");

        languageBase.put("Module.Modules.FastUse.name", "Быстрое использование");
        languageBase.put("Module.Modules.FastUse.description", "Уменьшить задержку между действиями");
        languageBase.put("Module.Modules.FastUse.Options.DoubleOption.Delay.name", "Задержка");
        languageBase.put("Module.Modules.FastUse.Options.DoubleOption.Delay.description", "Задержка между действиями");
        languageBase.put("Module.Modules.FastUse.Options.BooleanOption.Blocks.name", "Блоки");
        languageBase.put("Module.Modules.FastUse.Options.BooleanOption.Blocks.description", "Уменьшить интервал между блоками");
        languageBase.put("Module.Modules.FastUse.Options.BooleanOption.Crystals.name", "Кристаллы");
        languageBase.put("Module.Modules.FastUse.Options.BooleanOption.Crystals.description", "Уменьшить интервал между кристаллами");
        languageBase.put("Module.Modules.FastUse.Options.BooleanOption.XP.name", "Опыт");
        languageBase.put("Module.Modules.FastUse.Options.BooleanOption.XP.description", "Уменьшить интервал выбрасывания бутыльков опыта");

        languageBase.put("Module.Modules.ESP.name", "ESP игрока");
        languageBase.put("Module.Modules.ESP.description", "Всегда отображать информацию об игроках");

        languageBase.put("Module.Modules.AntiHungry.name", "Анти-голод");
        languageBase.put("Module.Modules.AntiHungry.description", "Уменьшает чувство голода разумным способом");
        languageBase.put("Module.Modules.AntiHungry.Options.EnumOption.AntiCheat.name", "Анти-чит");
        languageBase.put("Module.Modules.AntiHungry.Options.EnumOption.AntiCheat.description", "Определяет, какие функции блокируются режимом анти-чита");

        languageBase.put("Module.Modules.BetterChat.name", "Улучшенный чат");
        languageBase.put("Module.Modules.BetterChat.description", "Позволяет настроить интерфейс чата");
        languageBase.put("Module.Modules.BetterChat.Options.EnumOption.Prefix.name", "Префикс");
        languageBase.put("Module.Modules.BetterChat.Options.EnumOption.Prefix.description", "Стиль префикса клиента Vergence");
        languageBase.put("Module.Modules.BetterChat.Options.ColorOption.PrefixColor.name", "Цвет префикса");
        languageBase.put("Module.Modules.BetterChat.Options.ColorOption.PrefixColor.description", "Цвет префикса");
        languageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomNameColor.name", "Пользовательский цвет имени");
        languageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomNameColor.description", "Включить пользовательский цвет имени игрока");
        languageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerNameColor.name", "Цвет имени игрока");
        languageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerNameColor.description", "Цвет имени игрока");
        languageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNamePrefix.name", "Префикс имени");
        languageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNamePrefix.description", "Префикс перед именем игрока");
        languageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNameSuffix.name", "Суффикс имени");
        languageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerNameSuffix.description", "Суффикс после имени игрока");
        languageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomChatColor.name", "Пользовательский цвет чата");
        languageBase.put("Module.Modules.BetterChat.Options.BooleanOption.CustomChatColor.description", "Включить пользовательский цвет сообщений в чате");
        languageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerChatColor.name", "Цвет текста чата");
        languageBase.put("Module.Modules.BetterChat.Options.ColorOption.PlayerChatColor.description", "Цвет ваших сообщений в чате");
        languageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerChatPrefix.name", "Префикс чата");
        languageBase.put("Module.Modules.BetterChat.Options.TextOption.PlayerChatPrefix.description", "Префикс между именем и сообщением");
        languageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationTime.name", "Время анимации");
        languageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationTime.description", "Частота анимации");
        languageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationOffset.name", "Смещение анимации");
        languageBase.put("Module.Modules.BetterChat.Options.DoubleOption.AnimationOffset.description", "Смещение анимации");
        languageBase.put("Module.Modules.BetterChat.Options.EnumOption.AnimationQuadType.name", "Тип анимации");
        languageBase.put("Module.Modules.BetterChat.Options.EnumOption.AnimationQuadType.description", "Стиль вычисления анимации");

        languageBase.put("Module.Modules.SafeMode.name", "Безопасный режим");
        languageBase.put("Module.Modules.SafeMode.description", "Максимально избегает обнаружения анти-читом");
        languageBase.put("Module.Modules.SafeMode.Options.EnumOption.AntiCheatMode.name", "Режим анти-чита");
        languageBase.put("Module.Modules.SafeMode.Options.EnumOption.AntiCheatMode.description", "Определяет, какие функции блокируются режимом анти-чита");
        languageBase.put("Module.Modules.SafeMode.Options.BooleanOption.AllowTimer.name", "Разрешить таймер");
        languageBase.put("Module.Modules.SafeMode.Options.BooleanOption.AllowTimer.description", "Если включено, таймер может быть использован. Это может привести к обнаружению анти-читом");

        languageBase.put("Module.Modules.KillAura.name", "Kill Aura");
        languageBase.put("Module.Modules.KillAura.description", "Автоматически атакует объекты в радиусе");
        languageBase.put("Module.Modules.KillAura.Options.EnumOption.AntiCheat.name", "Анти-чит");
        languageBase.put("Module.Modules.KillAura.Options.EnumOption.AntiCheat.description", "Определяет, какую стратегию анти-чита обходить");
        languageBase.put("Module.Modules.KillAura.Options.EnumOption.ClickType.name", "Тип клика");
        languageBase.put("Module.Modules.KillAura.Options.EnumOption.ClickType.description", "Выберите режим автоматической атаки");
        languageBase.put("Module.Modules.KillAura.Options.DoubleOption.Range.name", "Радиус");
        languageBase.put("Module.Modules.KillAura.Options.DoubleOption.Range.description", "Максимальная дистанция атаки");
        languageBase.put("Module.Modules.KillAura.Options.DoubleOption.FOV.name", "Поле зрения");
        languageBase.put("Module.Modules.KillAura.Options.DoubleOption.FOV.description", "Угол поля зрения атаки");
        languageBase.put("Module.Modules.KillAura.Options.DoubleOption.MinCPS.name", "Мин CPS");
        languageBase.put("Module.Modules.KillAura.Options.DoubleOption.MinCPS.description", "Минимальное количество кликов в секунду");
        languageBase.put("Module.Modules.KillAura.Options.DoubleOption.MaxCPS.name", "Макс CPS");
        languageBase.put("Module.Modules.KillAura.Options.DoubleOption.MaxCPS.description", "Максимальное количество кликов в секунду");
        languageBase.put("Module.Modules.KillAura.Options.DoubleOption.Delay.name", "Задержка клика");
        languageBase.put("Module.Modules.KillAura.Options.DoubleOption.Delay.description", "Задержка между кликами");
        languageBase.put("Module.Modules.KillAura.Options.EnumOption.RotateType.name", "Режим вращения");
        languageBase.put("Module.Modules.KillAura.Options.EnumOption.RotateType.description", "Режим вращения при атаке");

        languageBase.put("Module.Modules.Scaffold.name", "Scaffold");
        languageBase.put("Module.Modules.Scaffold.description", "Автоматически размещает блоки под ногами при движении");
        languageBase.put("Module.Modules.Scaffold.Options.EnumOption.AntiCheat.name", "Анти-чит");
        languageBase.put("Module.Modules.Scaffold.Options.EnumOption.AntiCheat.description", "Определяет, какие функции блокируются режимом анти-чита");

        languageBase.put("Module.Modules.Reach.name", "Досягаемость");
        languageBase.put("Module.Modules.Reach.description", "Изменяет дальность взаимодействия и атаки");
        languageBase.put("Module.Modules.Reach.Options.DoubleOption.Range.name", "Дальность");
        languageBase.put("Module.Modules.Reach.Options.DoubleOption.Range.description", "Дальность взаимодействия и атаки");

        languageBase.put("Module.Modules.FakePlayer.name", "Фальшивый игрок");
        languageBase.put("Module.Modules.FakePlayer.description", "Создаёт фальшивого игрока");
        languageBase.put("Module.Modules.FakePlayer.Options.TextOption.PlayerName.name", "Имя игрока");
        languageBase.put("Module.Modules.FakePlayer.Options.TextOption.PlayerName.description", "Имя отображаемого фальшивого игрока");
        languageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.GoldenApple.name", "Золотое яблоко");
        languageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.GoldenApple.description", "Включить эффекты золотого яблока на фальшивого игрока");
        languageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.AutoTotem.name", "Авто тотем");
        languageBase.put("Module.Modules.FakePlayer.Options.BooleanOption.AutoTotem.description", "Автоматически использовать тотем при смерти фальшивого игрока");

        languageBase.put("Module.Modules.SafeWalk.name", "Безопасная ходьба");
        languageBase.put("Module.Modules.SafeWalk.description", "Позволяет безопасно ходить по краю блоков");
        languageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoInject.name", "Инъекция ввода");
        languageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoInject.description", "Использовать небезопасный метод, чтобы не упасть");
        languageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoShift.name", "Авто-приседание");
        languageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.DoShift.description", "Автоматически приседать у края");
        languageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.RandomThreshold.name", "Случайный порог");
        languageBase.put("Module.Modules.SafeWalk.Options.BooleanOption.RandomThreshold.description", "Случайное значение порога приседания у края");
        languageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.Threshold.name", "Порог края");
        languageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.Threshold.description", "Фиксированное расстояние для активации приседания");
        languageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MaxThreshold.name", "Макс порог");
        languageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MaxThreshold.description", "Максимальный случайный порог края");
        languageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MinThreshold.name", "Мин порог");
        languageBase.put("Module.Modules.SafeWalk.Options.DoubleOption.MinThreshold.description", "Минимальный случайный порог края");

        languageBase.put("Module.Modules.FOVModifier.name", "Изменитель FOV");
        languageBase.put("Module.Modules.FOVModifier.description", "Изменяет поле зрения игрока");
        languageBase.put("Module.Modules.FOVModifier.Options.DoubleOption.FOV.name", "FOV");
        languageBase.put("Module.Modules.FOVModifier.Options.DoubleOption.FOV.description", "Поле зрения");
        languageBase.put("Module.Modules.FOVModifier.Options.BooleanOption.Items.name", "Предметы");
        languageBase.put("Module.Modules.FOVModifier.Options.BooleanOption.Items.description", "Применить эффект к предметам");

        languageBase.put("Module.Modules.Disabler.name", "Отключатель");
        languageBase.put("Module.Modules.Disabler.description", "Попытка отключить определённые поведения анти-чита");

        languageBase.put("Module.Modules.InventoryMove.name", "Движение в инвентаре");
        languageBase.put("Module.Modules.InventoryMove.description", "Позволяет двигаться при открытии определённых GUI");
        languageBase.put("Module.Modules.InventoryMove.Options.BooleanOption.HorizontalCollision.name", "Горизонтальное столкновение");
        languageBase.put("Module.Modules.InventoryMove.Options.BooleanOption.HorizontalCollision.description", "Включить горизонтальное столкновение при отправке пакетов");

        languageBase.put("Module.Modules.AutoWalk.name", "Авто-ходьба");
        languageBase.put("Module.Modules.AutoWalk.description", "Автоматически двигаться вперёд");
        languageBase.put("Module.Modules.AutoWalk.Options.EnumOption.Mode.name", "Режим");
        languageBase.put("Module.Modules.AutoWalk.Options.EnumOption.Mode.description", "Поддержка Baritone");

        languageBase.put("Module.Modules.NoCooldown.name", "Без кулдауна");
        languageBase.put("Module.Modules.NoCooldown.description", "Уменьшает время кулдауна между действиями");
        languageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Attack.name", "Атака");
        languageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Attack.description", "Отключить кулдаун атаки");
        languageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Jump.name", "Прыжок");
        languageBase.put("Module.Modules.NoCooldown.Options.BooleanOption.Jump.description", "Отключить кулдаун прыжка");
        languageBase.put("Module.Modules.NoCooldown.Options.DoubleOption.JumpTicks.name", "Тики прыжка");
        languageBase.put("Module.Modules.NoCooldown.Options.DoubleOption.JumpTicks.description", "Количество тиков ожидания перед следующим прыжком");
    }
}
