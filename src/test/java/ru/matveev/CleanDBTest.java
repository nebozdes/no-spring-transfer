package ru.matveev;

import org.flywaydb.core.Flyway;

import javax.inject.Inject;

public abstract class CleanDBTest {

    @Inject
    private Flyway flyway;

    public void init() {
        flyway.clean();
        flyway.migrate();
    }
}
