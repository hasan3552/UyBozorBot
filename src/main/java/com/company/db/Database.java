package com.company.db;

import com.company.enums.*;
import com.company.model.*;

import java.util.ArrayList;
import java.util.List;


public class Database {

    public static final List<Language> LANGUAGES = new ArrayList<>();
    public static final List<Role> ROLES = new ArrayList<>();
    public static final List<Status> STATUSES = new ArrayList<>();
    public static final List<ProductStatus> PRODUCT_STATUSES = new ArrayList<>();
    public static final List<AdStatus> AD_STATUSES = new ArrayList<>();
    public static final List<CategoryStatus> CATEGORY_STATUSES = new ArrayList<>();

    public static List<User> customers = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();
    public static List<Location> locations = new ArrayList<>();
    public static List<Product> products = new ArrayList<>();
    public static List<Liked> likeds = new ArrayList<>();
    public static List<Advertisement> advertisements = new ArrayList<>();


    public static void compile() {

        LANGUAGES.add(Language.UZ);
        LANGUAGES.add(Language.RU);

        ROLES.add(Role.REGISTER);
        ROLES.add(Role.CUSTOMER);
        ROLES.add(Role.ADMIN);
        ROLES.add(Role.SUPER_ADMIN);

        STATUSES.add(Status.GIVE_CONTACT);
        STATUSES.add(Status.GIVE_LANGUAGE);
        STATUSES.add(Status.GIVE_FULL_NAME);
        STATUSES.add(Status.USER_SHOW_CATEGORY);
        STATUSES.add(Status.USER_SETTING_MENU);
        STATUSES.add(Status.SET_LANGUAGE);
        STATUSES.add(Status.SET_NEW_FULLNAME);
        STATUSES.add(Status.SET_NEW_CONTACT);
        STATUSES.add(Status.USER_SHOW_PRODUCT);
        STATUSES.add(Status.USER_SHOW_LIKED);
        STATUSES.add(Status.USER_GIVE_REKLAMA);
        STATUSES.add(Status.USER_SEND_PRODUCT_PHOTO);
        STATUSES.add(Status.USER_SEND_PRODUCT_CONTACT);
        STATUSES.add(Status.USER_SEND_PRODUCT_LOCATION);
        STATUSES.add(Status.USER_SEND_PRODUCT_INFO);
        STATUSES.add(Status.USER_SHOW_OWN_PRODUCT);
        STATUSES.add(Status.MENU);
        STATUSES.add(Status.SETTING);
        STATUSES.add(Status.REFRESH);
        STATUSES.add(Status.ADMIN_MENU);
        STATUSES.add(Status.ADMIN_SEND_CUSTOMER_RESPONSE);
        STATUSES.add(Status.ADMIN_ADD_OR_REVOKE_ADMIN);
        STATUSES.add(Status.PREMIUM_REKLAMA);
        STATUSES.add(Status.CATEGORY_CRUD);
        STATUSES.add(Status.ADMIN_CUSTOMER_CRUD);
        STATUSES.add(Status.BLOCKED_USER);
        STATUSES.add(Status.ADMIN_WRITE_RESPONSE);
        STATUSES.add(Status.WAIT_RESPONSE);
        STATUSES.add(Status.ADMIN_CREATE_CATEGORY);
        STATUSES.add(Status.ADMIN_SHOW_CATEGORY);
        STATUSES.add(Status.ADMIN_UPDATE_CATEGORY);
        STATUSES.add(Status.ADMIN_DELETED_CATEGORY);

        PRODUCT_STATUSES.add(ProductStatus.NEW);
        PRODUCT_STATUSES.add(ProductStatus.REQUEST);
        PRODUCT_STATUSES.add(ProductStatus.BREAK);

        AD_STATUSES.add(AdStatus.NEW);
        AD_STATUSES.add(AdStatus.HAS_PHOTO);
        AD_STATUSES.add(AdStatus.HAS_CAPTION);
        AD_STATUSES.add(AdStatus.HAS_URL);
        AD_STATUSES.add(AdStatus.HAS_INLINE_NAME);
        AD_STATUSES.add(AdStatus.READY);

        CATEGORY_STATUSES.add(CategoryStatus.NEW);
        CATEGORY_STATUSES.add(CategoryStatus.HAS_NAME_RU);
        CATEGORY_STATUSES.add(CategoryStatus.HAS_NAME_UZ);
    }

}
