package com.skillbox.redisdemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static java.lang.System.out;

public class RedisTest {

    // Запуск докер-контейнера:
    // docker run --rm --name skill-redis -p 127.0.0.1:6379:6379/tcp -d redis

    private static final SimpleDateFormat DF = new SimpleDateFormat("HH:mm:ss");

    // Всего 20 пользователей
    private static final int USERS = 20;

    private static void log(String UsersOnline) {
        String log = "— На главной странице показываем пользователя " + UsersOnline;
        out.println(log);
    }

    public static void main(String[] args) throws InterruptedException {

        RedisStorage redis = new RedisStorage();
        redis.init();
        // Регистрируем 20 пользователей
        for(int request = 1; request <= USERS; request++) {
            int user_id = request;
            redis.logPageVisit(user_id);
        }
        // Эмулируем работу программы по показу пользователей
        int count = 0;
        while (true) {

            log(redis.showUsers());
            redis.moveToTheEnd();
            Thread.sleep(1000);
            count++;
            if(count % 10 == 0)
            {
                redis.payService(new Random().nextInt(USERS));
            }
        }
    }
}
