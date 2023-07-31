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
package org.sonar.server.issue.ws.anticipatedtransition;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.sonar.api.rule.RuleKey;
import org.sonar.core.issue.AnticipatedTransition;

public class AnticipatedTransitionParser {
  private static final Gson GSON = new Gson();
  private static final String WONTFIX = "wontfix";
  private static final String FALSEPOSITIVE = "falsepositive";
  private static final Set<String> ALLOWED_TRANSITIONS = Set.of(WONTFIX, FALSEPOSITIVE);
  private static final String TRANSITION_NOT_SUPPORTED_ERROR_MESSAGE = "Transition '%s' not supported. Only 'wontfix' and 'falsepositive' are supported.";

  public List<AnticipatedTransition> parse(String requestBody, String userUuid, String projectKey) {
    List<GsonAnticipatedTransition> anticipatedTransitions;
    try {
      anticipatedTransitions = Arrays.asList(GSON.fromJson(requestBody, GsonAnticipatedTransition[].class));
    } catch (Exception e) {
      throw new IllegalStateException("Unable to parse anticipated transitions from request body.", e);
    }
    validateAnticipatedTransitions(anticipatedTransitions);
    return mapBodyToAnticipatedTransitions(anticipatedTransitions, userUuid, projectKey);
  }

  private static void validateAnticipatedTransitions(List<GsonAnticipatedTransition> anticipatedTransitions) {
    for (GsonAnticipatedTransition anticipatedTransition : anticipatedTransitions) {
      if (!ALLOWED_TRANSITIONS.contains(anticipatedTransition.transition())) {
        throw new IllegalArgumentException(String.format(TRANSITION_NOT_SUPPORTED_ERROR_MESSAGE, anticipatedTransition.transition()));
      }
    }
  }

  private static List<AnticipatedTransition> mapBodyToAnticipatedTransitions(List<GsonAnticipatedTransition> anticipatedTransitions, String userUuid, String projectKey) {
    return anticipatedTransitions.stream()
      .map(anticipatedTransition -> new AnticipatedTransition(
        projectKey,
        "branch",
        userUuid,
        RuleKey.parse(anticipatedTransition.ruleKey()),
        anticipatedTransition.message(),
        anticipatedTransition.filePath(),
        anticipatedTransition.line(),
        anticipatedTransition.lineHash(),
        anticipatedTransition.transition(),
        anticipatedTransition.comment()))
      .toList();
  }

}
