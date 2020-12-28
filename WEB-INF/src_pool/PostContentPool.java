package limy.sb.pool;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import soo.db.ConnectionPoolBean;

public class PostContentPool extends HttpServlet{
	//limy.sb.pool.PostContentPool
	public ConnectionPoolBean getPool(){
		try{
			ServletContext application = getServletContext();
			ConnectionPoolBean pool = (ConnectionPoolBean)application.getAttribute("pool"); //이 이름인 애가 없으면. 무대위에서 찾아보기.
			if(pool == null){
				pool = new ConnectionPoolBean(); //없어서 생성해줌.
				application.setAttribute("pool",pool); //생성할때 셋 어트리뷰트.
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
		String sql = "select * from BOARD where SEQ=?";
		res.setContentType("text/html;charset=utf-8");
		PrintWriter pw = res.getWriter();
		ConnectionPoolBean pool = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int seq = -1;
		String seqStr = req.getParameter("seq");
        if(seqStr != null){
			seqStr = seqStr.trim();
			if(seqStr.length() != 0){
				try{
					seq = Integer.parseInt(seqStr);
				}catch(NumberFormatException ne){ }
			}
		}
		try{
			pool = getPool();
			con = pool.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, seq);
			rs = pstmt.executeQuery();
			boolean flag = false;
			while(rs.next()){
				flag = true;
				String writer = rs.getString(2);
				String email = rs.getString(3);
				String subject = rs.getString(4);
				String content = rs.getString(5);
				Date rdate = rs.getDate(6);
				
				pw.println("<meta charset='utf-8'>");
				pw.println("<style>");
				pw.println("table, th, td {");
					pw.println("border: 1px solid black;");
					pw.println("border-collapse: collapse;");
				pw.println("}");
				pw.println("th, td {");
					pw.println("padding: 5px;");
					pw.println("}");
					pw.println("a { text-decoration:none }");
				pw.println("</style>");
				pw.println("<center>");
					pw.println("<hr width='600' size='2' noshade>");
					pw.println("<h2>Simple Board with Servlet</h2>");
					pw.println("&nbsp;&nbsp;&nbsp;");
					pw.println("<a href='writing.do'>글쓰기</a>");
					pw.println("<hr width='600' size='2' noshade>");
					pw.println("<table border='1' width='600' align='center' cellpadding='3'>");
						pw.println("<tr>");
							pw.println("<td width='100' align='center'>글번호</td>");
							pw.println("<td>"+seq+"</td>");
						pw.println("</tr>");
						pw.println("<tr>");
							pw.println("<td align='center'>글쓴이</td>");
							pw.println("<td>"+writer+"</td>");
						pw.println("</tr>");
						pw.println("<tr>");
							pw.println("<td align='center'>이메일</td>");
							pw.println("<td>"+email+"</td>");
						pw.println("</tr>");
						pw.println("<tr>");
							pw.println("<td align='center'>글제목</td>");
							pw.println("<td>"+subject+"</td>");
						pw.println("</tr>");
						pw.println("<tr>");
							pw.println("<td align='center'>글내용</td>");
							pw.println("<td>"+content+"</td>");
						pw.println("</tr>");
					pw.println("</table>");
					pw.println("<hr width='600' size='2' noshade>");
						pw.println("<b>");
							pw.println("<a href='update.do?seq="+seq+"'>수정</a>");
							pw.println("<a href='del.do?seq="+seq+"'>삭제</a>");
							pw.println("<a href='list.do'>목록</a>");
						pw.println("</b>");
					pw.println("<hr width='600' size='2' noshade>");
				pw.println("</center>");
			}
		}catch(SQLException se){
		}finally{
			try{
				if(pstmt != null) pstmt.close();
				if(con != null) pool.returnConnection(con);
			}catch(SQLException sse){}
		}
	}
}