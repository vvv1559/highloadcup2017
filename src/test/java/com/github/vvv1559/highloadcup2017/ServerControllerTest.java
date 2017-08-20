package com.github.vvv1559.highloadcup2017;

import com.github.vvv1559.highloadcup2017.dao.EntityNotFoundException;
import com.github.vvv1559.highloadcup2017.dao.ValidationException;
import com.github.vvv1559.highloadcup2017.dao.model.EntityType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServerControllerTest {

    private static final String EMPTY_RESPONSE = "{}";

    @Autowired
    private ServerController serverController;

    @Test
    public void getEntity() throws Exception {
        String user = serverController.getEntity(EntityType.users.name(), 1);
        String expected = "{\"id\":1,\"email\":\"omrorethacahraas@yandex.ru\",\"first_name\":\"Арина\",\"last_name\":\"Хопетасян\",\"gender\":\"f\",\"birth_date\":-74908800}";
        Assert.assertEquals(expected, user);

        String location = serverController.getEntity(EntityType.locations.name(), 1);
        expected = "{\"id\":1,\"place\":\"Речка\",\"country\":\"Камбоджа\",\"city\":\"Лейпатск\",\"distance\":80}";
        Assert.assertEquals(expected, location);

        String visit = serverController.getEntity(EntityType.visits.name(), 1);
        expected = "{\"id\":1,\"location\":68,\"user\":69,\"visited_at\":1081505959,\"mark\":1}";
        Assert.assertEquals(expected, visit);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getNoSuchUser() throws Exception {
        serverController.getEntity(EntityType.users.name(), 123456);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getNoSuchLocation() throws Exception {
        serverController.getEntity(EntityType.locations.name(), 123456);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getNoSuchVisit() throws Exception {
        serverController.getEntity(EntityType.visits.name(), 123456);
    }

    private void assertResponseIsEmpty(String response) {
        Assert.assertEquals(EMPTY_RESPONSE, response);
    }

    @Test
    public void updateUser() throws Exception {
        String userBeforeUpdate = serverController.getEntity(EntityType.users.name(), 2);
        String response = serverController.updateEntity(EntityType.users.name(), 2, "{\"email\":\"my_cool_email@mail.com\"}");
        String userAfterUpdate = serverController.getEntity(EntityType.users.name(), 2);

        String expectedAfterUpdate = "{\"id\":2,\"email\":\"my_cool_email@mail.com\",\"first_name\":\"Елена\",\"last_name\":\"Данашекая\",\"gender\":\"f\",\"birth_date\":764726400}";

        assertResponseIsEmpty(response);
        Assert.assertNotEquals(userBeforeUpdate, userAfterUpdate);
        Assert.assertEquals(expectedAfterUpdate, userAfterUpdate);
    }

    @Test
    public void updateLocation() throws Exception {
        String locationBeforeUpdate = serverController.getEntity(EntityType.locations.name(), 2);
        String response = serverController.updateEntity(EntityType.locations.name(), 2, "{\"place\":\"my_cool_place\", \"distance\":100500}");
        String locationAfterUpdate = serverController.getEntity(EntityType.locations.name(), 2);

        String expectedAfterUpdate = "{\"id\":2,\"place\":\"my_cool_place\",\"country\":\"Египет\",\"city\":\"Росград\",\"distance\":100500}";

        assertResponseIsEmpty(response);
        Assert.assertNotEquals(locationBeforeUpdate, locationAfterUpdate);
        Assert.assertEquals(expectedAfterUpdate, locationAfterUpdate);
    }

    @Test
    public void updateVisit() throws Exception {
        String visitBeforeUpdate = serverController.getEntity(EntityType.visits.name(), 2);
        String response = serverController.updateEntity(EntityType.visits.name(), 2, "{\"user\":100500}");
        String visitAfterUpdate = serverController.getEntity(EntityType.visits.name(), 2);

        String expectedAfterUpdate = "{\"id\":2,\"location\":2,\"user\":100500,\"visited_at\":1116074752,\"mark\":1}";

        assertResponseIsEmpty(response);
        Assert.assertNotEquals(visitBeforeUpdate, visitAfterUpdate);
        Assert.assertEquals(expectedAfterUpdate, visitAfterUpdate);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateNotExistedUser() throws Exception {
        serverController.updateEntity(EntityType.users.name(), 100500, "{\"email\":\"my_cool_email@mail.com\"}");
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateNotExistedLocation() throws Exception {
        serverController.updateEntity(EntityType.locations.name(), 100500, "{\"distance\":100500}");
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateNotExistedVisit() throws Exception {
        serverController.updateEntity(EntityType.visits.name(), 100500, "{\"user\":100500}");
    }

    @Test(expected = ValidationException.class)
    public void updateUserWithValidationError() throws Exception {
        serverController.updateEntity(EntityType.users.name(), 2, "{\"email\":null}");
    }

    @Test(expected = ValidationException.class)
    public void updateLocationWithValidationError() throws Exception {
        serverController.updateEntity(EntityType.locations.name(), 2, "{\"distance\":null}");
    }

    @Test(expected = ValidationException.class)
    public void updateVisitWithValidationError() throws Exception {
        serverController.updateEntity(EntityType.visits.name(), 2, "{\"user\":null}");
    }

    private void checkCreate(EntityType type, int id, String body) {
        try {
            serverController.getEntity(type.name(), id);
            Assert.fail();
        } catch (EntityNotFoundException ignore) {
        }

        assertResponseIsEmpty(serverController.newEntity(type.name(), id, body));
        String newEntity = serverController.getEntity(type.name(), id);
        Assert.assertEquals(body, newEntity);
    }

    @Test
    public void newUser() throws Exception {
        String expectedUser = "{\"id\":100500,\"email\":\"my_cool_email@mail.com\",\"first_name\":\"Елена\",\"last_name\":\"Данашекая\",\"gender\":\"f\",\"birth_date\":764726400}";
        checkCreate(EntityType.users, 100500, expectedUser);
    }

    @Test
    public void newLocation() throws Exception {
        String expectedLocation = "{\"id\":100501,\"place\":\"my_cool_place\",\"country\":\"Египет\",\"city\":\"Росград\",\"distance\":100500}";
        checkCreate(EntityType.locations, 100501, expectedLocation);
    }

    @Test
    public void newVisit() throws Exception {
        String expectedVisit = "{\"id\":100502,\"location\":2,\"user\":100500,\"visited_at\":1116074752,\"mark\":1}";
        checkCreate(EntityType.visits, 100502, expectedVisit);
    }

    @Test(expected = ValidationException.class)
    public void newUserWithEmptyField() throws Exception {
        String body = "{\"id\":200500,\"email\":\"my_cool_email@mail.com\",\"first_name\":null,\"last_name\":\"Данашекая\",\"gender\":\"f\",\"birth_date\":764726400}";
        serverController.newEntity(EntityType.users.name(), 200500, body);

    }

    @Test
    public void locationAvg() throws Exception {
        Assert.assertEquals("{\"avg\":2,0}", serverController.locationAvg(337, null, null, 12, null, 'm'));

    }

    @Test
    public void getUserVisits() throws Exception {
    }

}