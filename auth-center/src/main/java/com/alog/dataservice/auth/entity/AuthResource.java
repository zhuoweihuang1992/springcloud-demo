package com.alog.dataservice.auth.entity;

import javax.persistence.*;

@Entity
@Table(name = "auth_resource")
public class AuthResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "role")
    private String role;

    @Column(name = "resource")
    private String resource;

    @Column(name = "data_area_code")
    private String data_area_code;

    public String getData_area_code() {
        return data_area_code;
    }

    public void setData_area_code(String data_area_code) {
        this.data_area_code = data_area_code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "AuthResource{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", resource='" + resource + '\'' +
                ", data_area_code='" + data_area_code + '\'' +
                '}';
    }
}
