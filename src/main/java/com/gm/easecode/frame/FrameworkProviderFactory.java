package com.gm.easecode.frame;

import com.gm.easecode.frame.spi.AgricultureFrameProvider;
import com.gm.easecode.frame.spi.JavaEaseFrameProvider;

public class FrameworkProviderFactory {
	
	public static enum FrameworkProviderMode {
		JavaEaseFrame("JavaEaseFrame", "1.0.0"),
		AgricultureFrame("AgricultureFrame", "1.0.0");
		
		String name;
		String version;
		
		FrameworkProviderMode(String name, String version) {
			this.name = name;
			this.version = version;
		}

		public String getName() {
			return name;
		}

		public String getVersion() {
			return version;
		}
		
		public static FrameworkProviderMode getFrameworkProvider(String name, String version) {
			String frameKey = name + "#" + version;
			for (FrameworkProviderMode item : values()) {
				String key = item.getName() + "#" + item.getVersion();
				if (key.equalsIgnoreCase(frameKey)) {
					return item;
				}
			}
			return null;
		}
		
	}
	
//	private static Map<String, FrameworkProvider> providerMap = new ConcurrentHashMap<>();
//	
//	static {
//		String packageName = FrameworkProviderFactory.class.getPackage().getName() + ".spi";
//		List<Class<?>> list = findAllProvider(packageName);
//		for (Class<?> clazz : list) {
//			if (FrameworkProvider.class.isAssignableFrom(clazz)) {
//				try {
//					FrameworkProvider provider = (FrameworkProvider)clazz.newInstance();
//					String frameworkName = provider.getFrameworkName();
//					String frameworkVersion = provider.getFrameworkVersion();
//					String frameKey = frameworkName + "#" + frameworkVersion;
//					providerMap.put(frameKey, provider);
//				} catch (InstantiationException | IllegalAccessException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	
//	private static List<Class<?>> findAllProvider(String packageName) {
//		List<Class<?>> classes = new ArrayList<>();
//        String classPath = ClassLoader.getSystemResource("").getPath() + packageName.replace(".", "/");
//        File dir = new File(classPath);
//        if (dir.exists()) {
//            File[] files = dir.listFiles();
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    classes.addAll(findAllProvider(packageName + "." + file.getName()));
//                } else if (file.getName().endsWith(".class")) {
//                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
//                    try {
//                    	classes.add(Class.forName(className));
//                    } catch (Exception e) {
//                    	
//                    }
//                }
//            }
//        }
//        return classes;
//	}

	public static FrameworkProvider createFrameworkProvider(String frameworkName, String frameworkVersion) {
//		String frameKey = frameworkName + "#" + frameworkVersion;
//		return providerMap.get(frameKey);
		FrameworkProviderMode providerEnum = FrameworkProviderMode.getFrameworkProvider(frameworkName, frameworkVersion);
		if (providerEnum == null) {
			return null;
		}
		FrameworkProvider provider = null;
		switch (providerEnum) {
		case JavaEaseFrame:
			provider = new JavaEaseFrameProvider();
			break;
		case AgricultureFrame:
			provider = new AgricultureFrameProvider();
			break;
		}
		return provider;
	}
}
