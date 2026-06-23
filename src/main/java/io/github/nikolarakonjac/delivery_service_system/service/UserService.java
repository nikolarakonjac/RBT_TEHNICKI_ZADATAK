package io.github.nikolarakonjac.delivery_service_system.service;

import io.github.nikolarakonjac.delivery_service_system.dto.user.NewUserDto;
import io.github.nikolarakonjac.delivery_service_system.dto.user.UserDto;
import io.github.nikolarakonjac.delivery_service_system.entity.User;
import io.github.nikolarakonjac.delivery_service_system.repository.UserRepository;
import io.github.nikolarakonjac.delivery_service_system.utility.exceptions.ApiExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(NewUserDto newUserDto) {
        if(userRepository.existsByUsername(newUserDto.getUsername())){
            throw ApiExceptionFactory.userAlreadyExists();
        }

        userRepository.save(User.builder()
                .username(newUserDto.getUsername())
                .build());
    }


    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToUserDto)
                .toList();
    }

    private UserDto mapUserToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
