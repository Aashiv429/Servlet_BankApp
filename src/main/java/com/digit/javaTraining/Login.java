package com.digit.javaTraining;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/Login")
public class Login extends HttpServlet{
	private Connection con;
	private PreparedStatement pstmt;

	private ResultSet resultSet;

	protected void service(HttpServletRequest req, HttpServletResponse resp) {
		int cust_id = Integer.parseInt(req.getParameter("cust_id"));
		 int pin = Integer.parseInt(req.getParameter("pin"));
		 String url = "jdbc:mysql://localhost:3306/bank";

	        String user = "root";

	        String pwd = "Minion@29";
HttpSession session = req.getSession(true);
	        

	        //Database connection

	        try {

	            Class.forName("com.mysql.cj.jdbc.Driver");

	            con = DriverManager.getConnection(url, user, pwd);
	            pstmt = con.prepareStatement("select * from bankapp where Cust_id =? and pin = ?");
	            pstmt.setInt(1, cust_id);
	            pstmt.setInt(2, pin);

	            resultSet = pstmt.executeQuery();
	            if(resultSet.next() == true)
	            {
	            	session.setAttribute("accno",resultSet.getInt(4));
	            	session.setAttribute("cust_name",resultSet.getString(7));
	            	resp.sendRedirect("/BankingApplication/homePage.jsp");
	            }
	            else {
	            	resp.sendRedirect("/BankingApplication/loginFail.html");
	            }
	        }
	            catch (Exception e) {

	                e.printStackTrace();

	            }

	            
	}
}
