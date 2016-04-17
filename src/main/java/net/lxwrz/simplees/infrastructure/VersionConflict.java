package net.lxwrz.simplees.infrastructure;

public class VersionConflict extends Throwable {
    VersionConflict(long expectedVersion, long currentVersion) {
        super(String.format(
                "Version conflict: %d expected, but was %d",
                expectedVersion,
                currentVersion));
    }
}
