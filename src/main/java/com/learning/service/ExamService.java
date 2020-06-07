package com.learning.service;

import java.util.List;

import com.learning.dto.ExamDTO;
import com.learning.model.Exam;

public interface ExamService extends CrudService<Exam, ExamDTO> {



	ExamDTO convertModelToDTOWithoutQuestion(Exam exam);

	List<ExamDTO> convertEnititiesToDTOsWithoutQuestion(List<Exam> list);




	List<ExamDTO> findByUser(Long idUser);

	

	void launch(Long idExam);

}
