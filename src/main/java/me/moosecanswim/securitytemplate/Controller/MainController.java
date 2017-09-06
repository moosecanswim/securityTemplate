package me.moosecanswim.securitytemplate.Controller;


import me.moosecanswim.securitytemplate.Model.Role;
import me.moosecanswim.securitytemplate.Model.Uzer;
import me.moosecanswim.securitytemplate.Repository.RoleRepository;
import me.moosecanswim.securitytemplate.Repository.UzerRepository;
import me.moosecanswim.securitytemplate.Service.UzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class MainController {
    @Autowired
    private UzerService uzerService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UzerRepository uzerRepository;

    @RequestMapping("/")
    public String index(){
        if(roleRepository.count()<1){
            addDefaults();
        }
        return "index";
    }
    @RequestMapping("/login")
    public String login(Principal p){
        return "login";
    }
    @RequestMapping("/admin")
    public String admin(Model toSend){
        toSend.addAttribute("listUsers",uzerRepository.findAll());
        return "admin";
    }
    @RequestMapping("/secure")
    public String secure(){
        return "secure";
    }
    @RequestMapping("/testRoles")
    public @ResponseBody String showRoles(){
        Iterable <Role> r = roleRepository.findAll();
        String x="<h2>ROLE DETAILS</><br/>";
        for(Role item:r){
            x+="Role details:"+item.getRole()+" has an ID of " +item.getId() +"<br/>";
        }
        Role findR= roleRepository.findByRole("ADMIN");
        x+=findR.getRole()+" was found with an ID of "+ findR.getId();
        return x;
    }
    @GetMapping("/register")
    public String showRegistrationPage(Model toSend){
        toSend.addAttribute("user", new Uzer());
        return "registration";
    }
    @PostMapping("/register")
    public String processRegistrationPage(@Valid Uzer user, BindingResult result,Model toSend){
        toSend.addAttribute("user", user);
        if(result.hasErrors()){
            return "registration";
        }else{
            uzerService.saveUser(user);
            toSend.addAttribute("message","User Account Successfully Created");
        }
        return "index";
    }

    @RequestMapping("toggleadminrole/{id}")
    public String toggleAdminRole(@PathVariable("id")long id){
        Uzer tempUser = uzerRepository.findOne(id);
        if(tempUser.getRoles().contains(roleRepository.findByRole("ADMIN"))){
            uzerService.downgradeAdminToUser(tempUser);
        }
        else{
            uzerService.upgradeUserToAdmin(tempUser);
        }
        return "redirect:/admin";
    }

    @RequestMapping("/toggleuserstatus/{id}")
    public String toggleUserStatus(@PathVariable("id")long id){
        Uzer tempUser=uzerRepository.findOne(id);
        if(tempUser.isEnabled()){
            //togle to disabled
            uzerService.archiveUser(tempUser);
        }else{
            uzerService.reinstateUser(tempUser);
        }
        return "redirect:/admin";
    }




    public void addDefaults(){
        Role role1 = new Role();
        role1.setId(1);
        role1.setRole("USER");

        Role role2 = new Role();
        role2.setId(2);
        role2.setRole("ADMIN");

        roleRepository.save(role1);
        roleRepository.save(role2);

    }

}
