#!/bin/sh
# ====================================================================
# Copyright (c) 2021 Obeo
# This program and the accompanying materials
# are made available under the terms of the Eclipse Public License 2.0
# which accompanies this distribution, and is available at
# https://www.eclipse.org/legal/epl-2.0
#
# Contributors:
#    Obeo - initial API and implementation
# ====================================================================

if [ ${REFERENCE_TARGET_PLATFORM} != ${PLATFORM} ]; then
  exit 1
fi

# Exit on error
set -e

# The SSH account to use
export SSH_ACCOUNT="genie.emfcompare@projects-storage.eclipse.org"

NIGHTLIES_FOLDER="/home/data/httpd/download.eclipse.org/modeling/emf/compare/updates/nightly"
GROUP="modeling.emfcompare"

CORE_UPDATE_FOLDER=${WORKSPACE}/packaging/org.eclipse.emf.compare.update/target
EXTRAS_UPDATE_FOLDER=${WORKSPACE}/packaging/org.eclipse.emf.compare.update.extras/target

CORE_UPDATE_ZIP="$(ls ${CORE_UPDATE_FOLDER}/org.eclipse.emf.compare-*.zip | sort -V | tail -n1)"
EXTRAS_UPDATE_ZIP="$(ls  ${EXTRAS_UPDATE_FOLDER}/org.eclipse.emf.compare.extras-*.zip | sort -V | tail -n1)"

CORE_ZIP_NAME=$(echo ${CORE_UPDATE_ZIP} | sed 's/.*\(org.eclipse.emf.compare-.*.zip\)$/\1/')
EXTRAS_ZIP_NAME=$(echo ${EXTRAS_UPDATE_ZIP} | sed 's/.*\(org.eclipse.emf.compare.extras-.*.zip\)$/\1/')
QUALIFIER=$(echo ${CORE_UPDATE_ZIP} | sed 's/.*org.eclipse.emf.compare-\(.*\).zip$/\1/')

P2_TIMESTAMP=$(date +"%s000")

ssh "${SSH_ACCOUNT}" mkdir -p ${NIGHTLIES_FOLDER}/${QUALIFIER}
scp -rp ${CORE_UPDATE_ZIP} ${EXTRAS_UPDATE_ZIP} "${SSH_ACCOUNT}:${NIGHTLIES_FOLDER}/${QUALIFIER}"

# make a composite with both "core" and "extras" features
cat > compositeArtifacts.xml <<EOF
<?xml version='1.0' encoding='UTF-8'?>
<?compositeArtifactRepository version='1.0.0'?>
<repository name='Eclipse EMF Compare ${QUALIFIER}' type='org.eclipse.equinox.internal.p2.artifact.repository.CompositeArtifactRepository' version='1.0.0'>
  <properties size='1'>
    <property name='p2.timestamp' value='${P2_TIMESTAMP}'/>
  </properties>
  <children size='2'>
    <child location='core'/>
    <child location='extras'/>
  </children>
</repository>
EOF
cat > compositeContent.xml <<EOF
<?xml version='1.0' encoding='UTF-8'?>
<?compositeMetadataRepository version='1.0.0'?>
<repository name='Eclipse EMF Compare ${QUALIFIER}' type='org.eclipse.equinox.internal.p2.metadata.repository.CompositeMetadataRepository' version='1.0.0'>
  <properties size='1'>
    <property name='p2.timestamp' value='${P2_TIMESTAMP}'/>
  </properties>
  <children size='2'>
    <child location='core'/>
    <child location='extras'/>
  </children>
</repository>
EOF

# push this composite to the download area as well
scp -rp compositeArtifacts.xml compositeContent.xml "${SSH_ACCOUNT}:${NIGHTLIES_FOLDER}/${QUALIFIER}"

ssh "${SSH_ACCOUNT}" -T <<EOF
  pushd ${NIGHTLIES_FOLDER}/${QUALIFIER}
    unzip "${CORE_ZIP_NAME}" -d core/
    unzip "${EXTRAS_ZIP_NAME}" -d extras/
    rm ${CORE_ZIP_NAME}
    rm ${EXTRAS_ZIP_NAME}
  popd
  
  # make sure permissions are update for the emfcompare group
  chgrp -R ${GROUP} ${NIGHTLIES_FOLDER}/${QUALIFIER}
  chmod -R g+w ${NIGHTLIES_FOLDER}/${QUALIFIER}

  pushd ${NIGHTLIES_FOLDER}/latest
    rm -r *
    cp -r ../${QUALIFIER}/* .
  popd
EOF