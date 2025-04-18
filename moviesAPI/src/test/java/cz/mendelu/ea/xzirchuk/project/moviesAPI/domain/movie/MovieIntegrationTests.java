//package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;
//
//
//import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorRequest;
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.UUID;
//
//import static io.restassured.RestAssured.when;
//import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
//import static org.hamcrest.Matchers.*;
//
//@ActiveProfiles("test")
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql("/test-data/cleanup.sql")
//@Sql("/test-data/basedata.sql")
//public class MovieIntegrationTests {
//    private final static String BASE_URI = "http://localhost";
//
//    @LocalServerPort
//    private int port;
//
//    @BeforeEach
//    public void configureRestAssured() {
//        RestAssured.baseURI = BASE_URI;
//        RestAssured.port = port;
//    }
//
//    @Test
//    public void getMoviesPage(){
//       RestAssured.given()
//               .when()
//               .get("/movies/?pageSize=20&pageNumber=0")
//               .then()
//               .statusCode(200)
//               .body("items.size()", is(2))
//               .body("items.id",containsInAnyOrder("3b2e4c38-4c62-4884-a1d3-908cc1a3d9a7", "07ab22d5-67c7-45ec-953a-17ca0e1a8bd6"))
//               .body("items.title",containsInAnyOrder("Kill me","Bruh"))
//               .body("items.overview",containsInAnyOrder("no idea","idk"));
//    }
//    @Test
//    public void getMoviesSplitByPage() {
//        RestAssured.given()
//                .when()
//                .get("/movies/?pageSize=1&pageNumber=0")
//                .then()
//                .statusCode(200)
//                .body("items.size()", is(1))
//                .body("items.id", containsInAnyOrder("07ab22d5-67c7-45ec-953a-17ca0e1a8bd6"))
//                .body("items.title", containsInAnyOrder("Kill me"))
//                .body("items.overview", containsInAnyOrder("idk"));
//        RestAssured.given()
//                .when()
//                .get("/movies/?pageSize=1&pageNumber=1")
//                .then()
//                .statusCode(200)
//                .body("items.size()", is(1))
//                .body("items.id", containsInAnyOrder("3b2e4c38-4c62-4884-a1d3-908cc1a3d9a7"))
//                .body("items.title", containsInAnyOrder("Bruh"))
//                .body("items.overview", containsInAnyOrder("no idea"));
//    }
//
//    @Test
//    public void getMoviesTooHighPageNumber(){
//        RestAssured.given()
//                .when()
//                .get("/movies/?pageSize=20&pageNumber=172")
//                .then()
//                .statusCode(404);
//
//    }
//
//    @Test
//    public void getMoviesBadInputPageNumber(){
//        RestAssured.given()
//                .when()
//                .get("/movies/?pageSize=20&pageNumber=abce")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void getDirectorBadInputPageSize(){
//        RestAssured.given()
//                .when()
//                .get("/movies/?pageSize=abc&pageNumber=0")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void getDirectorBadInputBothParameters(){
//        RestAssured.given()
//                .when()
//                .get("/movies/?pageSize=abc&pageNumber=xyz")
//                .then()
//                .statusCode(400);
//
//    }
//
//    @Test
//    public void getMoviesWithBigPageSize(){
//        RestAssured.given()
//                .when()
//                .get("/movies/?pageSize=2000&pageNumber=0")
//                .then()
//                .statusCode(200)
//                .body("items.size()", is(2))
//                .body("items.id",containsInAnyOrder("3b2e4c38-4c62-4884-a1d3-908cc1a3d9a7", "07ab22d5-67c7-45ec-953a-17ca0e1a8bd6"))
//                .body("items.title",containsInAnyOrder("Kill me","Bruh"))
//                .body("items.overview",containsInAnyOrder("no idea","idk"));
//    }
//
//    @Test
//    public void getMovieByIdSucc(){
//        RestAssured.given()
//                .when()
//                .get("/movies/07ab22d5-67c7-45ec-953a-17ca0e1a8bd6")
//                .then()
//                .statusCode(200)
//                .body("content.id",is("07ab22d5-67c7-45ec-953a-17ca0e1a8bd6"))
//                .body("content.title",is("Kill me"))
//                .body("content.overview",is("idk"));
//    }
//
//    @Test
//    public void getMovieByIdWrongFormat(){
//        RestAssured.given()
//                .when()
//                .get("/movies/d7g")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void getMovieByIdDoesntExist(){
//        RestAssured.given()
//                .when()
//                .get("/movies/d639c6ea-ee0a-47d8-80ae-398152a2224a")
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    public void createMovie(){
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        String id = RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(201)
//                .extract()
//                .path("content.id");
//
//        when()
//                .get("/movies/" + id)
//                .then()
//                .statusCode(200)
//                .body("content.id", is(id))
//                .body("content.title", is("There are spiders under my skin"))
//                .body("content.overview", is("I can feel them crawling under my skin"));
//    }
//    @Test
//    public void createMovieDuplicate(){
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(201);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(409);
//    }
//
//    @Test
//    public void createMovieBadGenreName() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy,Something else",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNullName() {
//        var newMovie = new MovieRequest(
//                null,
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNullReleaseYear() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                null,
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNullOverview() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                null,
//                20,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieEmptyOverview() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "",
//                20,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieTooHighMetaScore() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                200,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNegativeMetaScore() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                -10,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNullMetaScore() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                null,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void crateMovieEmptyCertificate() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNullCertificate() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                null,
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNullRuntime() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                null,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNegativeRuntime() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                -100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieZeroRuntime() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                0,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNullGenre() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                null,
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//
//    @Test
//    public void createMovieEmptyGenre() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNegativeIMDBRating() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy",
//                -1.2f,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNullIMDBRating() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy",
//                null,
//                5000f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNegativeRevenue() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                -100f,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieNullRevenue() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                null,
//                UUID.fromString("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void createMovieInvalidDirectorId() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("fa4bf3ee-0c1c-4f52-b5b0-04b616f6e528")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(404);
//    }
//    @Test
//    public void createMovieAllNull() {
//        var newMovie = new MovieRequest(
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .post("/movies/")
//                .then()
//                .statusCode(400);
//    }
//// BRUH i can't 💀💀💀
//@Test
//public void updateMovie() {
//    var newMovie = new MovieRequest(
//            "There are spiders under my skin",
//            "2024-01-01",
//            "I can feel them crawling under my skin",
//            20,
//            "A",
//            100,
//            "Comedy",
//            1.2f,
//            5000f,
//            UUID.fromString("d639c6ea-ee0a-47d8-80ae-398152a2644a")
//    );
//    String id = RestAssured.given()
//            .contentType(ContentType.JSON)
//            .body(newMovie)
//            .when()
//            .put("/movies/"+"07ab22d5-67c7-45ec-953a-17ca0e1a8bd6")
//            .then()
//            .statusCode(202)
//            .extract()
//            .path("content.id");
//    when()
//            .get("/movies/" + id)
//            .then()
//            .statusCode(200)
//            .body("content.id", is(id))
//            .body("content.title", is("There are spiders under my skin"))
//            .body("content.overview", is("I can feel them crawling under my skin"))
//            .body("content.director.id",is("d639c6ea-ee0a-47d8-80ae-398152a2644a"));
//
//}
//
//
//    @Test
//    public void updateMovieBadRequest() {
//        var newMovie = new MovieRequest(
//                null,
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("d639c6ea-ee0a-47d8-80ae-398152a2644a")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .put("/movies/"+"07ab22d5-67c7-45ec-953a-17ca0e1a8bd6")
//                .then()
//                .statusCode(400);
//
//        when()
//                .get("/movies/" + "07ab22d5-67c7-45ec-953a-17ca0e1a8bd6")
//                .then()
//                .statusCode(200)
//                .body("content.id", is("07ab22d5-67c7-45ec-953a-17ca0e1a8bd6"))
//                .body("content.title", is("Kill me"))
//                .body("content.overview", is("idk"))
//                .body("content.director.id",is("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528"));
//
//    }
//    @Test
//    public void updateMovieDirectorDoesntExist() {
//        var newMovie = new MovieRequest(
//                "There are spiders under my skin",
//                "2024-01-01",
//                "I can feel them crawling under my skin",
//                20,
//                "A",
//                100,
//                "Comedy",
//                1.2f,
//                5000f,
//                UUID.fromString("d63926ea-ee0a-47d8-80ae-398152a2644a")
//        );
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(newMovie)
//                .when()
//                .put("/movies/"+"07ab22d5-67c7-45ec-953a-17ca0e1a8bd6")
//                .then()
//                .statusCode(404);
//
//        when()
//                .get("/movies/" + "07ab22d5-67c7-45ec-953a-17ca0e1a8bd6")
//                .then()
//                .statusCode(200)
//                .body("content.id", is("07ab22d5-67c7-45ec-953a-17ca0e1a8bd6"))
//                .body("content.title", is("Kill me"))
//                .body("content.overview", is("idk"))
//                .body("content.director.id",is("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528"));
//    }
//
//    @Test
//    public void deleteMovie(){
//
//        RestAssured.given()
//                .when()
//                .delete("/movies/07ab22d5-67c7-45ec-953a-17ca0e1a8bd6")
//                .then()
//                .statusCode(204);
//
//
//        RestAssured.given()
//                .when()
//                .get("/movies/?pageNumber=0&pageSize=20")
//                .then()
//                .statusCode(200)
//                .body("items.size()", is(1))
//                .body("items.id",containsInAnyOrder("3b2e4c38-4c62-4884-a1d3-908cc1a3d9a7"))
//                .body("items.title",containsInAnyOrder("Bruh"))
//                .body("items.overview",containsInAnyOrder("no idea"));
//    }
//
//    @Test
//    public void getMoviesByRevenueWithFilters() {
//
//        RestAssured.given()
//                .when()
//                .get("/movies/topMoviesByRevenueWithFilter?top_n=200&genre=Comedy")
//                .then()
//                .statusCode(200)
//                .body("items.size()", is(2))
//                .body("items.id", containsInRelativeOrder("07ab22d5-67c7-45ec-953a-17ca0e1a8bd6","3b2e4c38-4c62-4884-a1d3-908cc1a3d9a7"))
//                .body("items.title", containsInRelativeOrder("Kill me","Bruh"))
//                .body("items.overview", containsInAnyOrder("idk","no idea"));
//    }
//    @Test
//    public void getMoviesByImdbWithFilters() {
//
//        RestAssured.given()
//                .when()
//                .get("/movies/topMoviesByImdbWithFilter?top_n=5&genre=Comedy")
//                .then()
//                .statusCode(200)
//                .body("items.size()", is(2))
//                .body("items.id", containsInRelativeOrder("07ab22d5-67c7-45ec-953a-17ca0e1a8bd6","3b2e4c38-4c62-4884-a1d3-908cc1a3d9a7"))
//                .body("items.title", containsInRelativeOrder("Kill me","Bruh"))
//                .body("items.overview", containsInAnyOrder("idk","no idea"));
//    }
//    @Test
//    public void getMoviesByImdbWithFiltersWrongInput() {
//
//        RestAssured.given()
//                .when()
//                .get("/movies/topMoviesByImdbWithFilter?top_n=5&genre=aldfj")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void getMoviesByRevenueWithFiltersWrongInput() {
//
//        RestAssured.given()
//                .when()
//                .get("/movies/topMoviesByRevenueWithFilter?top_n=5&genre=aldfj")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void deleteMovieWrongId() {
//
//        RestAssured.given()
//                .when()
//                .delete("/movies/07ab2225-67c7-45ec-953a-17ca0e1a8bd6")
//                .then()
//                .statusCode(404);
//
//
//        RestAssured.given()
//                .when()
//                .get("/movies/?pageNumber=0&pageSize=20")
//                .then()
//                .statusCode(200)
//                .body("items.size()", is(2))
//                .body("items.id", containsInAnyOrder("3b2e4c38-4c62-4884-a1d3-908cc1a3d9a7", "07ab22d5-67c7-45ec-953a-17ca0e1a8bd6"))
//                .body("items.title", containsInAnyOrder("Kill me", "Bruh"))
//                .body("items.overview", containsInAnyOrder("no idea", "idk"));
//
//
//    }
//
//
//
//
//
//}
