There are two added classes in the model - both contain a reference to the other class.

The problem is not the creation of an mpatch, but its application:
-> if either class is added first, the other does not yet exist; hence, sequential sub-model creation does not work.
-> in this case, _both_ sub-models must be built _before_ cross-reference restoring!

