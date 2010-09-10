Some depending changes:

1. Additions

Element added: root.AddDependency.AddedElement
- sub element: AddedSubElement

Reference added: root.AddDependency.AddedReference::multiReference
- reference to: root.AddDependency.AddedElement

Reference added: root.AddDependency.AddedReference::multiReference
- reference to: root.AddDependency.AddedElement.AddedSubElement

Reference changed: root.AddDependency.Add_MovedReference::singleReference
- reference from: root.AddDependency
- reference to: root.AddDependency.AddedElement.AddedSubElement


2. Deletions

Element removed: root.RemoveDependency.RemovedElement
- sub element: RemovedSubElement

Reference removed: root.RemoveDependency.RemoveedReference::multiReference
- reference from: root.RemoveDependency.RemovedElement

Reference removed: root.RemoveDependency.RemoveedReference::multiReference
- reference from: root.RemoveDependency.RemovedElement.RemovedSubElement

Reference changed: root.RemoveDependency.RemoveedReference::singleReference
- reference from: root.RemoveDependency.RemovedElement.RemovedSubElement
- reference to: <null>

Reference changed: root.RemoveDependency.Remove_MovedReference::singleReference
- reference from: root.RemoveDependency.RemovedElement.RemovedSubElement
- reference to: root.RemoveDependency

