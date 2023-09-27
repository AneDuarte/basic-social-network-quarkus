package io.github.AneDuarte.quarkussocial.rest;

import io.github.AneDuarte.quarkussocial.domain.model.Follower;
import io.github.AneDuarte.quarkussocial.domain.model.User;
import io.github.AneDuarte.quarkussocial.domain.repository.FollowerRepository;
import io.github.AneDuarte.quarkussocial.domain.repository.UserRepository;
import io.github.AneDuarte.quarkussocial.rest.dto.FollowerRequest;
import io.github.AneDuarte.quarkussocial.rest.dto.FollowerResponse;
import io.github.AneDuarte.quarkussocial.rest.dto.FollowersPerUserResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("users/{id}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private UserRepository userRepository;
    private FollowerRepository followerRepository;

    @Inject
    public FollowerResource(UserRepository userRepository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    @PUT
    @Transactional
    public Response addFollower(@PathParam("id") Long userId, FollowerRequest followerRequest) {

        //Caso o usuario tente seguir a si mesmo
        if(userId.equals(followerRequest.getFollowerId())) return Response.status(Response.Status.CONFLICT).entity("Você não pode seguir a si mesmo").build();

        User user = userRepository.findById(userId);
        if (user == null) return Response.status(Response.Status.NOT_FOUND).build();

        var follower = userRepository.findById(followerRequest.getFollowerId());

        boolean follows = followerRepository.verificarFollow(follower, user);

        if (!follows) {
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);

            followerRepository.persist(entity);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers(@PathParam("id") Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) return Response.status((Response.Status.NOT_FOUND)).build();

        List<Follower> lista = followerRepository.listarPorUser(userId);
        FollowersPerUserResponse responseObject = new FollowersPerUserResponse();
        responseObject.setFollowersCount(lista.size());

        var listaSeguidores = lista.stream()
                .map( FollowerResponse::new )
                .collect(Collectors.toList());

        responseObject.setContent(listaSeguidores);
        return Response.ok(responseObject).build();
    }
}
