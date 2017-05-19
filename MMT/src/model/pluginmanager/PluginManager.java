package model.pluginmanager;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import dev.Plugin;

public class PluginManager {

	private static LinkedList<PluginHierarchyElement> plugins;
	public static LinkedList<URLClassLoader> classes;

	/**
	 * adds
	 * 
	 * @param menu
	 */
	public static LinkedList<PluginHierarchyElement> getPluginList() {
		return plugins;
	}

	@SuppressWarnings("unchecked")
	private static void loadPlugins(File dir,
			LinkedList<PluginHierarchyElement> appendList) throws Exception {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				Subgroup s = new Subgroup(file.getName());
				appendList.add(s);
				loadPlugins(file, s);
			} else if (file.getName().toLowerCase().endsWith(".jar")) {
				PluginManager.addPath(file.getPath());
				String path = file.getPath();
				JarFile jar = new JarFile(path);
				String interfaceVersion = jar.getManifest().getMainAttributes().getValue("MMTPluginInterfaceVersion");
				if(interfaceVersion != null && interfaceVersion.equals(Plugin.INTERFACE_VERSION)){
				Enumeration<JarEntry> en = jar.entries();

				URL[] urls = { new URL("jar:file:" + path + "!/") };
				URLClassLoader cl = URLClassLoader.newInstance(urls);
				classes.add(cl);
				while (en.hasMoreElements()) {
					JarEntry je = en.nextElement();
					if (je.isDirectory() || !je.getName().endsWith(".class")) {
						continue;
					}

					String className = je.getName().substring(0,
							je.getName().length() - 6);
					className = className.replace('/', '.');
					Class<?> c = cl.loadClass(className);
					if (c.getSuperclass() == Plugin.class) {
						appendList.add(new Loadable(
								(Class<? extends Plugin>) c, jar.getManifest()
										.getMainAttributes()));

					} else {

					}
				}
				}
				jar.close();

			}

		}
	}

	public static void loadPlugins() throws Exception {
		plugins = new LinkedList<PluginHierarchyElement>();
		classes = new LinkedList<URLClassLoader>();
		File f = new File("./plugin");
		loadPlugins(f, plugins);
	}

	public static void addPath(String s) throws Exception {
		File f = new File(s);
		URI u = f.toURI();
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		Class<URLClassLoader> urlClass = URLClassLoader.class;
		Method method = urlClass.getDeclaredMethod("addURL",
				new Class[] { URL.class });
		method.setAccessible(true);
		method.invoke(urlClassLoader, new Object[] { u.toURL() });
	}
}
