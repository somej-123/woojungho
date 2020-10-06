package org.edu.controller;

import java.util.List;

import javax.inject.Inject;

import org.edu.dao.IF_MemberDAO;
import org.edu.vo.MemberVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonDataController {
	@Inject
	private IF_MemberDAO memberDAO;
	
	@RequestMapping(value="/android/login", method= RequestMethod.POST)
	public ResponseEntity<MemberVO> androidLogin(@RequestParam("txtUsername") String user_id,@RequestParam("txtPassword") String user_pw) {
		ResponseEntity<MemberVO> entity = null;
		try {
			MemberVO memberVO = memberDAO.viewMember(user_id);
			String bcryptPassword = memberVO.getUser_pw();
			//스프링 시큐리티 4.x BCryptPasswordEncoder 암호화 사용
			BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(10);
			if(bcryptPasswordEncoder.matches(user_pw, bcryptPassword )){
				System.out.println("계정정보 일치");
				entity = new ResponseEntity<>(memberVO, HttpStatus.OK);//code 200
			}else{
				System.out.println("계정정보 불일치");
				entity = new ResponseEntity<>(HttpStatus.NO_CONTENT);//code 204
			}
		} catch (Exception e) {
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);//code 400
		}
		return entity;
	}
	
	@RequestMapping(value="/android/list", method= RequestMethod.POST)
	public ResponseEntity<List<MemberVO>> androidList() {
		ResponseEntity<List<MemberVO>> entity = null;
		try {
			entity = new ResponseEntity<>(memberDAO.androidMember(), HttpStatus.OK);
		} catch(Exception e) {
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);//code 400
		}
		return entity;
	}
	
	@RequestMapping(value="/android/delete/{user_id}", method= RequestMethod.POST)
	public ResponseEntity<String> androidDelete(@PathVariable("user_id") String user_id) {
		ResponseEntity<String> entity = null;
		try {
			memberDAO.deleteMember(user_id);
			entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
}
