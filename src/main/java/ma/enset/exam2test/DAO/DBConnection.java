package ma.enset.exam2test.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;

    // un block executer une seul fois au mement d'execution
    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/DB_ENTREPRISE","root","");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(){
        return connection;
    }


}
