package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	private void getConnection() {
		try {
			// 1. JDBC 드라이버(Oracle) 로딩
			Class.forName(driver); // 오라클 접속
			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버로딩실패-" + e);
		}

	}

	private void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 회원가입 insert

	public int insert(UserVo userVo) {

		int count = 0;
		getConnection();

		// SQL 문 준비

		try {
			String query = "";
			query += " insert into users";
			query += " values(seq_users_no.nextval, ?, ?, ?, ?)";
			System.out.println(query);

			// 바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userVo.getId());
			pstmt.setString(2, userVo.getPassword());
			pstmt.setString(3, userVo.getName());
			pstmt.setString(4, userVo.getGender());

			count = pstmt.executeUpdate();
			
			
			// 실행***
			// insert 가져오기
			System.out.println(count + "건이 등록되었습니다.");
			// 4.결과처리

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return count;
	}
	
	public UserVo getUser(UserVo userVo) {
		
		UserVo authUser = null;
		
		getConnection();
		
		try {
			String query = "";
			query += " select  no, ";
			query += " 		   id, ";
			query += " 		   password, ";
			query += " 		   name, ";
			query += " 		   gender ";
			query += " from users ";
			query += " where id = ? ";
			query += " and password = ? ";
			System.out.println(query);
			
			// 바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,userVo.getId());
			pstmt.setString(2,userVo.getPassword());
				
			// 실행***
			rs = pstmt.executeQuery();

			// 4.결과처리
			
			while(rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				
				authUser = new UserVo();
				authUser.setNo(no);
				authUser.setName(name);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();

		return authUser;
	}
	
	public int update(UserVo userVo) {
		int count = 0;
		getConnection();
		
		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " update users ";
			query += " set name = ?, ";
			query += "     password = ?, ";
			query += "     gender = ? ";
			query += " where id = ? ";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, userVo.getName()); 
			pstmt.setString(2, userVo.getPassword());
			pstmt.setString(3, userVo.getGender()); 
			pstmt.setString(4, userVo.getId()); 

			count = pstmt.executeUpdate(); // 쿼리문 실행

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
		
		
	}
	
}
