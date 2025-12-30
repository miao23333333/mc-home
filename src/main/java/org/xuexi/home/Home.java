package org.xuexi.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.function.Supplier;


// 这里的值应与META-INF/neoforge.mods.toml文件中的条目匹配
@Mod(Home.MODID)
public class Home {
    // 在一个公共位置定义mod id供所有内容引用
    public static final String MODID = "home";
    // 直接引用slf4j日志记录器
    private static final Logger LOGGER = LogUtils.getLogger();
    // 创建一个延迟注册器来保存方块，所有方块都将在"home"命名空间下注册
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // 创建一个延迟注册器来保存物品，所有物品都将在"home"命名空间下注册
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // 创建一个延迟注册器来保存创造模式标签页，所有标签页都将在"home"命名空间下注册
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    // 创建一个延迟注册器来保存DataComponentMap，所有DataComponentMap都将在"home"命名空间下注册
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    // 创建一个ID为"home:example_block"的新方块，结合命名空间和路径
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // 创建一个ID为"home:example_block"的新方块物品，结合命名空间和路径
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);






    public static final Supplier<DataComponentType<SetHoe>> SET_HOE = DATA_COMPONENTS.registerComponentType(
            "set_hoe",
            builder -> builder
                    .persistent(SetHoe.CODEC)
                    .networkSynchronized(SetHoe.STREAM_CODEC)
            );



    // mod类的构造函数是mod加载时运行的第一段代码。
    // FML会自动识别一些参数类型，如IEventBus或ModContainer，并自动传入它们。
    public Home(IEventBus modEventBus, ModContainer container) {
        // 为mod加载注册commonSetup方法
        modEventBus.addListener(this::commonSetup);

        // 将方块延迟注册器注册到mod事件总线，以便注册方块
        BLOCKS.register(modEventBus);
        // 将物品延迟注册器注册到mod事件总线，以便注册物品
        ITEMS.register(modEventBus);
        // 将创造模式标签页延迟注册器注册到mod事件总线，以便注册标签页
        CREATIVE_MODE_TABS.register(modEventBus);
        // 将DataComponentMap延迟注册器注册到mod事件总线，以便注册DataComponentMap
        DATA_COMPONENTS.register(modEventBus);


        // 为我们感兴趣的服务端和其他游戏事件注册我们自己。
        // 注意，只有当且仅当我们希望*这个*类(Home)直接响应事件时才需要这样做。
        // 如果这个类中没有@SubscribeEvent注解的函数，如下面的onServerStarting()，就不要添加这行。
        NeoForge.EVENT_BUS.register(this);

        // 将物品注册到创造模式标签页
        modEventBus.addListener(this::addCreative);

        // 注册我们mod的ModConfigSpec，以便FML可以为我们创建和加载配置文件
        container.registerConfig(ModConfig.Type.SERVER, HomeConfig.CONFIG_SPEC);

        // 注册我们mod的ModConfigSpec，以便FML可以为我们创建和加载配置文件
        container.registerConfig(ModConfig.Type.SERVER, HomeData.CONFIG_SPEC, "HomeData.toml");



    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // 一些通用的设置代码
        LOGGER.info("来自通用设置的问候");


    }


    // 将示例方块物品添加到建筑方块标签页
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(EXAMPLE_BLOCK_ITEM);
    }

    // 你可以使用SubscribeEvent并让事件总线发现要调用的方法
    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal("sethome")
                        .requires((commandSourceStack) -> commandSourceStack.hasPermission(0))
                        .executes(SethomeCommand.INSTANCE)
        );

    }

}
