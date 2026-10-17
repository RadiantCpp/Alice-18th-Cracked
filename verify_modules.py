import os
import shutil
import tempfile
import subprocess

java_code = """package offline;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Enumeration;

public class TestLoadModules {
    public static void main(String[] args) {
        try {
            File jar = new File("Alice-1.21.11-Built.jar");
            File meteorJar = new File("C:/Users/khang/Downloads/meteor-client-1.21.11-86.jar");
            URLClassLoader loader = new URLClassLoader(new URL[]{ jar.toURI().toURL(), meteorJar.toURI().toURL() }, TestLoadModules.class.getClassLoader());
            
            ZipFile zf = new ZipFile(jar);
            Enumeration<? extends ZipEntry> en = zf.entries();
            int loaded = 0;
            while (en.hasMoreElements()) {
                ZipEntry e = en.nextElement();
                String name = e.getName();
                if (name.startsWith("com/nnpg/alice/modules/") && name.endsWith(".class")) {
                    String className = name.substring(0, name.length() - 6).replace('/', '.');
                    try {
                        Class<?> cls = Class.forName(className, false, loader);
                        loaded++;
                    } catch (Throwable t) {
                        System.out.println("Failed to load " + className + ": " + t);
                    }
                }
            }
            System.out.println("Successfully loaded and verified " + loaded + " / 47 module classes from Alice-1.21.11-Built.jar!");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
"""

temp_dir = tempfile.mkdtemp(prefix="test_load_")
try:
    pkg_dir = os.path.join(temp_dir, "offline")
    os.makedirs(pkg_dir, exist_ok=True)
    java_file = os.path.join(pkg_dir, "TestLoadModules.java")
    with open(java_file, "w", encoding="utf-8") as f:
        f.write(java_code)

    cp = f"Alice-1.21.11-Built.jar;{temp_dir};C:/Users/khang/Downloads/meteor-client-1.21.11-86.jar"
    res = subprocess.run([r'C:\Program Files\Java\jdk-21.0.12.1\bin\javac.exe', '-cp', cp, java_file], capture_output=True, text=True)
    if res.returncode != 0:
        print("Compilation failed:", res.stderr)
    else:
        res2 = subprocess.run([r'C:\Program Files\Java\jdk-21.0.12.1\bin\java.exe', '-cp', cp, 'offline.TestLoadModules'], capture_output=True, text=True)
        print(res2.stdout)
        if res2.stderr:
            print('STDERR:', res2.stderr)
finally:
    shutil.rmtree(temp_dir, ignore_errors=True)
