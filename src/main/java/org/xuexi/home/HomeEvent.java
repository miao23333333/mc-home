package org.xuexi.home;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.*;

import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;

public class HomeEvent {
    private boolean setHomeStart = true;
    public Map<String, Integer> pos1;
    public Map<String, Integer> pos2;
    public String PlayerDisplayName;

    public HomeEvent(String playerDisplayName) {
        PlayerDisplayName = playerDisplayName;
    }

    // 提取重复代码为私有方法
    private boolean isSetHoe(ItemStack handItem) {
        return setHomeStart && handItem.has(Home.SET_HOE.get());
    }

    @SubscribeEvent
    private void onLeftClickBlock(LeftClickBlock event) {
        ItemStack handItem = event.getEntity().getItemInHand(InteractionHand.MAIN_HAND);

        if (isSetHoe(handItem)) {
            pos1 = Map.of(
                    "x", event.getPos().getX(),
                    "y", event.getPos().getY()
            );
        }
    }

    @SubscribeEvent
    private void onRightClickBlock(RightClickBlock event) {
        ItemStack handItem = event.getEntity().getItemInHand(InteractionHand.MAIN_HAND);
        if (isSetHoe(handItem)) {
            pos2 = Map.of(
                    "x", event.getPos().getX(),
                    "y", event.getPos().getY()
            );
            event.setCanceled(true);
            event.getEntity().sendSystemMessage(Component.empty().append(Component.literal("已选取第二点为(" + pos2.get("x") + "," + pos2.get("y") + ")").withStyle(ChatFormatting.GREEN)));
        }
    }

    @SubscribeEvent
    private void onPickupItem(ItemEntityPickupEvent event) {
        ItemEntity pickupItem = event.getItemEntity();
        boolean isSetHoe = pickupItem.getItem().has(Home.SET_HOE.get());
        String playerName = event.getPlayer().getDisplayName().getString();
    }
}
