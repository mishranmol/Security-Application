package com.SecurityApp.SecurityApplication.Repositories;

import com.SecurityApp.SecurityApplication.Entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity,Long> {

}
