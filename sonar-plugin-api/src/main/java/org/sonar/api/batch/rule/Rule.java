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
package org.sonar.api.batch.rule;

import com.google.common.annotations.Beta;
import org.sonar.api.rule.RemediationFunction;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.utils.Duration;

import javax.annotation.CheckForNull;

import java.util.Collection;

/**
 * @since 4.2
 */
@Beta
public interface Rule {

  RuleKey key();

  String name();

  @CheckForNull
  String description();

  @CheckForNull
  String metadata();

  String severity();

  @CheckForNull
  RuleParam param(String paramKey);

  Collection<RuleParam> params();

  RuleStatus status();

  /**
   * Characteristic key.
   *
   * @since 4.3
   */
  @CheckForNull
  String characteristic();

  /**
   * Remediation function : one of LINEAR, LINEAR_OFFSET or CONSTANT_ISSUE.
   *
   * @since 4.3
   */
  @CheckForNull
  RemediationFunction function();

  /**
   * Remediation factor duration (used for LINEAR function).
   *
   * @since 4.3
   */
  @CheckForNull
  Duration factor();

  /**
   * Remediation offset duration (used for LINEAR_OFFSET or CONSTANT_ISSUE function).
   *
   * @since 4.3
   */
  @CheckForNull
  Duration offset();

}
