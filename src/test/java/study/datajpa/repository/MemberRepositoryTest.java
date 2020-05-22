package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberQueryRepository memberQueryRepository;

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
    public void bulkAgePlus() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 400));
        memberRepository.save(new Member("member6", 30));


        int resultCount = memberRepository.bulkAgePlus(20);

        em.flush();
        em.clear();


        assertThat(resultCount).isEqualTo(4);
    }

    @Test
    public void findMemberLazy() {
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);


        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.team.getClass" + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        Member member = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();


        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.changeName("member2");

        em.flush();

    }

    @Test
    public void lock() {
        Member member = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();


        List<Member> findMember = memberRepository.findLockByUsername("member1");

    }

    @Test
    public void findMemberCustom() {
        List<Member> members = memberRepository.findMemberCustom();


    }

    @Test
    public void specBasic() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("m1", 10, teamA);
        Member member2 = new Member("m2", 10, teamA);

        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //when
        Specification<Member> spec = MemberSpec.username("m1").or(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void queryByExample() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("m1", 10, teamA);
        Member member2 = new Member("m2", 10, teamA);

        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //Probe
        Member member = new Member("m1", 10, teamA);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("m1");

    }
    @Test
    public void projections(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("m1", 10, teamA);
        Member member2 = new Member("m2", 10, teamA);

        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");

        for (UsernameOnly usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly.getUsername());

        }
    }
}