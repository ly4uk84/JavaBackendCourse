package org.JavaBackendCourse;

import java.util.concurrent.Callable;

public interface Transaction extends Callable<Boolean> {
    int getClientId();
    String getType();
}

