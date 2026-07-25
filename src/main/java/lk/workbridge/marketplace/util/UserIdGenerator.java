package lk.workbridge.marketplace.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserIdGenerator implements IdentifierGenerator {
    private static final String PREFIX = "workbridge#";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String query = "SELECT MAX(CAST(SUBSTRING(`user-id`, LENGTH('" + PREFIX + "') + 1) AS UNSIGNED)) FROM users";

        try (Connection connection = session.getJdbcConnectionAccess().obtainConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                int maxId = resultSet.getInt(1);
                int newId = maxId + 1;
                return PREFIX + String.format("%03d", newId);
            }
        } catch (Exception e) {
            e.printStackTrace();

            return PREFIX + "001";
        }
        return PREFIX + "001";
    }
}
