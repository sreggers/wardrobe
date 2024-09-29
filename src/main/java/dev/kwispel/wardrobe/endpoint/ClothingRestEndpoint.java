package dev.kwispel.wardrobe.endpoint;

import dev.kwispel.wardrobe.command.CreateClothingCommand;
import dev.kwispel.wardrobe.domain.ClothingPiece;
import dev.kwispel.wardrobe.domain.Tag;
import io.quarkus.mongodb.panache.common.reactive.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import java.util.Set;
import java.util.stream.Collectors;

@Path("clothing")
public class ClothingRestEndpoint {
    @GET
    @Path("all")
    public Multi<ClothingPiece> getClothing() {
        return ClothingPiece.streamAll();
    }

    @GET
    public Multi<ClothingPiece> getClothing(@RestQuery Set<Tag> tags) {
        var query = "{ tags: { $all: [\"" + tags.stream().map(Tag::name).collect(Collectors.joining("\", \"")) + "\"] } }";
        return ClothingPiece.stream(query);
    }

    @POST
    public Uni<RestResponse<ClothingPiece>> createClothing(@Context UriInfo uriInfo, CreateClothingCommand command) {
        var clothingPiece = new ClothingPiece();
        clothingPiece.name = command.name();
        clothingPiece.imageSrc = command.imageSrc();
        clothingPiece.tags = command.tags();
        return Panache.withTransaction(clothingPiece::persist).map(cp ->
                ResponseBuilder
                        .create(Response.Status.CREATED, clothingPiece)
                        .header(HttpHeaders.LOCATION, uriInfo.getAbsolutePathBuilder().path(clothingPiece.id.toString()).build())
                        .build());
    }
}
