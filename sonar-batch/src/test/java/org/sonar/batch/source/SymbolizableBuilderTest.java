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

package org.sonar.batch.source;

import org.junit.Test;
import org.sonar.api.component.Component;
import org.sonar.api.component.Perspective;
import org.sonar.api.source.Symbolizable;
import org.sonar.batch.index.ComponentDataCache;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SymbolizableBuilderTest {

  ComponentDataCache dataCache = mock(ComponentDataCache.class);

  @Test
  public void should_load_perspective() throws Exception {
    Component component = mock(Component.class);

    SymbolizableBuilder perspectiveBuilder = new SymbolizableBuilder(dataCache);
    Perspective perspective = perspectiveBuilder.loadPerspective(Symbolizable.class, component);

    assertThat(perspective).isInstanceOf(Symbolizable.class);
    assertThat(perspective.component()).isEqualTo(component);
  }
}
