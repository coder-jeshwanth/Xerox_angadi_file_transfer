package com.project.repository;

import com.project.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {
    List<UserFile> findByUserName(String username);

    @Query("SELECT DISTINCT uf.userName FROM UserFile uf")
    List<String> findAllUserNames();
}