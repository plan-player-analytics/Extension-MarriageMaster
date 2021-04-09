package com.djrapitops.extension;

import at.pcgamingfreaks.MarriageMaster.API.MarriageMasterPlugin;
import com.djrapitops.plan.extension.NotReadyException;
import com.djrapitops.plan.extension.annotation.PluginInfo;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@PluginInfo(name = "MarriageMaster", iconName = "ring", iconFamily = Family.SOLID, color = Color.AMBER)
public class BungeeMarriageMasterExtension extends MarriageMasterExtension {

    public BungeeMarriageMasterExtension() {
    }

    public BungeeMarriageMasterExtension(boolean forTest) {
        super(forTest);
    }

    @Override
    public MarriageMasterPlugin getMarriageMaster() {
        try {
            Plugin bungeePlugin = ProxyServer.getInstance().getPluginManager().getPlugin("MarriageMaster");
            if (!(bungeePlugin instanceof MarriageMasterPlugin)) {
                // MarriageMaster is not available
                throw new NotReadyException();
            }
            return (MarriageMasterPlugin) bungeePlugin;
        } catch (Exception e) {
            throw new NotReadyException();
        }
    }

}
