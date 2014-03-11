/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.batch.bootstrap;

import org.junit.Test;
import org.sonar.api.config.Settings;

import java.util.Properties;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class BatchDatabaseTest {
  @Test
  public void should_init_at_least_two_connections() {
    BatchDatabase db = new BatchDatabase(new Settings(), mock(AnalysisMode.class), mock(JdbcDriverHolder.class), mock(PreviewDatabase.class));
    Properties props = new Properties();

    db.doCompleteProperties(props);

    assertThat(Integer.parseInt(props.getProperty("sonar.jdbc.initialSize"))).isGreaterThanOrEqualTo(2);
    assertThat(Integer.parseInt(props.getProperty("sonar.jdbc.maxActive"))).isGreaterThanOrEqualTo(2);
  }

}
