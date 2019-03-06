package com.mcmpay.util;

import java.io.File;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.mcmpay.base.BaseTest;

public class ExtentManager {
	private static ExtentReports extent;

	public static ExtentReports getInstance(String path) {
		if (extent == null) {

			
			Date d = new Date();
			String folderName=d.toString().replace(":", "_").replace(" ","_");
			new File(path+folderName+"//log").mkdirs();
			BaseTest.reportFolder=path+folderName;
			String fileName = path+folderName+"//report.html";
			
			
			createInstance(fileName);

		}
		return extent;
	}

	public static ExtentReports createInstance(String fileName) {
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		htmlReporter.setAppendExisting(true);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		return extent;

	}

}
