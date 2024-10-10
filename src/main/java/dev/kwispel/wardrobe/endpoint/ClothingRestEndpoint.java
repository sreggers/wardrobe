package dev.kwispel.wardrobe.endpoint;

import dev.kwispel.wardrobe.command.CreateClothingCommand;
import dev.kwispel.wardrobe.command.UpdateClothingCommand;
import dev.kwispel.wardrobe.domain.ClothingPiece;
import dev.kwispel.wardrobe.domain.Tag;
import dev.kwispel.wardrobe.magic.ClothingPieceMapper;
import io.quarkus.mongodb.panache.common.reactive.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

@Path("clothing")
public class ClothingRestEndpoint {
    @Inject
    ClothingPieceMapper clothingPieceMapper;

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
        var clothingPiece = clothingPieceMapper.create(command);
        return Panache.withTransaction(clothingPiece::persist).map(cp ->
                ResponseBuilder
                        .create(Response.Status.CREATED, clothingPiece)
                        .header(HttpHeaders.LOCATION, uriInfo.getAbsolutePathBuilder().path(clothingPiece.id.toString()).build())
                        .build());
    }

    @PATCH
    @Path("{id}")
    public Uni<RestResponse<Object>> updateClothing(
            @Context UriInfo uriInfo,
            @PathParam("id") String id,
            UpdateClothingCommand command) {
        return ClothingPiece.<ClothingPiece>findById(new ObjectId(id))
                .onItem().ifNotNull().transform(clothinPiece -> clothingPieceMapper.update(command, clothinPiece))
                .onItem().ifNotNull().call(clothingPiece -> clothingPiece.persistOrUpdate())
                .onItem().ifNotNull().transform(clothingPiece ->
                        ResponseBuilder.noContent()
                                .header(HttpHeaders.LOCATION, uriInfo.getAbsolutePath())
                                .build())
                .onItem().ifNull().continueWith(ResponseBuilder.notFound().build())
                .ifNoItem()
                    .after(Duration.of(10, ChronoUnit.SECONDS))
                    .recoverWithItem(ResponseBuilder.serverError().build());
    }
}
