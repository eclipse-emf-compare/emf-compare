package org.eclipse.emf.compare.tests.diff;

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diff.IDiffProcessor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.ChangeFactory;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;

public class ChangeModelBridge implements IDiffProcessor {

	private ChangeRecorder recordedChanges;

	public ChangeModelBridge() {
		recordedChanges = new ChangeRecorder() {

			@Override
			protected void addAdapter(Notifier notifier) {
				/*
				 * we should not install ourself on the changed objects.
				 */
			}

		};
		recordedChanges.beginRecording(ChangeFactory.eINSTANCE.createChangeDescription(), Lists
				.newArrayList());

	}

	public void referenceChange(final Match match, final EReference reference, EObject value,
			DifferenceKind kind, DifferenceSource source) {
		someChange(match, reference, value, kind);
	}

	protected void someChange(final Match match, final EStructuralFeature feature, Object value,
			DifferenceKind kind) {
		int eventType = 0;
		Object oldValue = null;
		Object newValue = null;
		Object notifier = match.getLeft();
		int position = Notification.NO_INDEX;
		switch (kind) {
			case ADD:
				eventType = Notification.ADD;
				newValue = value;
				position = ((List)match.getLeft().eGet(feature)).indexOf(value);
				break;
			case CHANGE:
				if (match.getRight() == null) {
					eventType = Notification.UNSET;
					newValue = value;
					oldValue = match.getLeft().eGet(feature);
				} else {
					eventType = Notification.SET;
					newValue = value;
					oldValue = match.getRight().eGet(feature);
				}
				break;
			case DELETE:
				eventType = Notification.REMOVE;
				oldValue = value;
				break;
			case MOVE:
				eventType = Notification.MOVE;
				newValue = ((List)match.getLeft().eGet(feature)).indexOf(value);
				oldValue = ((List)match.getRight().eGet(feature)).indexOf(value);
				break;
			default:
				break;
		}
		Notification notification = new NotificationImpl(eventType, oldValue, newValue, position) {

			@Override
			public Object getNotifier() {
				return match.getLeft();
			}

			@Override
			public Object getFeature() {
				return feature;
			}

			@Override
			public int getFeatureID(Class<?> expectedClass) {
				return feature.getFeatureID();
			}
		};

		recordedChanges.notifyChanged(notification);
	}

	public void attributeChange(Match match, EAttribute attribute, Object value, DifferenceKind kind,
			DifferenceSource source) {
		someChange(match, attribute, value, kind);

	}

	public ChangeDescription getChanges() {
		return recordedChanges.summarize();

	}
}
