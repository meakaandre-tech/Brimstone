# Brimstone progress

## Where things stand

- **Now:** package 0 (repo, CI, bench harness), on branch `package-0`, adding the extras from the 2026-10-23 evening
  instructions (Vulkan profile, forced Intel multi-draw-indirect flag, counters that can't crash, `confirm` on scene
  benches) before merging.
- **Pending on Andre:** SIFTEC's `latest` release no longer carries SIFTEC's test commands (they moved to its CI-only
  `siftec_selftest` mod), so the SIFTEC factory scene is skipped. Attaching `siftec-selftest-26.3.jar` to SIFTEC's
  `latest` release brings it back; Brimstone's CI already fetches it from there. Laptop benches: see each package.
- **To decide:** see "Choices to confirm" under each package.

## Hand-off

Working notes for a new chat picking this up: read this file, then `docs/backend.md`, `docs/ci-limits.md` and
`docs/laptop-bench.md`. CI results are on the `ci-logs` branch (`compare/report.md`, `runs/<suite>-<run>/`). Local
builds aren't possible in the agent workspace (no access to Fabric's maven or Mojang); everything is built and
tested in CI.

---

## Package 0: repo, CI, benchmark harness

Report written when package 0 is merged.
