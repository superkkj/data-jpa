package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;


@SpringBootTest
@Transactional
@Rollback(false)
class memberJpaRepositoryTest {

        @Autowired MemberJpaRepository memberJpaRepository;


    @Test
    void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(saveMember.getId());

       assertThat(findMember.getId()).isEqualTo(member.getId());
       assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
       assertThat(findMember).isEqualTo(member);
    }


    @Test
    void basicCRUD() {

        Member member1 = new Member("Member1");
        Member member2 = new Member("Member2");


        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("member!!!!!!!!");

        //리스트 조회 검증

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);


        //삭제 검증

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterthen(){
        Member m1 = new Member("AAAA", 10);
        Member m2 = new Member("AAAA", 20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> members = memberJpaRepository.findByUsernameAndAgeGreaterThen("AAAA", 15);

        assertThat(members.get(0).getUsername()).isEqualTo("AAAA");
        assertThat(members.get(0).getAge()).isEqualTo(20);
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    void testNamedQuery() {

        Member m1 = new Member("AAAA", 10);
        Member m2 = new Member("AAAA", 20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> findMembers = memberJpaRepository.findByUsername("AAAA");
        Member findMember = findMembers.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    void paging() {

        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",10));
        memberJpaRepository.save(new Member("member3",10));
        memberJpaRepository.save(new Member("member4",10));
        memberJpaRepository.save(new Member("member5",10));
        memberJpaRepository.save(new Member("member6",10));
        memberJpaRepository.save(new Member("member7",10));
        memberJpaRepository.save(new Member("member8",10));
        memberJpaRepository.save(new Member("member9",10));

        //page 1 offset= 0, limit = 10 , page 2 -> offset = 10, limit 10 부터

        int age = 10;
        int offset = 1;
        int limit =3;


        List <Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totcalCount(age);

        //페이지 계산 공식 적용
        // totalPage = totalCount / size ..
        //마지막 페이지
        //최초 페이지

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(9);


    }

    @Test
    void bulkUopdate() {

        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",20));
        memberJpaRepository.save(new Member("member3",30));
        memberJpaRepository.save(new Member("member4",40));
        memberJpaRepository.save(new Member("member5",50));

        int resultCount  = memberJpaRepository.bulkAgePlus(10);

        assertThat(resultCount).isEqualTo(5);
    }
}