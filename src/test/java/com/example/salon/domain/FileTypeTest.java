package com.example.salon.domain;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class FileTypeTest {

    @Test
    public void getByNameTest(){
        Assertions.assertThat(FileType.getByName("appointments")).isEqualTo(FileType.APPOINTMENTS);
        Assertions.assertThat(FileType.getByName("clients")).isEqualTo(FileType.CLIENTS);
        Assertions.assertThat(FileType.getByName("purchases")).isEqualTo(FileType.PURCHASES);
        Assertions.assertThat(FileType.getByName("services")).isEqualTo(FileType.SERVICES);
    }

    @Test(expected =IllegalArgumentException.class)
    public void getByName_notExists_throwsExceptionTest(){
        FileType.getByName("wrong value");
    }
}
