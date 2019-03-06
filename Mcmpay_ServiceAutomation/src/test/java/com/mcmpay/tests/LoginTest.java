package com.mcmpay.tests;

import static io.restassured.RestAssured.given;

import java.util.Hashtable;
import java.util.regex.Pattern;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.testng.annotations.Test;

import com.mcmpay.base.BaseTest;
import com.mcmpay.base.Session;

public class LoginTest extends BaseTest {

	@Test(dataProvider = "getData")
	public void doLogin(Hashtable<String, String> data) {

		System.out.println(data.get("Username"));
		System.out.println(data.get("Password"));

		String u = data.get("Username");
		String p = data.get("Password");

		Session s = new Session();

		s.setUsername(u);
		s.setPassword(p);

		Response response = given().contentType(ContentType.JSON).log().all()
				.when().body(s).post("/login");

		response.prettyPrint();
		sessionid = response.header("sessionid");		

		System.out.println("Session id is" + sessionid);


		JsonPath jsonPathEvaluator = response.jsonPath();

		String errormsg = jsonPathEvaluator.get("errMsg");

		String status = jsonPathEvaluator.get("loginStatus");

		System.out.println("Status received from Response:--> " + status);

		System.out.println("Expected Login Status :-->"
				+ data.get("ExpectedLoginStatus"));

		if (!data.get("ExpectedLoginStatus").equals(status))
			reportFailure(errormsg, false);

		if (!Pattern.matches("\\w", sessionid)) {
			 reportFailure("sessionid Format not valid", false);

		
		
		}
		
		softAssert.assertAll();

	}

}
