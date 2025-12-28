package org.xuexi.home;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SetHoe(boolean set_hoe) {
    public static final Codec<SetHoe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("set_hoe").forGetter(SetHoe::set_hoe)
        ).apply(instance, SetHoe::new)
    );

    public static final StreamCodec<ByteBuf, SetHoe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SetHoe::set_hoe,
            SetHoe::new
    );
}