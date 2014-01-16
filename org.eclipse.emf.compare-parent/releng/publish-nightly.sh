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

source common.sh

usage() {
	echo "Usage: "$0" version"
}

[ -z "$WORKSPACE" -o -z "$GIT_BRANCH" -o -z "$BUILD_ID" ] && {
     echo "Execution aborted.

One or more of the required variables is not set. They are normally
provided by the Hudson build.

- WORKSPACE  : the build workspace root.
- BUILD_ID  : the Hudson build ID (e.g. 2013-10-15_07-07-07).
- GIT_BRANCH : the name fo the Git branch being build/published.
"
    exit 1
}

if [ $# -le 0 ]; then
	echo "$0: version argument not found"
	usage
	exit 1
else
	echo $1 | grep -E '^[0-9]+\.[0-9]+\.[0-9]+$' > /dev/null
	if [ $? -eq 0 ]; then
		export VERSION=$1
	else
		echo "$0: bad version format -- $1"
		usage
		exit 1
	fi
fi

######################################################################
# Setup
######################################################################

# Exit on error
set -e

# The type of build being published
export BUILD_TYPE="nightly"
export BUILD_TYPE_PREFIX="N"

# MINOR_STREAMs are of the form 1.0.x: only keep major and minor version number parts
export MINOR_STREAM=$(echo "$VERSION" | sed-regex -e 's/^([0-9]+\.[0-9]+\.).*$/\1x/')

# MINOR_STREAMs are of the form 1.x: only keep major version number parts
export MAJOR_STREAM=$(echo "$VERSION" | sed-regex -e 's/^([0-9]+\.).*$/\1x/')

# The short version, common to all versions in that MINOR_STREAM (e.g. 2.1)
export SHORT_VERSION=$(echo "$VERSION" | sed-regex -e 's/^([0-9]+\.[0-9]+)\..*$/\1/')

# Converts the Hudson BUILD_ID (e.g. 2013-10-15_07-07-07) into the
# syntax we want for our update-sites (e.g. 20131015-070707)
export BUILD_TIMESTAMP=$(echo "$BUILD_ID" | sed -e 's/-//g' -e 's/_/-/')

# The full version for this build, e.g. 0.9.0-N20131015-070707
export FULL_VERSION="${VERSION}-${BUILD_TYPE_PREFIX}${BUILD_TIMESTAMP}"

# The root folder where all the builds of the same type as this one
# are published
export NIGHTLY_PATH="$EMF_COMPARE_UPDATES_ROOT/$BUILD_TYPE"

# The folder for this particular build
export UPDATE_SITE_PATH="$NIGHTLY_PATH/$FULL_VERSION"

# The URL on which this particular build will be made available
export UPDATE_SITE_URL="$EMF_COMPARE_UPDATES_BASE_URL/$BUILD_TYPE/$FULL_VERSION"

# update the "latest" repositories to the new build. Should be used as a callback in 
# function visitVersions
updateLatestRedirections() {
	local path=$1
	local versionToPublish=$2
	local major=$3
	local minor=$4
	local micro=$5
	local latestMajor=$6
	local latestMinor=$7
	local latestMicro=$8
	local latestBuild=$9
	
	local updateSiteURL="$EMF_COMPARE_UPDATES_BASE_URL/$BUILD_TYPE/$latestBuild"
	local diff=$(compareOSGIVersions $versionToPublish $micro)
	
	if [ $diff -le 0 ]; then
		if [ "$major" = "$latestMajor" -a "$minor" = "$latestMinor" -a "$micro" = "$latestMicro" ]; then
			echo "    $EMF_COMPARE_UPDATES_BASE_URL/$BUILD_TYPE/latest"
			createRedirect "$path/latest" "$updateSiteURL" "EMF Compare latest $BUILD_TYPE build"
		fi

		echo "    $EMF_COMPARE_UPDATES_BASE_URL/$BUILD_TYPE/$micro/latest"
		createRedirect "$path/$micro/latest" "$updateSiteURL" "EMF Compare latest $micro $BUILD_TYPE build"
		
		if [ "$micro" = "$latestMicro" ]; then
			echo "    $EMF_COMPARE_UPDATES_BASE_URL/$BUILD_TYPE/$minor.x/latest"
			createRedirect "$path/$minor.x/latest" "$updateSiteURL" "EMF Compare latest $minor.x $BUILD_TYPE build"
		fi
		if [ "$minor" = "$latestMinor" -a "$micro" = "$latestMicro" ]; then
			echo "    $EMF_COMPARE_UPDATES_BASE_URL/$BUILD_TYPE/$major.x/latest"
			createRedirect "$path/$major.x/latest" "$updateSiteURL" "EMF Compare latest $major.x $BUILD_TYPE build"
		fi

	fi
	
	return 0
}

######################################################################
# Publish the build
######################################################################

echo "Publishing build $UPDATE_SITE_PATH to $UPDATE_SITE_URL"

# Ensure the target folder exists
mkdir -p "$UPDATE_SITE_PATH"
# The actual publication of the p2 repo produced by the build
cp -a "$WORKSPACE"/packaging/org.eclipse.emf.compare.update/target/repository/* "$UPDATE_SITE_PATH"
# Also publish a dump of the build environment, may be useful to debug
env | sort > "$UPDATE_SITE_PATH/build_env.txt"

######################################################################
# Setup or update the redirects (implemented as composite repos)
######################################################################

echo "Adding $UPDATE_SITE_URL to composites repositories:"

# add a link for all nightly list
echo "    $EMF_COMPARE_UPDATES_BASE_URL/$BUILD_TYPE"
composite-repository -location "$NIGHTLY_PATH" -add "$UPDATE_SITE_URL" \
	-repositoryName "EMF Compare $BUILD_TYPE builds"
createP2Index "$NIGHTLY_PATH"

# add a link for the $VERSION (e.g. "1.2.0" => "1.2.0-NYYYYMMDD-HHMM")
echo "    $EMF_COMPARE_UPDATES_BASE_URL/$BUILD_TYPE/$VERSION"
composite-repository -location "$NIGHTLY_PATH/$VERSION" -add "$UPDATE_SITE_URL" \
	-repositoryName "EMF Compare $VERSION $BUILD_TYPE builds"
createP2Index "$NIGHTLY_PATH/$VERSION"

# add a link for the $MINOR_STREAM (e.g. "1.2.x" => "1.2.0-NYYYYMMDD-HHMM")
echo "    $EMF_COMPARE_UPDATES_BASE_URL/$BUILD_TYPE/$MINOR_STREAM"
composite-repository -location "$NIGHTLY_PATH/$MINOR_STREAM" -add "$UPDATE_SITE_URL" \
	-repositoryName "EMF Compare $MINOR_STREAM $BUILD_TYPE builds"
createP2Index "$NIGHTLY_PATH/$MINOR_STREAM"

# add a link for the $MAJOR_STREAM (e.g. "1.x" => "1.2.0-NYYYYMMDD-HHMM")
echo "    $EMF_COMPARE_UPDATES_BASE_URL/$BUILD_TYPE/$MAJOR_STREAM"
composite-repository -location "$NIGHTLY_PATH/$MAJOR_STREAM" -add "$UPDATE_SITE_URL" \
	-repositoryName "EMF Compare $MAJOR_STREAM $BUILD_TYPE builds"
createP2Index "$NIGHTLY_PATH/$MAJOR_STREAM"

echo "Redirecting 'latest' repositories to $UPDATE_SITE_URL:"
visitVersions $NIGHTLY_PATH "updateLatestRedirections" $VERSION
