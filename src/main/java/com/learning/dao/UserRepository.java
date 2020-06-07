package com.learning.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.learning.model.StatutEnum;
import com.learning.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

//	@Query("select u from User u where u.level.id=?1 and u.branch.id=?2")
//	List<User> findByLevelAndBranch(Long idLevel,Long idBranch);

	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);







	@Query("select distinct u from User u,NoteExam ne where ne.user.id=u.id and ne.exam.id =?2 and( (LOWER(u.firstName) LIKE CONCAT(lower(?1), '%')) or (LOWER(u.lastName) LIKE CONCAT(lower(?1), '%')) or (LOWER(u.email) LIKE CONCAT(lower(?1), '%')))")
	List<User> findByNameContainingByExam(String name, Long idExam);

	
	

	
	
	

}
