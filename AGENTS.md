# AGENTS.md

NeoForge Minecraft mod (MC 1.21.1, NeoForge 21.1.219, Java 21, Gradle 9.2.1 wrapper). Mod id `java_error_404`, package root `www.unsa.java.error.error404`. It is a griefing/prank mod: "exception items" kill players and cause real JVM crashes or network-error disconnects on the victim's client.

## Build & verify

- **Build via GitHub Actions**: push to `main` (or `workflow_dispatch`) triggers `.github/workflows/build.yml` (JDK 21 temurin; artifact `build/libs/JavaERROR404-*.jar`). Treat CI as the canonical build/verification gate.
- Dev run: `./gradlew runClient` only (ModDevGradle `neoForge {}` block in `build.gradle`). Do **not** use `runServer`.
- No test sources (`src/test` is empty); the `test` task is a no-op. No linter/formatter configured.
- `gradle.properties` sets `org.gradle.daemon=false` and `-Xmx3G`: every local Gradle invocation is a cold JVM start. Don't chain multiple Gradle commands in one shell line.

## Committing

- Always commit with the identity configured in the local git config (`git config user.name` / `user.email` — currently `Ccat_Q <anqilacat@outlook.com>`, which is the verified primary email of the GitHub account `Ccat-Q`). Never change or bypass this identity, and never attribute a commit to any other account.
- Never add any credit/co-author trailer to commit messages — no `Co-authored-by:`, no "Ultraworked with", no other-account signatures. Every commit must be authored solely by the local identity above.

## How the mod works

- **Death/crash flow**: `ExceptionItem` attack (or crouch-use = suicide) writes a `CrashType` + fatal flag into the *victim's player persistent data NBT* (`player.getPersistentData()`, tag names in `ExceptionItem`). `ModEvents.onLivingDeath` reads those tags, cancels the death, broadcasts a gibberish death message, then sends `ClientboundCrashPacket` to the victim — or crashes locally in singleplayer (no network path exists there).
- **Client packet handling**: `fatal=true` → `CrashHelper.crashJvm()` (runs the real exception in a thread, then hard-crashes the JVM via `Unsafe.getAddress(0)`); `fatal=false` → `emergencySaveAndCrash(CrashReport.forThrowable(...))` — genuine crash report + exit, matching vanilla 1.21.1 crash behaviour. **Do NOT use `Minecraft.getInstance().execute(runnable-that-throws)` for errors**: `BlockableEventLoop.doRunTask` catches and swallows exceptions from queued tasks (only a FATAL log line).
- **Scissors**: each use on a player raises a drop probability (5%/use, capped 80%); on success sends `ActivatePacketDropPacket` (sets `RequiredDataPayload.dropActive` directly on the Netty thread — NO `enqueueWork`, to avoid a 16ms-3s race) followed immediately by a `RequiredDataPayload`. The client handler for `RequiredDataPayload` checks `dropActive`; when set, it walks the vanilla `Connection.exceptionCaught` path with a `DecoderException`, producing the standard network-error disconnect screen. A periodic heartbeat (every 60 ticks) sends `RequiredDataPayload` to all players as a fallback. The pending-grant flag is stored in the target's **player persistent data NBT** (`ExceptionItem.TAG_PENDING_PACKET`), so `java_network_packet` is granted on the target's next login. The flag `RequiredDataPayload.dropActive` is reset to false on client login (in `ClientEvents`, `@EventBusSubscriber(Dist.CLIENT)`) to prevent residual self-disconnect — a server-side `PlayerLoggedInEvent` reset would be ineffective because the flag lives in the client JVM.
- **Network**: NeoForge payload API via `PayloadRegistrar`; all active payloads (`ClientboundCrashPacket`, `ActivatePacketDropPacket`, `RequiredDataPayload`) are `playToClient` and registered in `JavaError404.registerPayloads`.

## Adding a new exception item (6 places)

1. `network/CrashType.java` — enum constant + `execute()` case (must throw the real Java exception) + `javaException()` case
2. `item/ModItems.java` — `register("<name>", () -> new ExceptionItem(CrashType.X, fatal))`; `fatal=true` → JVM crash, `false` → crash screen/disconnect
3. `item/ModCreativeTabs.java` — add to `displayItems`
4. `assets/java_error_404/models/item/<name>.json` — flat `item/generated` model
5. `lang/en_us.json` + `lang/zh_cn.json` — `item.java_error_404.<name>` (both languages are maintained)

## Gotchas

- All exception-item models reference texture `java_error_404:item/404`, but `textures/item/404.png` is **gitignored** — a fresh clone renders those items with missing textures. Only `java_item.png`, `java_network_packet.png`, `scissors.png` are tracked.
- `network/CrashPayload.java` and `network/DisconnectPayload.java` are dead, unregistered leftovers. `CrashPayload` reuses the same `java_error_404:crash` payload id as `ClientboundCrashPacket`, so registering it would collide. Extend the `*Packet` classes, not these.
- There is no mixin anymore: `ClientPacketListener.handleBundlePacket` is never dispatched in 1.21.1 (bundles are split by the pipeline `PacketBundleUnpacker` before reaching the listener), so bundle-based mixins are dead code — disconnect via `Connection.exceptionCaught`, not mixins.
- `src/main/resources/java_error_404.refmap.json` is committed; `processResources` in `build.gradle` expands `${...}` placeholders from `gradle.properties` into `META-INF/neoforge.mods.toml`.
- Crash info is routed through player persistent data (not item NBT / DamageSource) because DamageSource-based entity detection proved unreliable — keep that pattern.
