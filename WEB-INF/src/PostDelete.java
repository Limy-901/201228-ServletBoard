package limy.sb;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;

public class PostDelete extends HttpServlet {

	Connection con;
	PreparedStatement pstmt;


	public void init(){ 
	    String url = "jdbc:oracle:thin:@127.0.0.1:1521:JAVA";
		String sql = "delete from BOARD where SEQ=?";
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, "servlet", "java");
			pstmt = con.prepareStatement(sql);
		}catch(ClassNotFoundException cnfe){
		}catch(SQLException se){
		}
	}


	public void service(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException { 
		req.setCharacterEncoding("utf-8");
		String seqStr = req.getParameter("seq");
		int seq = -1;
		if(seqStr != null){
			seqStr = seqStr.trim();
			if(seqStr.length() != 0){
				try{
					seq = Integer.parseInt(seqStr);
					pstmt.setInt(1, seq);
					pstmt.executeUpdate();
					System.out.println("Delete Success!");
				}catch(NumberFormatException ne){
				}catch(SQLException se){ }
			}
		}
		res.sendRedirect("b_list.do");
	}
	public void destroy(){
		try{
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
		}catch(SQLException se){ }
	}


}
