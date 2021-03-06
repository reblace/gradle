/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.artifacts.result;

import org.gradle.api.Nullable;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.artifacts.result.ComponentSelectionReason;
import org.gradle.api.artifacts.result.DependencyResult;
import org.gradle.api.artifacts.result.ResolvedComponentResult;
import org.gradle.api.artifacts.result.ResolvedDependencyResult;
import org.gradle.api.internal.artifacts.component.DefaultModuleComponentIdentifier;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultResolvedComponentResult implements ResolvedComponentResult {
    private final ModuleVersionIdentifier id;
    private final Set<DependencyResult> dependencies = new LinkedHashSet<DependencyResult>();
    private final Set<ResolvedDependencyResult> dependents = new LinkedHashSet<ResolvedDependencyResult>();
    private final ComponentSelectionReason selectionReason;

    public DefaultResolvedComponentResult(ModuleVersionIdentifier id, ComponentSelectionReason selectionReason) {
        assert id != null;
        assert selectionReason != null;

        this.id = id;
        this.selectionReason = selectionReason;
    }

    public ModuleComponentIdentifier getId() {
        return DefaultModuleComponentIdentifier.newId(id.getGroup(), id.getName(), id.getVersion());
    }

    public Set<DependencyResult> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    public Set<ResolvedDependencyResult> getDependents() {
        return Collections.unmodifiableSet(dependents);
    }

    public DefaultResolvedComponentResult addDependency(DependencyResult dependency) {
        this.dependencies.add(dependency);
        return this;
    }

    public DefaultResolvedComponentResult addDependent(ResolvedDependencyResult dependent) {
        this.dependents.add(dependent);
        return this;
    }

    public ComponentSelectionReason getSelectionReason() {
        return selectionReason;
    }

    @Nullable
    public ModuleComponentIdentifier getPublishedAs() {
        return getId();
    }

    @Override
    public String toString() {
        return getId().getDisplayName();
    }
}
