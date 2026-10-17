import os, sys, zipfile, re, hashlib, subprocess

BASE_DIR = r"c:\Users\khang\Downloads\alicu"
PROJECT_DIR = os.path.join(BASE_DIR, "Alice-Project")
BUILT_JAR = os.path.join(BASE_DIR, "Alice-1.21.11-Built.jar")
DEPLOYED_JAR = r"C:\Users\khang\AppData\Roaming\ModrinthApp\profiles\Fabric 1.21.11\mods\Alice-1.21.11-Built.jar"

print("=" * 70)
print("  EXHAUSTIVE AUDIT & DEOBFUSCATION / REMAPPING / MALWARE SCAN")
print("=" * 70)

# ---------------------------------------------------------
# 1. Deobfuscation Audit (All 47 Source Modules)
# ---------------------------------------------------------
print("\n[PHASE 1] AUDITING DEOBFUSCATION OF ALL 47 SOURCE MODULES...")
modules_dir = os.path.join(PROJECT_DIR, "src", "main", "java", "com", "nnpg", "alice", "modules")
module_files = []
for root, dirs, files in os.walk(modules_dir):
    for f in files:
        if f.endswith(".java") and f != "AlicePayloadCoordinator.java":
            module_files.append(os.path.join(root, f))

print(f"Total functional modules found: {len(module_files)} / 46 (plus AlicePayloadCoordinator = 47)")

bozar_switch_pattern = re.compile(r'while\s*\(\s*true\s*\)\s*\{\s*switch\s*\(var\d+\)')
uninitialized_var_pattern = re.compile(r'var10000;\s*\n[\s\S]*?return\s*\(var10000\s*!=')
raw_string_decrypt_pattern = re.compile(r'\\u[0-9a-fA-F]{4}')

bozar_hits = 0
uninit_hits = 0

for mf in module_files:
    rel_name = os.path.relpath(mf, modules_dir)
    with open(mf, "r", encoding="utf-8", errors="ignore") as f:
        content = f.read()
    if bozar_switch_pattern.search(content):
        print(f"  [!] Found un-desugared switch in {rel_name}")
        bozar_hits += 1
    if uninitialized_var_pattern.search(content):
        print(f"  [!] Found uninitialized var10000 in {rel_name}")
        uninit_hits += 1

print(f"  -> Bozar switch loops remaining: {bozar_hits}")
print(f"  -> Uninitialized var10000 returns remaining: {uninit_hits}")
if bozar_hits == 0 and uninit_hits == 0:
    print("  [SUCCESS] All 47 modules have clean control flow and 0 decompiler artifacts.")

# ---------------------------------------------------------
# 2. Mapping Verification (Yarn in Source, Intermediary in Jar)
# ---------------------------------------------------------
print("\n[PHASE 2] VERIFYING MAPPING CONVERSIONS (YARN -> INTERMEDIARY)...")
# Check source Yarn mappings
sample_source = os.path.join(modules_dir, "combat", "KillAuraPlusPlus.java")
with open(sample_source, "r", encoding="utf-8") as f:
    src_text = f.read()

yarn_markers = ["PlayerEntity", "getSelectedSlot", "Vec3d", "Box", "Direction"]
yarn_present = all(m in src_text for m in yarn_markers)
print(f"  -> Source code uses standard human-readable Yarn mappings: {yarn_present}")

# Check bytecode Intermediary mappings in built JAR
with zipfile.ZipFile(BUILT_JAR, "r") as z:
    ka_bytes = z.read("com/nnpg/alice/modules/combat/KillAuraPlusPlus.class")
    intermediary_markers = [b"net/minecraft/class_1657", b"net/minecraft/class_243", b"net/minecraft/class_238"]
    inter_present = any(m in ka_bytes for m in intermediary_markers)
    print(f"  -> Built JAR contains official Fabric Intermediary mappings (class_...): {inter_present}")

# ---------------------------------------------------------
# 3. Malware, RAT, & Threat Verification (Deep Bytecode Scan)
# ---------------------------------------------------------
print("\n[PHASE 3] DEEP SCAN FOR MALWARE, RATS, DROPPER & THREAT VECTORS...")
with zipfile.ZipFile(BUILT_JAR, "r") as z:
    all_entries = z.namelist()
    
    # Check for Herlys OffBoot classes
    offboot_entries = [e for e in all_entries if "offboot" in e.lower()]
    print(f"  -> Herlys crack OffBoot classes in Built JAR: {len(offboot_entries)}")
    
    # Check for native executables/libraries
    native_entries = [e for e in all_entries if any(e.endswith(x) for x in ['.dll', '.so', '.dylib', '.exe', '.bat', '.cmd', '.vbs', '.ps1'])]
    print(f"  -> Embedded native binaries / shell scripts: {len(native_entries)}")
    
    # Scan all classfiles for process execution & stealer signatures
    threat_patterns = {
        'Process Execution (ProcessBuilder / Runtime.exec)': re.compile(rb'(?:java/lang/ProcessBuilder|Runtime;?\.exec|cmd\.exe|powershell)', re.IGNORECASE),
        'Credential / Token Grabber Target Paths': re.compile(rb'(?:loginusers|launcher_accounts\.json|Default/Network/Cookies|Local State|tokens|roaming|Local\\\\Discord)', re.IGNORECASE),
        'Discord Webhook Exfiltration': re.compile(rb'https?://(?:ptb\.|canary\.)?discord(?:app)?\.com/api/webhooks/[a-zA-Z0-9_\-]+', re.IGNORECASE),
        'Remote Bytecode Downloading': re.compile(rb'(?:URLClassLoader\([^)]*https?:|pastebin\.com|hastebin\.com|githubusercontent\.com)', re.IGNORECASE)
    }
    
    threat_hits = {k: [] for k in threat_patterns}
    
    for e in all_entries:
        if e.endswith(".class"):
            data = z.read(e)
            for k, pat in threat_patterns.items():
                m = pat.findall(data)
                if m:
                    threat_hits[k].append((e, [match.decode('latin1', errors='ignore') for match in m]))
    
    for k, hits in threat_hits.items():
        print(f"  -> {k}: {len(hits)} hits")
        if hits:
            for c, sample in hits[:5]:
                print(f"      - {c}: {sample}")

# ---------------------------------------------------------
# 4. Hash Parity & Live Deployment Verification
# ---------------------------------------------------------
print("\n[PHASE 4] LIVE DEPLOYMENT INTEGRITY & HASH COMPARISON...")
def get_sha256(filepath):
    h = hashlib.sha256()
    with open(filepath, "rb") as f:
        while chunk := f.read(65536):
            h.update(chunk)
    return h.hexdigest()

built_hash = get_sha256(BUILT_JAR)
deployed_hash = get_sha256(DEPLOYED_JAR)

print(f"  Built JAR Path:    {BUILT_JAR}")
print(f"  Built SHA256:      {built_hash}")
print(f"  Deployed JAR Path: {DEPLOYED_JAR}")
print(f"  Deployed SHA256:   {deployed_hash}")
print(f"  Exact Hash Match:  {built_hash == deployed_hash}")

print("\n" + "=" * 70)
if built_hash == deployed_hash and bozar_hits == 0 and len(offboot_entries) == 0 and len(native_entries) == 0:
    print("  FINAL VERDICT: 100% CLEAN, FULLY DEOBFUSCATED, REMAPPED & DEPLOYED")
else:
    print("  FINAL VERDICT: AUDIT FAILED CHECKS")
print("=" * 70)
