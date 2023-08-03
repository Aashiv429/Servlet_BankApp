package com.digit.javaTraining;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ChangePin")
public class ChangePin extends HttpServlet{
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet resultSet;

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = "jdbc:mysql://localhost:3306/bank";
		String user = "root";
		String pwd = "Minion@29";
		HttpSession session = req.getSession(true);
		int accno = (int)session.getAttribute("accno");
		int old_pin = Integer.parseInt(req.getParameter("oldpin"));
		int n_pin = Integer.parseInt(req.getParameter("newpin"));
		int con_pin = Integer.parseInt(req.getParameter("confirmpin"));
		
		
		
		// Database connection
		
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, pwd);
			if (n_pin == con_pin) {
			pstmt = con.prepareStatement("update bankapp set pin=? where accno =? and pin=?");
			pstmt.setInt(1, n_pin);
			pstmt.setInt(2, accno);
			pstmt.setInt(3, old_pin);

			int x = pstmt.executeUpdate();
				if(x>0) {
				resp.sendRedirect("/BankingApplication/pinSuccess.jsp");
			} else {
				resp.sendRedirect("/BankingApplication/pinFail.jsp");
			}
			}
			else {
				resp.sendRedirect("/BankingApplication/pinFail.jsp");
			}
		} catch (Exception e) {

			e.printStackTrace();

		}
		
	}
}
