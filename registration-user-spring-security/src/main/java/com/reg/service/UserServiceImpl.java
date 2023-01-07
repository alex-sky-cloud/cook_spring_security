package com.reg.service;

import com.reg.dto.UserDataDto;
import com.reg.exception.UserAlreadyExistException;
import com.reg.model.UserModel;
import com.reg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final Converter <UserDataDto, UserModel> converter;

    @Override
    public void register(UserDataDto userDataDto) throws UserAlreadyExistException {

        if(checkIfUserExist(userDataDto.getEmail())){
            throw new UserAlreadyExistException("User already exists for this email");
        }

        UserModel userModel = converter.convert(userDataDto);

        assert userModel != null;
        encodePassword(userDataDto, userModel);

        userRepository.saveUserRegistration(userModel);
    }

    private void encodePassword(UserDataDto userDataDto, UserModel user){

        String password = userDataDto.getPassword();
        String passwordEncoded = passwordEncoder.encode(password);

        user.setPassword(passwordEncoded);
    }

    @Override
    public boolean checkIfUserExist(String email) {

        return userRepository
                .findByEmail(email)
                .isPresent();
    }
}
