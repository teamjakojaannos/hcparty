package jakojaannos.life;

import jakojaannos.life.init.ModCommands;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDENCIES)
public class LIFe {

    @SidedProxy(clientSide = "jakojaannos.life.client.ClientProxy", serverSide = "jakojaannos.life.CommonProxy")
    public static CommonProxy proxy;

    @Instance(ModInfo.MODID)
    public static LIFe instance;

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        ModCommands.initCommands(event);
    }

    @EventHandler
    public void onInit(FMLPreInitializationEvent event) {

    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void onInit(FMLPostInitializationEvent event) {

    }
}
