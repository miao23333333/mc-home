package org.xuexi.home;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;


// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = Home.MODID)
public class HomeData {

    //public static ModConfigSpec SPEC = BUILDER.build();
    public static final HomeData CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;
    public final ModConfigSpec.ConfigValue<List> data;
    private HomeData(ModConfigSpec.Builder builder) {
        builder.comment("Home 数据")
                .push("data");
        data = builder.comment("数据")
                        .define("data", List.of());


        builder.pop();
    }
    static {
        Pair<HomeData, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(HomeData::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }




    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {

    }

}
