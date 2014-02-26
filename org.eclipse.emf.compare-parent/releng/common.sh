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

# The path to the p2-admin executable
export P2_ADMIN_PATH="/jobs/genie.modeling.emfcompare/p2-admin/lastStable/archive/org.eclipselabs.equinox.p2.admin.product/target/products/org.eclipse.equinox.p2.admin.rcp.product/linux/gtk/x86_64/p2-admin"

# The root folder for all EMF Compare udpate sites
export EMF_COMPARE_UPDATES_ROOT="/home/data/httpd/download.eclipse.org/modeling/emf/compare/updates"

# The base URL for all EMF Compare update sites
export EMF_COMPARE_UPDATES_BASE_URL="http://download.eclipse.org/modeling/emf/compare/updates"

NIGHTLY="nightly"
NIGHTLY_PREFIX="N"
INTEGRATION="integration"
INTEGRATION_PREFIX="I"
RELEASE="release"
RELEASE_PREFIX="R"
PREFIXES="$NIGHTLY_PREFIX$INTEGRATION_PREFIX$RELEASE_PREFIX"
VERSION_PATTERN="([0-9]+)\.([0-9]+)\.([0-9]+)-?([$PREFIXES])?([0-9]{8}-[0-9]{6})?"

# To avoid error find: paths must precede expression
# It takes apart the argument list to find and concatenates the arguments back into another 
# argument list but inserts -regextype posix-awk in front of any -iregex or -regex arguments it finds.
# see http://superuser.com/a/666634
findGnuRegex () {
    args=
    for arg in $*
    do
        case $arg in
            -ireges|-regex)
                args="$args -regextype posix-extended $arg"
                ;;
            *)
                args="$args $arg"
                ;;
        esac
    done
    set -f
    command find $args
    set +f
}

# define alias depending on the underlying OS 
# e.g., regex on BSD-like and GNU-like OS are not handled through the same options for
# find and sed.
if [[ "$OSTYPE" == "linux"* ]]; then
	alias sed-regex="sed -r"
	alias find-regex="findGnuRegex"
elif [[ "$OSTYPE" == "cygwin" ]]; then
    alias sed-regex="sed -r"
    alias find-regex="findGnuRegex"
elif [[ "$OSTYPE" == "freebsd"* ]]; then
	alias sed-regex="sed -E"
	alias find-regex="find -E"
elif [[ "$OSTYPE" == "darwin"* ]]; then
	alias sed-regex="sed -E"
	alias find-regex="find -E"
else
	echo "Unknown 'OSTYPE'=$OSTYPE."
	exit -1
fi

chmod u+x $P2_ADMIN_PATH
alias p2-admin="${JAVA_HOME}/bin/java -jar $P2_ADMIN_PATH/plugins/org.eclipse.equinox.launcher_*.jar"
alias composite-repository="p2-admin -application org.eclipselabs.equinox.p2.composite.repository -compressed"

# Create a p2 index file for composite repositories
createP2Index() {
	    cat > "$1/p2.index" <<EOF
version = 1
metadata.repository.factory.order = compositeContent.xml,\!
artifact.repository.factory.order = compositeArtifacts.xml,\!
EOF
}

# Remove any previous file from the $1 path and create a composite repository with a single
# child ($2). The composite will be name $3.
createRedirect() {
	local from="$1"
	local to="$2"
	local name="$3"
	
	mkdir -p "$from"
	rm -f "$from/compositeArtifacts."*
	rm -f "$from/compositeContent."*
	composite-repository -location "$from" -add "$to" -repositoryName "$name"
	createP2Index $from
}

strcmp() {
  local diff
  for ((i=0; i<=${#1}; ++i)); do
    if ((diff=$(printf %d \""${1:i:1}") - $(printf %d \""${2:i:1}") ));
      then echo $diff; return
    fi
  done
  echo 0
}

# Echo a negative integer, zero, or a positive integer if $1 version is less than, equal to,
# or greater than the specified $2 version.
compareOSGIVersions() {
    local this="$1"
	local that="$2"

	thisMajor=$(echo "$this" | sed-regex -e 's/'"$VERSION_PATTERN"'/\1/')
	thisMinor=$(echo "$this" | sed-regex -e 's/'"$VERSION_PATTERN"'/\2/')
	thisMicro=$(echo "$this" | sed-regex -e 's/'"$VERSION_PATTERN"'/\3/')
	thisQualifier=$(echo "$this" | sed-regex -e 's/'"$VERSION_PATTERN"'/\4\5/')
	
	thatMajor=$(echo "$that" | sed-regex -e 's/'"$VERSION_PATTERN"'/\1/')
	thatMinor=$(echo "$that" | sed-regex -e 's/'"$VERSION_PATTERN"'/\2/')
	thatMicro=$(echo "$that" | sed-regex -e 's/'"$VERSION_PATTERN"'/\3/')
	thatQualifier=$(echo "$that" | sed-regex -e 's/'"$VERSION_PATTERN"'/\4\5/')
	
	echo "'"$thisMajor"' => '"$thatMajor"'" 1>&2
	echo "'"$that"'" 1>&2
	if [ "$thisMajor" -ne "$thatMajor" ]; then
		echo $(($thisMajor-$thatMajor))  1>&2
	elif [ "$thisMinor" -ne "$thatMinor" ]; then
		echo $(($thisMinor-$thatMinor))	 1>&2
	elif [ "$thisMicro" -ne "$thatMicro" ]; then
		echo $(($thisMicro-$thatMicro))  1>&2
	elif [[ "$thisQualifier" != "$thatQualifier" ]]; then
		echo strcmp $thisQualifier $thatQualifier 1>&2
	else
		echo 0  1>&2
	fi
	echo 0
}

# print all major versions (sorted) in the $1 path on the standard output
# the output will be a list of integer
allMajors() {
	local path="$1"
	find-regex "$path" -regex '^'"$path"'/?'"$VERSION_PATTERN"'$' -type d \
		| sed-regex -e 's#^'"$path"'/?([0-9]+)\.([0-9]+)\.([0-9]+)-(['"$PREFIXES"'])([0-9]{8})-([0-9]{6})$#\1#' \
		| sort -un
}

# print all minor versions (sorted) for the given $2 major version in the $1 path on the standard output
# $2 must be an integer
# the output will be a list of the form X.Y
allMinors() {
	local path="$1"
	local major="$2"
	find-regex "$path" -regex '^'"$path"'/?'"$major"'\.[0-9]+\.[0-9]+-['"$PREFIXES"'][0-9]{8}-[0-9]{6}$' -type d \
		| sed-regex -e 's#^'"$path"'/?([0-9]+)\.([0-9]+)\.([0-9]+)-(['"$PREFIXES"'])([0-9]{8})-([0-9]{6})$#\2#' \
		| sort -un
}

# print all micro versions (sorted) for the given $2 minor version in the $1 path on the standard output
# $2 must be of the form x.y where x and y are integer
# the output will be a list of the form X.Y.Z
allMicros() {
	local path="$1"
	local major="$2"
	local minor="$3"
	find-regex "$path" -regex '^'"$path"'/?'"$major"'\.'"$minor"'\.[0-9]+-['"$PREFIXES"'][0-9]{8}-[0-9]{6}$' -type d \
		| sed-regex -e 's#^'"$path"'/?([0-9]+)\.([0-9]+)\.([0-9]+)-(['"$PREFIXES"'])([0-9]{8})-([0-9]{6})$#\3#' \
		| sort -un
}

# print all builds (sorted) for the given $2 micro version in the $1 path on the standard output
# $2 must be of the form x.y.z where x, y and z are integer
# the output will be a list of the form X.Y.Z-NYYYYMMDD-HHMMSS
allBuilds() {
	local path="$1"
	local major="$2"
	local minor="$3"
	local micro="$4"
	find-regex "$path" -regex '^'"$path"'/?'"$major"'\.'"$minor"'\.'"$micro"'-['"$PREFIXES"'][0-9]{8}-[0-9]{6}$' -type d \
		| sed-regex -e 's#'"$path"'/?##' \
		| sort -u
}

# visit all the builds in $1 path and call the $2 callback for each.
# $3 should the version of interest for this call. It is not used by this function but
# is given to the $2 callback.
#
# The callback must accept 10 parameters:
#  1/ the path (equals to $1)
#  2/ the version of interrest (equals to $3)
#  3/ the currently visited major version (an integer)
#  4/ the currently visited minor version (an integer)
#  5/ the currently visited micro version (an integer)
#  6/ the currently visited build (format x.y.z-TYYYYMMDD-HHMM, where x, y and z 
#     are integer, T is N for nightly, I for integration or R for release and YYYYMMDD-HHMM is a timestamp)
#  7/ the most recent major version in the given $1 path (an integer)
#  8/ the most recent minor version in the currently visited major version (an integer)
#  9/ the most recent micro version in the currently visited minor version (an integer)
#  10/ the most recent build version in the currently visited micro version (format x.y.z-TYYYYMMDD-HHMM, where x, y and z 
#     are integer, T is N for nightly, I for integration or R for release and YYYYMMDD-HHMM is a timestamp)
visitVersions() {
	local path="$1"
	local callback="$2"
	local version="$3"

	allMajors=$(allMajors "$path")
	latestMajor=$(echo "$allMajors" | tail -1)
	for major in $allMajors
	do
		allMinors=$(allMinors "$path" "$major")
		latestMinor=$(echo "$allMinors" | tail -1)
		for minor in $allMinors
		do
			allMicros=$(allMicros "$path" "$major" "$minor")
			latestMicro=$(echo "$allMicros" | tail -1)
			for micro in $allMicros
			do
				allBuilds=$(allBuilds "$path" "$major" "$minor" "$micro")
				latestBuild=$(echo "$allBuilds" | tail -1)

				for build in $allBuilds
				do
					$callback "$path" "$version" "$major" "$minor" "$micro" "$build" "$latestMajor" "$latestMinor" "$latestMicro" "$latestBuild" 
				done
			done
		done
	done
}

# Get the name of the minor stream from the $1 full version X.Y.Z.
# Example: majorStream 2.3.2 => 2.3.x
minorStream() {
	local version="$1"
	echo "$version" | sed-regex -e 's/^([0-9]+\.[0-9]+\.).*$/\1x/'
}

# Get the name of the major stream from the $1 full version X.Y.Z.
# Example: majorStream 2.3.2 => 2.x
majorStream() {
	local version="$1"
	echo "$version" | sed-regex -e 's/^([0-9]+\.).*$/\1x/'
}

# Converts the Hudson build id $1 (e.g. 2013-10-15_07-07-07) into the
# syntax we want for our update-sites (e.g. 20131015-070707)
buildTimestamp() {
	local buildId="$1"
	echo "$buildId" | sed -e 's/-//g' -e 's/_/-/'
}
