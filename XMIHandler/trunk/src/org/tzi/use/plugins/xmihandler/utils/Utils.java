package org.tzi.use.plugins.xmihandler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.tzi.use.uml.mm.ModelFactory;
import org.tzi.use.util.Log;

public class Utils {

	private static ResourceSet resourceSet = null;

	private static ModelFactory modelFactory = null;

	private static PrintWriter logWriter = null;

	private static File currentDirectory = null;


	public static void out(String output) {
		Log.println(output);
		if (logWriter != null) {
			logWriter.println(output);
		}
	}

	public static void error(Exception error) {
		error.printStackTrace();
		String errMsg = error.getMessage();
		if (errMsg == null || errMsg.isEmpty()) {
			errMsg = "Unknown Error";
		} else {
			errMsg = "Error: " + errMsg;
		}
		if (Log.isDebug()) {
			Log.error(error);
		} else {
			Log.error(errMsg);
		}
		if (logWriter != null) {
			logWriter.println(errMsg);
		}
	}

	public static void setLogWriter(PrintWriter theLogWriter) {
		logWriter = theLogWriter;
	}

	public static ModelFactory getModelFactory() {
		if (modelFactory == null) {
			modelFactory = new ModelFactory();
		}
		return modelFactory;
	}

	private static ResourceSet getResourceSet() throws Exception {

		if (resourceSet == null) {
			resourceSet = new ResourceSetImpl();

			UMLResourcesUtil.init(resourceSet);
		}

		return resourceSet;

	}

	public static Resource getResource(URI uri) throws Exception {

		if (!"xmi".equals(uri.fileExtension())
				&& !"uml".equals(uri.fileExtension())) {
			// Make sure we have a recognized file extension
			uri = uri.appendFileExtension("xmi");
		}

		ResourceSet rs = getResourceSet();

		if (rs == null) {
			throw new Exception("Failed to create resource set");
		}

		Resource r = rs.createResource(uri);

		if (r == null) {
			throw new Exception("Failed to create resource for URI " + uri);
		}

		return r;
	}

	public static String getXmiId(EObject x) {
		Resource resource = x.eResource();
		if (resource instanceof XMLResource) {
			XMLResource xmlResource = (XMLResource)resource;
			return xmlResource.getID(x);
		}
		return "";
	}

	public static boolean canWrite(File file) {
		try {
			new FileOutputStream(file, true).close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static File getCurrentDirectory() {
		if (currentDirectory == null) {
			currentDirectory = new File (System.getProperty("user.home"));
		}
		if (!currentDirectory.isDirectory()) {
			currentDirectory = currentDirectory.getParentFile();
		}
		return currentDirectory;
	}

	public static void setCurrentDirectory(File theCurrentDirectory) {
		currentDirectory = theCurrentDirectory;
	}

}
