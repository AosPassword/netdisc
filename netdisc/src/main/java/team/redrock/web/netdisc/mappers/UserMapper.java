package team.redrock.web.netdisc.mappers;

import org.apache.ibatis.annotations.*;
import team.redrock.web.netdisc.beens.User;

@Mapper
public interface UserMapper {
    @Select("select * from users where id = #{id}")
    public User findUserById(Integer id);

    @Insert("insert into users (username,password,email,identity) values (#{username},#{password},#{email},#{identity})")
    public void insertUser(User user);

    @Select("select * from users where username = #{username} or email = #{email}")
    public User findUserByUsernameOrEmail(User user);

    @Update("update users set password = #{password} where id =#{id}")
    public User UpdatePassword(Integer id, String password);

    @Select("select * from users where email = #{email} and password = #{password}")
    public User checkUser(@Param("email")String email,@Param("password") String password);

    @Select("select * from users where username = #{username}")
    public User findUserByUsername(String username);

}
