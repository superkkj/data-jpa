package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {


    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;


    @Test
    void testEntity() {

        Team teamA = new Team("testA");
        Team teamB = new Team("testB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("Member1", 10, teamA);
        Member member2 = new Member("Member2", 11, teamA);
        Member member3 = new Member("Member3", 12, teamB);
        Member member4 = new Member("Member4", 13, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush();
        em.clear();

        //확인
        List<Member> members = em.createQuery("select m from Member m ", Member.class).getResultList();

        for (Member member : members){
            System.out.println("member = " + member);
            System.out.println("member team = " + member.getTeam());
        }

    }

    @Test
    void JpaEventBaseEntity() throws InterruptedException {

        Member member = new Member("member1");
        memberRepository.save(member);


        Thread.sleep(100);
        member.setUsername("membber2");

        em.flush();;
        em.clear();


        Member member1 = memberRepository.findById(member.getId()).get();


        System.out.println("findMember = " + member1.getCreateDate());
        System.out.println("findMember = " + member1.getLastModifiedDate());
        System.out.println("findMember = " + member1.getCreateBy());
        System.out.println("findMember = " + member1.getLastModifiedBy());
    }
}