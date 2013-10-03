/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class ForwardingResource extends ForwardingAdaptable implements IResource {

	public boolean contains(ISchedulingRule rule) {
		return delegate().contains(rule);
	}

	public boolean isConflicting(ISchedulingRule rule) {
		return delegate().isConflicting(rule);
	}

	public void accept(IResourceProxyVisitor visitor, int memberFlags) throws CoreException {
		delegate().accept(visitor, memberFlags);
	}

	public void accept(IResourceProxyVisitor visitor, int depth, int memberFlags) throws CoreException {
		try {
			Method method = delegate().getClass().getDeclaredMethod("accept", IResourceProxyVisitor.class, //$NON-NLS-1$
					int.class, int.class);
			if (method != null) {
				method.invoke(delegate(), visitor, Integer.valueOf(depth), Integer.valueOf(memberFlags));
			}
		} catch (NoSuchMethodException e) {
			// swallow
		} catch (IllegalAccessException e) {
			// swallow
		} catch (IllegalArgumentException e) {
			// swallow
		} catch (InvocationTargetException e) {
			// swallow
		}
	}

	public void accept(IResourceVisitor visitor) throws CoreException {
		delegate().accept(visitor);
	}

	public void accept(IResourceVisitor visitor, int depth, boolean includePhantoms) throws CoreException {
		delegate().accept(visitor, depth, includePhantoms);
	}

	public void accept(IResourceVisitor visitor, int depth, int memberFlags) throws CoreException {
		delegate().accept(visitor, depth, memberFlags);
	}

	public void clearHistory(IProgressMonitor monitor) throws CoreException {
		delegate().clearHistory(monitor);
	}

	public void copy(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException {
		delegate().copy(destination, force, monitor);
	}

	public void copy(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException {
		delegate().copy(destination, updateFlags, monitor);
	}

	public void copy(IProjectDescription description, boolean force, IProgressMonitor monitor)
			throws CoreException {
		delegate().copy(description, force, monitor);
	}

	public void copy(IProjectDescription description, int updateFlags, IProgressMonitor monitor)
			throws CoreException {
		delegate().copy(description, updateFlags, monitor);
	}

	public IMarker createMarker(String type) throws CoreException {
		return delegate().createMarker(type);
	}

	public IResourceProxy createProxy() {
		return delegate().createProxy();
	}

	public void delete(boolean force, IProgressMonitor monitor) throws CoreException {
		delegate().delete(force, monitor);
	}

	public void delete(int updateFlags, IProgressMonitor monitor) throws CoreException {
		delegate().delete(updateFlags, monitor);
	}

	public void deleteMarkers(String type, boolean includeSubtypes, int depth) throws CoreException {
		delegate().deleteMarkers(type, includeSubtypes, depth);
	}

	public boolean exists() {
		return delegate().exists();
	}

	public IMarker findMarker(long id) throws CoreException {
		return delegate().findMarker(id);
	}

	public IMarker[] findMarkers(String type, boolean includeSubtypes, int depth) throws CoreException {
		return delegate().findMarkers(type, includeSubtypes, depth);
	}

	public int findMaxProblemSeverity(String type, boolean includeSubtypes, int depth) throws CoreException {
		return delegate().findMaxProblemSeverity(type, includeSubtypes, depth);
	}

	public String getFileExtension() {
		return delegate().getFileExtension();
	}

	public IPath getFullPath() {
		return delegate().getFullPath();
	}

	public long getLocalTimeStamp() {
		return delegate().getLocalTimeStamp();
	}

	public IPath getLocation() {
		return delegate().getLocation();
	}

	public URI getLocationURI() {
		return delegate().getLocationURI();
	}

	public IMarker getMarker(long id) {
		return delegate().getMarker(id);
	}

	public long getModificationStamp() {
		return delegate().getModificationStamp();
	}

	public String getName() {
		return delegate().getName();
	}

	public IPathVariableManager getPathVariableManager() {
		return delegate().getPathVariableManager();
	}

	public IContainer getParent() {
		return delegate().getParent();
	}

	public Map<QualifiedName, String> getPersistentProperties() throws CoreException {
		return delegate().getPersistentProperties();
	}

	public String getPersistentProperty(QualifiedName key) throws CoreException {
		return delegate().getPersistentProperty(key);
	}

	public IProject getProject() {
		return delegate().getProject();
	}

	public IPath getProjectRelativePath() {
		return delegate().getProjectRelativePath();
	}

	public IPath getRawLocation() {
		return delegate().getRawLocation();
	}

	public URI getRawLocationURI() {
		return delegate().getRawLocationURI();
	}

	public ResourceAttributes getResourceAttributes() {
		return delegate().getResourceAttributes();
	}

	public Map<QualifiedName, Object> getSessionProperties() throws CoreException {
		return delegate().getSessionProperties();
	}

	public Object getSessionProperty(QualifiedName key) throws CoreException {
		return delegate().getSessionProperty(key);
	}

	public int getType() {
		return delegate().getType();
	}

	public IWorkspace getWorkspace() {
		return delegate().getWorkspace();
	}

	public boolean isAccessible() {
		return delegate().isAccessible();
	}

	public boolean isDerived() {
		return delegate().isDerived();
	}

	public boolean isDerived(int options) {
		return delegate().isDerived(options);
	}

	public boolean isHidden() {
		return delegate().isHidden();
	}

	public boolean isHidden(int options) {
		return delegate().isHidden(options);
	}

	public boolean isLinked() {
		return delegate().isLinked();
	}

	public boolean isVirtual() {
		return delegate().isVirtual();
	}

	public boolean isLinked(int options) {
		return delegate().isLinked(options);
	}

	@Deprecated
	public boolean isLocal(int depth) {
		return delegate().isLocal(depth);
	}

	public boolean isPhantom() {
		return delegate().isPhantom();
	}

	@Deprecated
	public boolean isReadOnly() {
		return delegate().isReadOnly();
	}

	public boolean isSynchronized(int depth) {
		return delegate().isSynchronized(depth);
	}

	public boolean isTeamPrivateMember() {
		return delegate().isTeamPrivateMember();
	}

	public boolean isTeamPrivateMember(int options) {
		return delegate().isTeamPrivateMember(options);
	}

	public void move(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException {
		delegate().move(destination, force, monitor);
	}

	public void move(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException {
		delegate().move(destination, updateFlags, monitor);
	}

	public void move(IProjectDescription description, boolean force, boolean keepHistory,
			IProgressMonitor monitor) throws CoreException {
		delegate().move(description, force, keepHistory, monitor);
	}

	public void move(IProjectDescription description, int updateFlags, IProgressMonitor monitor)
			throws CoreException {
		delegate().move(description, updateFlags, monitor);
	}

	public void refreshLocal(int depth, IProgressMonitor monitor) throws CoreException {
		delegate().refreshLocal(depth, monitor);
	}

	public void revertModificationStamp(long value) throws CoreException {
		delegate().revertModificationStamp(value);
	}

	@Deprecated
	public void setDerived(boolean isDerived) throws CoreException {
		delegate().setDerived(isDerived);
	}

	public void setDerived(boolean isDerived, IProgressMonitor monitor) throws CoreException {
		delegate().setDerived(isDerived, monitor);
	}

	public void setHidden(boolean isHidden) throws CoreException {
		delegate().setHidden(isHidden);
	}

	@Deprecated
	public void setLocal(boolean flag, int depth, IProgressMonitor monitor) throws CoreException {
		delegate().setLocal(flag, depth, monitor);
	}

	public long setLocalTimeStamp(long value) throws CoreException {
		return delegate().setLocalTimeStamp(value);
	}

	public void setPersistentProperty(QualifiedName key, String value) throws CoreException {
		delegate().setPersistentProperty(key, value);
	}

	@Deprecated
	public void setReadOnly(boolean readOnly) {
		delegate().setReadOnly(readOnly);
	}

	public void setResourceAttributes(ResourceAttributes attributes) throws CoreException {
		delegate().setResourceAttributes(attributes);
	}

	public void setSessionProperty(QualifiedName key, Object value) throws CoreException {
		delegate().setSessionProperty(key, value);
	}

	public void setTeamPrivateMember(boolean isTeamPrivate) throws CoreException {
		delegate().setTeamPrivateMember(isTeamPrivate);
	}

	public void touch(IProgressMonitor monitor) throws CoreException {
		delegate().touch(monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.ForwardingAdaptable#delegate()
	 */
	@Override
	protected abstract IResource delegate();

}
