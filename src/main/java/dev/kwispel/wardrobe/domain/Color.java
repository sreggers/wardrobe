package dev.kwispel.wardrobe.domain;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public record Color(String name) {
    public static class ColorCodec implements Codec<Color> {
        @Override
        public Color decode(BsonReader reader, DecoderContext decoderContext) {
            return new Color(reader.readString());
        }

        @Override
        public void encode(BsonWriter writer, Color value, EncoderContext encoderContext) {
            writer.writeString(value.name());
        }

        @Override
        public Class<Color> getEncoderClass() {
            return Color.class;
        }
    }
}
