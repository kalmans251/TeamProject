package com.himedia.order.dto;

import com.himedia.member.entity.Member;
import com.himedia.member.entity.MemberAddress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
	
	private String url;
	
	private String subject;
	
	private Long price;
	
	private String size;
	
	private String color;
	
	private Integer orderCount;
	
	private Member member;
	
	private MemberAddress memberAdress;
	
	public OrderDto(String url,String subject , Long price , String size , String color , Integer orderCount , Member member, MemberAddress memberAddress){
		this.url = url;
		this.subject=subject;
		this.price=price;
		this.size=size;
		this.color=color;
		this.orderCount=orderCount;
		this.member=member;
		this.memberAdress=memberAddress;
	}
	
}