
package my.finalproject;

import java.sql.*;

public class UserDAO {

public User login(String username, String password){

try{

Connection con = DBConnection.getConnection();

PreparedStatement ps =
con.prepareStatement(
"SELECT * FROM users WHERE username=? AND password=?"
);

ps.setString(1, username.trim());
ps.setString(2,Password.hashPassword(password.trim()));
ResultSet rs = ps.executeQuery();

if(rs.next()){

return new User(
rs.getInt("user_id"),
rs.getString("username").trim(),
rs.getString("password").trim(),
rs.getString("user_role").trim()
);

}

}catch(Exception e){
e.printStackTrace();
}

return null;
}

}