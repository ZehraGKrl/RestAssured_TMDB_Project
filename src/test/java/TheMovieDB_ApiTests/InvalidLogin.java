package TheMovieDB_ApiTests;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
public class InvalidLogin {

    Faker randomMaker=new Faker();
    RequestSpecification reqSpec;
    int userID=0;

    @BeforeClass
    public void Setup(){
        baseURI="https://www.themoviedb.org";
        reqSpec= new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYjU0YWQ4YTI1N2RmZWJiZWRjZDdlMDY2ZDllZjRlYiIsInN1YiI6IjY2M2NiY2VlMzMzMjM2ZDg1OWEzOWY0MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.28sPUhvJakuiNY3u9cHnVJbCAN5pzgwbiz_0ZzYqyyM")
                .setContentType(ContentType.JSON)
                .build();
    }


    @Test
    public void InvalidLogin_Test() {

        String rndmFullName = RandomStringUtils.randomAlphabetic(10);
        String rndmPassword = RandomStringUtils.randomAlphanumeric(12);

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", rndmFullName);
        userInfo.put("password", rndmPassword);


        given()
                .spec(reqSpec)
                .body(userInfo)
                .post("/login")
                .then()
                .statusCode(400);
    }
}
