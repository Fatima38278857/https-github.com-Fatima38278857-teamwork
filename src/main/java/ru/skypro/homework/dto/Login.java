package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Schema(name = "Login")
public class Login {
    @Size(min = 8, max = 16)
    @Schema(description = "пароль")
    private String password;
    @Size(min = 4, max = 32)
    @Schema(description = "логин")
    private String username;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
