package org.xuexi.home;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.*;

import java.util.Map;
import java.util.Objects;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class HomeEvent {
    private Boolean setHomeStart = true;
    public Map<String, Integer> pos1;
    public Map<String, Integer> pos2;
    public String PlayerDisplayName;
    private static final Logger LOGGER = LogUtils.getLogger();

    public HomeEvent(String playerDisplayName) {
        PlayerDisplayName = playerDisplayName;
        NeoForge.EVENT_BUS.register(this);
    }

    public void clearHomeEvent() {
        NeoForge.EVENT_BUS.unregister(this);
    }

    // 提取重复代码为私有方法
    private boolean isSetHoe(ItemStack handItem) {
        return setHomeStart && handItem.has(Home.SET_HOE.get());
    }

    @SubscribeEvent
    private void onLeftClickBlock(LeftClickBlock event) {
        if (pos1 == null | !Map.of("x", event.getPos().getX(), "y", event.getPos().getY()).equals(pos1)) {
            ItemStack handItem = event.getItemStack();
            LOGGER.info(event.getEntity().getDisplayName().getString());
            if (isSetHoe(handItem) && Objects.equals(event.getEntity().getDisplayName().getString(), PlayerDisplayName)) {
                pos1 = Map.of(
                        "x", event.getPos().getX(),
                        "y", event.getPos().getY()
                );
                event.setCanceled(true);
                event.getEntity().sendSystemMessage(Component.empty().append(Component.literal("已选取第一点为(" + pos1.get("x") + "," + pos1.get("y") + ")").withStyle(ChatFormatting.GREEN)));
            }
        } else if (isSetHoe(event.getItemStack()) && Objects.equals(event.getEntity().getDisplayName().getString(), PlayerDisplayName)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    private void onRightClickBlock(RightClickBlock event) {
        if (pos2 == null | !Map.of("x", event.getPos().getX(), "y", event.getPos().getY()).equals(pos2)) {
            ItemStack handItem = event.getItemStack();
            LOGGER.info(event.getEntity().getDisplayName().getString());
            if (isSetHoe(handItem) && event.getEntity().getDisplayName().getString().equals(PlayerDisplayName)) {
                pos2 = Map.of(
                        "x", event.getPos().getX(),
                        "y", event.getPos().getY()
                );
                event.setCanceled(true);
                event.getEntity().sendSystemMessage(Component.empty().append(Component.literal("已选取第二点为(" + pos2.get("x") + "," + pos2.get("y") + ")").withStyle(ChatFormatting.GREEN)));
            }
        } else if (isSetHoe(event.getItemStack()) && event.getEntity().getDisplayName().getString().equals(PlayerDisplayName)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    private void onDropItem(ItemTossEvent event) {
        ItemEntity dropItem = event.getEntity();
        boolean isSetHoe = dropItem.getItem().has(Home.SET_HOE.get());
        String playerName = event.getPlayer().getDisplayName().getString();
        LOGGER.info(String.valueOf(isSetHoe));
        if (isSetHoe && playerName.equals(PlayerDisplayName)) {
            if (pos1 != null && pos2 != null) {
                event.getEntity().kill();
                setHomeStart = false;
                event.getPlayer().sendSystemMessage(Component.empty().append(Component.literal("完成").withStyle(ChatFormatting.GREEN)));
                clearHomeEvent();
            } else {
                event.getEntity().kill();
                setHomeStart = false;
                event.getPlayer().sendSystemMessage(Component.empty().append(Component.literal("请完整选择两点").withStyle(ChatFormatting.RED)));
                clearHomeEvent();
            }
        }
    }
}
