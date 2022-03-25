import api.Register;
import api.Specifications;
import api.SuccessReg;
import api.UserData;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.junit.Test;


import java.util.List;

import static io.restassured.RestAssured.given;

public class RegresTest {
    private final static String URL = "https://reqres.in/";

    @Test
    public void checkAvatarAndTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        List<UserData> users = given()
                .when()
                .get( "api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath()
                .getList("data", UserData.class);
                users.forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
       Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in"))); // проверяет на все совпадения с частью строки
    }

    @Test
    public void successRegTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in", "pistol");
        SuccessReg successReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessReg.class);
        Assert.assertEquals(id, successReg.getId());
        Assert.assertEquals(token, successReg.getToken());

    }

}
