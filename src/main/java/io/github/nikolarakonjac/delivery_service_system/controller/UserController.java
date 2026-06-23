package io.github.nikolarakonjac.delivery_service_system.controller;

import io.github.nikolarakonjac.delivery_service_system.dto.user.NewUserDto;
import io.github.nikolarakonjac.delivery_service_system.dto.user.UserDto;
import io.github.nikolarakonjac.delivery_service_system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create user", description = "Creates a new user with a unique username.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "409", description = "Username already exists")
    })
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid NewUserDto newUser) {
        userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get all users", description = "Returns a list of all registered users.")
    @ApiResponse(responseCode = "200", description = "List of users")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
