package dev.kwispel.wardrobe.command;

import dev.kwispel.wardrobe.domain.Tag;

import java.util.Set;

public record CreateClothingCommand(String name, String imageSrc, Set<Tag> tags) {
}
