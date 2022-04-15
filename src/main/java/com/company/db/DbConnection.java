package com.company.db;


import com.company.enums.*;
import com.company.model.*;

import java.sql.*;
import java.util.Optional;

import static com.company.util.ComponentContainer.*;

public class DbConnection {

    public static Connection connection;

    public static void readFromDatabase() {

        String readFromUsers = "SELECT * FROM users ORDER BY id";
        String readFromCategory = "SELECT * FROM category ORDER BY id";
        String readFromLocation = "SELECT * FROM location ORDER BY id";
        String readFromProduct = "SELECT * FROM product ORDER  BY id";
        String readFromLiked = "SELECT * FROM liked ORDER  BY id";
        String readFromAdvertisement = "SELECT * FROM advertisement ORDER  BY id";

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet3 = statement.executeQuery(readFromUsers);

            while (resultSet3.next()) {

                Long id = resultSet3.getLong("id");
                String username = resultSet3.getString("username");
                String fullName = resultSet3.getString("fullname");
                String phoneNumber = resultSet3.getString("phone_number");
                String status = resultSet3.getString("status");
                Optional<Status> optional = Database.STATUSES.stream()
                        .filter(status1 -> status1.name().equals(status))
                        .findAny();

                String language = resultSet3.getString("language");
                Optional<Language> optional1 = Database.LANGUAGES.stream()
                        .filter(language1 -> language1.name().equals(language))
                        .findAny();
                Language language1 = null;
                if (optional1.isPresent()) {
                    language1 = optional1.get();
                }

                String role = resultSet3.getString("role");
                Optional<Role> optional2 = Database.ROLES.stream()
                        .filter(role1 -> role1.name().equals(role))
                        .findAny();

                boolean isBlocked = resultSet3.getBoolean("is_blocked");

                User user = new User(id, username, fullName, phoneNumber, optional.get(),
                        language1, optional2.get(), isBlocked);
                Database.customers.add(user);

            }

            ResultSet resultSet2 = statement.executeQuery(readFromCategory);

            while (resultSet2.next()) {

                Integer id = resultSet2.getInt("id");
                String nameUz = resultSet2.getString("name_uz");
                String nameRu = resultSet2.getString("name_ru");
                Integer categoryId = resultSet2.getInt("category_id");
                Boolean isDeleted = resultSet2.getBoolean("is_deleted");
                String status = resultSet2.getString("status");
                CategoryStatus categoryStatus1 = Database.CATEGORY_STATUSES.stream()
                        .filter(categoryStatus -> categoryStatus.name().equals(status))
                        .findAny().get();

                Category category = new Category(id, nameUz, nameRu, categoryId, isDeleted, categoryStatus1);
                Database.categories.add(category);

            }

            ResultSet resultSet = statement.executeQuery(readFromLocation);

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                double lang = resultSet.getDouble("lang");
                double late = resultSet.getDouble("late");
                boolean isDeleted = resultSet.getBoolean("is_deleted");

                Location location = new Location(id, lang, late, isDeleted);
                Database.locations.add(location);

            }

            ResultSet resultSet1 = statement.executeQuery(readFromProduct);

            while (resultSet1.next()) {

                long id = resultSet1.getLong("id");
                long userId = resultSet1.getLong("user_id");
                String text = resultSet1.getString("text");
                int locationId = resultSet1.getInt("location_id");
                String contactProduct = resultSet1.getString("contact_product");
                int categoryId = resultSet1.getInt("category_id");
                Timestamp when = resultSet1.getTimestamp("when");
                boolean isDeleted = resultSet1.getBoolean("is_deleted");
                boolean isSending = resultSet1.getBoolean("is_sending");
                String fileId = resultSet1.getString("file_id");
                String status = resultSet1.getString("status");

                ProductStatus status1 = Database.PRODUCT_STATUSES.stream()
                        .filter(productStatus -> productStatus.name().equals(status))
                        .findAny().get();

                Product product = new Product(id, userId, categoryId, text, locationId
                        , contactProduct, when.toLocalDateTime(), fileId, isDeleted, isSending, status1);
                Database.products.add(product);

            }

            ResultSet resultSet5 = statement.executeQuery(readFromLiked);

            while (resultSet5.next()) {

                long id = resultSet5.getLong("id");
                long userId = resultSet5.getLong("user_id");
                long productId = resultSet5.getLong("product_id");
                boolean isDeleted = resultSet5.getBoolean("is_deleted");

                Liked liked = new Liked(id, userId, productId, isDeleted);
                Database.likeds.add(liked);
            }

            ResultSet resultSet4 = statement.executeQuery(readFromAdvertisement);

            while (resultSet4.next()) {

                int id = resultSet4.getInt("id");
                String body = resultSet4.getString("body");
                String photo = resultSet4.getString("photo");
                String inlineName = resultSet4.getString("inline_name");
                String inlineUrl = resultSet4.getString("inline_url");
                Timestamp when = resultSet4.getTimestamp("when");
                String status = resultSet4.getString("status");

                AdStatus adStatus1 = Database.AD_STATUSES.stream()
                        .filter(adStatus -> adStatus.name().equals(status))
                        .findAny().get();

                boolean isDeleted = resultSet4.getBoolean("is_deleted");
                boolean isSending = resultSet4.getBoolean("is_sending");

                Advertisement advertisement = new Advertisement(id, body, photo, inlineName, inlineUrl,
                        when.toLocalDateTime(), adStatus1, isDeleted, isSending);
                Database.advertisements.add(advertisement);

            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void addCustomer(User user) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String addUser = String.format("CALL add_users(%s,'%s')", user.getId(), user.getUsername());

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(addUser));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setStatusUser(Long id, Status status) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE users SET status = '" + status.name() + "' WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setLanguageUser(Long id, Language language) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE users SET language = '" + language.name() + "' WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setProductStatus(Long id, ProductStatus status) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE product SET status = '" + status.name() + "' WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAdvertisement(Advertisement advertisement) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String addAdvertisement = "INSERT INTO advertisement (id) VALUES(" + advertisement.getId() + ")";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(addAdvertisement));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void setProductIsSending(Long id, Boolean isSending) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE product SET is_sending = " + isSending + " WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setRoleUser(Long id, Role role) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE users SET role = '" + role.name() + "' WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setUserIsBlocked(Long id, Boolean isBlocked) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE users SET is_blocked = " + isBlocked + " WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setCategoryNameUz(Integer id, String nameUz, CategoryStatus status) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE category " +
                "SET name_uz = '" + nameUz + "', status = '" + status +
                "' WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setCategoryNameRu(Integer id, String nameRu, CategoryStatus status, Boolean isDeleted) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE category " +
                "SET name_ru = '" + nameRu + "', status = '" + status + "', is_deleted = " + isDeleted +
                " WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCategory(Category category) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String addAdvertisement = "INSERT INTO category (id,status,category_id, status) " +
                " VALUES(" + category.getId() + ", '" + category.getStatus() + "'," + category.getCategoryId() + ",'NEW')";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(addAdvertisement));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setCategoryStatus(Integer id, CategoryStatus status, Boolean isDeleted) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE category " +
                "SET status = '" + status + "',  is_deleted = " + isDeleted +
                " WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setAdvertisementPhoto(Advertisement advertisement1) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE advertisement " +
                "SET status = '" + advertisement1.getStatus() + "',  photo = '" + advertisement1.getPhoto() +
                "' WHERE id = " + advertisement1.getId() + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setAdvertisementBody(Advertisement advertisement1) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE advertisement " +
                "SET status = '" + advertisement1.getStatus() + "',  body = '" + advertisement1.getBody() +
                "' WHERE id = " + advertisement1.getId() + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setAdvertisementInlineName(Advertisement advertisement1) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE advertisement " +
                "SET status = '" + advertisement1.getStatus() + "',  inline_name = '" + advertisement1.getInlineName() +
                "' WHERE id = " + advertisement1.getId() + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setAdvertisementInlineUrl(Advertisement advertisement1) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE advertisement " +
                "SET status = '" + advertisement1.getStatus() + "',  inline_url = '" + advertisement1.getInlineUrl() +
                "' WHERE id = " + advertisement1.getId() + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addLikedProduct(Liked liked) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String addAdvertisement = "INSERT INTO liked (id,user_id,product_id) " +
                " VALUES(" + liked.getId() + ", " + liked.getUserId() + "," + liked.getProductId() + ")";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(addAdvertisement));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setUserPhoneNumber(Long id, String phoneNumber) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE users SET phone_number = '" + phoneNumber + "' WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setUserFullName(Long id, String fullName) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE users SET fullname = '" + fullName + "' WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setUserLanguage(Long id, Language language) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE users SET language = '" + language.name() + "' WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setLikedPotho(Long id, Boolean isDeleted) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE liked SET is_deleted = " + isDeleted + " WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setProductPotho(Long id, String fileId) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE product SET file_id = '" + fileId + "' WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setProductPhoneNumber(Long id, String contactProduct) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE product SET contact_product = '" + contactProduct + "' WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void addLocatsion(Location location) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String addAdvertisement = "INSERT INTO location (id,lang,late) " +
                " VALUES(" + location.getId() + ", " + location.getLang() + "," + location.getLate() + ")";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(addAdvertisement));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setProductLocation(Long id, Integer id1) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE product SET location_id = " + id1 + " WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setProductText(Long id, String text) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE product SET text = '" + text + "' WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void addProduct(Product product) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String addAdvertisement = "INSERT INTO product (id,user_id,category_id, status) " +
                " VALUES(" + product.getId() + ", " + product.getUserId() + "," + product.getCategoryId() + ",'NEW')";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(addAdvertisement));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setProductDeleted(Long id, Boolean isDeleted) {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String setStatus = "UPDATE product SET is_deleted = " + isDeleted + " WHERE id = " + id + ";";

        try (Statement statement = connection.createStatement()) {

            System.out.println(statement.executeUpdate(setStatus));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
