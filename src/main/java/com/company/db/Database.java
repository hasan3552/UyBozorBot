package com.company.db;

import com.company.enums.Language;
import com.company.enums.Role;
import com.company.enums.Status;
import com.company.model.*;

import java.util.ArrayList;
import java.util.List;


public class Database {

    public static final List<Language> LANGUAGES = new ArrayList<>();
    public static final List<Role> ROLES = new ArrayList<>();
    public static List<Status> statuses=  new ArrayList<>();

    public static List<User> customers = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();
    public static List<Location> locations = new ArrayList<>();
    public static List<Product> products = new ArrayList<>();
    public static List<Photo> photos = new ArrayList<>();
    public static List<Liked> likeds = new ArrayList<>();



    public static void compile(){

        LANGUAGES.add(Language.UZ);
        LANGUAGES.add(Language.RU);

        ROLES.add(Role.REGISTER);
        ROLES.add(Role.CUSTOMER);
        ROLES.add(Role.ADMIN);

        statuses.add(Status.MENU);
        statuses.add(Status.GIVE_CONTACT);
        statuses.add(Status.GIVE_LANGUAGE);
        statuses.add(Status.GIVE_FULL_NAME);
        statuses.add(Status.SETTING);
        statuses.add(Status.ADMIN_MENU);


        // in progress
    }

}