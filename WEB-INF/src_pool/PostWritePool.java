package limy.sb.pool;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import soo.db.ConnectionPoolBean;

public class PostWritePool extends HttpServlet{

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
		String sql = "insert into BOARD values(BOARD_SEQ.nextval, ?, ?, ?, ?, SYSDATE)";
		ConnectionPoolBean pool = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		res.setContentType("text/html;charset=utf-8");
		PrintWriter pw = res.getWriter();
		req.setCharacterEncoding("utf-8");
		String writer = req.getParameter("writer");
		String email = req.getParameter("email");
		String subject = req.getParameter("subject");
		String content = req.getParameter("content");
		pw.println("<meta charset='utf-8'>");
		pw.println("<title>MVC Board</title>");
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
		pw.println("<script language='javascript'>");
		   pw.println("function check()");
		   pw.println("{");
			   pw.println("for(var i=0; i<document.input.elements.length; i++)");
			   pw.println("{");
				  pw.println("if(document.input.elements[i].value == '')");
				  pw.println("{");
					 pw.println("alert('모든 값을 입력 하셔야 합니다. ');");
					 pw.println("return false;");
				  pw.println("}");
			   pw.println("}");
			   pw.println("document.input.submit();");
		   pw.println("}");
		pw.println("</script>");
		  pw.println("</head>");
		  pw.println("<body onload='input.writer.focus()'>");
			pw.println("<font color='gray' size='4' face='휴먼편지체'>");
			pw.println("<center>");
			   pw.println("<hr width='600' size='2' color='gray' noshade>");
				  pw.println("<h3> MVC Board </h3>");
					pw.println("<font color='gray' size='3' face='휴먼편지체'>");
					pw.println("<a href='list.do'>리스트</a>");
					pw.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					pw.println("<a href='./'>인덱스</a>");
					pw.println("</font>");
			   pw.println("<hr width='600' size='2' color='gray' noshade>");
			pw.println("</center>");
			pw.println("<form name='input' method='post' action='writing.do'>");
			   pw.println("<table border='0' width='500' align='center'  cellpadding='3' cellspacing='1' bordercolor='gray'>");
				  pw.println("<tr>");
					 pw.println("<td width='30%' align='center'>WRITER</td>");
					 pw.println("<td><input type='text' name='writer' size='67'></td>");
				  pw.println("</tr>");
				  pw.println("<tr>");
					 pw.println("<td align='center'>EMAIL</td>");
					 pw.println("<td><input type='text' name='email' size='67'></td>");
				  pw.println("</tr>");
				  pw.println("<tr>");
					 pw.println("<td align='center'>SUBJECT</td>");
					 pw.println("<td><input type='text' name='subject' size='67'></td>");
				  pw.println("</tr>");
				  pw.println("<tr>");
					 pw.println("<td align='center'>CONTENT</td>");
					 pw.println("<td><textarea  name='content' rows='15' cols='60'></textarea></td>");
				  pw.println("</tr>");
				  pw.println("<tr>");
					 pw.println("<td colspan='2' align='center'>");
						pw.println("<input type='button' value='전송' onclick='check()'>");
						pw.println("<input type='reset' value='다시입력' onclick='input.writer.focus()'>");
					 pw.println("</td>");
				  pw.println("</tr>");
			   pw.println("</table>");
			   pw.println("<hr width='600' size='2' color='gray' noshade>");
			pw.println("</form>");
			pw.println("</font>");
			if(writer != null){
				try{
					pool = getPool();
					con = pool.getConnection();
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1,writer);
					pstmt.setString(2,email);
					pstmt.setString(3,subject);
					pstmt.setString(4,content);
					pstmt.executeUpdate();
					res.sendRedirect("list.do");
				}catch(SQLException se){
				}finally{
					try{
					if(pstmt != null) pstmt.close();
					if(con != null) pool.returnConnection(con);
				}catch(SQLException sse){}
			}
		}
	}
}