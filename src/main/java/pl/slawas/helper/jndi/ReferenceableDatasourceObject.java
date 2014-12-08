/*
 * Copyright 2014 Sci Software Sławomir Cichy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.	
 */
package pl.slawas.helper.jndi;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Referenceable;
import javax.sql.DataSource;

/**
 * ReferenceableDatasourceObject
 * 
 * @author Sławomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
@SuppressWarnings("serial")
abstract class ReferenceableDatasourceObject implements DataSource,
		Referenceable, Serializable {

	protected final DataSource ds;

	public ReferenceableDatasourceObject(DataSource ds) {
		super();
		this.ds = ds;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.ds.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.ds.isWrapperFor(iface);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return this.ds.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		this.ds.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		this.ds.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return this.ds.getLoginTimeout();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return this.ds.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return this.ds.getConnection(username, password);
	}

}