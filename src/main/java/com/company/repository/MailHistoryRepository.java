package com.company.repository;



import com.company.entity.EmailHistoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface MailHistoryRepository extends CrudRepository<EmailHistoryEntity,Integer> {

}
