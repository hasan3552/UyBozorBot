package com.company.models;

import com.company.enumm.Language;
import com.company.enumm.Role;
import com.company.enumm.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class User{


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
