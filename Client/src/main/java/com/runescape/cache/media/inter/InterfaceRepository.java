package com.runescape.cache.media.inter;

import com.runescape.cache.media.inter.impl.*;

import java.util.ArrayList;

/**
 * An {@link com.runescape.cache.media.RSComponent} {@link com.runescape.cache.media.inter.InterfacePlugin} repository.
 *
 * @author Gielinor
 */
public class InterfaceRepository extends ArrayList<InterfacePlugin> {

    /**
     * The default constructor.
     */
    public InterfaceRepository() {
        add(new TreasureInterfacePlugin());
        add(new MagicBookInterfacePlugin());
        add(new LunarInterfacePlugin());
        add(new QuestDiaryInterfacePlugin());
        add(new AchievementDiaryInterfacePlugin());
        add(new EquipmentTabInterfacePlugin());
        add(new OptionInterfacePlugin());
        add(new BankInterfacePlugin());
        add(new WrenchTabInterfacePlugin());
        add(new QuestMenuInterfacePlugin());
        add(new WildernessWarningInterfacePlugin());
        add(new EnchantCrossbowBoltInterfacePlugin());
        add(new SkillMenuInterfacePlugin());
        add(new SignPostInterfacePlugin());
        add(new DoubleItemMessageInterfacePlugin());
        add(new ResourcesInterfacePlugin());
        add(new ClanChatTabInterfacePlugin());
        add(new ClanChatSetupInterfacePlugin());
        add(new DuelArenaInterfacePlugin());
        add(new AgilityArenaTicketInterfacePlugin());
        add(new SkillTabInterfacePlugin());
        add(new StatSpyInterfacePlugin());
        add(new JewelleryCraftInterfacePlugin());
        add(new PestControlDepartureInterfacePlugin());
        add(new PestControlGameInterfacePlugin());
        add(new CommandsInterface());
        add(new AttackDummyInterfacePlugin());
        add(new MinigameInterfacePlugin());
        add(new GraphicSettingsInterfacePlugin());
        add(new ItemsOnDeathInterfacePlugin());
        add(new AdvancedOptionsInterfacePlugin());
        add(new SpinWheelInterfacePlugin());
        add(new BarbarianAssaultInterfacePlugin());
        add(new DarkInterfacePlugin());
        add(new VoidKnightRewardsInterfacePlugin());
        add(new ClimbDownWarningInterfacePlugin());
        add(new QuickPrayersInterfacePlugin());
        add(new InformationTabInterfacePlugin());
        add(new BossKillLogInterfacePlugin());
        add(new SlayerRewardInterface());
        add(new LoyaltyTitleInterfacePlugin());
        add(new TeleportInterfacePlugin());
        add(new GrandExchangeInterfacePlugin());
        add(new RunePouchInterfacePlugin());
        add(new GuidePricesInterfacePlugin());
        add(new BankSettingPlugin());
        add(new FamiliarInventoryPlugin());
        add(new PlayerStorePlugin());
        add(new ClanWarsSetupPlugin());
        add(new SummoningCreationPlugin());
        add(new SummoningTabPlugin());
        add(new SummoningLeftClickPlugin());
        add(new FurnitureBuildPlugin());
    }
}
