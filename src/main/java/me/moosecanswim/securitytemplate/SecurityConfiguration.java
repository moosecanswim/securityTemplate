package me.moosecanswim.securitytemplate;

import me.moosecanswim.securitytemplate.Repository.UzerRepository;
import me.moosecanswim.securitytemplate.Service.SSUzerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private SSUzerDetailsService uzerDetailsService;

    @Autowired
    private UzerRepository uzerRepository;

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception{
        return new SSUzerDetailsService(uzerRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers("/img/**","/css/**","/","/register","/js/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/mypage")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll().permitAll()
                .and()
                .httpBasic();
        http
                .csrf().disable();
        http
                .headers().frameOptions().disable();

    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                        .userDetailsService(userDetailsServiceBean());
//        auth.inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER")
//                .and()
//                .withUser("dave").password("begreat").roles("ADMIN")
//                .and()
//                .withUser("newuser").password("newuserpa$$").roles("USER")
//                .and()
//                .withUser("admin").password("password").roles("ADMIN");

    }
}
