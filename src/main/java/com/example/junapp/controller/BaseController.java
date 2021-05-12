package com.example.junapp.controller;

import com.example.junapp.model.User;
import com.example.junapp.service.MailSenderService;
import com.example.junapp.service.RoleService;
import com.example.junapp.service.UserService;
import com.example.junapp.validation.EmailUniquenessValidator;
import com.example.junapp.validation.UsernameUniquenessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class BaseController {

    private UserService userService;
    private EmailUniquenessValidator emailUniquenessValidator;
    private UsernameUniquenessValidator usernameUniquenessValidator;
    private RoleService roleService;
    private MailSenderService mailSenderService;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setEmailUniquenessValidator(EmailUniquenessValidator emailUniquenessValidator) {
        this.emailUniquenessValidator = emailUniquenessValidator;
    }

    @Autowired
    public void setUsernameUniquenessValidator(UsernameUniquenessValidator usernameUniquenessValidator) {
        this.usernameUniquenessValidator = usernameUniquenessValidator;
    }

    @Autowired
    public void setMailSenderService(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home() {
        return "home page";
    }

    @GetMapping("/login")
    public void login() {
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    @GetMapping("/registration")
    public User registration() {
        return new User();
    }

    @PostMapping(value = "/saveUser")
    public User tryingToSaveUser(@Valid @RequestBody User usr, BindingResult bindingResult) {
        usernameUniquenessValidator.validate(usr, bindingResult);
        emailUniquenessValidator.validate(usr, bindingResult);
        if (!bindingResult.hasErrors()) {
            usr.setPassword(passwordEncoder.encode(usr.getPassword()));
            usr.getRoles().add(roleService.findByName("ROLE_USER"));
            usr.setActivationCode(UUID.randomUUID().toString());
            userService.saveUser(usr);
            String messageEmail = String.format(
                    "Здравствуйте, %s! \n" +
                            "Для подтверждения почтового ящика перейдите, пожалуйста, "
                            + "по ссылке: http://localhost:2021/activate/%s",
                    usr.getUsername(),
                    usr.getActivationCode()
            );
            mailSenderService.send(usr.getEmail(), "Activation code", messageEmail);
            String messageForUser = "На почту пришло сообщение со ссылкой для активации аккаунта";
            usr.setMessageForUser(messageForUser);
        }
        return usr;
    }


    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            return "Активация прошла успешно";
        } else {
            return "Ссылка недействительна";
        }
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @PostMapping("/main")
    public String successLogin() {
        return "redirect:/main";
    }
}



