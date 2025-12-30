package org.xuexi.home;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.xuexi.home.SetHoe;

import java.util.List;
import java.util.function.Supplier;


public class SethomeCommand implements Command<CommandSourceStack> {
    public static final Command<CommandSourceStack> INSTANCE = new SethomeCommand();
    @Override
    public int run(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        CommandSourceStack source = commandContext.getSource();
        if (commandContext.getSource().isPlayer()) {
            ItemStack stack = new ItemStack(Items.WOODEN_HOE);
            //DataComponentType<SetHoe> type = ;
            stack.set(DataComponents.ITEM_NAME, Component.literal("一个不普通的木锄，它可以用来圈地").withStyle(ChatFormatting.GREEN));
            stack.set(DataComponents.LORE, new ItemLore(List.of(
                    Component.literal("左键选择第一点，右键选择第二点"),
                    Component.literal("丢弃以确认")
            )));
            stack.set(Home.SET_HOE.get(), new SetHoe(true));

            if (source.getPlayer().addItem(stack)) {
                commandContext.getSource().sendSuccess(() -> Component.literal("使用木锄进行选区，左键选择第一点，右键选择第二点，仅计算x和y"), true);
            } else {
                commandContext.getSource().sendSuccess(() -> Component.literal("你的背包似乎快爆了..."), true);
                return 0;
            }
            HomeEvent homeEvent = new HomeEvent(source.getPlayer().getDisplayName().getString());
        }
        return 0;
    }

}
