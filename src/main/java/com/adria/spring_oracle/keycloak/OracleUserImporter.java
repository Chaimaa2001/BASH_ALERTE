package com.adria.spring_oracle.keycloak;

import com.adria.spring_oracle.entities.BackOffice;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class OracleUserImporter {

    private static final String JDBC_URL = "jdbc:oracle:thin:@//localhost:1521/orclpdb";
    private static final String JDBC_USER = "chichi";
    private static final String JDBC_PASSWORD = "orcl";

    public List<BackOffice> getUsers() {
        List<BackOffice> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT user_name, password_encrepted FROM utilisateur WHERE client_type='BACK_OFFICE'";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String username = resultSet.getString("user_name");
                    String password = resultSet.getString("password_encrepted");
                    BackOffice user = new BackOffice(username, password);
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
