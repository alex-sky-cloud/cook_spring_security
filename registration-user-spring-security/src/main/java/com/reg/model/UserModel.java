package com.reg.model;

import lombok.Data;

import java.util.Set;

@Data
public class UserModel {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Set<Authority> authoritySet;


    public static final class UserModelBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private Set<Authority> authoritySet;

        private UserModelBuilder() {
        }

        public static UserModelBuilder anUserModel() {
            return new UserModelBuilder();
        }

        public UserModelBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public UserModelBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserModelBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserModelBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserModelBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserModelBuilder setAuthoritySet(Set<Authority> authoritySet) {
            this.authoritySet = authoritySet;
            return this;
        }

        public UserModel build() {
            UserModel userModel = new UserModel();
            userModel.setId(id);
            userModel.setFirstName(firstName);
            userModel.setLastName(lastName);
            userModel.setEmail(email);
            userModel.setPassword(password);
            userModel.setAuthoritySet(authoritySet);
            return userModel;
        }
    }
}


