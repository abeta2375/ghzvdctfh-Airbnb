package com.airbnb.service.impl;

import com.airbnb.dto.LoginDto;
import com.airbnb.dto.PropertyUserDto;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.service.PropertyUserService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PropertyUserServiceImpl implements PropertyUserService {
    private PropertyUserRepository propertyUserRepository;

    public PropertyUserServiceImpl(PropertyUserRepository propertyUserRepository) {
        this.propertyUserRepository = propertyUserRepository;
    }

    @Override
    public PropertyUser addUser(PropertyUserDto propertyUserDto) {
        PropertyUser propertyUser = new PropertyUser();
        propertyUser.setFirstName(propertyUserDto.getFirstName());
        propertyUser.setLastName(propertyUserDto.getLastName());
        propertyUser.setEmail(propertyUserDto.getEmail());
        propertyUser.setUserRole(propertyUserDto.getUserRole());
        propertyUser.setUsername(propertyUserDto.getUsername());
        propertyUser.setPassword(BCrypt.hashpw(propertyUserDto.getPassword(), BCrypt.gensalt(10)));

        PropertyUser saved = propertyUserRepository.save(propertyUser);
        return saved;
    }

    @Override
    public Boolean verifyLogin(LoginDto loginDto) {
        Optional<PropertyUser> opUser = propertyUserRepository.findByUsername(loginDto.getUsername());

        if(opUser.isPresent()){
            PropertyUser propertyUser = opUser.get();
            return BCrypt.checkpw(loginDto.getPassword(), propertyUser.getPassword());
        }
        else {
            return false;
        }
    }
}
