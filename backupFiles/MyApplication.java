package com.bot.spotifyapp;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Set;

import java.util.HashSet;

@ApplicationPath("res")
public class MyApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(StudentResource.class);
        System.out.println("Student Resource Added!");
        return s;
    }
}
