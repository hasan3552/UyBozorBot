package com.company.db;


import com.company.enums.Language;
import com.company.enums.ProductStatus;
import com.company.enums.Role;
import com.company.enums.Status;
import com.company.model.*;

import java.sql.*;
import java.util.Optional;

public class DbConnection {

    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASSWORD = "hasan";
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/home_market";
    public static Connection connection;

    public static void readFromDatabase() {

        String readFromUsers = "SELECT * FROM users ORDER BY id";
        String readFromCategory = "SELECT * FROM category ORDER BY id";
        String readFromLocation = "SELECT * FROM location ORDER BY id";
        String readFromProduct = "SELECT * FROM product ORDER  BY id";
        String readFromLiked = "SELECT * FROM liked ORDER  BY id";

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
                Optional<Status> optional = Database.statuses.stream()
                        .filter(status1 -> status1.name().equals(status))
                        .findAny();

                String language = resultSet3.getString("language");
                Optional<Language> optional1 = Database.LANGUAGES.stream()
                        .filter(language1 -> language1.name().equals(language))
                        .findAny();

                String role = resultSet3.getString("role");
                Optional<Role> optional2 = Database.ROLES.stream()
                        .filter(role1 -> role1.name().equals(role))
                        .findAny();

                boolean isBlocked = resultSet3.getBoolean("is_blocked");

                User user = new User(id, username, fullName, phoneNumber, optional.get(),
                        optional1.get(), optional2.get(), isBlocked);
                Database.customers.add(user);

            }

            ResultSet resultSet2 = statement.executeQuery(readFromCategory);

            while (resultSet2.next()) {

                Integer id = resultSet2.getInt("id");
                String nameUz = resultSet2.getString("name_uz");
                String nameRu = resultSet2.getString("name_ru");
                Integer categoryId = resultSet2.getInt("category_id");
                Boolean isDeleted = resultSet2.getBoolean("is_deleted");

                Category category = new Category(id, nameUz, nameRu, categoryId, isDeleted);
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

                ProductStatus status1 = Database.productStatuses.stream()
                        .filter(productStatus -> productStatus.name().equals(status))
                        .findAny().get();

                Product product = new Product(id, userId, categoryId, text, locationId
                        , contactProduct, when.toLocalDateTime(), fileId, isDeleted, isSending,status1);
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

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
