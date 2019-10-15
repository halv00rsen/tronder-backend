package org.tronder.words.repository;

import org.springframework.data.repository.CrudRepository;
import org.tronder.words.model.Role;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    @Override
    public List<Role> findAll();

}
