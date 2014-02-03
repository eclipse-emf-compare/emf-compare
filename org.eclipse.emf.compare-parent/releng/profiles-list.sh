#!/bin/sh
# ====================================================================
# Copyright (c) 2014 Obeo
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#    Obeo - initial API and implementation
# ====================================================================

# define alias depending on the underlying OS 
# e.g., regex on BSD-like and GNU-like OS are not handled through the same options for
# find and sed.
if [[ "$OSTYPE" == "linux"* ]]; then
	alias sed-regex="sed -r"
elif [[ "$OSTYPE" == "cygwin" ]]; then
    alias sed-regex="sed -r"
elif [[ "$OSTYPE" == "freebsd"* ]]; then
	alias sed-regex="sed -E"
elif [[ "$OSTYPE" == "darwin"* ]]; then
	alias sed-regex="sed -E"
else
	echo "Unknown 'OSTYPE'=$OSTYPE."
	exit -1
fi

######################################################################
# Constants and helper function
######################################################################

SUPPORTED_PLATFORMS="luna kepler juno indigo helios galileo"

GALILEO_PROFILES="core,ui,diagram"
HELIOS_PROFILES="$GALILEO_PROFILES"
INDIGO_PROFILES="$HELIOS_PROFILES"
JUNO_PROFILES="$INDIGO_PROFILES,ecoretools,papyrus"
KEPLER_PROFILES="$JUNO_PROFILES"
LUNA_PROFILES="$KEPLER_PROFILES,update,sign"

PROG=$(basename $0)
USER_PLATFORM="$1"
usage() {
	echo "Usage: "$PROG" platform-name"
	echo "  Valid platform names are $(echo $SUPPORTED_PLATFORMS | sed-regex -e 's/ /, /g' -e 's/([A-Za-z]+)/'\\\''\1'\\\''/g')"
}

# Check arguments
if [ $# -ne 1 ]; then
	echo "$0: 'platform-name' argument is mandatory" >&2
	usage >&2 
	exit 1
fi

case $USER_PLATFORM in
	galileo) echo $GALILEO_PROFILES
		;;
	helios) echo $HELIOS_PROFILES
		;;
	indigo) echo $INDIGO_PROFILES
		;;
	juno) echo $JUNO_PROFILES
		;;
	kepler) echo $KEPLER_PROFILES
		;;
	luna) echo $LUNA_PROFILES
		;;
	*)
		echo "$0: '$USER_PLATFORM' is not a valid platform-name" >&2
		usage >&2 
		exit 1
		;;
esac

exit 0

