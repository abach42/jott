package com.abach42.jott.user;

import com.abach42.jott.config.validation.OnCreate;
import com.abach42.jott.config.validation.OnUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

@Entity
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 250)
    @Column(length = 250)
    private String userName;

    @Size(max = 250)
    @Column(length = 250, unique = true)
    private String identifier;

    @Length(groups = OnCreate.class, min = 2, max = 255, message = "Password must be between 2 and 255 characters.")
    @Null(groups = OnUpdate.class)
    private String password;

    @Size(max = 250)
    @Column(length = 250, unique = true)
    private String email;

    @NotNull
    @Convert(converter = UserRoleConverter.class)
    private UserRole role;

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
