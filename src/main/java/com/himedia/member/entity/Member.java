package com.himedia.member.entity;

import com.himedia.member.role.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long idx;
	
	@Column(unique=true)
	public String username;
	
	public String password;
	
	public String nickName;
	
	public String phoneNum;
	
	@Enumerated(EnumType.STRING)
	public MemberRole memberRole;
	
	public String type;
	
}
