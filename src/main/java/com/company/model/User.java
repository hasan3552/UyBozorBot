package com.company.model;

import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Comparator;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class User implements Comparable<User> {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }

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

    @Override
    public int compareTo(User o) {
        return this.getRole().ordinal() > o.getRole().ordinal() ? 1 :
                this.getRole().ordinal() == o.getRole().ordinal() ? this.getId() > o.getId() ? 1 : -1 : -1;
    }
}
