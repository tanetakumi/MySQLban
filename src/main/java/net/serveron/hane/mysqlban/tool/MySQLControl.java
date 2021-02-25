package net.serveron.hane.mysqlban.tool;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class MySQLControl {

    //SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
    private Connection connection;

    public static String name = "Unknown";
    //public static Date logout;
    public static int offset = 0;
    private String host, database, username, password;
    private int port;
    /**
     * ライブラリー読込時の初期設定
     *
     */
    public MySQLControl() {
        host = "localhost";
        port = 3306;
        database = "test_database";
        username = "mc";
        password = "tanem81418";
    }

    private void openConnection() throws SQLException, ClassNotFoundException {



        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized ( this ) {
            if ( connection != null && !connection.isClosed() ) {
                return;
            }
            Class.forName( "com.mysql.jdbc.Driver" );
            connection = DriverManager.getConnection( "jdbc:mysql://" + host + ":" + port + "/" + database, username, password );

            //  テーブルの作成
            //		uuid : varchar(36)	player uuid
            //		name : varchar(20)	player name
            //		logiut : DATETIME	last Logout Date
            //		offset : int 		total Login Time
            //  存在すれば、無視される
            String sql = "CREATE TABLE IF NOT EXISTS player( uuid varchar(36), name varchar(20), logout DATETIME, offset int )";
            PreparedStatement preparedStatement = connection.prepareStatement( sql );
            preparedStatement.executeUpdate();
        }
    }

    /**
     * UUIDからプレイヤー情報を取得する
     *
     * @param uuid
     * @return
     */
    public boolean GetSQL( UUID uuid ) {
        try {
            openConnection();
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM player WHERE uuid = '" + uuid.toString() + "';";
            ResultSet rs = stmt.executeQuery( sql );
            if ( rs.next() ) {
                this.name = rs.getString( "name" );
                this.logout = rs.getDate( "logout" );
                this.offset = rs.getInt( "offset" );
                Tools.Prt( "Get Data from SQL Success.", Tools.consoleMode.full , programCode );
                return true;
            }
        } catch ( ClassNotFoundException | SQLException e ) {
            Tools.Prt( "Error GetPlayer", programCode );
        }
        return false;
    }
}
