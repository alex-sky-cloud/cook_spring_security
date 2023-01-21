package com.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class UserDataDto {

    @NotEmpty(message = "First name can not be empty")
    private String firstName;

    @NotEmpty(message = "Last name can not be empty")
    private String lastName;

    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Please provide a valid email id")
    private String email;

    @NotEmpty(message = "Password can not be empty")
    private String password;

    public UserDataDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static final class UserDataDtoBuilder {
        private @NotEmpty(message = "First name can not be empty") String firstName;
        private @NotEmpty(message = "Last name can not be empty") String lastName;
        private @NotEmpty(message = "Email can not be empty") @Email(message = "Please provide a valid email id") String email;
        private @NotEmpty(message = "Password can not be empty") String password;

        private UserDataDtoBuilder() {
        }

        public static UserDataDtoBuilder anUserDataDto() {
            return new UserDataDtoBuilder();
        }

        public UserDataDtoBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserDataDtoBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserDataDtoBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserDataDtoBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserDataDto build() {
            UserDataDto userDataDto = new UserDataDto();
            userDataDto.setFirstName(firstName);
            userDataDto.setLastName(lastName);
            userDataDto.setEmail(email);
            userDataDto.setPassword(password);
            return userDataDto;
        }
    }
}
