package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;


    @Test
    void tsetMember() {

        System.out.println("memberRepository = " + memberRepository.getClass());

        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get(); // 옵셔널 헤지하기위해 겟

        assertThat(findMember.getId()).isEqualTo(saveMember.getId());


    }

    @Test
    void basicCRUD() {

        Member member1 = new Member("Member1");
        Member member2 = new Member("Member2");


        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("member!!!!!!!!");

        //리스트 조회 검증

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);


        //삭제 검증

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterthen() {
        Member m1 = new Member("AAAA", 10);
        Member m2 = new Member("AAAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("AAAA", 15);

        assertThat(members.get(0).getUsername()).isEqualTo("AAAA");
        assertThat(members.get(0).getAge()).isEqualTo(20);
        assertThat(members.size()).isEqualTo(1);
    }


    @Test
    void 네임드쿼리인터페이스구현() {

        Member m1 = new Member("AAAA", 10);
        Member m2 = new Member("AAAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findMembers = memberRepository.findByUsername("AAAA");
        Member findMember = findMembers.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    void 테스트쿼리() {

        Member m1 = new Member("AAAA", 10);
        Member m2 = new Member("AAAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);


        List<Member> result = memberRepository.findUser("AAAA", 10);

        assertThat(result.get(0)).isEqualTo(m1);

    }

    @Test
    void 유저네임리스트() {

        Member m1 = new Member("AAAA", 10);
        Member m2 = new Member("AAAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("name = " + s);
        }
    }

    @Test
    void Dto테스트() {

        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> dtoList = memberRepository.findMemberDto();

        for (MemberDto s : dtoList) {
            System.out.println("name = " + s.getId());
            System.out.println("name = " + s.getUsername());
            System.out.println("name = " + s.getTeamName());
        }
    }

    @Test
    void 컬렉션인테스트() {

        Member m1 = new Member("AAAA", 10);
        Member m2 = new Member("BBBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> usernameList = memberRepository.findByNames(Arrays.asList("AAAA", "BBBB"));

        for (Member s : usernameList) {
            System.out.println("name = " + s);
        }
    }

    @Test
    void retunType() {

        Member m1 = new Member("AAAA", 10);
        Member m2 = new Member("BBBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> listMember = memberRepository.findListByUsername("AAAA");
        List<Member> emptyMember = memberRepository.findListByUsername("asdfsdfsdf"); // emptyCollection 반환 if(result != null) 사용금지

        Member singleMember = memberRepository.findMemberByUsername("AAAA");
        Member singleMember2 = memberRepository.findMemberByUsername("ㅁㄴㄹㄴㅁㅇㄹㄴㅇㄹ"); // 단건은 Null 나옴

        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAAA");
        Optional<Member> optionalMember2 = memberRepository.findOptionalByUsername("ㅁㄴㅇㄹㄴㄹㄴㅇ");


        System.out.println("listMember = " + listMember);
        System.out.println("singleMember = " + singleMember);
        System.out.println("singleMember2 = " + singleMember2);
        System.out.println("optionalMember = " + optionalMember);
        System.out.println("optionalMember2 = " + optionalMember2);
        System.out.println("emptyMember = " + emptyMember.size());
    }


    @Test
    void paging() {

        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));
        memberRepository.save(new Member("member9", 10));


        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(10, pageRequest); //totalcount 쿼리까지 같이날림
//        Slice<Member> slice = memberRepository.findSliceByAge(10 , pageRequest); //totalcount 쿼리까지 같이날림
//        List<Member> members = memberRepository.findListByAge(10 , pageRequest); //totalcount 쿼리까지 같이날림


        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements(); //totalCount

        for (Member member : page) {
            System.out.println("member = " + member);
        }

        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(9);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.isFirst()).isTrue(); //첫번째 페이지니?
        assertThat(page.hasNext()).isTrue(); // 다음페이지니?

//        assertThat(slice.getNumber()).isEqualTo(0);
//        assertThat(slice.isFirst()).isTrue(); //첫번째 페이지니?
//        assertThat(slice.hasNext()).isTrue(); // 다음페이지니?


    }

    @Test
    void paging2() {

        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));


        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(10, pageRequest); //totalcount 쿼리까지 같이날림


        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        System.out.println("tomap = " + toMap.get());
        System.out.println("tomap = " + toMap.getContent().get(0));
        System.out.println("tomap = " + toMap.getContent().get(1));
        System.out.println("tomap = " + toMap.getContent().get(2));
        System.out.println("tomap = " + toMap.getTotalElements());
        System.out.println("tomap = " + toMap.getPageable().getPageNumber());
        System.out.println("tomap = " + toMap.getPageable().getPageSize());
//
//        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements(); //totalCount
//
//        for(Member member : page){
//            System.out.println("member = " +member);
//        }
//
//        System.out.println("totalElements = " + totalElements);
//
//        assertThat(content.size()).isEqualTo(3);
//        assertThat(page.getTotalElements()).isEqualTo(5);
//        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(2);
//        assertThat(page.isFirst()).isTrue(); //첫번째 페이지니?
//        assertThat(page.hasNext()).isTrue(); // 다음페이지니?
    }


    @Test
    void bulkUopdate() {

        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 40));
        memberRepository.save(new Member("member5", 50));

        int resultCount = memberRepository.bulkAgePlus(10);


        List<Member> result = memberRepository.findByUsername("member5");

        Member member5 = result.get(0);

        System.out.println("member5 -> " + member5);


        assertThat(resultCount).isEqualTo(5);
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
        Member member2 = new Member("member2", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member = " + member.getTeam().getClass()); //proxy 조회
            System.out.println("member = " + member.getTeam().getName()); // 멤버 한명 조회할때마다 팀 조회 쿼리 또날라감
        }

    }

    @Test
    void QueryHint() {

        //given
        Member member = memberRepository.save(new Member("member1", 10));
        memberRepository.save(member);
        em.flush();
        em.clear();

        //when

        Member findMember = memberRepository.findReadOnlyByUsername("member1"); //가져오는 순간 원본이랑 복사본을 가져온다.
        //최적화 방법? hint!
        findMember.setUsername("member2");

        em.flush();

    }

    @Test
    void lockTest() {

        Member member = memberRepository.save(new Member("member1", 10));
        memberRepository.save(member);
        em.flush();
        em.clear();

        List<Member> member1 = memberRepository.findLockByUsername("member1");
    }

    @Test
    void callCustom() {

        List memberCustom = memberRepository.findMemberCustom();

        System.out.println("@@@@@@:" + memberCustom);
        for (Object member : memberCustom) {
            System.out.println("member -> " + member);
        }

    }

//    @Test
//    void projecons() {
//
//        Team teamA = new Team("teamA");
//        em.persist(teamA);
//        Member m1 = new Member("m1", 0, teamA);
//        Member m2 = new Member("m2", 0, teamA);
//        em.persist(m1);
//        em.persist(m2);
//        em.flush();
//        em.clear();
//
//        List<UsernameOnly> result = memberRepository.findProjectonsByUsername("m1");
//        assertThat(result.size()).isEqualTo(1);
//
//    }

    @Test
    public void nativeQuery() {


        Team teamA = new Team("teamA");
        em.persist(teamA);
        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);
        em.flush();
        em.clear();


        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0,10));

        for(MemberProjection projection : result.getContent()){
            System.out.printf("@@@@@ = " + projection.getUsername());
            System.out.printf("@@@@@ = " + projection.getTeamName());
        }

    }
}