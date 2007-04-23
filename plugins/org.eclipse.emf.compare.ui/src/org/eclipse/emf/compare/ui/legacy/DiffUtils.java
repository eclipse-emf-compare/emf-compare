/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.variants.IResourceVariant;

public class DiffUtils {

	private static AdapterFactoryLabelProvider labelProvider;
	static {
		final List<AdapterFactoryImpl> factories = new ArrayList<AdapterFactoryImpl>();
//		factories.add(new UMLResourceItemProviderAdapterFactory());
//		factories.add(new UMLItemProviderAdapterFactory());
		factories.add(new EcoreItemProviderAdapterFactory());
//		factories.add(new UMLReflectiveItemProviderAdapterFactory());
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				factories);
		labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
	}

	public static String computeEObjectName(final Object EObject) {
		/*
		 * if (EObject instanceof NamedEObject) if (((NamedEObject)
		 * EObject).getName() != null) return ((NamedEObject)
		 * EObject).getName(); else return "<unset>"; if (EObject instanceof
		 * Comment) if (EObject.getOwner() instanceof NamedEObject) return
		 * ((NamedEObject) EObject.getOwner()).getName() + "'s comment"; if
		 * (EObject instanceof PackageMerge) if ( ((PackageMerge)
		 * EObject).getMergedPackage() == null) return "<Merged Package>"; else
		 * return "Merged package " + ((PackageMerge)
		 * EObject).getMergedPackage().getName();
		 * LoggerFactory.getLogger(DiffFactory.class).error("Undefined EObject
		 * type, please add case to computeEObjectName in DiffFactory"); //DEBUG
		 * 
		 * if (EObject == null) return "null EObject"; return
		 * EObject.toString();
		 */

		return labelProvider.getText(EObject);
	}

	public static Image computeEObjectImage(final Object EObject) {
		return labelProvider.getImage(EObject);
	}

	public static void initializeCompareConfiguration(final CompareConfiguration cc,
			final Object left, final Object right, final Object ancestor) {
		cc.setLeftLabel(computeResourceName(left));
		cc.setLeftImage(computeEObjectImage(left));
		cc.setRightLabel(computeResourceName(right));
		cc.setRightImage(computeEObjectImage(right));
		cc.setAncestorLabel(computeResourceName(ancestor));
		cc.setAncestorImage(computeEObjectImage(ancestor));
	}

	public static String computeResourceName(final Object file) {
		if (file == null) {
			return null;
		}
		if (file instanceof IFile) {
			return ((IFile) file).getProjectRelativePath().toPortableString();
		}
		if (file instanceof IFileRevision) {
			return ((IFileRevision) file).getURI().getPath();
		}
		if (file instanceof IFileState) {
			return ((IFileState) file).getName() + " "
					+ new Date(((IFileState) file).getModificationTime());
		}
		if (file instanceof IResourceVariant) {
			return "Repository file (" + ((IResourceVariant) file).getName()
					+ ")";
		}
		// LoggerFactory.getLogger("This file will throw an
		// exception").error(file);
		throw new IllegalStateException("unknown type");
	}
}
