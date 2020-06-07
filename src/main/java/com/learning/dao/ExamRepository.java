package com.learning.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.learning.model.Exam;
import com.learning.model.TypeEnumExam;

public interface ExamRepository extends JpaRepository<Exam, Long> {
	@Query("SELECT e FROM Exam e WHERE (LOWER(e.name) LIKE CONCAT(?1, '%'))")
	Page<Exam> findByNameAndModule(String code, Pageable pageable);

	@Query("SELECT e FROM Exam e WHERE (LOWER(e.name) LIKE CONCAT(?1, '%'))")
	Page<Exam> findByName(String name, Pageable pageable);






	@Query("SELECT distinct e FROM Exam e,NoteExam ne WHERE ne.exam.id=e.id and e.startDateTime>=?1 and ne.user.id=?2 ")
	List<Exam> findByUser(LocalDateTime localDateTime,Long idUser);



}
