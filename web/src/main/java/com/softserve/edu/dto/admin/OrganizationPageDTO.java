package com.softserve.edu.dto.admin;

/**
 * Created by vova on 03.09.15.
 */
public class OrganizationPageDTO {

        private Long id;
        private String name;
        private String email;
        private String phone;
        private String[] types;

    public OrganizationPageDTO(){}

    public OrganizationPageDTO(Long id, String name, String email, String[] types, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.types = types;
        this.phone = phone;
    }

    public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

       public String[] getTypes() {
            return types;
        }

        public void setTypes(String[] types) {
            this.types = types;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
