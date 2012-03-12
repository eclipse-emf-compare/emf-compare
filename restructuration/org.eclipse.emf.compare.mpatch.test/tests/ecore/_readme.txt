Changes:


1. Add reference: SubType1.eSuperType (SuperType1 -> SuperType1,SuperType2)
2. Remove reference: SubType2.eSuperType (SuperType1,SuperType2 -> SuperType1)

3. Change attribute: SubType2.superType1.defaultLiteralName ("" -> "blub")
4. Change attribute: SubType2.superType2.defaultLiteralName ("blub" -> "")
5. Change attribute: SubType2.superType3.defaultLiteralName ("bla" -> "blub")

6. Change attribute: SuperType1.abstract: (true -> false)

7. Delete element: SuperType1.operation (incl. cross reference to SuperType2)

8. Add element: SuperType2.reff (incl. cross reference to SuperType1)

9. Move element: SubType1.moveMe (to SubType2)

10. Change reference: SubType2.superType1.eType (X -> Y)
11. Change reference: SubType2.superType2.eType (null -> Y)
12. Change reference: SubType2.superType3.eType (X -> null)

13. Delete element: X
14. Add element: Y

15. Add element: annotation (incl. sub element details entry)


Note that there are three order changes which are ignored at the moment.
Therefore, EMF Compare finds 18 differences although there are only 15 differences specified for this test.