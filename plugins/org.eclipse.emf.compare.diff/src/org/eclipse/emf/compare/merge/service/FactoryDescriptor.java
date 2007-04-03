package org.eclipse.emf.compare.merge.service;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.merge.api.MergeFactory;

/**
 * Descriptor class for MergeFactory contribution
 * 
 * @author Cédric Brun <cedric.brun@obeo.fr>
 * 
 */
public class FactoryDescriptor implements Comparable {
	protected String priority;

	protected String factoryClassName;

	protected IConfigurationElement element;

	public FactoryDescriptor(IConfigurationElement element) {
		this.element = element;
		priority = getAttribute("priority", "low");
		factoryClassName = getAttribute("class", null);
	}

	private String getAttribute(String name, String defaultValue) {
		String value = element.getAttribute(name);
		if (value != null)
			return value;
		if (defaultValue != null)
			return defaultValue;
		throw new IllegalArgumentException("Missing " + name + " attribute");
	}

	public String getPriority() {
		return priority.toLowerCase();
	}

	public String getFactoryClassName() {
		return factoryClassName;
	}

	private MergeFactory factory = null;

	public MergeFactory getEngineInstance() {
		if (factory == null)
			;
		{
			try {
				factory = (MergeFactory) element
						.createExecutableExtension("class");
			} catch (CoreException e) {
				EMFComparePlugin.getDefault().log(e, false);
			}
		}
		return factory;
	}

	private int getPriorityValue(String priority) {
		if (priority.equals("lowest"))
			return 1;
		if (priority.equals("low"))
			return 2;
		if (priority.equals("normal"))
			return 3;
		if (priority.equals("high"))
			return 4;
		if (priority.equals("highest"))
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
		} else {
			System.err.println("Silly compare with" + other);
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
