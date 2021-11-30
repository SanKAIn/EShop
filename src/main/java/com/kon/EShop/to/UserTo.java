package com.kon.EShop.to;

import com.kon.EShop.HasIdAndEmail;
import com.kon.EShop.model.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Getter @Setter
public class UserTo implements HasIdAndEmail, Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 4, max = 20)
    private String password;

    @Size(min = 10, max = 13, message = "{error.phone}")
    private String phone;

    public UserTo() {
    }

    public UserTo(Long id, String name, String email, String password, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
