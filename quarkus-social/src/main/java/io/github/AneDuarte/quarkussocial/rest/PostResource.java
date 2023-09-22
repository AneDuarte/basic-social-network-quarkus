package io.github.AneDuarte.quarkussocial.rest;

import io.github.AneDuarte.quarkussocial.rest.dto.CreateUserRequest;
import io.github.AneDuarte.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/users/{id}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    public Response createPost() {
        return Response.status(Response.Status.CREATED).build();
    }

    public Response listPosts() {
        return Response.ok().build();
    }
}