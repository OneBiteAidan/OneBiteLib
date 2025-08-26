package dev.onebiteaidan.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class SQLiteRepository<T, ID> implements Repository<T, ID> {
    protected final Connection connection;

    public SQLiteRepository(Connection connection) {
        this.connection = connection;
    }

    protected PreparedStatement prepare(String sql, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        return stmt;
    }
}