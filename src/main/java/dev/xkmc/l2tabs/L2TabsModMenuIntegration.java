package dev.xkmc.l2tabs;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.xkmc.l2tabs.data.L2TabsConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class L2TabsModMenuIntegration implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> AutoConfig.getConfigScreen(L2TabsConfig.Global.class, parent).get();
    }
}
