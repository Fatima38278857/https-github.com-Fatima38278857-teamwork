package com.example.program.Online_Store.сontroller;

import com.example.program.Online_Store.dto.RegisterDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @GetMapping("/default")
    public RegisterDto getDefaultRegister() {
        return new RegisterDto();
    }
}
