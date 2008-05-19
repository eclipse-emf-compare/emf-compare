============================
  EMF Compare Architecture
============================

:Authors: Cédric
:Contact: cedric.brun@obeo.fr

.. contents:: Table Of Contents

.. include:: <isonum.txt>

Copyright |copy| 2007-2008, Obeo\ |trade|.

  
Comparison process
==================
The comparison process is divided in 2 phases: matching and differencing. 
The matching phase browses the model version figuring out which element comes from which other one, then the differencing process browses the matching result and create the corresponding delta.
This delta may itself be serialized as a model.

.. image:: ../images/Process.png

Plugins Architecture
=======================
Here are the plugin architecture of the EMF Compare component :

.. image:: ../images/Plugins.png

API's
=====

The red boxes in the following picture are the places you can plug your own engine / extend the default behavior.

.. image:: ../images/compare_general_extensibility.png

