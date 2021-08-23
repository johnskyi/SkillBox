import java.sql.*;

public class DBConnection {

    private static Connection connection;

    private static String dbName = "skillbox";
    private static String dbUser = "rootroot";
    private static String dbPass = "test";
    private static StringBuilder insertQuery = new StringBuilder();

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/" + dbName +
                                "?user=" + dbUser + "&password=" + dbPass);
                connection.createStatement().execute("DROP TABLE IF EXISTS voter_count");
                connection.createStatement().execute("CREATE TABLE voter_count(" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "name TINYTEXT NOT NULL, " +
                        "birthDate DATE NOT NULL, " +
                        "`count` INT NOT NULL, " +
                        "PRIMARY KEY(id)), " +
                        "UNIQUE KEY name_date(name(50),birthDate))," +
                        "KEY(name(50)))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void executeMultiInsert(String name, String birthDay) {
        String birthDate = birthDay.replace('.', '-');
        insertQuery.append((insertQuery.length() == 0 ? "" : ",") + "('" + name + "','" + birthDate + "',1)");
        if(insertQuery.length() > 100000)
        {
            executeMultiInsert(name,birthDay);
            insertQuery = new StringBuilder();
        }
        String sql = "INSERT INTO voter_count(name, birthDate, `count`) " +
                "VALUES" + insertQuery.toString() +
                "ON DUPLICATE KEY UPDATE 'count' = 'count' + 1";
        try {
            DBConnection.getConnection().createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void printVoterCounts() throws SQLException {
        String sql = "SELECT name, birthDate, `count` FROM voter_count WHERE `count` > 1";
        ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql);
        while (rs.next()) {
            System.out.println("\t" + rs.getString("name") + " (" +
                    rs.getString("birthDate") + ") - " + rs.getInt("count"));
        }
        rs.close();
    }
}
