package org.eclipse.emf.compare.merge;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;

import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This adapter is supposed to be installed on a {@link Comparison}'s {@link ResourceSet}s and their
 * {@link Resource}s to react to content changes. Participants can then react to such changes to jointly
 * decide whether a resource must be marked for deletion. The same instance of adapter should be used for all
 * the resources of a comparison's {@link ResourceSet}s. EMFCompare installs such an adapter on the comparison
 * to make it easy to retrieve.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ResourceChangeAdapter extends AdapterImpl {

	/** The comparison. */
	private final Comparison comparison;

	/** The compaison scope. */
	private final IComparisonScope scope;

	/**
	 * Set of resources to delete on save. These are the resources that are marked for deletion and which will
	 * be deleted when the comparison will be saved.
	 */
	private final Set<Resource> resourcesToDelete;

	/**
	 * Set of added resources. Those are the resources that have been added in a ResourceSet (left or right)
	 * by a merge operation, before the comparison has been saved.
	 */
	private final Set<Resource> addedResources;

	/** The participants to consult when a content change occurs. */
	private final List<IResourceChangeParticipant> participants;

	/**
	 * Constructor.
	 * 
	 * @param comparison
	 *            The comparison, cannot be <code>null</code>.
	 * @param scope
	 *            The scope, cannot be <code>null</code>. Moreover, the left and right notifiers of the scope
	 *            must be {@link ResourceSet}s.
	 */
	public ResourceChangeAdapter(Comparison comparison, IComparisonScope scope) {
		this.comparison = checkNotNull(comparison);
		this.scope = checkNotNull(scope);
		checkArgument(scope.getLeft() instanceof ResourceSet);
		checkArgument(scope.getRight() instanceof ResourceSet);
		resourcesToDelete = newLinkedHashSet();
		addedResources = newLinkedHashSet();
		participants = newArrayList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void notifyChanged(Notification msg) {
		if (!msg.isTouch()) {
			if (msg.getNotifier() instanceof Resource
					&& msg.getFeatureID(Resource.class) == Resource.RESOURCE__CONTENTS) {
				Resource resource = (Resource)msg.getNotifier();
				resourceContentsChanged(resource, msg);
			} else if (msg.getNotifier() instanceof ResourceSet
					&& msg.getFeatureID(ResourceSet.class) == ResourceSet.RESOURCE_SET__RESOURCES) {
				switch (msg.getEventType()) {
					case Notification.ADD:
						resourceAdded((Resource)msg.getNewValue());
						break;
					case Notification.ADD_MANY:
						for (Resource r : (List<Resource>)msg.getNewValue()) {
							resourceAdded(r);
						}
						break;
					default:
				}
			}
		}
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == ResourceChangeAdapter.class;
	}

	/**
	 * Register the given participant.
	 * 
	 * @param participant
	 *            The participant, must not be <code>null</code>
	 */
	public void addParticipant(IResourceChangeParticipant participant) {
		participants.add(checkNotNull(participant));
	}

	/**
	 * Unregister the given participant, has no action if the participant was not previously registered.
	 * 
	 * @param participant
	 *            The participant to unregister
	 */
	public void removeParticipant(IResourceChangeParticipant participant) {
		participants.remove(participant);
	}

	/**
	 * Indicate whether a given Resource needs to be deleted.
	 * 
	 * @param r
	 *            The resource
	 * @return <code>true</code> if the given resource has been marked for deletion.
	 */
	public boolean mustDelete(Resource r) {
		return resourcesToDelete.contains(r);
	}

	/**
	 * Callback invoked when a resource has just been added to a resource set. By default, it walks over the
	 * interested participants and creates all the associated resources that these participants declare as
	 * associated to the given resource.
	 * 
	 * @param resource
	 *            The newly added resource
	 */
	protected void resourceAdded(Resource resource) {
		addedResources.add(resource);
		if (EcoreUtil.getAdapter(resource.eAdapters(), ResourceChangeAdapter.class) == null) {
			resource.eAdapters().add(this);
		}
		for (IResourceChangeParticipant participant : filter(participants, interestedIn(resource))) {
			for (URI relatedURI : participant.associatedResourceURIs(resource)) {
				if (resource.getResourceSet().getResource(relatedURI, false) == null
						&& getResourceSetOnOtherSide(resource).getResource(relatedURI, false) != null) {
					resource.getResourceSet().createResource(relatedURI);
				}
			}
		}
	}

	/**
	 * Get the resource set on the other side of the given resource.
	 * 
	 * @param r
	 *            The resource, which must be either on the left or on the right of the comparison.
	 * @return The ResourceSet on the other side, never <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the given resource is neither on the left nor on the right.
	 */
	protected ResourceSet getResourceSetOnOtherSide(Resource r) {
		ResourceSet resourceSet = r.getResourceSet();
		if (scope.getLeft() == resourceSet) {
			return (ResourceSet)scope.getRight();
		} else if (scope.getRight() == resourceSet) {
			return (ResourceSet)scope.getLeft();
		}
		throw new IllegalArgumentException("The given resource is neither on the left nor on the right"); //$NON-NLS-1$
	}

	/**
	 * React to a Resource contents change to determine if this change involves the deletion of one or several
	 * resources. A Resource must be deleted if:
	 * <ol>
	 * <li>Their contents is <code>null</code> or empty;</li>
	 * <li>It is not matched on the other side of the comparison;</li>
	 * <li>Every participant is OK to delete it.</li>
	 * </ol>
	 * Otherwise, it must not be deleted. When a resource is detected as 'to be deleted', all interested
	 * participants are asked for associated resources to delete along with it, and all these resources are
	 * marked for deletion without any further test. When a resource is detected as 'not to be deleted', and
	 * it had previously been marked for deletion (in the case of an undo for instance), then all interested
	 * participants are asked for associated resources which are all marked as 'not te be deleted'.
	 * 
	 * @param resource
	 *            The resource the contents of which have changed
	 * @param msg
	 *            The notification of the change
	 */
	protected void resourceContentsChanged(Resource resource, Notification msg) {
		if (resource.getContents() == null || resource.getContents().isEmpty()) {
			if (isEmptyAndMissingOnOtherSide(resource)) {
				Iterable<IResourceChangeParticipant> interestedParticipants = filter(participants,
						interestedIn(resource));
				if (all(interestedParticipants, acceptDelete(resource))) {
					resourcesToDelete.add(resource);
					for (IResourceChangeParticipant participant : interestedParticipants) {
						for (URI relatedURI : participant.associatedResourceURIs(resource)) {
							Resource related = resource.getResourceSet().getResource(relatedURI, false);
							if (related != null) {
								resourcesToDelete.add(related);
							}
						}
					}
				}
			}
		} else {
			if (resourcesToDelete.remove(resource)) {
				Iterable<IResourceChangeParticipant> interestedParticipants = filter(participants,
						interestedIn(resource));
				for (IResourceChangeParticipant participant : interestedParticipants) {
					for (URI relatedURI : participant.associatedResourceURIs(resource)) {
						Resource related = resource.getResourceSet().getResource(relatedURI, false);
						if (related != null) {
							resourcesToDelete.remove(related);
						}
					}
				}
			}
		}
	}

	/**
	 * Indicate whether a resource is empty and is only on its side of the comparison (i.e. if it should be
	 * deleted unless a special restriction prevents it).
	 * 
	 * @param resource
	 *            The resource
	 * @return <code>true</code> if the resource is empty and is not matched on the other side of the
	 *         comparison.
	 */
	public boolean isEmptyAndMissingOnOtherSide(Resource resource) {
		if (resource.getContents().isEmpty()) {
			ResourceMatch match = getResourceMatch(resource);
			if (addedResources.contains(resource) || (match != null && match.isMissingOnOtherSide())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the MatchResource corresponding to the given <code>resource</code>.
	 * 
	 * @param resource
	 *            Resource for which we need a MatchResource.
	 * @return The MatchResource corresponding to the given <code>resource</code>, <code>null</code> if the
	 *         resource is not in any side of this comparison (package, profiles, ...).
	 */
	protected ResourceMatch getResourceMatch(Resource resource) {
		for (MatchResource matchRes : comparison.getMatchedResources()) {
			if (matchRes.getLeft() == resource) {
				return new ResourceMatch(matchRes, LEFT);
			} else if (matchRes.getRight() == resource) {
				return new ResourceMatch(matchRes, RIGHT);
			}
		}
		return null;
	}

	/**
	 * A predicate for participants interested in a given resource.
	 * 
	 * @param r
	 *            The resource
	 * @return A predicate that returns <code>true</code> for participants interested in the given resource.
	 */
	private Predicate<IResourceChangeParticipant> interestedIn(final Resource r) {
		return new Predicate<IResourceChangeParticipant>() {
			public boolean apply(IResourceChangeParticipant input) {
				return input.interestedIn(r);
			}
		};
	}

	/**
	 * A predicate for participants that accept the delete of a given resource.
	 * 
	 * @param r
	 *            The resource
	 * @return A predicate that returns <code>true</code> for participants that accept the delete of the
	 *         resource.
	 */
	private Predicate<IResourceChangeParticipant> acceptDelete(final Resource r) {
		return new Predicate<IResourceChangeParticipant>() {
			public boolean apply(IResourceChangeParticipant input) {
				return input.acceptDelete(r);
			}
		};
	}

	/**
	 * The match of a given Resource on a given side.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class ResourceMatch {

		/** The Matchresource. */
		private final MatchResource matchResource;

		/** The side. */
		private final DifferenceSource side;

		/**
		 * Constructor.
		 * 
		 * @param matchResource
		 *            The MatchResource, not <code>null</code>
		 * @param side
		 *            The side, not <code>null</code>
		 */
		private ResourceMatch(MatchResource matchResource, DifferenceSource side) {
			this.matchResource = checkNotNull(matchResource);
			this.side = checkNotNull(side);
		}

		/**
		 * Indicate whether the resource is missing on the other side.
		 * 
		 * @return <code>true</code> if the matched resource not matched on the other side.
		 */
		public boolean isMissingOnOtherSide() {
			switch (side) {
				case LEFT:
					return matchResource.getRight() == null;
				case RIGHT:
					return matchResource.getLeft() == null;
				default:
			}
			throw new IllegalStateException();
		}
	}

	/**
	 * A participant in a Resource content change, useful to indicate whether an empty resource must actually
	 * be deleted or not, and which other resources need to be deleted/undeleted along.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public interface IResourceChangeParticipant {
		/**
		 * Whether the participant is interested in the given resource.
		 * 
		 * @param r
		 *            The resource
		 * @return <code>true</code> if the participant is interested in (relevant for) the given resource.
		 */
		boolean interestedIn(Resource r);

		/**
		 * Whether the participant accepts the delete of the given resource.
		 * 
		 * @param r
		 *            The resource
		 * @return <code>true</code> if the participant is OK to delete the resource, <code>false</code>
		 *         otherwise, which will block the deletion.
		 */
		boolean acceptDelete(Resource r);

		/**
		 * Provide the resources to (un)delete along with the given resource. This allows tools that want to
		 * atomically create/delete several resources at a time (for example, one sematin + one graphical
		 * resource) to deal with this atomicity.
		 * 
		 * @param r
		 *            The resource to (un)delete
		 * @return A collection of associated resources URI, must never be <code>null</code> but can be empty.
		 */
		Collection<URI> associatedResourceURIs(Resource r);
	}
}
