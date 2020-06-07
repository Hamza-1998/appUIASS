package com.learning.service;

import java.util.List;

import com.learning.dto.QuestionDTO;
import com.learning.model.Exam;
import com.learning.model.Question;

public interface QuestionService extends CrudService<Question, QuestionDTO> {


	void saveQuestionsByExam(List<QuestionDTO> questions, Exam exam);

	
	
	List<QuestionDTO> findByExam(Long examId);

	void detachExam(Long examId);

	
	boolean existingQuestion(String name, Long idExam);

	boolean existingQuestionById(Long id , String name,Long idExam);

}
