package io.github.AneDuarte.quarkussocial.rest;

import io.github.AneDuarte.quarkussocial.domain.model.Follower;
import io.github.AneDuarte.quarkussocial.domain.model.User;
import io.github.AneDuarte.quarkussocial.domain.repository.FollowerRepository;
import io.github.AneDuarte.quarkussocial.domain.repository.UserRepository;
import io.github.AneDuarte.quarkussocial.rest.dto.FollowerRequest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.awt.*;

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
            entity.setUserId(user);
            entity.setFollowerId(follower);

            followerRepository.persist(entity);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
