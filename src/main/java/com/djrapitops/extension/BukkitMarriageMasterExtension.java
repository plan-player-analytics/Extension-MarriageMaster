package com.djrapitops.extension;

import at.pcgamingfreaks.MarriageMaster.API.MarriageMasterPlugin;
import com.djrapitops.plan.extension.NotReadyException;
import com.djrapitops.plan.extension.annotation.PluginInfo;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@PluginInfo(name = "MarriageMaster", iconName = "ring", iconFamily = Family.SOLID, color = Color.AMBER)
public class BukkitMarriageMasterExtension extends MarriageMasterExtension {

    public BukkitMarriageMasterExtension() {
    }

    BukkitMarriageMasterExtension(boolean forTest) {
        super(forTest);
    }

    @Override
    public MarriageMasterPlugin getMarriageMaster() {
        try {
            Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin("MarriageMaster");
            if (!(bukkitPlugin instanceof MarriageMasterPlugin)) {
                // MarriageMaster is not available
                throw new NotReadyException();
            }
            return (MarriageMasterPlugin) bukkitPlugin;
        } catch (Exception e) {
            throw new NotReadyException();
        }
    }

}
