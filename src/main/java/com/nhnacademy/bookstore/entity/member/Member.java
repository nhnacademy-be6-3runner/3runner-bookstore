package com.nhnacademy.bookstore.entity.member;


import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.bookstore.entity.address.Address;
import com.nhnacademy.bookstore.entity.member.enums.AuthProvider;
import com.nhnacademy.bookstore.entity.member.enums.Grade;
import com.nhnacademy.bookstore.entity.member.enums.Status;
import com.nhnacademy.bookstore.entity.memberAuth.MemberAuth;
import com.nhnacademy.bookstore.entity.pointRecord.PointRecord;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.member.member.dto.request.CreateMemberRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String password;

	@NotNull
	private Long point;

	@NotNull
	@Size(min = 1, max = 10)
	private String name;

	private int age;

	@NotNull
	@Size(min = 1, max = 11)
	private String phone;

	@NotNull
	@Column(unique = true)
	private String email;

	private ZonedDateTime birthday;

	@NotNull
	private Grade grade;

	@NotNull
	private Status status;

	private ZonedDateTime lastLoginDate;

	@NotNull
	private ZonedDateTime createdAt;

	private ZonedDateTime modifiedAt;
	private ZonedDateTime deletedAt;
	@NotNull
	private AuthProvider authProvider;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Address> addressList = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MemberAuth> memberAuthList = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PointRecord> pointRecordList = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Purchase> purchaseList = new ArrayList<>();

	public Member(CreateMemberRequest request) {
		this.setPassword(request.password());
		this.setPoint(5000L);
		this.setName(request.name());
		this.setAge(request.age());
		this.setStatus(Status.Active);
		this.setPhone(request.phone());
		this.setEmail(request.email());
		this.setBirthday(request.birthday());
		this.setGrade(Grade.General);
		this.setCreatedAt(ZonedDateTime.now());
		this.setAuthProvider(AuthProvider.General);
	}

}
