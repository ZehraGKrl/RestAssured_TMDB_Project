package TheMovieDB_ApiTests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class TmdbApiTests {

    RequestSpecification reqSpec;
    String authenticityToken;
    // String username = "zhrgkrl";
    // String password = "q1q1q1";
    public int accountID = 0;
    String url = "https://api.themoviedb.org/3";
    String mediaID = "5e959bc3db72c00014ad69d6";


    @BeforeClass
    public void Setup() {
        baseURI = "https://www.themoviedb.org";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYjU0YWQ4YTI1N2RmZWJiZWRjZDdlMDY2ZDllZjRlYiIsInN1YiI6IjY2M2NiY2VlMzMzMjM2ZDg1OWEzOWY0MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.28sPUhvJakuiNY3u9cHnVJbCAN5pzgwbiz_0ZzYqyyM")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void authenticityToken() {

        authenticityToken =
                given()
                        .when()
                        .get("/login")
                        .then()
                        .statusCode(200)
                        .extract().htmlPath().getString("**.find { it.@name == 'authenticity_token' }.@value");


        System.out.println("authenticityToken = " + authenticityToken);

    }

    @Test(dependsOnMethods = "authenticityToken")
    public void InvalidLogin() {

        String rndmFullName = RandomStringUtils.randomAlphabetic(10);
        String rndmPassword = RandomStringUtils.randomAlphanumeric(12);

        Map<String, String> user = new HashMap<>();
        user.put("username", rndmFullName);
        user.put("password", rndmPassword);


        given()
                .spec(reqSpec)
                .body(user)
                .post("/login")
                .then()
                .statusCode(400);
    }


    @Test(dependsOnMethods = "authenticityToken")
    public void getAccountDetails() {


        accountID =

                given()
                        .spec(reqSpec)
                        .when()
                        .get(url + "/account")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().path("id")

        ;
        System.out.println("accountID = " + accountID);

    }

    @Test(dependsOnMethods = "getAccountDetails")
    public void addToFavorites() {

        Map<String, Object> addToFavorites = new HashMap<>();
        addToFavorites.put("media_type", "movie");
        addToFavorites.put("media_id", "5e959bc3db72c00014ad69d6");
        addToFavorites.put("favorite", true);


        given()
                .spec(reqSpec)
                .body(addToFavorites)
                .when()
                .post(url + "/account/" + accountID + "/favorite")

                .then()
                .log().body()
                .statusCode(201)
        ;
    }

    @Test(dependsOnMethods = "addToFavorites")
    public void addToWatchlist() {

        Map<String, Object> addToWatchlist = new HashMap<>();
        addToWatchlist.put("media_type", "movie");
        addToWatchlist.put("media_id", mediaID);
        addToWatchlist.put("watchlist", true);


        given()
                .spec(reqSpec)
                .body(addToWatchlist)
                .when()
                .post(url + "/account/" + accountID + "/watchlist")

                .then()
                .log().body()
                .statusCode(201)
        ;
    }

    @Test(dependsOnMethods = "addToWatchlist")
    public void GetFavoriteMovies() {

        given()
                .spec(reqSpec)

                .when()
                .get(url + "/account" + "/" + accountID + "/favorite/movies")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "GetFavoriteMovies")
    public void GetRatedMovies() {

        given()
                .spec(reqSpec)

                .when()
                .get(url + "/account" + "/" + accountID + "/rated/movies")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "GetRatedMovies")
    public void GetWatchlistMovies() {

        given()
                .spec(reqSpec)

                .when()
                .get(url + "/account" + "/" + accountID + "/watchlist/movies")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "GetWatchlistMovies")
    public void GetMovieGenres() {

        given()
                .spec(reqSpec)

                .when()
                .get(url + "/genre/movie/list")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "GetMovieGenres")
    public void GetNowPlaying() {

        given()
                .spec(reqSpec)

                .when()
                .get(url + "/movie/now_playing")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "GetNowPlaying")
    public void GetPopulars() {

        given()
                .spec(reqSpec)

                .when()
                .get(url + "/movie/popular")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }
    @Test(dependsOnMethods = "GetPopulars")
    public void GetTopRated() {

        given()
                .spec(reqSpec)

                .when()
                .get(url + "/movie/top_rated")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "GetTopRated")
    public void GetUpcoming() {

        given()
                .spec(reqSpec)

                .when()
                .get(url + "/movie/upcoming")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }
    @Test(dependsOnMethods = "GetUpcoming")
    public void SearchForMovies(){

        given()
                .spec(reqSpec)
                .param("query","dune")

                .when()
                .get(url + "/search/movie")


                .then()
                .log().body()
                .statusCode(200)
        ;

    }


    @Test(dependsOnMethods = "SearchForMovies")
    public void SearchForKeywords(){

        given()
                .spec(reqSpec)
                .param("query","way")

                .when()
                .get(url + "/search/keyword")


                .then()
                .log().body()
                .statusCode(200)
        ;

    }
    @Test(dependsOnMethods = "SearchForKeywords")
    public void GetMovieDetails() {

        given()
                .spec(reqSpec)

                .when()
                .get(url + "/movie" + "/" +mediaID)

                .then()
                .log().body()
                .statusCode(200)
        ;
    }
    @Test(dependsOnMethods = "GetMovieDetails")
    public void AddMovieRating(){
        Map<String,Double> ratings=new HashMap<>();
        ratings.put("value", 8.5);

        given()
                .spec(reqSpec)
                .body(ratings)

                .when()
                .post(url + "/movie/"+mediaID+"/rating")

                .then()
                .log().body()
                .statusCode(201)

        ;

    }
    @Test(dependsOnMethods = "AddMovieRating")
    public void DeleteMovieRating(){


        given()
                .spec(reqSpec)


                .when()
                .delete(url + "/movie/"+mediaID+"/rating")

                .then()
                .log().body()
                .statusCode(200)

        ;

    }

    @Test(dependsOnMethods = "DeleteMovieRating")
    public void unauthorizedAccess(){

        int mediaID=18;
        Map<String, Integer> unauth = new HashMap<>();
        unauth.put("media_id",mediaID);


        int status_code=
                given()
                        .spec(reqSpec)
                        .pathParam("listID", "[valid_list_id]")
                        .queryParam("session_id","[invalid_session_id]")
                        .body(unauth)


                        .when()
                        .post(url+"/list/"+"{listID}"+"/add_item")


                        .then()
                        .statusCode(401)
                        //.log().body()
                        .extract().path("status_code")
                ;
        Assert.assertEquals(status_code,3);
    }

    }


