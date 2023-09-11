/*
 * SonarQube
 * Copyright (C) 2009-2023 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.db.provisioning;

import java.util.Set;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sonar.db.DbSession;
import org.sonar.db.DbTester;
import org.sonar.db.audit.AuditPersister;
import org.sonar.db.audit.model.GithubPermissionsMappingNewValue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GithubPermissionsMappingDaoIT {

  private static final String MAPPING_UUID = "uuid";

  private final AuditPersister auditPersister = mock();

  @Rule
  public final DbTester db = DbTester.create(auditPersister);

  private final ArgumentCaptor<GithubPermissionsMappingNewValue> newValueCaptor = ArgumentCaptor.forClass(GithubPermissionsMappingNewValue.class);

  private final DbSession dbSession = db.getSession();

  private final GithubPermissionsMappingDao underTest = db.getDbClient().githubPermissionsMappingDao();

  @Test
  public void insert_savesGithubPermissionsMappingDto() {
    GithubPermissionsMappingDto githubPermissionsMappingDto = new GithubPermissionsMappingDto(MAPPING_UUID, "GH_role", "SQ_role");

    underTest.insert(dbSession, githubPermissionsMappingDto);

    Set<GithubPermissionsMappingDto> savedGithubPermissionsMappings = underTest.findAll(dbSession);
    assertThat(savedGithubPermissionsMappings).hasSize(1);
    GithubPermissionsMappingDto savedMapping = savedGithubPermissionsMappings.iterator().next();
    assertThat(savedMapping.uuid()).isEqualTo(githubPermissionsMappingDto.uuid());
    assertThat(savedMapping.githubRole()).isEqualTo(githubPermissionsMappingDto.githubRole());
    assertThat(savedMapping.sonarqubePermission()).isEqualTo(githubPermissionsMappingDto.sonarqubePermission());

    verify(auditPersister).addGithubPermissionsMapping(eq(dbSession), newValueCaptor.capture());
    assertThat(newValueCaptor.getValue().getGithubRole()).isEqualTo(githubPermissionsMappingDto.githubRole());
    assertThat(newValueCaptor.getValue().getSonarqubePermission()).isEqualTo(githubPermissionsMappingDto.sonarqubePermission());
  }

  @Test
  public void findAll_shouldReturnAllGithubOrganizationGroup() {
    GithubPermissionsMappingDto mapping1 = new GithubPermissionsMappingDto(MAPPING_UUID, "GH_role", "SQ_role");
    GithubPermissionsMappingDto mapping2 = new GithubPermissionsMappingDto(MAPPING_UUID + "2", "GH_role2", "SQ_role");

    underTest.insert(dbSession, mapping1);
    underTest.insert(dbSession, mapping2);

    Set<GithubPermissionsMappingDto> all = underTest.findAll(dbSession);

    assertThat(all).hasSize(2)
      .containsExactlyInAnyOrder(
        mapping1,
        mapping2
      );
  }

}
