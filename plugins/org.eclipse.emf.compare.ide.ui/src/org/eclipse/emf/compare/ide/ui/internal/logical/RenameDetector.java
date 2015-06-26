/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Munich and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.diff.IThreeWayDiff;
import org.eclipse.team.core.subscribers.Subscriber;

/**
 * Detector for revealing potential file renames that may have occurred in {@link DiffSide#SOURCE} or
 * {@link DiffSide#REMOTE} in the context of a {@link Subscriber}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class RenameDetector {

	/** We don't report progress to the outside at the moment, so we use a static NullProgressMonitor. */
	private static final NullProgressMonitor NPM = new NullProgressMonitor();

	/** The subscriber for accessing the diffs. */
	private final Subscriber subscriber;

	/** The accessor for accessing the file contents. */
	private final IStorageProviderAccessor accessor;

	/** Cache for affected files. */
	private Iterable<IFile> affectedFiles;

	/** Cache for already computed files before rename on the {@link DiffSide#SOURCE}. */
	private Map<IFile, Optional<IFile>> sourceRenameBeforeCache = new HashMap<IFile, Optional<IFile>>();

	/** Cache for already computed files after rename on the {@link DiffSide#SOURCE}. */
	private Map<IFile, Optional<IFile>> sourceRenameAfterCache = new HashMap<IFile, Optional<IFile>>();

	/** Cache for already computed files before rename on the {@link DiffSide#REMOTE}. */
	private Map<IFile, Optional<IFile>> remoteRenameBeforeCache = new HashMap<IFile, Optional<IFile>>();

	/** Cache for already computed files after rename on the {@link DiffSide#REMOTE}. */
	private Map<IFile, Optional<IFile>> remoteRenameAfterCache = new HashMap<IFile, Optional<IFile>>();

	/**
	 * Constructor.
	 * 
	 * @param subscriber
	 *            The subscriber to access the diffs. This parameter may be <code>null</code>, and as such,
	 *            will result in no rename detection.
	 * @param accessor
	 *            The accessor to access the file variants.
	 */
	public RenameDetector(Subscriber subscriber, IStorageProviderAccessor accessor) {
		this.accessor = Preconditions.checkNotNull(accessor);
		this.subscriber = subscriber;
	}

	/**
	 * Given a source or remote file, this method optionally returns the corresponding {@link IFile} before it
	 * has been renamed on the respective {@code side}, if it has been renamed at all.
	 * <p>
	 * Only {@link DiffSide#SOURCE} or {@link DiffSide#REMOTE} are valid values for {@code side}.
	 * </p>
	 * 
	 * @param sourceOrRemoteFile
	 *            The potentially renamed file.
	 * @param side
	 *            The {@link DiffSide} to look for the rename (only {@link DiffSide#SOURCE} or
	 *            {@link DiffSide#REMOTE} are valid).
	 * @return The file before the rename, if it has been renamed at all, {@link Optional#absent()} otherwise.
	 */
	public Optional<IFile> getFileBeforeRename(IFile sourceOrRemoteFile, DiffSide side) {
		Preconditions.checkArgument(isSourceOrRemoteSide(side));
		if (!isFileBeforeRenameCached(sourceOrRemoteFile, side)) {
			cacheFileBeforeRename(computeFileBeforeRename(sourceOrRemoteFile, side), sourceOrRemoteFile, side);
		}
		return getCachedFileBeforeRename(sourceOrRemoteFile, side).get();
	}

	/**
	 * Given an origin file, this method optionally returns the corresponding {@link IFile} after it has been
	 * renamed on the respective {@code side}, if it has been renamed at all.
	 * <p>
	 * Only {@link DiffSide#SOURCE} or {@link DiffSide#REMOTE} are valid values for {@code side}.
	 * </p>
	 * 
	 * @param originFile
	 *            The potentially renamed file.
	 * @param side
	 *            The {@link DiffSide} to look for the rename (only {@link DiffSide#SOURCE} or
	 *            {@link DiffSide#REMOTE} are valid).
	 * @return The file after the rename, if it has been renamed at all, {@link Optional#absent()} otherwise.
	 */
	public Optional<IFile> getFileAfterRename(IFile originFile, DiffSide side) {
		Preconditions.checkArgument(isSourceOrRemoteSide(side));
		if (!isFileAfterRenameCached(originFile, side)) {
			cacheFileAfterRename(computeFileAfterRename(originFile, side), originFile, side);
		}
		return getCachedFileAfterRename(originFile, side).get();
	}

	/**
	 * Specifies whether the result of {@link #computeFileBeforeRename(IFile, DiffSide)} has been cached for
	 * the given {@code sourceOrRemoteFile} and {@code side}.
	 * 
	 * @param sourceOrRemoteFile
	 *            The source or remote file.
	 * @param side
	 *            The side.
	 * @return <code>true</code> if it is cached, <code>false</code> otherwise.
	 */
	private boolean isFileBeforeRenameCached(IFile sourceOrRemoteFile, DiffSide side) {
		return getCachedFileBeforeRename(sourceOrRemoteFile, side).isPresent();
	}

	/**
	 * Caches the result of {@link #computeFileBeforeRename(IFile, DiffSide)} specified as
	 * {@code fileBeforeRename} for the given {@code sourceOrRemoteFile} and {@code side}.
	 * 
	 * @param fileBeforeRename
	 *            The result to be cached.
	 * @param sourceOrRemoteFile
	 *            The input file.
	 * @param side
	 *            The input side.
	 */
	private void cacheFileBeforeRename(Optional<IFile> fileBeforeRename, IFile sourceOrRemoteFile,
			DiffSide side) {
		if (DiffSide.SOURCE.equals(side)) {
			sourceRenameBeforeCache.put(sourceOrRemoteFile, fileBeforeRename);
		} else if (DiffSide.REMOTE.equals(side)) {
			remoteRenameBeforeCache.put(sourceOrRemoteFile, fileBeforeRename);
		}
	}

	/**
	 * Returns the optional cached result of {@link #computeFileBeforeRename(IFile, DiffSide)}.
	 * 
	 * @param sourceOrRemoteFile
	 *            The source or remote file.
	 * @param side
	 *            The side.
	 * @return The cached result, or {@link Optional#absent()}
	 */
	private Optional<Optional<IFile>> getCachedFileBeforeRename(IFile sourceOrRemoteFile, DiffSide side) {
		final Optional<Optional<IFile>> cachedFile;
		if (DiffSide.SOURCE.equals(side)) {
			cachedFile = Optional.fromNullable(sourceRenameBeforeCache.get(sourceOrRemoteFile));
		} else if (DiffSide.REMOTE.equals(side)) {
			cachedFile = Optional.fromNullable(remoteRenameBeforeCache.get(sourceOrRemoteFile));
		} else {
			cachedFile = Optional.absent();
		}
		return cachedFile;
	}

	/**
	 * Specifies whether the result of {@link #computeFileAfterRename(IFile, DiffSide)} has been cached for
	 * the given {@code originFile} and {@code side}.
	 * 
	 * @param originFile
	 *            The origin file.
	 * @param side
	 *            The side.
	 * @return <code>true</code> if it is cached, <code>false</code> otherwise.
	 */
	private boolean isFileAfterRenameCached(IFile originFile, DiffSide side) {
		return getCachedFileAfterRename(originFile, side).isPresent();
	}

	/**
	 * Caches the result of {@link #computeFileAfterRename(IFile, DiffSide)} specified as
	 * {@code fileAfterRename} for the given {@code originFile} and {@code side}.
	 * 
	 * @param fileAfterRename
	 *            The result to be cached.
	 * @param originFile
	 *            The input file.
	 * @param side
	 *            The input side.
	 */
	private void cacheFileAfterRename(Optional<IFile> fileAfterRename, IFile originFile, DiffSide side) {
		if (DiffSide.SOURCE.equals(side)) {
			sourceRenameAfterCache.put(originFile, fileAfterRename);
		} else if (DiffSide.REMOTE.equals(side)) {
			remoteRenameAfterCache.put(originFile, fileAfterRename);
		}
	}

	/**
	 * Returns the optional cached result of {@link #computeFileAfterRename(IFile, DiffSide)}.
	 * 
	 * @param originFile
	 *            The origin file.
	 * @param side
	 *            The side.
	 * @return The cached result, or {@link Optional#absent()}
	 */
	private Optional<Optional<IFile>> getCachedFileAfterRename(IFile originFile, DiffSide side) {
		final Optional<Optional<IFile>> cachedFile;
		if (DiffSide.SOURCE.equals(side)) {
			cachedFile = Optional.fromNullable(sourceRenameAfterCache.get(originFile));
		} else if (DiffSide.REMOTE.equals(side)) {
			cachedFile = Optional.fromNullable(remoteRenameAfterCache.get(originFile));
		} else {
			cachedFile = Optional.absent();
		}
		return cachedFile;
	}

	/**
	 * Specifies whether {@code side} is either a {@link DiffSide#SOURCE} or {@link DiffSide#REMOTE}.
	 * 
	 * @param side
	 *            The side to check.
	 * @return <code>true</code> if {@code side} is a {@link DiffSide#SOURCE} or {@link DiffSide#REMOTE},
	 *         <code>false</code> otherwise.
	 */
	private boolean isSourceOrRemoteSide(DiffSide side) {
		return DiffSide.SOURCE.equals(side) || DiffSide.REMOTE.equals(side);
	}

	/**
	 * Given a source or remote file, this method optionally returns the corresponding {@link IFile} before it
	 * has been renamed on the respective {@code side}, if it has been renamed at all.
	 * <p>
	 * Only {@link DiffSide#SOURCE} or {@link DiffSide#REMOTE} are valid values for {@code side}.
	 * </p>
	 * 
	 * @param sourceOrRemoteFile
	 *            The potentially renamed file.
	 * @param side
	 *            The {@link DiffSide} to look for the rename (only {@link DiffSide#SOURCE} or
	 *            {@link DiffSide#REMOTE} are valid).
	 * @return The file before the rename, if it has been renamed at all, {@link Optional#absent()} otherwise.
	 */
	private Optional<IFile> computeFileBeforeRename(IFile sourceOrRemoteFile, DiffSide side) {
		if (isAddedFile(sourceOrRemoteFile, side)) {
			for (IFile removedOriginFile : getRemovedFiles(side)) {
				if (isRename(removedOriginFile, sourceOrRemoteFile, side)) {
					return Optional.of(removedOriginFile);
				}
			}
		}
		return Optional.absent();
	}

	/**
	 * Given an origin file, this method optionally returns the corresponding {@link IFile} after it has been
	 * renamed on the respective {@code side}, if it has been renamed at all.
	 * <p>
	 * Only {@link DiffSide#SOURCE} or {@link DiffSide#REMOTE} are valid values for {@code side}.
	 * </p>
	 * 
	 * @param originFile
	 *            The potentially renamed file.
	 * @param side
	 *            The {@link DiffSide} to look for the rename (only {@link DiffSide#SOURCE} or
	 *            {@link DiffSide#REMOTE} are valid).
	 * @return The file after the rename, if it has been renamed at all, {@link Optional#absent()} otherwise.
	 */
	private Optional<IFile> computeFileAfterRename(IFile originFile, DiffSide side) {
		if (isRemovedFile(originFile, side)) {
			for (IFile addedSourceOrRemoteFile : getAddedFiles(side)) {
				if (isRename(originFile, addedSourceOrRemoteFile, side)) {
					return Optional.of(addedSourceOrRemoteFile);
				}
			}
		}
		return Optional.absent();
	}

	/**
	 * Specifies whether the given {@code originFile} has been removed on the given {@code side}.
	 * 
	 * @param originFile
	 *            The file to check.
	 * @param side
	 *            The side to check.
	 * @return <code>true</code> {@code originFile} has been removed, <code>false</code> otherwise.
	 */
	private boolean isRemovedFile(IFile originFile, DiffSide side) {
		return isChangedWithDiffKind(IDiff.REMOVE, side).apply(originFile);
	}

	/**
	 * Specifies whether the given {@code originFile} has been added on the given {@code side}.
	 * 
	 * @param originFile
	 *            The file to check.
	 * @param side
	 *            The side to check.
	 * @return <code>true</code> {@code originFile} has been added, <code>false</code> otherwise.
	 */
	private boolean isAddedFile(IFile originFile, DiffSide side) {
		return isChangedWithDiffKind(IDiff.ADD, side).apply(originFile);
	}

	/**
	 * Returns all files that have been added on the given {@code side}.
	 * 
	 * @param side
	 *            The side to get the additions of.
	 * @return The files that have been added.
	 */
	private Iterable<IFile> getAddedFiles(DiffSide side) {
		return filter(getAffectedFiles(), isChangedWithDiffKind(IDiff.ADD, side));
	}

	/**
	 * Returns all files that have been removed on the given {@code side}.
	 * 
	 * @param side
	 *            The side to get the deletions of.
	 * @return The files that have been removed.
	 */
	private Iterable<IFile> getRemovedFiles(DiffSide side) {
		return filter(getAffectedFiles(), isChangedWithDiffKind(IDiff.REMOVE, side));
	}

	/**
	 * Returns all files that have been affected (i.e., changed in some way).
	 * 
	 * @return All files that have been changed.
	 */
	private Iterable<IFile> getAffectedFiles() {
		if (affectedFiles == null) {
			if (subscriber != null) {
				final List<IResource> roots = asList(subscriber.roots());
				final Iterable<IResource> resources = concat(transform(roots, toAllChildren()));
				affectedFiles = filter(resources, IFile.class);
			} else {
				affectedFiles = Collections.emptySet();
			}
		}
		return affectedFiles;
	}

	/**
	 * Specifies whether the given {@code originFile} should be considered as renamed to
	 * {@code addedSourceOrOriginFile} on the given {@code side} according to their contents.
	 * 
	 * @param originFile
	 *            The origin file to check.
	 * @param addedSourceOrRemoteFile
	 *            The source or remote file to check.
	 * @param side
	 *            The side.
	 * @return <code>true</code> if {@code addedSourceOrRemoteFile} at the given {@code side} should be
	 *         considered as a renamed version of {@code originFile}, <code>false</code> otherwise.
	 */
	private boolean isRename(IFile originFile, IFile addedSourceOrRemoteFile, DiffSide side) {
		try {
			final IStorage origin = accessor.getStorageProvider(originFile, DiffSide.ORIGIN).getStorage(NPM);
			final IStorage added = accessor.getStorageProvider(addedSourceOrRemoteFile, side).getStorage(NPM);
			if (origin != null && added != null) {
				return SimilarityComputer.isSimilar(origin.getContents(), added.getContents());
			}
		} catch (CoreException e) {
			// can't access a storage so ignore, fall through and return false
		} catch (IOException e) {
			// can't access a storage so ignore, fall through and return false
		}
		return false;
	}

	/**
	 * Transforms a {@link IResource} to all of its direct and indirect children.
	 * 
	 * @return A function to transform a resource into all its children.
	 */
	private Function<IResource, Iterable<IResource>> toAllChildren() {
		return new Function<IResource, Iterable<IResource>>() {
			public Iterable<IResource> apply(IResource input) {
				final Builder<IResource> allChildren = ImmutableList.builder();
				try {
					if (input != null) {
						for (IResource child : subscriber.members(input)) {
							allChildren.add(child).addAll(toAllChildren().apply(child));
						}
					}
				} catch (TeamException e) {
					// ignore and fall through
				} catch (NullPointerException e) {
					// org.eclipse.egit.core.internal.merge.GitResourceVariantCache.members(IResource)
					// throws NPE if base doesn't contain a folder that exists in source or remote
					// so ignore and fall through
				}
				return allChildren.build();
			}
		};
	}

	/**
	 * Specifies whether an {@link IFile} has been changed with a specified {@code diffKind}.
	 * 
	 * @param diffKind
	 *            The diff kind to test against.
	 * @param side
	 *            The side to test.
	 * @return A predicate for {@link IFile IFiles} that returns <code>true</code>, if the file has been
	 *         changed with the specified {@code diffKind} or <code>false</code> otherwise.
	 */
	private Predicate<IFile> isChangedWithDiffKind(final int diffKind, final DiffSide side) {
		return new Predicate<IFile>() {
			public boolean apply(IFile input) {
				try {
					if (input != null && subscriber != null) {
						final IDiff diff = subscriber.getDiff(input);
						if (diff != null) {
							return isDiffKind(diffKind, diff, side);
						}
					}
				} catch (CoreException e) {
					// ignore and fall through
				}
				return false;
			}
		};
	}

	/**
	 * Specifies whether a given {@code diff} is a difference of the given {@code diffKind}. Note that we only
	 * consider three-way diffs on the specified side and neglect the {@link IDiff#getKind()}, which may
	 * summarize the diffKind without taking the actual side into account.
	 * 
	 * @param diffKind
	 *            The difference kind we want to test against.
	 * @param diff
	 *            The difference we want to test.
	 * @param side
	 *            The side.
	 * @return <code>true</code> if the difference kind of the given {@code diff} is equal to the given
	 *         {@code diffKind}, <code>false</code> otherwise.
	 */
	private boolean isDiffKind(int diffKind, IDiff diff, DiffSide side) {
		boolean isDiffKind = false;
		if (diff instanceof IThreeWayDiff) {
			final IThreeWayDiff threeWayDiff = (IThreeWayDiff)diff;
			if (DiffSide.REMOTE.equals(side) && threeWayDiff.getRemoteChange() != null) {
				isDiffKind = threeWayDiff.getRemoteChange().getKind() == diffKind;
			} else if (DiffSide.SOURCE.equals(side) && threeWayDiff.getLocalChange() != null) {
				isDiffKind = threeWayDiff.getLocalChange().getKind() == diffKind;
			}
		}
		return isDiffKind;
	}
}
