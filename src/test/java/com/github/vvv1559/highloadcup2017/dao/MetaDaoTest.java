package com.github.vvv1559.highloadcup2017.dao;

import com.github.vvv1559.highloadcup2017.dao.model.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MetaDaoTest {
    @Test
    public void getAge() throws Exception {
        User user = new User();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -30);
        user.setBirthDateTimestamp((int) TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis()));
        Assert.assertEquals(2, MetaDao.getAge(user));
    }


    @Test
    public void getAgeBefore1970() throws Exception {
        User user = new User();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1930);
        user.setBirthDateTimestamp((int) TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis()));

        Calendar now = Calendar.getInstance();
        Assert.assertEquals(now.get(Calendar.YEAR) - calendar.get(Calendar.YEAR), MetaDao.getAge(user));
    }
}