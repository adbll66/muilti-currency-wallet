package com.wallet.mmulticurrencywallet.controller;

import com.wallet.mmulticurrencywallet.model.Users;
import com.wallet.mmulticurrencywallet.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public Users createUser(@RequestBody Users user){
        return userService.registerUser(user);
    }

    @GetMapping
    public List<Users>getAllUsers(){
        return userService.getAllUsers();
    }
}
