========================
 EMF Compare User Guide
========================

:Authors: Cédric
:Contact: cedric.brun@obeo.fr

.. contents:: Table Of Contents

.. include:: <isonum.txt>

Copyright |copy| 2007-2008, Obeo\ |trade|.

Installation
============

To install EMF Compare you'll need to use either the EMFT update-site :  <http://www.eclipse.org/modeling/emft/updates/>`_ 
or the build bits from the download page  : <http://www.eclipse.org/modeling/emft/downloads/?project=compare>`_

Requirements
------------
EMF Compare depends on:
#. EMF runtime
#. Eclipse Platform (if you don't want to run EMF Compare in "standalone mode".

 

Configuration
=============

Activating EMF Compare on a given file extension
-------------------------------------------------

EMF compare uses a content-type to know whether it should be used for comparison. This content-type will initially register itself against .ecore and .uml files. 
You may add your own extension using the Preferences view / Global / Content-types and adding your file extension in the "EMF Compare" content-type.

.. image:: ../images/ContentTypes.png

Usage
-----
Once activated you can compare your file (localy or from any Configuration Management System supported by the Team API) using the "compare with" menu in Eclipse.

.. image:: ../images/CompareUI.png

The following areas are highlighted in the picture : 

1. The diff model displaying all the differences found on the models
2. The version 1 model
3. The version 2 model
4. The "export differences" button
5. Move to next/Move to previous difference
6. Merge the current difference (left to right or right to left)
7. Merge all non conflicting differences (left to right or right to left)
8. Display properties differences

Note that buttons may be disabled depending on whether you are using Configuration Management System or not.

