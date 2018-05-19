package board;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.opensymphony.xwork2.ActionSupport;

//스트러츠라는걸 잊지말고 액션서포트를 상속받아 사용
public class commentListAction extends ActionSupport {

	public static Reader reader; // 파일 스트림을 위한 reader.>>boardSQL.xml 에 설정해준 것들 읽어오기위해
	public static SqlMapClient sqlMapper; // SqlMapClient API를 사용하기 위한 sqlMapper 객체.

	// 널포인트 익셉션은 선언만하고 사용하지않았을경우 안의내용이안차서 오류뜨는거야

	private List<boardVO> list = new ArrayList<boardVO>();;

	private int currentPage = 1; // 현재 페이지
	private int totalCount; // 총 게시물의 수
	private int blockCount = 10; // 한 페이지의 게시물의 수
	private int blockPage = 5; // 한 화면에 보여줄 페이지 수
	private String pagingHtml; // 페이징을 구현한 HTML
	private pagingAction page; // 페이징 클래스

	// comment 달기위한 originNO 선언 >>> 쿼리 검색시 사용 originNo(게시판 리스트 글 번호) 에따라서 Comment
	// 달리니까
	private int originno;

	// // 검색을위한 객체 선언
	// private String Keyword;
	//
	// private String Opt;

	public int getOriginno() {
		return originno;
	}

	public void setOriginno(int originno) {
		this.originno = originno;
	}

	// 생성자 >>>boardSQL.xml 을 사용하기위해
	public commentListAction() throws IOException {

		reader = Resources.getResourceAsReader("sqlMapConfig.xml"); // sqlMapConfig.xml 파일의 설정내용을 가져온다.
		sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader); // sqlMapConfig.xml의 내용을 적용한 sqlMapper 객체 생성.
		reader.close();
	}

	// 게시판 LIST 액션
	public String execute() throws Exception {
		// 한글값도 검색되게끔 설정??한글설정 어케하니
		/* Keyword = new String(Keyword.getBytes("iso-8859-1"), "euc-kr"); */

		// // 검색옵션이 선택되지않으면
		// if (Opt == null) {
		// // 전체 리스트 출력
		list = sqlMapper.queryForList("search_comment");
		// 검색된것을 리스트에서 출력
		// } else if (Opt.equals("subject")) {
		// list = sqlMapper.queryForList("search_subject", '%' + getKeyword() + '%');
		// } else if (Opt.equals("content")) {
		// list = sqlMapper.queryForList("search_content", '%' + getKeyword() + '%');
		// }

		totalCount = list.size(); // 전체 글 갯수를 구한다.
		// pagingAction 객체 생성.
		page = new pagingAction(currentPage, totalCount, blockCount, blockPage);
		pagingHtml = page.getPagingHtml().toString(); // 페이지 HTML 생성.

		// 현재 페이지에서 보여줄 마지막 글의 번호 설정.
		int lastCount = totalCount;

		// 현재 페이지의 마지막 글의 번호가 전체의 마지막 글 번호보다 작으면
		// lastCount를 +1 번호로 설정.
		if (page.getEndCount() < totalCount)
			lastCount = page.getEndCount() + 1;

		// 전체 리스트에서 현재 페이지만큼의 리스트만 가져온다.
		list = list.subList(page.getStartCount(), lastCount);

		return SUCCESS;
	}

	// // 검색을위한 겟셋------------------------------------------------
	// public String getKeyword() {
	// return Keyword;
	// }
	//
	// public void setKeyword(String Keyword) {
	// this.Keyword = Keyword;
	// }
	//
	// public String getOpt() {
	// return Opt;
	// }
	//
	// public void setOpt(String Opt) {
	// this.Opt = Opt;
	// }
	// // -----------------------------------------------------------

	public List<boardVO> getList() {
		return list;
	}

	public void setList(List<boardVO> list) {
		this.list = list;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getBlockCount() {
		return blockCount;
	}

	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}

	public int getBlockPage() {
		return blockPage;
	}

	public void setBlockPage(int blockPage) {
		this.blockPage = blockPage;
	}

	public String getPagingHtml() {
		return pagingHtml;
	}

	public void setPagingHtml(String pagingHtml) {
		this.pagingHtml = pagingHtml;
	}

	public pagingAction getPage() {
		return page;
	}

	public void setPage(pagingAction page) {
		this.page = page;
	}
}
