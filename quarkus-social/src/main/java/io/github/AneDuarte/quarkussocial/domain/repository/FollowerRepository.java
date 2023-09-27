package io.github.AneDuarte.quarkussocial.domain.repository;

import io.github.AneDuarte.quarkussocial.domain.model.Follower;
import io.github.AneDuarte.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.lang.reflect.Parameter;
import java.security.Policy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean verificarFollow(User follower, User user) {
        /*Modo 1
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("follower", follower);
        parametros.put("user", user); */

        //Modo 2
        var parametros = Parameters.with("follower", follower).and("user", user).map();

        PanacheQuery<Follower> query = find("follower =:follower and user =:user", parametros);
        Optional<Follower> resultado = query.firstResultOptional();

        return resultado.isPresent();
    }

    public List<Follower> listarPorUser(Long userId) {
        PanacheQuery<Follower> query = find("user.id", userId);
        return query.list();
    }

    public void deleteFollower(Long followerId, Long userId) {
        var parametros = Parameters.with("userId", userId).and("followerId", followerId).map();

        delete("follower.id =:followerId and user.id =:userId", parametros);
    }
}
