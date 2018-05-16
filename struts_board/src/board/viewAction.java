package board;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URLEncoder;

import org.apache.jasper.tagplugins.jstl.core.Param;

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
		sqlMapper.update("updateReadHit", paramClass);
		// 글가져오고
		resultClass = (boardVO) sqlMapper.queryForObject("selectOne", getNo());

		return SUCCESS;
	}

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
