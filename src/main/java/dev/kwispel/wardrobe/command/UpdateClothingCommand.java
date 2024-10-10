package dev.kwispel.wardrobe.command;

import dev.kwispel.wardrobe.domain.Color;
import dev.kwispel.wardrobe.domain.Tag;

import java.util.Set;

public record UpdateClothingCommand(
        String name,
        String imageSrc,
        Set<Tag> tags,
        Set<Color> colors) {
}
