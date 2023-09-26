package io.github.AneDuarte.quarkussocial.rest;

import io.github.AneDuarte.quarkussocial.domain.model.Post;
import io.github.AneDuarte.quarkussocial.domain.model.User;
import io.github.AneDuarte.quarkussocial.domain.repository.PostRepository;
import io.github.AneDuarte.quarkussocial.domain.repository.UserRepository;
import io.github.AneDuarte.quarkussocial.rest.dto.CreatePostRequest;
import io.github.AneDuarte.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.ejb.Local;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.bytebuddy.TypeCache;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{id}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    @Inject
    public PostResource(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @POST
    @Transactional
    public Response createPost(@PathParam("id") Long userId, CreatePostRequest postRequest) {
        User user = userRepository.findById(userId);
        if (user == null) return Response.status(Response.Status.NOT_FOUND).build();

        Post post = new Post();
        post.setText(postRequest.getText());
        post.setUserId(user);
        //Uma das formas de enviar o horario da postagem
        post.setDateTime(LocalDateTime.now());

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("id") Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) return Response.status(Response.Status.NOT_FOUND).build();

        //postRepository.find("select post from post where user = :user");
        PanacheQuery query = postRepository.find("userId", Sort.by("dateTime", Sort.Direction.Descending),user);
        //esse Sort.Direction.Descending é para listar de traz pra frente, os ultimos adicionados são mostrados primeiro
        List<Post> listagem = query.list();

        //Vai mapear um objeto da lista atual (tipo Post) para um objeto PostResponse (dados formatados)
        var listagemPostResponse = listagem.stream()
                //.map(post -> PostResponse.formatarPosts(post))
                .map(PostResponse::formatarPosts) //método de referência
                .collect(Collectors.toList());

        return Response.ok(listagemPostResponse).build();
    }
}
