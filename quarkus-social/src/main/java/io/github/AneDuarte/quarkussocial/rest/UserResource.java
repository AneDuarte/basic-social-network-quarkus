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

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private jakarta.validation.Validator validator;

    @Inject
    public UserResource(Validator validator) {
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser( CreateUserRequest userRequest ) {
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
        if (!violations.isEmpty()) {
            String mensagemErro = violations.stream().findAny().get().getMessage();
            return Response.status(400).entity(mensagemErro).build();
        }

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        user.persist();

        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(user)
                .build();
    }

    @GET
    public Response listAllUsers() {
        PanacheQuery<User> query = User.findAll();
        return Response.ok(query.list()).build();
    }

    @GET
    @Path("{id}")
    public Response getUserById(@PathParam("id") Long userId) {
        User user = User.findById(userId);
        if(user == null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(user).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long userId, CreateUserRequest userData) {
        User user = User.findById(userId);
        if (user == null) return Response.status(Response.Status.NOT_FOUND).build();

        user.setName(userData.getName());
        user.setAge(userData.getAge());
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long userId) {
        User user = User.findById(userId);
        if (user == null) return Response.status(Response.Status.NOT_FOUND).build();

        user.delete();
        return Response.ok().build();
    }
}