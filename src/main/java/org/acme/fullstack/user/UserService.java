package org.acme.fullstack.user;

import io.quarkus.elytron.security.common.BcryptUtil;
//import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.ObjectNotFoundException;
import java.util.List;

@ApplicationScoped
public class UserService {

    public Uni<User> findById(long id) {
        return User.<User>findById(id)
        .onItem().ifNull().failWith(() -> new ObjectNotFoundException(id, "User"));
    }
    public Uni<User> findByName(String name) {
        return User.find("name", name).firstResult();
    }
    public Uni<List<User>> list() {
        return User.listAll();
    }

    //@ReactiveTransactional
    public Uni<User> create(User user) {
        user.password = BcryptUtil.bcryptHash(user.password);
        return user.persistAndFlush();
    }

    //@ReactiveTransactional
    public Uni<User> update(User user) {
        return findById(user.id).chain(u -> User.getSession())
        .chain(s -> s.merge(user));
    }


}
