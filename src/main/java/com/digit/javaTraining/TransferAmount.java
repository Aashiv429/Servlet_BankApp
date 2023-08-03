package com.digit.javaTraining;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/TransferAmount")
public class TransferAmount extends HttpServlet {
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet res1;
	private ResultSet res2;
	private ResultSet res3;

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = "jdbc:mysql://localhost:3306/bank";
		String user = "root";
		String pwd = "Minion@29";
		HttpSession session = req.getSession(true);

		// sender's details

		int sen_accno = Integer.parseInt(req.getParameter("s_accno"));
		int c_id = Integer.parseInt(req.getParameter("cust_id"));
		String b_name = req.getParameter("bank_name");
		String s_ifsc = req.getParameter("ifsc_code");
		int pin = Integer.parseInt(req.getParameter("pin"));
		int amt = Integer.parseInt(req.getParameter("amount"));

		// receiver's details

		int rec_accno = Integer.parseInt(req.getParameter("r_accNo"));
		String r_ifsc = req.getParameter("r_ifsc");
		
		int t_id = new Random().nextInt(100000)+300000;

		// Database connection

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, pwd);
			// validate sender's details
			pstmt = con.prepareStatement("select * from bankapp where cust_id =? and ifsc_code = ? and accno =? and pin=?");
			pstmt.setInt(1, c_id);
			pstmt.setString(2, s_ifsc);
			pstmt.setInt(3, sen_accno);
			pstmt.setInt(4, pin);
			res1 = pstmt.executeQuery();

			// validate receiver's details
			if (res1.next() == true) {
				pstmt = con.prepareStatement("select * from bankapp where  ifsc_code = ? and accno =? ");
				pstmt.setString(1, r_ifsc);
				pstmt.setInt(2, rec_accno);
				res2 = pstmt.executeQuery();

				// check for sufficient balance
				int bal = res1.getInt(8);
					// debit amount
					if (bal > amt) {
						pstmt = con.prepareStatement("update bankapp set balance = balance-? where accno=?");
						pstmt.setInt(1, amt);
						pstmt.setInt(2, sen_accno);
						int x1 = pstmt.executeUpdate();

						if (x1 > 0) {
							pstmt = con.prepareStatement("update bankapp set balance = balance + ? where accno=? ");
							pstmt.setInt(1, amt);
							pstmt.setInt(2, rec_accno);
							int x2 = pstmt.executeUpdate();
							if (x2 > 0) {
								pstmt = con.prepareStatement("insert into transferStatus values(?,?,?,?,?,?,?,?)");
								pstmt.setInt(1, c_id);
								pstmt.setString(2, b_name);
								pstmt.setString(3, s_ifsc);
								pstmt.setInt(4, sen_accno);
								pstmt.setString(5, r_ifsc);
								pstmt.setInt(6, rec_accno);
								pstmt.setInt(7, amt);
								pstmt.setInt(8, t_id);
								int x3 = pstmt.executeUpdate();
								if (x3 > 0) {
									resp.sendRedirect("/BankingApplication/transferSuccess.html");
								} else {
									resp.sendRedirect("/BankingApplication/transferFail.html");
								}
							} else {
								resp.sendRedirect("/BankingApplication/transferFail.html");
							}
						} else {
							resp.sendRedirect("/BankingApplication/transferFail.html");
						}
					} else {
						resp.sendRedirect("/BankingApplication/transferFail.html");
					}
				} else {
					resp.sendRedirect("/BankingApplication/transferFail.html");
				}
		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}
