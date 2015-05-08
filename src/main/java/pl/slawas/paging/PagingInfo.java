/**
 * 
 */
package pl.slawas.paging;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.cxf.aegis.type.java5.IgnoreProperty;

/**
 * PagingInfo obiekt transportowy informacji o stronach rezultatu
 * 
 * @author Karol Kowalczyk
 */
@SuppressWarnings("serial")
public class PagingInfo implements _IPagingInfo {

	public PagingInfo() {
	}

	public PagingInfo(final _IPagingInfo pagingInfo) {
		currentPageInfo = pagingInfo.getCurrentPageInfo();
		firstPageInfo = pagingInfo.getFirstPageInfo();
		try {
			previousPageInfo = pagingInfo.getPreviousPageInfo();
		} catch (PagedResultException ex) {
		}
		try {
			nextPageInfo = pagingInfo.getNextPageInfo();
		} catch (PagedResultException ex) {
		}
		lastPageInfo = pagingInfo.getLastPageInfo();
		message = pagingInfo.getMessage();
		hasMoreResultRows = pagingInfo.hasMoreResultRows();
		hasPreviousResultRows = pagingInfo.hasPreviousResultRows();
		lastRowPosition = pagingInfo.getLastRowPosition();
		firstRowPositionOfPreviousResultRows = pagingInfo
				.getFirstRowPositionOfPreviousResultRows();
		pageSize = pagingInfo.getPageSize();
		numberOfRowsOnThePage = pagingInfo.getNumberOfRowsOnThePage();
		pageNr = pagingInfo.getPageNr();
		pagingParams = pagingInfo.getPagingParams();
		resultSize = pagingInfo.getResultSize();
		setPages(Collections.list(pagingInfo.getAllPages()));
	}

	@Override
	public Page getCurrentPageInfo() {
		return currentPageInfo;
	}

	public void setCurrentPageInfo(Page currentPageInfo) {
		this.currentPageInfo = currentPageInfo;
	}

	private Page currentPageInfo;

	@Override
	public Page getFirstPageInfo() {
		return firstPageInfo;
	}

	public void setFirstPageInfo(Page firstPageInfo) {
		this.firstPageInfo = firstPageInfo;
	}

	private Page firstPageInfo;

	@Override
	public Page getPreviousPageInfo() throws PagedResultException {
		return previousPageInfo;
	}

	public void setPreviousPageInfo(Page previousPageInfo) {
		this.previousPageInfo = previousPageInfo;
	}

	private Page previousPageInfo;

	@Override
	public Page getNextPageInfo() throws PagedResultException {
		return nextPageInfo;
	}

	public void setNextPageInfo(Page nextPageInfo) {
		this.nextPageInfo = nextPageInfo;
	}

	private Page nextPageInfo;

	@Override
	public Page getPageInfo(Integer pageNumber) throws PagedResultException {
		try {
			return pages.get(pageNumber.intValue());
		} catch (Exception ex) {
			throw new PagedResultException(ex);
		}
	}

	@Override
	public Page getLastPageInfo() {
		return lastPageInfo;
	}

	public void setLastPageInfo(Page lastPageInfo) {
		this.lastPageInfo = lastPageInfo;
	}

	private Page lastPageInfo;

	@Override
	@IgnoreProperty
	public Enumeration<Page> getAllPages() {
		return (pages == null) ? null : Collections.enumeration(pages);
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = (pages == null) ? null : Collections
				.unmodifiableList(pages);
	}

	private List<Page> pages;

	@Override
	@IgnoreProperty
	public ResultMessage getMessage() {
		return message;
	}

	public String getMessageByString() {
		return (message == null) ? null : message.toString();
	}

	public void setMessageByString(String message) {
		this.message = (message == null) ? null : ResultMessage
				.valueOf(message);
	}

	private ResultMessage message;

	@Override
	@IgnoreProperty
	public Long getResultSize() {
		return resultSize;
	}

	private Long resultSize;

	public String getResultSizeByString() {
		return (resultSize == null) ? null : resultSize.toString();
	}

	public void setResultSizeByString(String resultSize) {
		this.resultSize = (resultSize == null) ? null : Long
				.valueOf(resultSize);
	}

	@Override
	public boolean hasMoreResultRows() {
		return hasMoreResultRows;
	}

	public void setHasMoreResultRows(boolean hasMoreResultRows) {
		this.hasMoreResultRows = hasMoreResultRows;
	}

	private boolean hasMoreResultRows;

	@Override
	public boolean hasPreviousResultRows() {
		return hasPreviousResultRows;
	}

	public void setHasPreviousResultRows(boolean hasPreviousResultRows) {
		this.hasPreviousResultRows = hasPreviousResultRows;
	}

	private boolean hasPreviousResultRows;

	@Override
	@IgnoreProperty
	public Long getLastRowPosition() {
		return lastRowPosition;
	}

	private Long lastRowPosition;

	public String getLastRowPositionByString() {
		return (lastRowPosition == null) ? null : lastRowPosition.toString();
	}

	public void setLastRowPositionByString(String lastRowPosition) {
		this.lastRowPosition = (lastRowPosition == null) ? null : Long
				.valueOf(lastRowPosition);
	}

	@Override
	@IgnoreProperty
	public Long getFirstRowPositionOfPreviousResultRows() {
		return firstRowPositionOfPreviousResultRows;
	}

	private Long firstRowPositionOfPreviousResultRows;

	public String getFirstRowPositionOfPreviousResultRowsByString() {
		return (firstRowPositionOfPreviousResultRows == null) ? null
				: firstRowPositionOfPreviousResultRows.toString();
	}

	public void setFirstRowPositionOfPreviousResultRowsByString(
			String firstRowPositionOfPreviousResultRows) {
		this.firstRowPositionOfPreviousResultRows = (firstRowPositionOfPreviousResultRows == null) ? null
				: Long.valueOf(firstRowPositionOfPreviousResultRows);
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	private int pageSize;

	@Override
	public int getNumberOfRowsOnThePage() {
		return numberOfRowsOnThePage;
	}

	public void setNumberOfRowsOnThePage(int numberOfRowsOnThePage) {
		this.numberOfRowsOnThePage = numberOfRowsOnThePage;
	}

	private int numberOfRowsOnThePage;

	@Override
	public int getPageNr() {
		return pageNr;
	}

	public void setPageNr(int pageNr) {
		this.pageNr = pageNr;
	}

	private int pageNr;

	@Override
	public PagingParams getPagingParams() {
		return pagingParams;
	}

	public void setPagingParams(PagingParams pagingParams) {
		this.pagingParams = pagingParams;
	}

	private PagingParams pagingParams;
}
