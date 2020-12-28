package limy.sb;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;

public class PostUpdate extends HttpServlet{

	Connection con;
	PreparedStatement pstmt1, pstmt2;


	public void init(){ 
	    String url = "jdbc:oracle:thin:@127.0.0.1:1521:JAVA";
		String sql1 = "select * from BOARD where SEQ=?";
		String sql2 = "update BOARD set EMAIL=?, SUBJECT=?, CONTENT=? where SEQ=?";
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, "servlet", "java");
			pstmt1 = con.prepareStatement(sql1);
			pstmt2 = con.prepareStatement(sql2);
		}catch(ClassNotFoundException cnfe){
		}catch(SQLException se){ }
	}

	public void service(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String seqStr = req.getParameter("seq");
		String getEmail = req.getParameter("email");
		String getSubject = req.getParameter("subject");
		String getContent = req.getParameter("content");
		int seq = -1;
		if(seqStr != null){
			seqStr = seqStr.trim();
			if(seqStr.length() != 0){
				try{
					seq = Integer.parseInt(seqStr);
				}catch(NumberFormatException ne){ }
			}
		}
		if(getEmail != null){
			seqStr = seqStr.trim();
			getEmail = getEmail.trim();
			getSubject = getSubject.trim();
			getContent = getContent.trim();
			try{
				seq = Integer.parseInt(seqStr);
				pstmt2.setString(1,getEmail);
				pstmt2.setString(2,getSubject);
				pstmt2.setString(3,getContent);
				pstmt2.setInt(4,seq);
				pstmt2.executeUpdate();
				res.sendRedirect("b_list.do");
			}catch(NumberFormatException ne){
			}catch(SQLException se){ }
		}else{
			res.setContentType("text/html;charset=utf-8");
			PrintWriter pw = res.getWriter();
			ResultSet rs = null;
			pw.println("<meta charset='utf-8'>");
			try{
				pstmt1.setInt(1, seq);
				rs = pstmt1.executeQuery();
				boolean flag = false;
				while(rs.next()){
					flag = true;
					String writer = rs.getString(2);
					String email = rs.getString(3);
					String subject = rs.getString(4);
					String content = rs.getString(5);
					Date rdate = rs.getDate(6);
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
						pw.println("<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js'></script>");
						pw.println("<script>");
							pw.println("function f(){");
								pw.println("input.email.value = '';");
								pw.println("input.subject.value = '';");
								pw.println("$('#ta').text('');");
								pw.println("input.email.focus();");
							pw.println("}");
						pw.println("</script>");
					pw.println("</head>");
					pw.println("<body>");
					pw.println("<center>");
					pw.println("<font color='gray' size='4' face='휴먼편지체'>");
						pw.println("<hr width='600' size='2' noshade>");
						pw.println("<h2>Simple Board with Servlet</h2>");
						pw.println("&nbsp;&nbsp;&nbsp;");
						pw.println("<a href='b_list.do'>글목록</a>");
						pw.println("<hr width='600' size='2' noshade>");
						pw.println("<form name='f' method='post' action='b_update.do'>");
							pw.println("<input type='hidden' name='seq' value='"+seq+"'>");
							pw.println("<input type='hidden' name='writer' value='"+writer+"'>");
							pw.println("<table border='1' width='600' align='center' cellpadding='3' cellspacing='1'>");
								pw.println("<tr>");
									pw.println("<td width='30%' align='center'>글쓴이</td>");
									pw.println("<td align='center'><input type='text' name='aa' size='60' value='"+writer+"' disabled></td>");
								pw.println("</tr>");
								pw.println("<tr>");
									pw.println("<td width='30%' align='center'>이메일</td>");
									pw.println("<td align='center'><input type='text' name='email' size='60' value='"+email+"'></td>");
								pw.println("</tr>");
								pw.println("<tr>");
									pw.println("<td width='30%' align='center'>글제목</td>");
									pw.println("<td align='center'><input type='text' name='subject' size='60' value='"+subject+"'></td>");
								pw.println("</tr>");
								pw.println("<tr>");
									pw.println("<td width='30%' align='center'>글내용</td>");
									pw.println("<td align='center'><textarea name='content' rows='5' cols='53'>"+content+"</textarea></td>");
								pw.println("</tr>");
								pw.println("<tr>");
									pw.println("<td colspan='2' align='center'>");
										pw.println("<input type='submit' value='수정'>");
									pw.println("</td>");
								pw.println("</tr>");
								pw.println("</table>");
						pw.println("</form>");
						pw.println("<hr width='600' size='2' noshade>");
							pw.println("<b>");
								pw.println("<a href='b_update.do?seq="+seq+"'>수정</a>");
								pw.println("<a href='b_del.do?seq="+seq+"'>삭제</a>");
								pw.println("<a href='b_list.do'>목록</a>");
							pw.println("</b>");
						pw.println("<hr width='600' size='2' noshade>");
					pw.println("</center>");
				}
			}catch(SQLException se){ }
		}
	}

	public void destroy(){
		try{
			if(pstmt1 != null) pstmt1.close();
			if(pstmt2 != null) pstmt2.close();
			if(con != null) con.close();
		}catch(SQLException se){ }
	}
}