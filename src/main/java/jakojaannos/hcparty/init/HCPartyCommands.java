package jakojaannos.hcparty.init;

import jakojaannos.api.mod.CommandsBase;
import jakojaannos.hcparty.command.CommandParty;

public class HCPartyCommands extends CommandsBase {
    @Override
    protected void initCommands() {
        register(new CommandParty());
    }
}
