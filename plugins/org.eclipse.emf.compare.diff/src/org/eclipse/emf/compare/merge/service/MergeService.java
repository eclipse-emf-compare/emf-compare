package org.eclipse.emf.compare.merge.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.DiffPlugin;
import org.eclipse.emf.compare.merge.api.MergeFactory;

/**
 * Services for merging models
 * 
 * @author Cédric Brun <cedric.brun@obeo.fr>
 * 
 */
public class MergeService {
	// The shared instance
	private static MergeService service;

	private Collection engines = new ArrayList();

	/**
	 * The constructor
	 */
	public MergeService() {
		service = this;
		parseExtensionMetadata();
	}

	private void parseExtensionMetadata() {
		IExtension[] extensions = Platform.getExtensionRegistry()
				.getExtensionPoint(DiffPlugin.PLUGIN_ID, "mergeFactory")
				.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] configElements = extensions[i]
					.getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				FactoryDescriptor desc = parseEngine(configElements[j]);
				storeEngineDescriptor(desc);
			}
		}

	}

	private void storeEngineDescriptor(FactoryDescriptor desc) {
		engines.add(desc);
	}

	private FactoryDescriptor getBestDescriptor() {
		return getHighestDescriptor((List) (engines));
	}

	private FactoryDescriptor getHighestDescriptor(List set) {
		Collections.sort(set, Collections.reverseOrder());
		if (set.size() > 0)
			return (FactoryDescriptor) set.get(0);
		return null;
	}

	private static final String TAG_ENGINE = "factory";

	private FactoryDescriptor parseEngine(IConfigurationElement configElements) {
		if (!configElements.getName().equals(TAG_ENGINE))
			return null;
		FactoryDescriptor desc = new FactoryDescriptor(configElements);
		return desc;
	}

	/**
	 * get the singleton instance
	 * 
	 * @return
	 */
	public static MergeService getInstance() {
		if (service == null)
			service = new MergeService();
		return service;
	}

	/**
	 * Return the best merge factory found
	 * 
	 * @return
	 */
	public MergeFactory getBestFactory() {
		FactoryDescriptor desc = getBestDescriptor();
		MergeFactory currentEngine = desc.getEngineInstance();
		return currentEngine;
	}

	public MergeFactory getBestDiffEngine(String extension) {
		FactoryDescriptor desc = getBestDescriptor();
		return desc.getEngineInstance();
	}

}
