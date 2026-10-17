# Java Bytecode Deobfuscation & Anti-Decompiler Doctrine

When analyzing or reverse-engineering obfuscated Java binaries (JAR/WAR/class files, especially protectors like Bozar, SkidSuite, SandMark, or custom game-mod packagers), apply these operational rules:

## 1. Mandatory Case-Collision Disambiguation
- **Rule:** Never perform raw extraction (`jar -xf`, `unzip`, `Expand-Archive`, `tar -xf`) directly on Windows NTFS filesystems without checking for case collisions.
- **Surface:** Obfuscators routinely generate collision pairs (e.g. `mI.class` payload coordinator [80+ KB] vs `mi.class` dummy [10 KB]). Windows silently overwrites the larger payload class with the smaller helper.
- **Action:** Inspect the archive's internal path list. If collisions exist, append a deterministic CRC-32/hash suffix (`_CASE_<crc>`) during extraction or extract in a case-sensitive environment (e.g., WSL, in-memory zip processing).

## 2. Pre-Decompiler Constant Pool Sanitization (Mitigate AST Bloat)
- **Rule:** Never pass classes with long whitespace-padded UTF-8 strings directly to AST-formatting decompilers (Vineflower, Fernflower, CFR).
- **Surface:** Obfuscators inject 2,000–4,000 continuous space characters (`0x20`) into class/field/method UTF-8 constants. The decompiler's line wrapping and layout engines suffer quadratic latency degradation, generating 70+ MB source files and stalling for minutes per class.
- **Action:** Pre-process class bytecode in memory before decompilation: parse the constant pool, locate `CONSTANT_Utf8` entries containing runs of spaces or padding markers, collapse `\s{4,}` into a single space, and rewrite the 2-byte length tag. Decompilation latency drops from ~90s to <2s with a >99% reduction in file bloat.

## 3. High-Speed Single-Pass Regex Token Remapping
- **Rule:** Do not iterate sequentially over mapping tables (e.g., Yarn/Intermediary 26,000+ entries) using linear `re.sub()` or string replacements.
- **Action:** Compile a single regex matching the token family (e.g., `r'\b(class_\d+)\b'` and `r'\b(field_\d+|method_\d+)\b'`), passing a dictionary lookup callback (`lambda m: mapping.get(m.group(1), m.group(1))`). This completes token remapping in single-digit milliseconds per file instead of minutes.

## 4. Initialization Breadcrumb Mining for Ground-Truth Identity
- **Rule:** When module names, package paths, or feature identifiers are stripped or dynamic, inspect static initialization blocks (`<clinit>`) and coordinator constructors for exception breadcrumbs.
- **Surface:** Obfuscated frameworks frequently assign debug breadcrumb variables (e.g., `zt = fY[idx]`) immediately before constructing modules so that crash handlers can print meaningful context. These assignments reveal the exact 1-to-1 canonical identity of obfuscated classes.

## 5. Automated Post-Decompilation Quality Verification
- **Rule:** Run an automated validator over generated sources checking:
  1. Zero obfuscation prefix remnants (`ALC!!`, `ALC_`).
  2. Zero leftover licensing call sites (`ExecutionGuard`, `requireAllowed`).
  3. Zero AST layout whitespace runs (` {50,}`).
  4. Class name declarations matching file basenames.

## 6. Bozar Invokedynamic & Opaque Predicate Desugaring
- **Rule:** When decompilers emit synthetic indy calls (`<" ", "...", tag>(...)`):
  1. `<" ", "...", 0>` represents Bozar opaque predicate control flow flattening. The genuine execution logic is inside the `else` branch; strip the outer wrapper and eliminate the dead branch.
  2. `<" ", "...", 1>` represents dynamic method dispatch. In UI/client modules, this predominantly wraps fluent setting builders (`.name()`, `.description()`, `.defaultValue()`, `.build()`, `.add()`) and coordinate accessors (`.getX()`, `.getY()`, `.getZ()`).
  3. Fold constant bitwise XOR and shift expressions (`(int)(A ^ B)`, `(boolean)((int)(A ^ B))`) before string resolution to recover real indices and boolean values.
  4. Offline JVM string dumpers for Minecraft mods must bootstrap registries (`SharedConstants.createGameVersion()` and `Bootstrap.initialize()`) and match Intermediary bytecode namespaces to avoid `NoClassDefFoundError` or `ExceptionInInitializerError`.

## 7. Method-to-Method Boundary Regex Anchoring
- **Rule:** When writing automated desugar regexes for decompiled methods with broken control flow or uninitialized variables:
  - Never anchor on single closing braces `}`. Decompiled code often has nested synthetic blocks, and matching single `}` causes truncated or unbalanced methods.
  - Anchor the regex match from the target method signature up to the start of the next method signature:
    ```python
    pattern = r'private\s+boolean\s+TargetMethod\([^)]*\)\s*\{[\s\S]*?\n\s*private\s+[A-Za-z0-9_<>\[\]]+\s+NextMethod\('
    replacement = clean_method_code + '\n\n   private NextMethodType NextMethod('
    ```
  - This ensures 100% structural class integrity and eliminates syntax errors across large codebases.

## 8. Dual-Layer Mapping Architecture (Yarn Sources -> Intermediary Jar)
- **Rule:** When producing a clean client mod from decompiled sources:
  1. Decompile and map obfuscated classes directly to human-readable Yarn mappings for development.
  2. Maintain a minimal Gradle Fabric Loom project targeting Yarn mappings.
  3. Resolve all compilation diagnostics to ensure clean typing (`gradlew compileJava`).
  4. Execute Fabric Loom's `remapJar` task to generate official Fabric Intermediary bytecode (`net.minecraft.class_*`) with valid StackMapTable frames.
  5. Merge the remapped clean classes into the final mod archive.

## 9. In-Memory Offline Licensing vs. Process Spawning
- **Rule:** When bypassing client mod licensing or online DRM:
  - Implement an in-memory Java ClassLoader (`offline.AliceOfflineLoader`) that intercepts payload classes and supplies the seed contribution directly to the protection layer (`W.a(seed)`).
  - Never spawn external shell processes (`powershell.exe -ExecutionPolicy Bypass`, `ProcessBuilder`, `Runtime.getRuntime().exec()`) or manipulate `%LOCALAPPDATA%` / DPAPI files on disk. External process spawning from a game client triggers AV/EDR detections and creates persistence artifacts. In-memory bypasses have zero disk footprint and zero telemetry signatures.


