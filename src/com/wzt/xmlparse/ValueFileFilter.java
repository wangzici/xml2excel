package com.wzt.xmlparse;

import java.io.File;
import java.io.FilenameFilter;

public class ValueFileFilter implements FilenameFilter{

	public boolean accept(File dir, String name) {
		System.out.println("dir = " + dir.getAbsolutePath());
		if(dir.exists()&& dir.getName().startsWith("values")){
			return true;
		}
		return false;
	}

}
