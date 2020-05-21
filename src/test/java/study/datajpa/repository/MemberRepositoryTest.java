package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.dto.MemberDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void basicCRUD() {
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {

        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 5);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");

    }

    @Test
    public void findByUsername() {
        Member member = new Member("aaa", 10);
        memberRepository.save(member);
        List<Member> members = memberRepository.findByUsername("aaa");

        for (Member member1 : members) {
            System.out.println("username = " + member1.getUsername());
        }
    }

    @Test
    public void findUser() {
        Member member = new Member("aaa", 10);
        memberRepository.save(member);
        List<Member> members = memberRepository.findUser("aaa", 10);

        for (Member member1 : members) {
            System.out.println("username = " + member1.getUsername());
            System.out.println("username age = " + member1.getAge());
        }
    }

    @Test
    public void findUsernameList() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);
        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("test" + s);
        }
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        Member member1 = new Member("AAA", 10, team);
        Member member2 = new Member("BBB", 20, team);

        teamRepository.save(team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("test" + s.toString());
        }
    }

    @Test
    public void findByNames() {
        Team team = new Team("teamA");
        Member member1 = new Member("AAA", 10, team);
        Member member2 = new Member("BBB", 20, team);

        teamRepository.save(team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> usernameList = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        for (Member s : usernameList) {
            System.out.println("test" + s);
        }
    }

    @Test
    public void returnType() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        Optional<Member> aaa = memberRepository.findOptionalByUsername("AAA");
        System.out.println("findMember" + aaa);

    }
    @Test
    public void paging(){
        memberRepository.save(new Member("member1" , 10));
        memberRepository.save(new Member("member2" , 10));
        memberRepository.save(new Member("member3" , 10));
        memberRepository.save(new Member("member4" , 10));
        memberRepository.save(new Member("member5" , 10));
        memberRepository.save(new Member("member6" , 10));
        memberRepository.save(new Member("member7" , 10));


        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> dtoMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //페이지 계산 공식 적용...
        // totalPage = totalCount /size
        //마지막 페이지
        //최초페이지

        List<Member> content = page.getContent();
        long totalCount = page.getTotalElements();
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalCount = " + totalCount);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();


        Slice<Member> slicePage = memberRepository.findSliceByAge(age, pageRequest);


        //페이지 계산 공식 적용...
        // totalPage = totalCount /size
        //마지막 페이지
        //최초페이지

        List<Member> sliceContent = page.getContent();
        long sliceTotalCount = page.getTotalElements();
        for (Member member : sliceContent) {
            System.out.println("member = " + member);
        }
        System.out.println("totalCount = " + sliceTotalCount);

        assertThat(sliceContent.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

        //리스트도 된다
    }
}