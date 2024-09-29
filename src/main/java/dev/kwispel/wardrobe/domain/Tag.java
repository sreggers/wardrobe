package dev.kwispel.wardrobe.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public record Tag(@JsonValue String name) {
    public static class TagCodec implements Codec<Tag> {
        @Override
        public Tag decode(BsonReader reader, DecoderContext decoderContext) {
            return new Tag(reader.readString());
        }

        @Override
        public void encode(BsonWriter writer, Tag value, EncoderContext encoderContext) {
            writer.writeString(value.name());
        }

        @Override
        public Class<Tag> getEncoderClass() {
            return Tag.class;
        }
    }
}
