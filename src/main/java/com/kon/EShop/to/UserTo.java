package com.kon.EShop.to;

import com.kon.EShop.HasIdAndEmail;
import com.kon.EShop.repository.userPack.RegisterStep;
import com.kon.EShop.repository.userPack.UpdateStap;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
public class UserTo implements HasIdAndEmail, Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(groups = {RegisterStep.class, UpdateStap.class})
    @Size(min = 2, max = 100, groups = {RegisterStep.class, UpdateStap.class})
    private String name;

    @Email(groups = {RegisterStep.class, UpdateStap.class})
    @NotBlank(groups = {RegisterStep.class, UpdateStap.class})
    @Size(max = 100, groups = {RegisterStep.class, UpdateStap.class})
    private String email;

    @NotBlank(groups = {RegisterStep.class})
    @Size(min = 4, max = 20,groups = {RegisterStep.class})
    private String password;

    @Size(min = 10, max = 13, message = "{error.phone}", groups = {RegisterStep.class, UpdateStap.class})
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
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTo userTo = (UserTo) o;
        return Objects.equals(id, userTo.id) && name.equals(userTo.name) && email.equals(userTo.email) && phone.equals(userTo.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, phone);
    }
}
