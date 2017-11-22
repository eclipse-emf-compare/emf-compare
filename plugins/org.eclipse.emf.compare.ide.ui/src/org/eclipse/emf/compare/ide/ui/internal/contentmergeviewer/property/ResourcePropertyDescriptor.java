/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.property;

import java.util.Collection;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

final class ResourcePropertyDescriptor implements IItemPropertyDescriptor {

	private final Resource resource;

	private final AdapterFactoryItemDelegator itemDelegator;

	ResourcePropertyDescriptor(Resource resource, AdapterFactoryItemDelegator itemDelegator) {
		this.resource = resource;
		this.itemDelegator = itemDelegator;
	}

	public void setPropertyValue(Object obj, Object value) {
	}

	public void resetPropertyValue(Object obj) {
	}

	public boolean isSortChoices(Object obj) {
		return false;
	}

	public boolean isPropertySet(Object obj) {
		return true;
	}

	public boolean isMultiLine(Object obj) {
		return false;
	}

	public boolean isMany(Object obj) {
		return true;
	}

	public boolean isCompatibleWith(Object obj, Object anotherObject,
			IItemPropertyDescriptor anotherPropertyDescriptor) {
		return false;
	}

	public Object getPropertyValue(Object obj) {
		return resource.getContents();
	}

	public IItemLabelProvider getLabelProvider(Object obj) {
		return new IItemLabelProvider() {

			public String getText(Object theObject) {
				return itemDelegator.getText(theObject);
			}

			public Object getImage(Object theObject) {
				return itemDelegator.getImage(theObject);
			}
		};
	}

	public String getId(Object obj) {
		return getDisplayName(obj);
	}

	public Object getHelpContextIds(Object obj) {
		return null;
	}

	public String[] getFilterFlags(Object obj) {
		return null;
	}

	public Object getFeature(Object obj) {
		return Integer.valueOf(Resource.RESOURCE__CONTENTS);
	}

	public String getDisplayName(Object obj) {
		return EMFCompareIDEUIMessages.getString("PropertyContentMergeViewer.resourceContentsProperty.label"); //$NON-NLS-1$
	}

	public String getDescription(Object obj) {
		return EMFCompareIDEUIMessages
				.getString("PropertyContentMergeViewer.resourceContentsProperty.description"); //$NON-NLS-1$
	}

	public Collection<?> getChoiceOfValues(Object obj) {
		return null;
	}

	public String getCategory(Object obj) {
		return null;
	}

	public boolean canSetProperty(Object obj) {
		return false;
	}
}
