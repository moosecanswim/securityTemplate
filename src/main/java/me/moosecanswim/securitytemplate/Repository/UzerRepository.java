package me.moosecanswim.securitytemplate.Repository;

import me.moosecanswim.securitytemplate.Model.Uzer;
import org.springframework.data.repository.CrudRepository;

public interface UzerRepository extends CrudRepository<Uzer,Long> {
    Uzer findByUsername(String username);
    Uzer findByEmail(String email);
    Long countByEmail(String email);
    Long countByUsername(String username);
}
