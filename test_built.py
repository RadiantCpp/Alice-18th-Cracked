import os
import shutil
import tempfile
import subprocess

code = """package offline;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class TestBuilt {
    public static void main(String[] args) {
        try {
            System.out.println("Testing Alice-1.21.11-Built.jar...");
            File meteorJar = new File("C:/Users/khang/Downloads/meteor-client-1.21.11-86.jar");
            URLClassLoader parent = new URLClassLoader(new URL[]{ meteorJar.toURI().toURL() }, TestBuilt.class.getClassLoader());
            
            AliceOfflineLoader loader = new AliceOfflineLoader(parent);
            Class<?> idCls = Class.forName("com.nnpg.alice.identity.PayloadIdentity", true, loader);
            long c = (Long)idCls.getMethod("seedContribution").invoke(null);
            long seed = 846455302749015062L ^ c;
            Class<?> wCls = Class.forName("com.nnpg.alice.protection.W", true, loader);
            wCls.getMethod("a", long.class).invoke(null, seed);
            System.out.println("Built JAR Protection unlocked OK with seed: " + seed);
            
            Class<?> aoCls = Class.forName("offline.AliceOffline", true, loader);
            Object ao = aoCls.getConstructor().newInstance();
            System.out.println("AliceOffline instantiated OK from Built JAR: " + ao);
            System.out.println("Package declaration: " + aoCls.getMethod("getPackage").invoke(ao));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
"""

temp_dir = tempfile.mkdtemp(prefix="test_built_")
try:
    pkg_dir = os.path.join(temp_dir, "offline")
    os.makedirs(pkg_dir, exist_ok=True)
    java_file = os.path.join(pkg_dir, "TestBuilt.java")
    with open(java_file, "w", encoding="utf-8") as f:
        f.write(code)

    cp = f"Alice-1.21.11-Built.jar;{temp_dir};C:/Users/khang/Downloads/meteor-client-1.21.11-86.jar"
    res = subprocess.run([r'C:\Program Files\Java\jdk-21.0.12.1\bin\javac.exe', '-cp', cp, java_file], capture_output=True, text=True)
    if res.returncode != 0:
        print("Compilation failed:", res.stderr)
    else:
        res2 = subprocess.run([r'C:\Program Files\Java\jdk-21.0.12.1\bin\java.exe', '-cp', cp, 'offline.TestBuilt'], capture_output=True, text=True)
        print(res2.stdout)
        if res2.stderr:
            print('STDERR:', res2.stderr)
finally:
    shutil.rmtree(temp_dir, ignore_errors=True)
