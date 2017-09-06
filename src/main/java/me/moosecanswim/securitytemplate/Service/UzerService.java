package me.moosecanswim.securitytemplate.Service;

import me.moosecanswim.securitytemplate.Model.Role;
import me.moosecanswim.securitytemplate.Model.Uzer;
import me.moosecanswim.securitytemplate.Repository.RoleRepository;
import me.moosecanswim.securitytemplate.Repository.UzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
public class UzerService {
    @Autowired
    UzerRepository uzerRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    public UzerService(UzerRepository uzerRepository){
        this.uzerRepository=uzerRepository;
    }
    public Uzer findByEmail(String email){
        return uzerRepository.findByEmail(email);
    }
    public Long countByEmail(String email){
        return uzerRepository.countByEmail(email);
    }
    public Uzer findByUsername(String username){
        return uzerRepository.findByEmail(username);
    }
    public void saveUser(Uzer user){
        user.setRoles(Arrays.asList(roleRepository.findByRole("USER")));
        user.setEnabled(true);
        uzerRepository.save(user);
    }
    public void saveAdmin(Uzer user){
        user.setRoles(Arrays.asList(roleRepository.findByRole("ADMIN")));
        user.setEnabled(true);
        uzerRepository.save(user);
    }
    public void upgradeUserToAdmin(Uzer user){
        if(!user.getRoles().contains(roleRepository.findByRole("ADMIN"))){
            user.addRole(roleRepository.findByRole("ADMIN"));
            System.out.println("added Admin role to user");
            uzerRepository.save(user);
        }else {
            System.out.println("did not add ADMIN role to user (already asigned)");
        }

    }
    public void downgradeAdminToUser(Uzer user){
        if(user.getRoles().contains(roleRepository.findByRole("ADMIN"))){
            user.removeRole(roleRepository.findByRole("ADMIN"));
            System.out.println("removed ADMIN role from user (1/2)");
            if(!user.getRoles().contains(roleRepository.findByRole("USER"))){
                user.addRole(roleRepository.findByRole("USER"));
                System.out.println("added USER role from user (2/2)");
            }else{
                System.out.println("USER role is already asigned to user (2/2)");
            }
            uzerRepository.save(user);
        }


    }
    public void archiveUser(Uzer user){
        user.setEnabled(false);
        uzerRepository.save(user);
    }
    public void reinstateUser(Uzer user){
        user.setEnabled(true);
        uzerRepository.save(user);
    }



}
