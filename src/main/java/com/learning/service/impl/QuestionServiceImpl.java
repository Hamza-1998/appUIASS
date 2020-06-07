package com.learning.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.dao.QuestionRepository;
import com.learning.dao.QuestionRepositorySearchCriteria;
import com.learning.dto.QuestionDTO;
import com.learning.model.Exam;
import com.learning.model.Question;
import com.learning.model.Suggestion;
import com.learning.model.base.Demande;
import com.learning.model.base.PartialList;
import com.learning.service.ExamService;
import com.learning.service.QuestionService;
import com.learning.service.SuggestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepository questionRepository;


	@Autowired
	private ExamService examService;

	@Autowired
	private SuggestionService suggestionService;
	@Autowired
	private QuestionRepositorySearchCriteria questionRepositorySearchCriteria;

	@Override
	public QuestionDTO save(QuestionDTO questionDTO) {
		final Long idExam = questionDTO.getExam() != null ? questionDTO.getExam().getId() : null;
		
		
		if (questionDTO.getId() != null) {
			if (!existingQuestionById(questionDTO.getId(), questionDTO.getName(), idExam))
				return null;
		} else if (!existingQuestion(questionDTO.getName(), idExam))
			return null;
		Question question = convertDTOtoModel(questionDTO);
		question = questionRepository.save(question);
		if (questionDTO.getSuggestions() != null) {
			suggestionService.saveSuggestionsByQuestion(questionDTO.getSuggestions(), question);
		}
		return convertModelToDTO(question);
	}

	@Override
	public QuestionDTO findById(long idOut) {
		Optional<Question> optional = questionRepository.findById(idOut);

		if (optional.isPresent()) {
			Question questionFromDb = optional.get();
			return convertModelToDTO(questionFromDb);
		}
		return null;
	}

	@Override
	public void delete(Question question) {
		questionRepository.delete(question);
	}

	@Override
	public PartialList<QuestionDTO> findByCriteres(Demande<QuestionDTO> demande) {

		List<Question> questions = questionRepositorySearchCriteria.findByCriteres(demande);
		Long count = questionRepositorySearchCriteria.countByCriteres(demande);
		return new PartialList<QuestionDTO>(count, convertEntitiesToDtos(questions));
	}

	@Override
	public Question convertDTOtoModel(QuestionDTO questionDTO) {
		Question question = new Question();
		question.setId(questionDTO.getId());
		question.setName(questionDTO.getName());
		question.setCode(questionDTO.getCode());
		question.setCorrectComment(questionDTO.getCorrectComment());
		question.setNote(questionDTO.getNote());
		question.setIndexNumerator(questionDTO.getIndexNumerator());
		
		

		if (questionDTO.getExam() != null) {
			question.setExam(examService.convertDTOtoModel(questionDTO.getExam()));
		}
		if(questionDTO.getSuggestions()!=null) {
			question.setSuggestions(suggestionService.convertDtosToEntities(questionDTO.getSuggestions()));
		}
		return question;
	}

	@Override
	public QuestionDTO convertModelToDTO(Question question) {
		QuestionDTO questionDTO = new QuestionDTO();
		questionDTO.setId(question.getId());
		questionDTO.setName(question.getName());
		questionDTO.setCode(question.getCode());
		questionDTO.setCorrectComment(question.getCorrectComment());
		questionDTO.setNote(question.getNote());
		questionDTO.setIndexNumerator(question.getIndexNumerator());
		Exam exam = question.getExam();
		
		List<Suggestion> suggestion = question.getSuggestions();
		if (exam != null) {
			questionDTO.setExam(examService.convertModelToDTOWithoutQuestion(exam));
		}
		if (suggestion != null) {
			questionDTO.setSuggestions(suggestionService.convertEntitiesToDtos(suggestion));
		}
	
		questionDTO.setCreatedAt(question.getCreatedAt());
		questionDTO.setUpdatedAt(question.getUpdatedAt());
		return questionDTO;
	}

	@Override
	public PartialList<QuestionDTO> convertToListDTO(PartialList<Question> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Long id) {
		questionRepository.deleteById(id);

	}

	@Override
	public List<QuestionDTO> convertEntitiesToDtos(List<Question> questions) {
		// basic methode
		List<QuestionDTO> list = new ArrayList<QuestionDTO>();
		for (Question question : questions) {
			list.add(convertModelToDTO(question));
		}
		return list;
	}

	@Override
	public List<Question> convertDtosToEntities(List<QuestionDTO> questionsDTO) {
		List<Question> list = new ArrayList<Question>();
		for (QuestionDTO questionDTO : questionsDTO) {
			list.add(convertDTOtoModel(questionDTO));
		}
		return list;
	}



	@Override
	public void saveQuestionsByExam(List<QuestionDTO> questions, Exam exam) {
		detachExam(exam.getId());
		for (QuestionDTO questionDTO : questions) {
			questionDTO.setExam(examService.convertModelToDTOWithoutQuestion(exam));
			save(questionDTO);
		}
	

	}

	@Override
	public List<QuestionDTO> findByExam(Long examId) {
		return convertEntitiesToDtos(questionRepository.findByExam(examId));
	}

	@Override
	public void detachExam(Long examId) {
		questionRepository.detacheExam(examId);

	}



	

	@Override
	public boolean existingQuestion(String name, Long idExam) {
		Question existQuestion =  questionRepository.findByNameAndExam(name, idExam);
		return existQuestion == null || existQuestion.getId() == null;
	}

	@Override
	public boolean existingQuestionById(Long id, String name, Long idExam) {
		Question existQuestion =  questionRepository.findByNameAndExam(name, idExam);
		return existQuestion == null || existQuestion.getId().equals(id);
	}



}
