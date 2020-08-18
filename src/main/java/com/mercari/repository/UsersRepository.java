package com.mercari.repository;

import java.util.List;

import com.mercari.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class UsersRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        return user;
    };

    /**
	 * 
	 * @param email
	 * @param password
	 * @return userList メールアドレスとパスワードからログイン
	 */
	public User findByMailAddressAndPassward(String email, String password) {
		String sql = "select * from users where email=:email and password=:password";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email).addValue("password", password);
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}

	/**
	 * 登録画面に名前、メールアドレス、パスワード、郵便番号、住所、電話番号をインサート
	 * 
	 * @param user
	 */
	public void insert(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String insertSql = "INSERT INTO users(name,email,password) VALUES(:name,:email,:password)";
		template.update(insertSql, param);
	}

	/**
	 * メールアドレスの重複確認
	 * 
	 * @param email
	 * @return
	 */
	public User findByMailAddress(String email) {
		try{
			String sql = "select * from users where email = :email";
			SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
			List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
			if (userList.size() == 0) {
				return null;
			}
			return userList.get(0);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * マイページ編集画面で名前、メールアドレス、郵便番号、住所、電話番号を更新.
	 * 
	 * @param user
	 */
	public void update(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String updateSql = "UPDATE users SET name=:name, email=:email WHERE id=:id";
		template.update(updateSql, param);
	}
    
}