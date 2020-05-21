package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;

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
    public void basicCRUD(){
    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){

        Member member1 = new Member("AAA" , 10 );
        Member member2 = new Member("BBB" , 20 );

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 5);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");

    }

    @Test
    public void findByUsername(){
        Member member = new Member("aaa" , 10);
        memberRepository.save(member);
        List<Member> members = memberRepository.findByUsername("aaa");

        for (Member member1 : members) {
            System.out.println("username = " + member1.getUsername());
        }
    }
    @Test
    public void findUser(){
        Member member = new Member("aaa" , 10);
        memberRepository.save(member);
        List<Member> members = memberRepository.findUser("aaa" , 10);

        for (Member member1 : members) {
            System.out.println("username = " + member1.getUsername());
            System.out.println("username age = " + member1.getAge());
        }
    }
    @Test
    public void findUsernameList(){
        Member member1 = new Member("AAA" , 10 );
        Member member2 = new Member("BBB" , 20 );

        memberRepository.save(member1);
        memberRepository.save(member2);
        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("test" +s);
        }
    }
    @Test
    public void findMemberDto(){
        Team team = new Team("teamA");
        Member member1 = new Member("AAA" , 10 ,team);
        Member member2 = new Member("BBB" , 20 ,team);

        teamRepository.save(team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("test" +s.toString() );
        }
    }
    @Test
    public void findByNames(){
        Team team = new Team("teamA");
        Member member1 = new Member("AAA" , 10 ,team);
        Member member2 = new Member("BBB" , 20 ,team);

        teamRepository.save(team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> usernameList = memberRepository.findByNames(Arrays.asList("AAA" , "BBB"));

        for (Member s : usernameList) {
            System.out.println("test" +s );
        }
    }
}