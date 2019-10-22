package org.tronder.words.repository;

import org.springframework.data.repository.CrudRepository;
import org.tronder.words.model.Dialect;

public interface DialectRepository extends CrudRepository<Dialect, Integer> {

    Iterable<Dialect> findAllByPublicDialectIsTrue();
    Iterable<Dialect> findAllByPublicDialectIsTrueOrCreatedByEquals(String createdBy);

}
