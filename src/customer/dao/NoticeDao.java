package customer.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import customer.db.DBCon;
import customer.vo.Notice;

public class NoticeDao {
	
	// 테이블의 모든 행(동그랑땡)을 가져오기 위해 list사용
	public List<Notice> noticeSelAll(String field,String query) throws Exception{
		
		// DB접속, 결과 조회
		Connection con=DBCon.getConnection();
		// String sql="SELECT * FROM notices ORDER BY to_number(seq) DESC";
		String sql="SELECT * FROM notices WHERE "+field+" like ? ORDER BY to_number(seq) DESC";
		
		// 실행
		// Statement st=con.createStatement();
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, "%"+query+"%");
		
		// 결과
		// ResultSet rs=st.executeQuery(sql); // sql의 결과값을 ResultSet에 담는다
		ResultSet rs=pstmt.executeQuery();
		
		// List에 Notice내용 담기
		List<Notice> list=new ArrayList<Notice>();
		
		while(rs.next()) {
			Notice n=new Notice();
			n.setSeq(rs.getString("seq")); // 앞에 seq는 Notice의 seq변수를 의미하고 뒤에 seq는 디비에 있는 seq값을 의미함(디비 내용을 가져와서 Notice에 넣어주는 것)
			n.setTitle(rs.getString("title"));
			n.setWriter(rs.getString("writer"));
			n.setContent(rs.getString("content"));
			n.setRegdate(rs.getDate("regdate"));
			n.setHit(rs.getInt("hit"));
			
			list.add(n);

		}
		rs.close();
		pstmt.close();
		con.close();
		
		return list;
	}
	
	public int delete(String seq) throws Exception{
		String sql="DELETE FROM notices WHERE seq=?";

		/*// DB connect
		Class.forName("oracle.jdbc.driver.OracleDriver");

		String url="jdbc:oracle:thin:@localhost:1521:xe";
		String user="hr";
		String pw="123456";
		Connection con=DriverManager.getConnection(url,user,pw);*/
		
		Connection con=DBCon.getConnection();

		// 실행
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, seq);
		int del=pstmt.executeUpdate(); // insert, delete, update (executeUpdate()는 sql에서 실행된 행의 갯수가 나오니까 int로 받을 수 있음)

		pstmt.close();
		con.close();
		
		return del;
	}

	public int write(Notice nc) throws Exception{
		String sql = "INSERT INTO notices VALUES(" + "(SELECT max(to_number(seq))+1 FROM notices)"
				+ ",?,'cj',sysdate,0,?)";

		/*// DB connect
		Class.forName("oracle.jdbc.driver.OracleDriver");

		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "hr";
		String pw = "123456";
		Connection con = DriverManager.getConnection(url, user, pw);*/
		
		Connection con=DBCon.getConnection();

		// 실행
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, nc.getTitle());
		pstmt.setString(2, nc.getContent());
		
		int af=pstmt.executeUpdate(); // insert 실행

		pstmt.close();
		con.close();
		
		return af;
	}

	// <방법1> 변수로 수정내용 받기
	public int update2(String s, String t, String c) throws Exception { // update가 몇 개 되었느냐 볼꺼라 int로 리턴
		String sql = "UPDATE notices" + " SET title=?, content=? WHERE seq=?"; // notices와 SET이 붙어서 쓰여지지 않게 앞에 한 칸 띄워줘야
																				// 됨

		/*// DB connect
		Class.forName("oracle.jdbc.driver.OracleDriver");

		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "hr";
		String pw = "123456";
		Connection con = DriverManager.getConnection(url, user, pw);*/
		
		Connection con=DBCon.getConnection();

		// 실행
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, t);
		pstmt.setString(2, c);
		pstmt.setString(3, s);

		int af = pstmt.executeUpdate();

		pstmt.close();
		con.close();

		return af; // 몇 개가 업데이트 됐는지 리턴
	}

	// <방법2> 객체로 수정내용받기 - 많은 데이터를 수정해야 할 경우 객체로 받는게 효율적임
	public int update(Notice notice) throws Exception { // update가 몇 개 되었느냐 볼꺼라 int로 리턴
		String sql = "UPDATE notices SET title=?, content=? WHERE seq=?"; // notices와 SET이 붙어서 쓰여지지 않게 앞에 한 칸 띄워줘야
																				// 됨

		/*// DB connectㄴ
		Class.forName("oracle.jdbc.driver.OracleDriver");

		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "hr";
		String pw = "123456";
		Connection con = DriverManager.getConnection(url, user, pw);*/
		
		Connection con=DBCon.getConnection();

		// 실행
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, notice.getTitle());
		pstmt.setString(2, notice.getContent());
		pstmt.setString(3, notice.getSeq());

		int af = pstmt.executeUpdate();

		pstmt.close();
		con.close();

		return af; // 몇 개가 업데이트 됐는지 리턴
	}

	public void hitupdate(String seq,int num) throws Exception {
		System.out.println("hit up");
		Connection con=DBCon.getConnection();
		String sql="update notices set hit=? where seq=?";
		
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, num+1);		
		pstmt.setString(2, seq);
		
		pstmt.executeUpdate();
	}
	
	public Notice getNotice(String seq) throws Exception { // 글번호에 맞는 Notice를 리턴받기	
		String sql = "SELECT * FROM notices WHERE seq=" + seq;
		Connection con=DBCon.getConnection();
		// 실행
		Statement st = con.createStatement();
		// 결과
		ResultSet rs = st.executeQuery(sql);
		rs.next();
		// Notice에 select 결과물 담아보기
		Notice n = new Notice();
		n.setSeq(rs.getString("seq")); // DB에가 가져온 rs의 seq를 가져와서 Notice에 담기
		n.setWriter(rs.getString("writer"));
		n.setTitle(rs.getString("title"));
		n.setContent(rs.getString("content"));
		n.setRegdate(rs.getDate("regdate"));
		n.setHit(rs.getInt("hit"));

		rs.close();
		st.close();
		con.close();

		return n; // n에 데이터를 다 입력받은 후 Notice타입인 n을 리턴
	}
	public Notice getNotice(String seq,String hit) throws Exception { // 글번호에 맞는 Notice를 리턴받기
		int hnum=Integer.parseInt(hit);
//		hit update 함수로 처리
		hitupdate(seq,hnum);
			
		String sql = "SELECT * FROM notices WHERE seq=" + seq;

		Connection con=DBCon.getConnection();
		// 실행
		Statement st = con.createStatement();
		// 결과
		ResultSet rs = st.executeQuery(sql);
		rs.next();

		// Notice에 select 결과물 담아보기
		Notice n = new Notice();
		n.setSeq(rs.getString("seq")); // DB에가 가져온 rs의 seq를 가져와서 Notice에 담기
		n.setWriter(rs.getString("writer"));
		n.setTitle(rs.getString("title"));
		n.setContent(rs.getString("content"));
		n.setRegdate(rs.getDate("regdate"));
		n.setHit(rs.getInt("hit"));

		rs.close();
		st.close();
		con.close();

		return n; // n에 데이터를 다 입력받은 후 Notice타입인 n을 리턴
	}
}
