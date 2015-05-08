/**
 * 
 */
package pl.slawas.paging;

import java.util.Collection;

import org.apache.cxf.aegis.type.java5.IgnoreProperty;

/**
 * PagedResult obiekt transportowy stronicowanych wyników
 * 
 * @author Karol Kowalczyk
 */
@SuppressWarnings("serial")
public class PagedResult<E> extends PagingInfo implements _IPagedResult<E> {

	public PagedResult() {
		super();
	}

	public PagedResult(final _IPagedResult<E> pagedResult) {
		super(pagedResult);
		result = pagedResult.getResult();
	}

	@Override
	public Collection<E> getResult() {
		return result;
	}

	public void setResult(Collection<E> result) {
		this.result = result;
	}

	private Collection<E> result;

	@Override
	@IgnoreProperty
	public Collection<E> getResult(Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	@IgnoreProperty
	public E getUniqueResult() {
		throw new UnsupportedOperationException();
	}

	@Override
	@IgnoreProperty
	public _IPagedQuery<E> getQuery() {
		throw new UnsupportedOperationException();
	}

}
