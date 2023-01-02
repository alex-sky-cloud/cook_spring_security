package com.example.model;

public record CustomerModel(
        Long id,
        String firstName,
        String lastName,
        String email,
        String password) {


    public static final class CustomerModelBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        private CustomerModelBuilder() {
        }

        public static CustomerModelBuilder aCustomerModel() {
            return new CustomerModelBuilder();
        }

        public CustomerModelBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CustomerModelBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CustomerModelBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CustomerModelBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CustomerModelBuilder password(String password) {
            this.password = password;
            return this;
        }

        public CustomerModel build() {
            return new CustomerModel(id, firstName, lastName, email, password);
        }
    }
}
