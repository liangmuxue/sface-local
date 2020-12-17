package com.ss.sdk.mapper;


import com.ss.sdk.pojo.terminal.model.PersonData;
import com.ss.sdk.pojo.terminal.model.PersonInfo;
import com.ss.sdk.pojo.terminal.model.SfaceCapture;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface PersonDataMapper {

    PersonData checkPerson(PersonData personData);

    int insertPerson(PersonData personData);

    int updatePerson(PersonData personData);

    List<PersonData> personPage(PersonData personData);

    List<PersonInfo> personList(PersonData personData);

    int insertSfaceCapture(SfaceCapture sfaceCapture);
}
