package jakojaannos.hcparty;

import jakojaannos.hcparty.command.CommandParty;
import jakojaannos.hcparty.party.PartyManager;
import jakojaannos.lib.init.BiomesBase;
import jakojaannos.lib.init.BlocksBase;
import jakojaannos.lib.init.ItemsBase;
import jakojaannos.lib.mod.ModMainBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDENCIES)
public class HardcoreParty extends ModMainBase<HardcoreParty, BlocksBase, ItemsBase, BiomesBase> {

    @SidedProxy(clientSide = "jakojaannos.hcparty.client.ClientProxy", serverSide = "jakojaannos.hcparty.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static HardcoreParty instance;


    public final PartyManager partyManager;

    public HardcoreParty() {
        partyManager = new PartyManager();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandParty(partyManager));
    }

    @Mod.EventHandler
    @Override
    public void onInit(FMLPreInitializationEvent event) {
        super.onInit(event);
    }

    @Mod.EventHandler
    @Override
    public void onInit(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    @Override
    public void onInit(FMLPostInitializationEvent event) {

    }
}
