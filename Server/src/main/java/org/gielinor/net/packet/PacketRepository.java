package org.gielinor.net.packet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.gielinor.net.packet.out.AnimateInterface;
import org.gielinor.net.packet.out.AnimateObjectPacket;
import org.gielinor.net.packet.out.AudioPacket;
import org.gielinor.net.packet.out.BuildDynamicScene;
import org.gielinor.net.packet.out.CameraViewPacket;
import org.gielinor.net.packet.out.ChatboxInterface;
import org.gielinor.net.packet.out.ClearGroundItem;
import org.gielinor.net.packet.out.ClearMinimapFlag;
import org.gielinor.net.packet.out.ClearObject;
import org.gielinor.net.packet.out.ClearRegionChunk;
import org.gielinor.net.packet.out.CloseChatboxInterface;
import org.gielinor.net.packet.out.CloseInterface;
import org.gielinor.net.packet.out.CommunicationMessage;
import org.gielinor.net.packet.out.Config;
import org.gielinor.net.packet.out.ConstructGroundItem;
import org.gielinor.net.packet.out.ConstructObject;
import org.gielinor.net.packet.out.ContactPackets;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.net.packet.out.DisplayModel;
import org.gielinor.net.packet.out.GameMessage;
import org.gielinor.net.packet.out.GrandExchangeOfferPacket;
import org.gielinor.net.packet.out.HideComponent;
import org.gielinor.net.packet.out.HintIcon;
import org.gielinor.net.packet.out.InputDialogue;
import org.gielinor.net.packet.out.InputStatePacket;
import org.gielinor.net.packet.out.InstancedLocationUpdate;
import org.gielinor.net.packet.out.InteractionOption;
import org.gielinor.net.packet.out.Interface;
import org.gielinor.net.packet.out.InterfaceColour;
import org.gielinor.net.packet.out.InterfaceConfig;
import org.gielinor.net.packet.out.InterfaceMaxScrollPacket;
import org.gielinor.net.packet.out.InterfaceScrollPosition;
import org.gielinor.net.packet.out.InventoryInterface;
import org.gielinor.net.packet.out.Logout;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.net.packet.out.MultiCombatPacket;
import org.gielinor.net.packet.out.MusicPacket;
import org.gielinor.net.packet.out.PingPacket;
import org.gielinor.net.packet.out.PlayerDetail;
import org.gielinor.net.packet.out.PlayerObjectTransformPacket;
import org.gielinor.net.packet.out.PositionedGraphic;
import org.gielinor.net.packet.out.RepositionChild;
import org.gielinor.net.packet.out.RunEnergy;
import org.gielinor.net.packet.out.RunScriptPacket;
import org.gielinor.net.packet.out.SetWalkOption;
import org.gielinor.net.packet.out.SidebarInterface;
import org.gielinor.net.packet.out.SkillLevel;
import org.gielinor.net.packet.out.StaticSkillLevel;
import org.gielinor.net.packet.out.StringPacket;
import org.gielinor.net.packet.out.StringPacket498;
import org.gielinor.net.packet.out.SummoningInformationPacket;
import org.gielinor.net.packet.out.SummoningOptions;
import org.gielinor.net.packet.out.SystemUpdatePacket;
import org.gielinor.net.packet.out.TooltipPacket;
import org.gielinor.net.packet.out.URLMessage;
import org.gielinor.net.packet.out.URLPacket;
import org.gielinor.net.packet.out.UpdateAreaPosition;
import org.gielinor.net.packet.out.UpdateClanChat;
import org.gielinor.net.packet.out.UpdateGroundItemAmount;
import org.gielinor.net.packet.out.UpdateRandomFile;
import org.gielinor.net.packet.out.UpdateSceneGraph;
import org.gielinor.net.packet.out.WeightUpdate;
import org.gielinor.net.packet.out.XPDrop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The packet repository.
 *
 * @author Emperor
 */
public final class PacketRepository {

    private static final Logger log = LoggerFactory.getLogger(PacketRepository.class);

    /**
     * The outgoing packets mapping.
     */
    private final static Map<Class<?>, OutgoingPacket<? extends Context>> OUTGOING_PACKETS = new HashMap<>();

    /**
     * The incoming packets mapping.
     */
    private final static Map<Integer, IncomingPacket> INCOMING_PACKETS = new HashMap<>();

    /**
     * Populate the mappings.
     */
    static {
        OUTGOING_PACKETS.put(UpdateSceneGraph.class, new UpdateSceneGraph());
        OUTGOING_PACKETS.put(Interface.class, new Interface());
        OUTGOING_PACKETS.put(SidebarInterface.class, new SidebarInterface());
        OUTGOING_PACKETS.put(ChatboxInterface.class, new ChatboxInterface());
        OUTGOING_PACKETS.put(InterfaceColour.class, new InterfaceColour());
        OUTGOING_PACKETS.put(InventoryInterface.class, new InventoryInterface());
        OUTGOING_PACKETS.put(InputDialogue.class, new InputDialogue());
        OUTGOING_PACKETS.put(SkillLevel.class, new SkillLevel());
        OUTGOING_PACKETS.put(StaticSkillLevel.class, new StaticSkillLevel());
        OUTGOING_PACKETS.put(XPDrop.class, new XPDrop());
        OUTGOING_PACKETS.put(Config.class, new Config());
        OUTGOING_PACKETS.put(GameMessage.class, new GameMessage());
        OUTGOING_PACKETS.put(RunScriptPacket.class, new RunScriptPacket());
        OUTGOING_PACKETS.put(RunEnergy.class, new RunEnergy());
        OUTGOING_PACKETS.put(ContainerPacket.class, new ContainerPacket());
        OUTGOING_PACKETS.put(StringPacket498.class, new StringPacket498());
        OUTGOING_PACKETS.put(StringPacket.class, new StringPacket());
        OUTGOING_PACKETS.put(Logout.class, new Logout());
        OUTGOING_PACKETS.put(CloseInterface.class, new CloseInterface());
        OUTGOING_PACKETS.put(CloseChatboxInterface.class, new CloseChatboxInterface());
        OUTGOING_PACKETS.put(AnimateInterface.class, new AnimateInterface());
        OUTGOING_PACKETS.put(DisplayModel.class, new DisplayModel());
        OUTGOING_PACKETS.put(HideComponent.class, new HideComponent());
        OUTGOING_PACKETS.put(PingPacket.class, new PingPacket());
        OUTGOING_PACKETS.put(UpdateAreaPosition.class, new UpdateAreaPosition());
        OUTGOING_PACKETS.put(ConstructObject.class, new ConstructObject());
        OUTGOING_PACKETS.put(ClearObject.class, new ClearObject());
        OUTGOING_PACKETS.put(HintIcon.class, new HintIcon());
        OUTGOING_PACKETS.put(ClearMinimapFlag.class, new ClearMinimapFlag());
        OUTGOING_PACKETS.put(InteractionOption.class, new InteractionOption());
        OUTGOING_PACKETS.put(SetWalkOption.class, new SetWalkOption());
        OUTGOING_PACKETS.put(MinimapState.class, new MinimapState());
        OUTGOING_PACKETS.put(ConstructGroundItem.class, new ConstructGroundItem());
        OUTGOING_PACKETS.put(ClearGroundItem.class, new ClearGroundItem());
        OUTGOING_PACKETS.put(RepositionChild.class, new RepositionChild());
        OUTGOING_PACKETS.put(PositionedGraphic.class, new PositionedGraphic());
        OUTGOING_PACKETS.put(SystemUpdatePacket.class, new SystemUpdatePacket());
        OUTGOING_PACKETS.put(CameraViewPacket.class, new CameraViewPacket());
        OUTGOING_PACKETS.put(MusicPacket.class, new MusicPacket());
        OUTGOING_PACKETS.put(AudioPacket.class, new AudioPacket());
        OUTGOING_PACKETS.put(BuildDynamicScene.class, new BuildDynamicScene());
        OUTGOING_PACKETS.put(AnimateObjectPacket.class, new AnimateObjectPacket());
        OUTGOING_PACKETS.put(ClearRegionChunk.class, new ClearRegionChunk());
        OUTGOING_PACKETS.put(ContactPackets.class, new ContactPackets());
        OUTGOING_PACKETS.put(CommunicationMessage.class, new CommunicationMessage());
        OUTGOING_PACKETS.put(UpdateClanChat.class, new UpdateClanChat());
        OUTGOING_PACKETS.put(UpdateGroundItemAmount.class, new UpdateGroundItemAmount());
        OUTGOING_PACKETS.put(WeightUpdate.class, new WeightUpdate());
        OUTGOING_PACKETS.put(UpdateRandomFile.class, new UpdateRandomFile());
        OUTGOING_PACKETS.put(InstancedLocationUpdate.class, new InstancedLocationUpdate());
        OUTGOING_PACKETS.put(URLPacket.class, new URLPacket());
        OUTGOING_PACKETS.put(PlayerDetail.class, new PlayerDetail());
        OUTGOING_PACKETS.put(InterfaceMaxScrollPacket.class, new InterfaceMaxScrollPacket());
        OUTGOING_PACKETS.put(InterfaceConfig.class, new InterfaceConfig());
        OUTGOING_PACKETS.put(InterfaceScrollPosition.class, new InterfaceScrollPosition());
        OUTGOING_PACKETS.put(URLMessage.class, new URLMessage());
        OUTGOING_PACKETS.put(PlayerObjectTransformPacket.class, new PlayerObjectTransformPacket());
        OUTGOING_PACKETS.put(MultiCombatPacket.class, new MultiCombatPacket());
        OUTGOING_PACKETS.put(GrandExchangeOfferPacket.class, new GrandExchangeOfferPacket());
        OUTGOING_PACKETS.put(SummoningOptions.class, new SummoningOptions());
        OUTGOING_PACKETS.put(InputStatePacket.class, new InputStatePacket());
        OUTGOING_PACKETS.put(TooltipPacket.class, new TooltipPacket());
        OUTGOING_PACKETS.put(SummoningInformationPacket.class, new SummoningInformationPacket());
    }

    private static final Set<String> SEND_PACKETS = new HashSet<>(Arrays.asList("CameraViewPacket", "SummoningOptions",
        "GrandExchangeOfferPacket", "MultiCombatPacket", "PositionedGraphic", "URLMessage", "MusicPacket",
        "UpdateSceneGraph", "GameMessage", "ConstructObject", "UpdateAreaPosition", "PlayerDetail", "ClearObject",
        "ConstructGroundItem", "Config", "SkillLevel", "StaticSkillLevel", "XPDrop", "RunEnergy",
        "InteractionOption", "ClearMinimapFlag", "ClearRegionChunk", "Logout", "ContainerPacket", "ContactPackets",
        "Interface", "StringPacket", "SidebarInterface", "CloseInterface", "ChatboxInterface", "DisplayModel",
        "AnimateInterface", "CloseChatboxInterface", "InterfaceColour", "InputDialogue", "ClearGroundItem",
        "WeightUpdate", "InterfaceConfig", "HintIcon", "InventoryInterface", "URLPacket", "CommunicationMessage",
        "MinimapState", "InterfaceMaxScrollPacket", "HideComponent", "UpdateClanChat", "AudioPacket", "MusicPacket",
        "SystemUpdatePacket", "InterfaceScrollPosition", "UpdateGroundItemAmount", "PlayerObjectTransformPacket",
        "BuildDynamicScene", "AnimateObjectPacket", "InputStatePacket", "TooltipPacket", "RepositionChild",
        "SummoningInformationPacket"));

    /**
     * Sends a new packet.
     *
     * @param clazz
     *            The class of the outgoing packet to send.
     * @param context
     *            The context.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void send(Class<? extends OutgoingPacket> clazz, Context context) {
        if (!SEND_PACKETS.contains(clazz.getSimpleName())) {
            log.info("Ignoring packet: [{}] for [{}].", clazz.getSimpleName(), context.getPlayer().getName());
            return;
        }

        OutgoingPacket p = OUTGOING_PACKETS.get(clazz);

        if (p == null) {
            log.warn("Missing outgoing packet handler [{}] for [{}].", clazz.getSimpleName(), context.getPlayer().getName());
            return;
        }

        if (context.getPlayer() != null && context.getPlayer().getSession() != null) {
            p.send(context);
        }
    }

    /**
     * Binds an opcode to an {@link org.gielinor.net.packet.IncomingPacket}.
     *
     * @param id
     *            The opcode.
     * @param incomingPacket
     *            The {@link org.gielinor.net.packet.IncomingPacket}.
     */
    public static void bind(int id, IncomingPacket incomingPacket) {
        IncomingPacket current = INCOMING_PACKETS.get(id);
        if (current != null) {
            throw new IllegalStateException(id + " - packet is already bound to: " + current.getClass().getName());
        }
        INCOMING_PACKETS.put(id, incomingPacket);
    }

    /**
     * Gets an incoming packet.
     *
     * @param opcode
     *            The opcode.
     * @return The incoming packet.
     */
    public static IncomingPacket getIncoming(int opcode) {
        return INCOMING_PACKETS.get(opcode);
    }

}
