package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void basicCRUD(){

    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){

        Member member1 = new Member("AAA" , 10 );
        Member member2 = new Member("BBB" , 20 );

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("AAA", 5);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");

    }
    @Test
    public void findByUsername(){
        Member member = new Member("aaa" , 10);
        memberJpaRepository.save(member);
        List<Member> members = memberJpaRepository.findByUsername("aaa");

        for (Member member1 : members) {
            System.out.println("username = " + member1.getUsername());
        }
    }


}