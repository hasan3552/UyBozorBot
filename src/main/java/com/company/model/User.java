package com.company.model;

import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String fullName;
    private String phoneNumber;

    private Status status;
    private Language language;
    private Role role;
    private Boolean isBlocked = false;

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
        role = Role.REGISTER;
        status = Status.GIVE_CONTACT;
    }
}
