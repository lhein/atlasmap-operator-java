package com.redhat.atlasmap.operator;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.javaoperatorsdk.operator.Operator;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class AtlasMapOperator {

    @Inject
    Operator operator;

    void onStart(@Observes StartupEvent ignored) {
        operator.start();
    }

    void onStop(@Observes ShutdownEvent ignored) {
        operator.close();
    }

    public static void bar() throws IOException {
        //new FtBasic(new TkFork(new FkRegex("/health", "ALL GOOD!")), 8080).start(Exit.NEVER);
    }
}
