CREATE TABLE Activity
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    `TYPE`    VARCHAR(31)  NOT NULL, -- Corrigido de DTYPE para TYPE, conforme anotação
    statement VARCHAR(255) NOT NULL,
    `order`   INT          NOT NULL,
    course_id BIGINT       NOT NULL,
    CONSTRAINT FK_Activity_Course FOREIGN KEY (course_id) REFERENCES Course (id),
    CONSTRAINT UQ_Course_Statement UNIQUE (course_id, statement)
) ENGINE=InnoDB;

CREATE TABLE `Option`
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    text        VARCHAR(80) NOT NULL,
    isCorrect   BOOLEAN     NOT NULL,
    activity_id BIGINT      NOT NULL,
    CONSTRAINT FK_Option_Activity FOREIGN KEY (activity_id) REFERENCES Activity (id) ON DELETE CASCADE
) ENGINE=InnoDB;

