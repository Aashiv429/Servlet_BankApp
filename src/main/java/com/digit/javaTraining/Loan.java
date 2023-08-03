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
@WebServlet("/Loan")
public class Loan extends HttpServlet{
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet resultSet;

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = "jdbc:mysql://localhost:3306/bank";
		String user = "root";
		String pwd = "Minion@29";
		HttpSession session = req.getSession(true);

		//int loan_id = (int)session.getAttribute("choice");
		int loan_id = Integer.parseInt(req.getParameter("choice"));
		// Database connection

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");

			con = DriverManager.getConnection(url, user, pwd);
			pstmt = con.prepareStatement("select * from loan where l_id =? ");
			pstmt.setInt(1, loan_id);

			resultSet = pstmt.executeQuery();
			if (resultSet.next() == true) {
				session.setAttribute("l_id", resultSet.getInt(1));
				session.setAttribute("l_type", resultSet.getString(2));
				session.setAttribute("tenure", resultSet.getInt(3));
				session.setAttribute("interest", resultSet.getFloat(4));
				session.setAttribute("description", resultSet.getString(5));
				resp.sendRedirect("/BankingApplication/loanDetails.jsp");
			} else {
				resp.sendRedirect("/BankingApplication/loanFail.html");
			}
		} catch (Exception e) {

			e.printStackTrace();

		}
}
}
