package br.com.alura.AluraFake.security.config;

public enum Endpoint {
    AUTH("/auth/**"),
    USER_ALL("/user/all"),
    COURSE_ALL("/course/all"),
    TASK("/task/**"),
    COURSE_NEW("/course/new"),
    INSTRUCTOR("/instructor/**");

    private final String path;

    Endpoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
