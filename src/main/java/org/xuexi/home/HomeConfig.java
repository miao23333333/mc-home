package org.xuexi.home;

import org.apache.commons.lang3.tuple.Pair;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
@EventBusSubscriber(modid = Home.MODID)
public class HomeConfig {
    public static final HomeConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.ConfigValue<Integer> maxLong;
    private HomeConfig(ModConfigSpec.Builder builder) {
        builder.comment("Home 配置")
                .push("settings");
        maxLong = builder.comment("最大边长")
                .define("maxLong", 500);
        builder.pop();
    }

    static {
        Pair<HomeConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(HomeConfig::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {

    }
}
