package com.mcmpay.base;

import static io.restassured.RestAssured.given;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.commons.io.output.WriterOutputStream;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.mcmpay.util.DataUtil;
import com.mcmpay.util.ExtentManager;
import com.mcmpay.util.Xls_Reader;

import io.restassured.RestAssured;

public class BaseTest {

	public static String sessionid;

	public ExtentReports rep;
	public static String reportFolder;
	public int iteration;
	public ExtentTest test;	

	public Properties testprop;
	public Xls_Reader xls;
	public SoftAssert softAssert = new SoftAssert();
	public static StringWriter requestWritier;
	public static PrintStream requestCapture;

	


	@BeforeTest
	public void init() {

		testprop = new Properties();

		try {
			FileInputStream fs = new FileInputStream(
					System.getProperty("user.dir")
							+ "//src//test//resources//project.properties");

			testprop.load(fs); // loading the Properties

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		xls = new Xls_Reader(testprop.getProperty("xlspath"));
		RestAssured.baseURI = testprop.getProperty("baseurl");
		String testname = this.getClass().getSimpleName().toLowerCase();
		RestAssured.basePath = testprop.getProperty(testname);

	}

	@BeforeMethod
	public void before() {
		iteration++;
		rep = ExtentManager.getInstance(testprop.getProperty("reportPath"));
		test = rep.createTest("Login");
		

		requestWritier = new StringWriter();
		requestCapture = new PrintStream(new WriterOutputStream(requestWritier),true);


	}

	@DataProvider
	public Object[][] getData() {

		return DataUtil.getData1(xls, this.getClass().getSimpleName());
	}

	public void reportFailure(String errMsg, boolean stop) {
		softAssert.fail(errMsg);

		if (stop)
			softAssert.assertAll();

	}
	
	

	
	@AfterMethod
	public void after(){
		rep.flush();
	}
	
	public void addReqResLinkToReport(String message,String fileName,String content){
		// file create
		try {
			System.out.println(reportFolder);
			System.out.println(reportFolder+"//log//"+fileName+".html");
			new File(reportFolder+"//log//"+fileName+".html").createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// write
		FileWriter fw;
		try {
			fw = new FileWriter(reportFolder+"//log//"+fileName+".html");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// add to report
		test.log(Status.INFO, "<a href='log/"+fileName+".html' target='_blank'>Click Here for "+message+"</a>");
		
		
	}

	
	

}
