package com.reg.converter;

import com.reg.dto.UserDataDto;
import com.reg.model.UserModel;
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
