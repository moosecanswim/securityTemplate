package me.moosecanswim.securitytemplate.Repository;


import me.moosecanswim.securitytemplate.Model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role,Long> {
    Role findByRole(String in);
}
