package limy.sb.pool;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import soo.db.ConnectionPoolBean;
//limy.sb.pool.PostDeletePool

public class PostDeletePool extends HttpServlet {

	public ConnectionPoolBean getPool(){
		try{
			ServletContext application = getServletContext();
			ConnectionPoolBean pool = (ConnectionPoolBean)application.getAttribute("pool"); //�� �̸��� �ְ� ������. ���������� ã�ƺ���.
			if(pool == null){
				pool = new ConnectionPoolBean(); //��� ��������.
				application.setAttribute("pool",pool); //�����Ҷ� �� ��Ʈ����Ʈ.
			}
			return pool; 
		}catch(ClassNotFoundException cnfe){
			return null;
		}catch(SQLException se){
			return null;
		}
	}

	public void service(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException { 
		req.setCharacterEncoding("utf-8");
		ConnectionPoolBean pool = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "delete from BOARD where SEQ=?";

		String seqStr = req.getParameter("seq");
		int seq = -1;
		if(seqStr != null){
			seqStr = seqStr.trim();
			if(seqStr.length() != 0){
				try{
					pool = getPool();
					con = pool.getConnection();
					pstmt = con.prepareStatement(sql);
					seq = Integer.parseInt(seqStr);
					pstmt.setInt(1, seq);
					pstmt.executeUpdate();
					System.out.println("Delete Success!");
					res.sendRedirect("list.do");
				}catch(NumberFormatException ne){
				}catch(SQLException se){
				}finally{
					try{
						if(pstmt != null) pstmt.close();
						if(con != null) con.close();
					}catch(SQLException se){ }
				}
			}
		}

	}
}