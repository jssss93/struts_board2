package board;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.opensymphony.xwork2.ActionSupport;

public class viewAction extends ActionSupport {
	public static Reader reader;
	public static SqlMapClient sqlMapper;

	private boardVO paramClass = new boardVO();
	private boardVO resultClass = new boardVO();

	private int currentPage;
	private int no;
	private String password;
	private String fileUploadPath = "C:\\upload\\";

	private InputStream inputStream;
	private String contentDisposition;
	private long contentLength;

	// 코멘트 리스트 띄우기 위한 선언

	
	private List<cboardVO> list_c = new ArrayList<cboardVO>();
	
	//코멘트 리스트 서치를 위한 기준?
	private int originno;
	
	//코멘트리스트 페이징선언
	
	private int currentPage_c = 1; // 현재 페이지
	private int totalCount_c; // 총 게시물의 수
	private int blockCount_c = 10; // 한 페이지의 게시물의 수
	private int blockPage_c = 5; // 한 화면에 보여줄 페이지 수
//	private String pagingHtml_c; // 페이징을 구현한 HTML  >>필요없고
	private pagingAction page_c; // 페이징 클래스
	
	
	// 기본생성자
	public viewAction() throws IOException {
		reader = Resources.getResourceAsReader("sqlMapConfig.xml");
		sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader);
		reader.close();
	}

	// execute 해서 상세보기
	public String execute() throws Exception {
		// 조회수 ++
		paramClass.setNo(getNo());
		//updateReadHit 문으로 조회수 올려주고
		sqlMapper.update("updateReadHit", paramClass);
		// 글가져오고
		resultClass = (boardVO) sqlMapper.queryForObject("selectOne", getNo());

		
		
		//여기서부터 코멘트 관련 ------------------------------------------------------------
		
		
		// 코멘트 리스트 띄우기
		
		//쿼리의 조건 기준이되는 boardc_cjs의 originno 을 sboard_cjs 의 no 로 겟셋으로 데이터 설정해주고
//		paramClass_c.setOriginno(paramClass.getNo()); >>필요없어?
		
		// 글가져오고  >>>>>>>>>>여기서 자꾸 에러뜨는데 
		// 이유를 생각해보면 
		//1. list>arraylist 형태를 받지못해서 ?
		//2. 쿼리 틀려서?
		//3. 매핑과정 틀려서 ? 이건 ㄴㄴ
		//4 . 
		
		list_c  =sqlMapper.queryForList("select_comment", getNo());
		
		
		/* 코멘트 페이징 
		 * totalCount_c = list_c.size(); // 전체 글 갯수를 구한다.
		// pagingAction 객체 생성.
		page_c = new pagingAction(currentPage_c, totalCount_c, blockCount_c, blockPage_c);
//		pagingHtml_c = page_c.getPagingHtml().toString(); // 페이지 HTML 생성.  >>>필요없고

		// 현재 페이지에서 보여줄 마지막 글의 번호 설정.
		int lastCount_c = totalCount_c;

		// 현재 페이지의 마지막 글의 번호가 전체의 마지막 글 번호보다 작으면
		// lastCount를 +1 번호로 설정.
		if (page_c.getEndCount() < totalCount_c)
			lastCount_c = page_c.getEndCount() + 1;

		// 전체 리스트에서 현재 페이지만큼의 리스트만 가져온다.
		list_c = list_c.subList(page_c.getStartCount(), lastCount_c);
		*/
		return SUCCESS;
	}
		// 코멘트 끝 ----------------------------------------------------------------------

	// 첨부파일 다운로드
	public String download() throws Exception {
		// 해당번호 파일 정보를 가져온다
		resultClass = (boardVO) sqlMapper.queryForObject("selectOne", getNo());

		// 파일 경로와 파일명을 file 객체에 넣음
		File fileInfo = new File(fileUploadPath + resultClass.getFile_savname());

		// 다운로드 파일 정보 설정
		setContentLength(fileInfo.length());
		setContentDisposition("attachment);filename=" + URLEncoder.encode(resultClass.getFile_orgname(), "UTF-8"));
		setInputStream(new FileInputStream(fileUploadPath + resultClass.getFile_savname()));

		return SUCCESS;

	}

	// checkForm~~~
	public String checkForm() throws Exception {
		return SUCCESS;
	}

	// 비번 validation 액션
	public String checkAction() throws Exception {

		// 비밀번호 입력값 파라미터 설정
		paramClass.setNo(getNo());
		paramClass.setPassword(getPassword());
		resultClass = (boardVO) sqlMapper.queryForObject("selectPassword", paramClass);
		if (resultClass == null)
			return ERROR;

		return SUCCESS;
	}



	public int getCurrentPage_c() {
		return currentPage_c;
	}

	public void setCurrentPage_c(int currentPage_c) {
		this.currentPage_c = currentPage_c;
	}

	public int getOriginno() {
		return originno;
	}

	public void setOriginno(int originno) {
		this.originno = originno;
	}

	public boardVO getParamClass() {
		return paramClass;
	}

	public void setParamClass(boardVO paramClass) {
		this.paramClass = paramClass;
	}

	public boardVO getResultClass() {
		return resultClass;
	}

	public void setResultClass(boardVO resultClass) {
		this.resultClass = resultClass;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
