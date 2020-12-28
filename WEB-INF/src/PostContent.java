package limy.sb;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;

public class PostContent extends HttpServlet{

	Connection con;
	PreparedStatement pstmt;
	public void init(){ 
	    String url = "jdbc:oracle:thin:@127.0.0.1:1521:JAVA";
		String sql = "select * from BOARD where SEQ=?";
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, "servlet", "java");
			pstmt = con.prepareStatement(sql);
		}catch(ClassNotFoundException cnfe){
		}catch(SQLException se){ }
	}

	public void service(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException { 
		res.setContentType("text/html;charset=utf-8");
		PrintWriter pw = res.getWriter();
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
					pw.println("<a href='write.do'>글쓰기</a>");
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
							pw.println("<a href='b_update.do?seq="+seq+"'>수정</a>");
							pw.println("<a href='b_del.do?seq="+seq+"'>삭제</a>");
							pw.println("<a href='b_list.do'>목록</a>");
						pw.println("</b>");
					pw.println("<hr width='600' size='2' noshade>");
				pw.println("</center>");
			}
		}catch(SQLException se){
			try{
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			}catch(SQLException sse){}
		}
	}
}