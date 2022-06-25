package com.company.repository;
// PROJECT NAME Kun_Uz
// TIME 17:07
// MONTH 06
// DAY 20


import com.company.entity.AttachEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface AttachRepository extends CrudRepository<AttachEntity, String> {

}
