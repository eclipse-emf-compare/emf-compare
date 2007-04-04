package org.eclipse.emf.compare.merge.service;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.merge.api.MergeFactory;

/**
 * Descriptor class for MergeFactory contribution
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * 
 */
public class FactoryDescriptor implements Comparable {
	protected String priority;

	protected String factoryClassName;

	protected IConfigurationElement element;

	/**
	 * Create a descriptor
	 * 
	 * @param element
	 */
	public FactoryDescriptor(IConfigurationElement element) {
		this.element = element;
		priority = getAttribute("priority", "low"); //$NON-NLS-1$//$NON-NLS-2$
		factoryClassName = getAttribute("class", null); //$NON-NLS-1$
	}

	private String getAttribute(String name, String defaultValue) {
		String value = element.getAttribute(name);
		if (value != null)
			return value;
		if (defaultValue != null)
			return defaultValue;
		throw new IllegalArgumentException("Missing " + name + " attribute"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Return the engine priority
	 * 
	 * @return the engine priority
	 */
	public String getPriority() {
		return priority.toLowerCase();
	}

	/**
	 * 
	 * @return the factory class name
	 */
	public String getFactoryClassName() {
		return factoryClassName;
	}

	private MergeFactory factory = null;

	/**
	 * 
	 * @return the engine instances
	 */
	public MergeFactory getEngineInstance() {
		if (factory == null)
			;
		{
			try {
				factory = (MergeFactory) element
						.createExecutableExtension("class"); //$NON-NLS-1$
			} catch (CoreException e) {
				EMFComparePlugin.getDefault().log(e, false);
			}
		}
		return factory;
	}

	private int getPriorityValue(String priority) {
		if (priority.equals("lowest")) //$NON-NLS-1$
			return 1;
		if (priority.equals("low")) //$NON-NLS-1$
			return 2;
		if (priority.equals("normal")) //$NON-NLS-1$
			return 3;
		if (priority.equals("high")) //$NON-NLS-1$
			return 4;
		if (priority.equals("highest")) //$NON-NLS-1$
			return 5;
		return 0;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME
				* result
				+ ((factoryClassName == null) ? 0 : factoryClassName.hashCode());
		result = PRIME * result
				+ ((priority == null) ? 0 : priority.hashCode());
		return result;
	}

	public int compareTo(Object other) {
		if (other instanceof FactoryDescriptor) {
			int nombre1 = getPriorityValue(((FactoryDescriptor) other)
					.getPriority());
			int nombre2 = getPriorityValue(this.getPriority());
			if (nombre1 > nombre2)
				return -1;
			else if (nombre1 == nombre2)
				return 0;
			else
				return 1;
		} 
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FactoryDescriptor other = (FactoryDescriptor) obj;
		if (factoryClassName == null) {
			if (other.factoryClassName != null)
				return false;
		} else if (!factoryClassName.equals(other.factoryClassName))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		return true;
	}
}
