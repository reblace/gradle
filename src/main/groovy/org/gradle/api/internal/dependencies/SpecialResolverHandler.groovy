/*
 * Copyright 2007 the original author or authors.
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

package org.gradle.api.internal.dependencies

import org.apache.ivy.core.cache.DefaultRepositoryCacheManager
import org.apache.ivy.plugins.resolver.RepositoryResolver
import org.apache.ivy.plugins.resolver.FileSystemResolver
import org.apache.ivy.plugins.lock.NoLockStrategy
import org.gradle.api.DependencyManager
import org.apache.ivy.plugins.resolver.IBiblioResolver

/**
 * @author Hans Dockter
 */
class SpecialResolverHandler {
    File buildResolverDir

    DefaultRepositoryCacheManager cacheManagerInternal

    SpecialResolverHandler() {

    }

    SpecialResolverHandler(File buildResolverDir) {
        this.buildResolverDir = buildResolverDir
    }

    RepositoryResolver buildResolverInternal

    RepositoryResolver getBuildResolver() {
        if (!buildResolverInternal) {
            assert buildResolverDir
            buildResolverInternal = new FileSystemResolver()
            configureBuildResolver(buildResolverInternal)
        }
        buildResolverInternal
    }

    FileSystemResolver createFlatDirResolver(String name, File[] roots) {
        FileSystemResolver resolver = new FileSystemResolver()
        resolver.name = name 
        resolver.setRepositoryCacheManager(cacheManager)
        roots.collect {"$it.absolutePath/$DependencyManager.FLAT_DIR_RESOLVER_PATTERN"}.each { String pattern ->
            resolver.addIvyPattern(pattern)
            resolver.addArtifactPattern(pattern)
        }
        resolver.validate = false
        resolver
    }

    private void configureBuildResolver(FileSystemResolver buildResolver) {
        buildResolver.setRepositoryCacheManager(cacheManager)
        buildResolver.name = DependencyManager.BUILD_RESOLVER_NAME
        String pattern = "$buildResolverDir.absolutePath/$DependencyManager.BUILD_RESOLVER_PATTERN"
        buildResolver.addIvyPattern(pattern)
        buildResolver.addArtifactPattern(pattern)
        buildResolver.validate = false
    }

    DefaultRepositoryCacheManager getCacheManager() {
        if (!cacheManagerInternal) {
            cacheManagerInternal = new DefaultRepositoryCacheManager()
            cacheManagerInternal.basedir = new File(buildResolverDir, 'cache')
            cacheManagerInternal.name = 'build-resolver-cache'
            cacheManagerInternal.useOrigin = true
            cacheManagerInternal.lockStrategy = new NoLockStrategy()
        }
        cacheManagerInternal
    }
}
