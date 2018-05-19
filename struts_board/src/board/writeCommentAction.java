package board;

import java.io.IOException;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.opensymphony.xwork2.ActionSupport;

public class writeCommentAction extends ActionSupport {

	public static Reader reader;
	public static SqlMapClient sqlMapper;

	private cboardVO paramClass;
	private cboardVO resultClass;

	private int currentPage;

	private int no;
	private int originno;
	private String name;
	private String password;
	private String content;
	private Date regdate;

	Calendar today = Calendar.getInstance();

	boolean reply = false;

	public writeCommentAction() throws IOException {
		reader = Resources.getResourceAsReader("sqlMapConfig.xml");
		sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader);
		reader.close();

	}

	public String execute() throws Exception {

		paramClass = new cboardVO();
		resultClass = new cboardVO();

		paramClass.setNo(getNo());
		paramClass.setName(getName());
		paramClass.setPassword(getPassword());
		paramClass.setContent(getContent());
		paramClass.setRegdate(today.getTime());

		sqlMapper.insert("insertBoardComment", paramClass);

		return SUCCESS;
	}
	

	public cboardVO getParamClass() {
		return paramClass;
	}

	public void setParamClass(cboardVO paramClass) {
		this.paramClass = paramClass;
	}

	public cboardVO getResultClass() {
		return resultClass;
	}

	public void setResultClass(cboardVO resultClass) {
		this.resultClass = resultClass;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getOriginno() {
		return originno;
	}

	public void setOriginno(int originno) {
		this.originno = originno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

}
