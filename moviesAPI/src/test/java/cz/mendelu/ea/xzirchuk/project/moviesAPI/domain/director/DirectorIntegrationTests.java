package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/test-data/cleanup.sql")
@Sql("/test-data/base-data.sql")
public class DirectorIntegrationTests {
    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    public void getDirectorsOnOnePage() {
        given()
                .when()
                .get("/directors/?pageSize=20&pageNumber=0")
                .then()
                .statusCode(200)
                .body("items.size()", is(2))
                .body("items.id",containsInAnyOrder("d639c6ea-ee0a-47d8-80ae-398152a2644a", "fa4bfeee-0c1c-4f52-b5b0-04b616f6e528"))
                .body("items.name",containsInAnyOrder("John Doe","Joe Rogan"))
                .body("items.net_worth",containsInAnyOrder(3.4f,1.2f));

    }
    @Test
    public void getDirectorsSplitByPage() {
        given()
                .when()
                .get("/directors/?pageSize=1&pageNumber=0")
                .then()
                .statusCode(200)
                .body("items.size()", is(1))
                .body("items.id",containsInAnyOrder("d639c6ea-ee0a-47d8-80ae-398152a2644a"))
                .body("items.name",containsInAnyOrder("John Doe"))
                .body("items.net_worth",containsInAnyOrder(3.4f));
        given()
                .when()
                .get("/directors/?pageSize=1&pageNumber=1")
                .then()
                .statusCode(200)
                .body("items.size()", is(1))
                .body("items.id",containsInAnyOrder("fa4bfeee-0c1c-4f52-b5b0-04b616f6e528"))
                .body("items.name",containsInAnyOrder("Joe Rogan"))
                .body("items.net_worth",containsInAnyOrder(1.2f));
    }

    @Test
    public void getDirectorsTooHighPageNumber(){
        given()
                .when()
                .get("/directors/?pageSize=20&pageNumber=172")
                .then()
                .statusCode(404);

    }

    @Test
    public void getDirectorBadInputPageNumber(){
        given()
                .when()
                .get("/directors/?pageSize=20&pageNumber=abce")
                .then()
                .statusCode(400);

    }
    @Test
    public void getDirectorBadInputPageSize(){
        given()
                .when()
                .get("/directors/?pageSize=abc&pageNumber=0")
                .then()
                .statusCode(400);

    }
    @Test
    public void getDirectorBadInputBothParameters(){
        given()
                .when()
                .get("/directors/?pageSize=abc&pageNumber=xyz")
                .then()
                .statusCode(400);

    }

    @Test
    public void getDirectorsWithBigPageSize(){
        given()
                .when()
                .get("/directors/?pageSize=2000&pageNumber=0")
                .then()
                .statusCode(200)
                .body("items.size()", is(2))
                .body("items.id",containsInAnyOrder("d639c6ea-ee0a-47d8-80ae-398152a2644a", "fa4bfeee-0c1c-4f52-b5b0-04b616f6e528"))
                .body("items.name",containsInAnyOrder("John Doe","Joe Rogan"))
                .body("items.net_worth",containsInAnyOrder(3.4f,1.2f));
    }

    @Test
    public void getDirectorByIdSucc(){
        given()
                .when()
                .get("/directors/d639c6ea-ee0a-47d8-80ae-398152a2644a")
                .then()
                .statusCode(200)
                .body("content.id",is("d639c6ea-ee0a-47d8-80ae-398152a2644a"))
                .body("content.name",is("John Doe"))
                .body("content.net_worth",is(3.4f));
    }
    @Test
    public void getDirectorByIdWrongFormat(){
        given()
                .when()
                .get("/directors/d7g")
                .then()
                .statusCode(400);

    }

    @Test
    public void getDirectorByIdDoesntExist(){
        given()
                .when()
                .get("/directors/d639c6ea-ee0a-47d8-80ae-398152a2224a")
                .then()
                .statusCode(404);
    }

    @Test
    public void createDirector(){
        var newDirector = new DirectorRequest(
                "Johny Test",
                1.3f
        );
        String id = given()
                .contentType(ContentType.JSON)
                .body(newDirector)
        .when()
                .post("/directors/")
                .then()
                .statusCode(201)
                .extract()
                .path("content.id");

        when()
                .get("/directors/" + id)
                .then()
                .statusCode(200)
                .body("content.id", is(id))
                .body("content.net_worth", is(1.3f))
                .body("content.name", is("Johny Test"));
    }

    @Test
    public void createDirectorDuplicateName(){
        var newDirector = new DirectorRequest(
                "Johny Test",
                1.3f
        );
         given()
                .contentType(ContentType.JSON)
                .body(newDirector)
                .when()
                .post("/directors/")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(newDirector)
                .when()
                .post("/directors/")
                .then()
                .statusCode(409);
    }

    @Test
    public void createDirectorBadRequestName(){
        var newDirector = new DirectorRequest(
                null,
                1.3f
        );
        given()
                .contentType(ContentType.JSON)
                .body(newDirector)
                .when()
                .post("/directors/")
                .then()
                .statusCode(400);

    }
    @Test
    public void createDirectorBadRequestNetWorth(){
        var newDirector = new DirectorRequest(
                "Johny Test",
                null
        );
        given()
                .contentType(ContentType.JSON)
                .body(newDirector)
                .when()
                .post("/directors/")
                .then()
                .statusCode(400);

    }

    @Test
    public void updateDirector(){
        var newDirector = new DirectorRequest(
                "Johny Test",
                2.2f
        );
        String id = given()
                .contentType(ContentType.JSON)
                .body(newDirector)
                .when()
                .put("/directors/fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
                .then()
                .statusCode(202)
                .extract()
                .path("content.id");

        when()
                .get("/directors/" + id)
                .then()
                .statusCode(200)
                .body("content.id", is(id))
                .body("content.net_worth", is(2.2f))
                .body("content.name", is("Johny Test"));
    }

    @Test
    public void updateDirectorBadRequest(){
        var newDirector = new DirectorRequest(
                null,
                2.2f
        );
        String id = given()
                .contentType(ContentType.JSON)
                .body(newDirector)
                .when()
                .put("/directors/fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
                .then()
                .statusCode(400)
                .extract()
                .path("content.id");
    }

    @Test
    public void updateDirectorIdDoesntExist(){
        var newDirector = new DirectorRequest(
                "Johny Test",
                2.2f
        );
        String id = given()
                .contentType(ContentType.JSON)
                .body(newDirector)
                .when()
                .put("/directors/fa4bfeee-0c1c-4f52-b5b0-14b616f6e528")
                .then()
                .statusCode(404)
                .extract()
                .path("content.id");
    }
    @Test
    public void deleteDirector(){

         given()
                .when()
                .delete("/directors/fa4bfeee-0c1c-4f52-b5b0-04b616f6e528")
                .then()
                .statusCode(204);


         given()
                 .when()
                 .get("/directors/?pageNumber=0&pageSize=20")
                 .then()
                 .statusCode(200)
                 .body("items.size()", is(1))
                 .body("items.id",containsInAnyOrder("d639c6ea-ee0a-47d8-80ae-398152a2644a"))
                 .body("items.name",containsInAnyOrder("John Doe"))
                 .body("items.net_worth",containsInAnyOrder(3.4f));
    }
    @Test
    public void deleteDirectorIdDoesntExist(){

        given()
                .when()
                .delete("/directors/fa3bfeee-0c1c-4f52-b5b0-04b616f6e528")
                .then()
                .statusCode(404);


        given()
                .when()
                .get("/directors/?pageNumber=0&pageSize=20")
                .then()
                .statusCode(200)
                .body("items.size()", is(2))
                .body("items.id",containsInAnyOrder("d639c6ea-ee0a-47d8-80ae-398152a2644a", "fa4bfeee-0c1c-4f52-b5b0-04b616f6e528"))
                .body("items.name",containsInAnyOrder("John Doe","Joe Rogan"))
                .body("items.net_worth",containsInAnyOrder(3.4f,1.2f));

    }

    @Test
    public void getDirectorByNetworth(){

        given()
                .when()

                .get("/directors/directorsByNetWorth?top_n=6")
                .then()
                .statusCode(200)
                .body("items.size()", is(2))
                .body("items.id",containsInRelativeOrder("d639c6ea-ee0a-47d8-80ae-398152a2644a", "fa4bfeee-0c1c-4f52-b5b0-04b616f6e528"))
                .body("items.name",containsInRelativeOrder("John Doe","Joe Rogan"))
                .body("items.net_worth",containsInRelativeOrder(3.4f,1.2f));

    }

    @Test
    public void getDirectorByNetworthWrongInput(){

        given()
                .when()

                .get("/directors/directorsByNetWorth?top_n=abc")
                .then()
                .statusCode(400);

    }


}






