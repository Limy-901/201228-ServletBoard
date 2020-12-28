package limy.sb.pool;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import soo.db.ConnectionPoolBean;

public class PostListPool extends HttpServlet{

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
		res.setContentType("text/html;charset=utf-8");
		PrintWriter pw = res.getWriter();
		ConnectionPoolBean pool = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "select * from BOARD order by SEQ desc";
		try{
			pool = getPool();
			con = pool.getConnection();
			stmt = con.createStatement();
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
			pw.println("<font color='gray' size='4' face='�޸�����ü'>");
			pw.println("<hr width='600' size='2' color='gray' noshade>");
			pw.println("<h3> MVC Board </h3>");
			pw.println("<font color='gray' size='4' face='�޸�����ü'>");
			pw.println("<a href='./'>�ε���</a>");
			pw.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			pw.println("<a href='writing.do'>�۾���</a><br/>");
			pw.println("</font>");
			pw.println("<hr width='600' size='2' color='gray' noshade>");
				pw.println("<TABLE border='2' width='600' align='center' noshade>");
				pw.println("<TR size='2' align='center' noshade bgcolor='AliceBlue'>");
					pw.println("<th bgcolor='AliceBlue'>��ȣ</th>");
					pw.println("<th align='center' width='10%'>�ۼ���</th>");
					pw.println("<th align='center' width='30%'>�̸���</th>");
					pw.println("<th align='center' width='30%'>������</th>");
					pw.println("<th align='center' width='15%'>�ۼ���</th>");
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
					pw.println("<TD align='center'><a href='content.do?seq="+seq+"'>"+subject+"</a></TD>");
					pw.println("<TD align='center'>"+rdate+"</TD>");
				pw.println("</TR>");
				if(!flag){
					pw.println("<tr>");
						pw.println("<td colspan='5' align='center'>�Խñ� ����</a>");
					pw.println("</tr>");
				}
			}
			pw.println("</TABLE>");
			pw.println("</center>");
		}catch(SQLException se){
		}finally{
			try{
				if(stmt != null) stmt.close();
				if(con != null) pool.returnConnection(con);
			}catch(SQLException se){ }
		}
	}
}
//limy.sb.pool.PostListPool