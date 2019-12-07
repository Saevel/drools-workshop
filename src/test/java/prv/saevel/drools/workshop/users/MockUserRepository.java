package prv.saevel.drools.workshop.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class MockUserRepository implements CrudRepository<User, Long> {

    List<User> users = new LinkedList<>();

    @Override
    public <S extends User> S save(S entity) {
        users.add(entity);
        return entity;
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<User> findAll() {
        return users;
    }

    @Override
    public Iterable<User> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
