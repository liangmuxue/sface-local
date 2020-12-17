package com.ss.sdk.service.data.impl;



import com.ss.sdk.pojo.terminal.model.PersonData;
import com.ss.sdk.pojo.terminal.model.PersonInfo;
import com.ss.sdk.pojo.terminal.model.PersonVerification;

import java.util.List;

public interface IPersonDataService {

    int addPerson(PersonVerification personVerification);

    List<PersonData> personPage(PersonData personData);

    List<PersonInfo> personList(PersonData personData);
}
