package me.moosecanswim.securitytemplate.Service;

import me.moosecanswim.securitytemplate.Model.Role;
import me.moosecanswim.securitytemplate.Model.Uzer;
import me.moosecanswim.securitytemplate.Repository.UzerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUzerDetailsService implements UserDetailsService {

    private UzerRepository uzerRepository;

    public SSUzerDetailsService(UzerRepository uzerRepository){
        this.uzerRepository =uzerRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            Uzer user = uzerRepository.findByUsername(username);
            if(user == null){
                System.out.println("user not found with the provided username ");
                return null;
            }
            return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),getAuthorities(user));
        }
        catch (Exception e){
            System.out.println("exception! " +e.toString());

            throw new UsernameNotFoundException("User not found");
        }
    }
    private Set<GrantedAuthority> getAuthorities(Uzer user){

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

        for(Role role: user.getRoles()){
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}
