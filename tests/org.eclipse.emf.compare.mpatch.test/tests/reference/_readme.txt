From: https://bugs.eclipse.org/bugs/show_bug.cgi?id=270439


Build ID: I20090313-0100

Steps To Reproduce:
A change of a reference is not detected in 3.5M6.
Please see example project attached to reproduce the bug.

eachonce.zip is a plugin project with a very simple ecore model (including
generated code) with just one EClass and some attributes and references.

eachonce-runtime.zip contains two example models for the runtime workbench. If
I select both (ref_v1 and ref_v2), select 'Compare with each other', EMF
Compare does not show any difference. However, the xmi files clearly show the
changed reference from
    <children singleAttribute="changeUpdateReference"
singleReference="UpdateReference"/>
to
    <children singleAttribute="changeUpdateReference"
singleReference="changeUpdateReference"/>


(IIRC it was working in M5)
