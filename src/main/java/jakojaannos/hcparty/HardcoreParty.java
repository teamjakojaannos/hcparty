package jakojaannos.hcparty;

import jakojaannos.api.mod.BiomesBase;
import jakojaannos.api.mod.BlocksBase;
import jakojaannos.api.mod.ItemsBase;
import jakojaannos.api.mod.ModMainBase;
import jakojaannos.hcparty.init.HCPartyCommands;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDENCIES)
public class HardcoreParty extends ModMainBase<BlocksBase, ItemsBase, BiomesBase, HCPartyCommands> {

    @SidedProxy(clientSide = "jakojaannos.hcparty.client.ClientProxy", serverSide = "jakojaannos.hcparty.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static HardcoreParty instance;

    @Mod.EventHandler
    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        super.onServerStarting(event);
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
