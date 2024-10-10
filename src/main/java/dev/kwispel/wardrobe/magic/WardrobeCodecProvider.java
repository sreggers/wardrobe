package dev.kwispel.wardrobe.magic;

import dev.kwispel.wardrobe.domain.Color;
import dev.kwispel.wardrobe.domain.Tag;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class WardrobeCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (aClass == Tag.class) {
            return (Codec<T>) new Tag.TagCodec();
        } else if (aClass == Color.class) {
            return (Codec<T>) new Color.ColorCodec();
        } else {
            return null;
        }
    }
}
