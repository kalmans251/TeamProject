package com.himedia.member.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.himedia.member.dto.MemberAddrForm;
import com.himedia.member.dto.MemberCreateForm;
import com.himedia.member.entity.Member;
import com.himedia.member.entity.MemberAddress;
import com.himedia.member.role.MemberRole;
import com.himedia.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberService;
	
	//member login 관련 controller 메소드 이후 MemberConfigService 에서 해결
	@GetMapping("/login")
	private String memberlogin() {
		
		return "login";
	}
	//회원 가입 관련 controller method
	@GetMapping("/create")
	private String memberSave(MemberCreateForm memberCreateForm) {
		
		return "member_create";
	}
	
	@PostMapping("/create")
	private String memberinsert(@Valid MemberCreateForm memberCreateForm,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			
			return "member_create";
		}
		if(!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect","두개의 패스워드가 일치하지 않습니다");
			return "member_create";
		}
		String addr =memberCreateForm.getAddr2()+memberCreateForm.getAddr3();
		try {
			this.memberService.memberInsert(memberCreateForm.getUsername(), memberCreateForm.getPassword1(), memberCreateForm.getNickName() ,memberCreateForm.getPhoneNum(), MemberRole.USER);
		}catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("singupFail","이미 등록된 사용자 입니다.");
			return "member_create";
		}catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("singupFailed",e.getMessage());
			return "member_create";
		}
		Member member = this.memberService.getMember(memberCreateForm.getUsername());
		try {
			this.memberService.addrInsert(member, Integer.parseInt(memberCreateForm.getAddr1()), addr,memberCreateForm.getReference());
		}catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			return "member_create";
		}catch(Exception e) {
			e.printStackTrace();
			return "member_create";
		}
		return "redirect:/member/login";
	}
	@PostMapping("/ajaxtest")
	@ResponseBody
	public Integer ajax(@RequestParam("username")String username) {
		System.out.println(username);

		int a;
		if(username.equals("")) {
			a=2;
			return a;
		}else{
			try {
				Member member = this.memberService.getMember(username);
				a=1;
				return a;
			}catch(Exception e) {
				a=0;
				return a;
			}
		}
	}
	
	//memberAddr 관련 controller메소드
	@GetMapping("/create/addr")
	public String addrAddForm(MemberAddrForm memberAddrForm,Model model,Principal principal) {
		Member member = this.memberService.getMember(principal.getName());
		List<MemberAddress> memberaddr = this.memberService.findMemberAddr(member);
		model.addAttribute("memberAddr", memberaddr);
		return "addr_create";	
	}
	@PostMapping("/create/addr")
	public String addrAdd(@Valid MemberAddrForm memberAddrForm,Principal principal) {
		String addr =memberAddrForm.getAddr2()+memberAddrForm.getAddr3();
		Member member = this.memberService.getMember(principal.getName());
		int addr1 = Integer.parseInt(memberAddrForm.getAddr1());
		this.memberService.addrInsert(member,addr1, addr,memberAddrForm.getReference());
		
		return "redirect:/member/create/addr";
	}
	@GetMapping(value="/addr/delete/{idx}")
	public String addrdel(@PathVariable Long idx) {
		this.memberService.memberAddrDel(idx);
		
		return "redirect:/member/create/addr";
	}
	//member addr change 관련 controller
	@GetMapping(value="/addr/main/change/{idx}")
	public String changeMainAddr(@PathVariable Long idx, Principal principal) {
		Member mebmer = this.memberService.getMember(principal.getName());
		this.memberService.AddrChange(mebmer);
		this.memberService.AddrChangeMain(idx);
		
		return "redirect:/member/create/addr";
	}
	//member delete 관련 controller 메소드
	@GetMapping("/delete/form")
	public String deleteMember() {
		
		return "member_delete";
	}
	
	@PostMapping("/delete/form")
	public String deleteMember(@RequestParam String password, Principal principal, BindingResult bindingResult) {
		Member member = this.memberService.getMember(principal.getName());
		String password1 = member.getPassword();
		if(password.equals(password1)) {
			this.memberService.memberDelete(member.getUsername());
			return "redirect:/member/logout";
		}else{
			bindingResult.rejectValue("password", "passwordInCorrect","패스워드가 일치하지 않습니다");
			return "member_delete";
		}
	}
}
