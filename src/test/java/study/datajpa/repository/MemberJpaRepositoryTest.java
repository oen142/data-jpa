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
    @Test
    public void bulkAgePlus(){
        memberJpaRepository.save(new Member("member1" , 10));
        memberJpaRepository.save(new Member("member2" , 19));
        memberJpaRepository.save(new Member("member3" , 20));
        memberJpaRepository.save(new Member("member4" , 21));
        memberJpaRepository.save(new Member("member5" , 400));
        memberJpaRepository.save(new Member("member6" , 30));

        int resultCount = memberJpaRepository.bulkAgePlus(20);

        assertThat(resultCount).isEqualTo(4);
    }

    @Test
    public void paging(){
        memberJpaRepository.save(new Member("member1" , 10));
        memberJpaRepository.save(new Member("member2" , 10));
        memberJpaRepository.save(new Member("member3" , 10));
        memberJpaRepository.save(new Member("member4" , 10));
        memberJpaRepository.save(new Member("member5" , 10));
        memberJpaRepository.save(new Member("member6" , 10));
        memberJpaRepository.save(new Member("member7" , 10));
        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        //페이지 계산 공식 적용...
        // totalPage = totalCount /size
        //마지막 페이지
        //최초페이지

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(7);

    }
}