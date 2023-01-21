package com.security.converter;


import com.security.dto.UserDataDto;
import com.security.model.UserModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDataConverter implements Converter<UserDataDto, UserModel> {


    @Override
    public UserModel convert(UserDataDto source) {

      return   UserModel
                .UserModelBuilder
                .anUserModel()
                .setFirstName(source.getFirstName())
                .setLastName(source.getLastName())
                .setPassword(source.getPassword())
                .setEmail(source.getEmail())
                .build();
    }
}
