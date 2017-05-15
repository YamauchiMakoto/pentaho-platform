/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License, version 2 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/gpl-2.0.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 *
 * Copyright 2006 - 2014 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.platform.engine.services.connection.datasource.dbcp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.pentaho.platform.api.data.DBDatasourceServiceException;
import org.pentaho.platform.api.data.IDBDatasourceService;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.platform.engine.services.MockDataSourceService;

@RunWith( MockitoJUnitRunner.class )
public class DynamicallyPooledDatasourceSystemListenerTest {

  private static final String CONNECTION_NAME = "TEST CONNECTION";

  @Mock
  IDBDatasourceService datasourceService;

  @Mock
  IDatabaseConnection connection;

  @Spy
  DynamicallyPooledDatasourceSystemListener listener;

  @Before
  public void before() {
    when( connection.getName() ).thenReturn( CONNECTION_NAME );
  }

  @Test
  public void testGetDataSource() {
    DataSource ds = null;
    when( listener.getDatasourceService() ).thenReturn( new MockDataSourceService( false ) );
    ds = listener.getDataSource( connection );
    assertNotNull( ds );
  }

  @Test
  public void testDBDatasourceServiceException() throws Exception {
    DataSource ds = null;
    when( datasourceService.getDataSource( connection.getName() ) ).thenThrow( new DBDatasourceServiceException() );
    when( listener.getDatasourceService() ).thenReturn( datasourceService );
    ds = listener.getDataSource( connection );
    assertNull( ds );
  }

  @Test
  public void testDriverInitializeFailedException() throws Exception {
    DataSource ds = null;
    when( datasourceService.getDataSource( connection.getName() ) ).thenThrow( new DriverInitializeFailedException() );
    when( listener.getDatasourceService() ).thenReturn( datasourceService );
    ds = listener.getDataSource( connection );
    assertNull( ds );
  }
}