A UML model for difference creation and application test.

The following changes are performed:

Add element 'Authors' : Class
Add element 'A_library_authors' : Association
Add element 'A_authors_writtenBooks' : Association
Del element 'Book.authors' : Attribute
Move element 'Borrowable.title' : Attribute from 'Borrowable' to 'Magazine'
Change attribute 'Borrowable.copies' : String from "copiesAvailable" to "copies"
Change attribute 'A_library_borrowables.library.Lower' : Integer from 0 to 1
Add reference 'A_library_borrowable.borrowables' to 'A_library_borrowable.navigableOwnedEnd'
Del reference to 'A_library_customers.customers' from 'A_library_customers.navigableOwnedEnd'
Change reference 'Book.generalization' from 'Borrowable' to 'Magazine'


Additional test about internal cross-references: ref_(un)changed.uml

Add element 'assoc' : Association
(memberEnds has cross-references to sub-elements)
