Each change occurs at least once here:


Element added: root.AddModelElement.added
- sub element: subAdded

Element removed: root.RemovedModelElement.toRemove
- sub element: removedSubElement

Element moved: root.MovedModelElement.oldPlace.toMove
- moved to: root.MovedModelElement.newPlace.toMove

2x Attribute added: root.AddAttribute.addAttribute::multiAttribute
- value: new, new2

Attribute removed: root.RemoveAttribute.removeAttribute::multiAttribute
// - value: b
========================> NOT DETECTED (in EMF Compare)
=> therefore I added another attribute to compensate the missing change:
Attribute added: root.RemoveAttribute.removeAttribute::multiAttribute
- value d 

Attribute changed: root.UpdateAttribute.toChange::additionalAttribute
- old value: toChange
- new value: changed

2x Reference added: root.AddReference.addReference::multiReference
- reference to: root
- reference to: root.AddModelElement.added.subAdded

Reference removed: root.RemoveReference.removeReference::multiReference
- reference to: root.RemoveReference.removeReference

Reference updated: root.UpdateReference.changeUpdateReference::singleReference
- reference from: root.UpdateReference
- reference to: root.UpdateReference.changeUpdateReference

Reference updated: root.UpdateReference.removeUpdateReference::singleReference
- reference from: root.UpdateReference
- reference to: <null>

Reference updated: root.UpdateReference.addUpdateReference::singleReference
- reference from: <null>
- reference to: root.UpdateReference

Element moved: root.MoveContainmentModelElement.containmentMoved
- from: root.MoveContainmentModelElement::children
- to: root.MoveContainmentModelElement::children2

