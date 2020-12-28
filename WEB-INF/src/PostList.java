package limy.sb;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;

public class PostList extends HttpServlet{

	Connection con;
	Statement stmt;


	public void init(){
		try{
			String url = "jdbc:oracle:thin:@127.0.0.1:1521:JAVA";
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, "servlet", "java");
			stmt = con.createStatement();
		}catch(ClassNotFoundException cnfe){
		}catch(SQLException se){ }
	}

	public void service(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		res.setContentType("text/html;charset=utf-8");
		PrintWriter pw = res.getWriter();
		ResultSet rs = null;
		String sql = "select * from BOARD order by SEQ desc";
		try{
			rs = stmt.executeQuery(sql);
			boolean flag = false;
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
			pw.println("</head>");
			pw.println("<body>");
			pw.println("<center>");
			pw.println("<font color='gray' size='4' face='휴먼편지체'>");
			pw.println("<hr width='600' size='2' color='gray' noshade>");
			pw.println("<h3> MVC Board </h3>");
			pw.println("<font color='gray' size='4' face='휴먼편지체'>");
			pw.println("<a href='./'>인덱스</a>");
			pw.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			pw.println("<a href='write.do'>글쓰기</a><br/>");
			pw.println("</font>");
			pw.println("<hr width='600' size='2' color='gray' noshade>");
				pw.println("<TABLE border='2' width='600' align='center' noshade>");
				pw.println("<TR size='2' align='center' noshade bgcolor='AliceBlue'>");
					pw.println("<th bgcolor='AliceBlue'>번호</th>");
					pw.println("<th align='center' width='10%'>작성자</th>");
					pw.println("<th align='center' width='30%'>이메일</th>");
					pw.println("<th align='center' width='30%'>글제목</th>");
					pw.println("<th align='center' width='15%'>작성일</th>");
				pw.println("</TR>");
			while(rs.next()){
				flag = true;
				int seq = rs.getInt(1);
				String writer = rs.getString(2);
				String email = rs.getString(3);
				String subject = rs.getString(4);
				String content = rs.getString(5);
				Date rdate = rs.getDate(6);
				pw.println("<TR>");
					pw.println("<TD align='center'>"+seq+"</TD>");
					pw.println("<TD align='center'>"+writer+"</TD>");
					pw.println("<TD align='center'>"+email+"</TD>");
					pw.println("<TD align='center'><a href='b_content.do?seq="+seq+"'>"+subject+"</a></TD>");
					pw.println("<TD align='center'>"+rdate+"</TD>");
				pw.println("</TR>");
				if(!flag){
					pw.println("<tr>");
						pw.println("<td colspan='5' align='center'>게시글 없음</a>");
					pw.println("</tr>");
				}
			}
			pw.println("</TABLE>");
			pw.println("</center>");
		}catch(SQLException se){}
	}

	public void destroy(){
		try{
			if(stmt != null) stmt.close();
			if(con != null) con.close();
		}catch(SQLException se){
		}
	}
}
