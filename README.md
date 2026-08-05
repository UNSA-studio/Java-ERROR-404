# Java ERROR 404

**Java cannot parse the data properly — ERROR 404**

A NeoForge prank/griefing mod for **Minecraft 1.21.1**. Attack a player with an "exception item" and their game will *really* crash — a hard JVM crash, a genuine crash report, or a network-error disconnect that looks exactly like the real thing.

> 一个 NeoForge 整蛊/恶作剧模组（MC 1.21.1）。用"异常物品"攻击玩家，让他们的客户端**真实崩溃**：JVM 直接崩溃、生成真实崩溃报告，或模拟出与真实网络错误完全一致的断线界面。

---

## Features / 功能

- **47 exception items / 47 种异常物品**
  - **Fatal (致命)** — e.g. `NullPointerException`, `StackOverflowError`, `OutOfMemoryError`: runs the real Java exception, then hard-crashes the victim's JVM via `Unsafe` (SIGSEGV).
  - **Non-fatal (非致命)** — e.g. `IOException`, `SocketException`, `BufferUnderflowException`: throws the real exception and produces a genuine crash report + exit, matching vanilla client crash behaviour.
- **Java item (`java_item`)** — eating it crashes the JVM with `UnsatisfiedLinkError`; attacking with it instantly kills. Crouch + left-click air cycles modes (Ordinary / Data Marker / Data Analysis / Overload / Nothing).
- **Scissors (`scissors`)** — "network packet drop": each use raises a 5% drop probability (capped at 80%); on success the target's client receives the exact genuine disconnect screen *"Internal Exception: io.netty.handler.codec.DecoderException: java.io.IOException: Packet was discarded"*.
- **Java Network Packet (`java_network_packet`)** — the reward: after the disconnect, re-entering the world (even after a full restart) grants the item, persisted in the player's NBT data.

## Usage / 使用方法

| Action / 操作 | Effect / 效果 |
|---|---|
| Attack a player with an exception item / 用异常物品攻击玩家 | Victim's death is cancelled, a gibberish death message is broadcast, and the victim's client crashes / disconnects |
| Crouch + right-click with an exception item / 蹲下 + 右键异常物品 | Suicide — crash yourself |
| Right-click a player with scissors / 右键玩家使用剪刀 | Ramp the drop chance (5% per use, +5% each, cap 80%) and try to drop the target's network packet |
| Right-click air with scissors / 对空气右键剪刀 | Same, targeting yourself (watch the action-bar chance) |
| Re-enter after being disconnected / 被踢后重新进服 | `java_network_packet` is granted to your inventory |

Give yourself items in-game: `/give @s java_error_404:scissors`

## Requirements / 环境要求

- Minecraft **1.21.1**
- NeoForge **21.1.219+**
- Java **21** (to build from source)

## Build / 构建

```bash
./gradlew build          # -> build/libs/JavaERROR404-1.0.0.jar
./gradlew runClient      # dev client (do not use runServer for this mod)
```

- CI: pushing to `main` runs GitHub Actions (`.github/workflows/build.yml`, JDK 21 temurin) and publishes the jar as an artifact.
- On Windows, if `gradlew.bat` is absent, run the Gradle distribution directly (e.g. from `%USERPROFILE%\.gradle\wrapper\dists`) or use IntelliJ IDEA.

## Disclaimer / 免责声明

This mod intentionally crashes or disconnects the *victim's* client — use it on private servers with friends, and **never** on public servers without explicit consent. The authors are not responsible for any damage caused by griefing.

> 本模组会故意让**受害者**的客户端崩溃或断线。请在私服与好友间使用，**切勿**在未经许可的公共服务器上使用。因整蛊造成的任何损失，作者概不负责。

---

**UNSA-Studio** · License: All rights reserved
