package elements.spring.explore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.aryEq;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SampleSpringJdbcApplicationTests {

    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private BankersService bankersService;

    //@Test
    void testDeleteOne() {
        // Mock data
        int id = 123;
        lenient().when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Call the method
        String result = bankersService.deleteOne(id);

        // Verify
        assertEquals("123 has deleted", result); // Assuming you want this message
//        verify(jdbcTemplate).update("delete from bankers where banker_id=?", new Object[] { id });
    }

    //@Test
    void testInsertion() {
        // Mock data
        Bankers bankers = new Bankers();
        bankers.setBankerName("John");
        bankers.setBankerPasscode("1234");

        lenient().when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Call the method
        String result = bankersService.insertion(bankers);

        // Verify
        assertEquals("John access created", result); // Assuming you want this message

    }

    //@Test
    void testUpdateOne() {
        // Mock data
        Bankers bankers1=new Bankers();
        bankers1.setBankerId(139001);
        bankers1.setBankerPasscode("Razak@123");
        bankers1.setBankerName("razzaksr");


        //lenient().when(jdbcTemplate.update(anyString(),any(Object[].class))).thenReturn(1);
//        lenient().when(jdbcTemplate.update(any(String.class), any(PreparedStatementSetter.class))).thenReturn(1);
//        lenient().when(jdbcTemplate.update(eq("update bankers set banker_name=?, banker_passcode=? where banker_id=?"),eq(bankers1.getBankerName(), bankers1.getBankerPasscode(), bankers1.getBankerId()).thenReturn(1);
        lenient().when(jdbcTemplate.update(
                eq("update bankers set banker_name=?, banker_passcode=? where banker_id=?"),
                aryEq(new Object[]{bankers1.getBankerName(), bankers1.getBankerPasscode(), bankers1.getBankerId()})
        )).thenReturn(1);
        String result = bankersService.updateOne(bankers1);

        // Verify
        assertEquals("139001 has updated", result); // Assuming you want this message
//        Bankers bankers = new Bankers();
//        bankers.setBankerId(123);
//        bankers.setBankerName("John Doe");
//        bankers.setBankerPasscode("pass123");
//
//        lenient().when(jdbcTemplate.update(
//                anyString(),
//                any(Object[].class),
//                any(int[].class)
//        )).thenReturn(1);
//
//        // Call the method
//        String result = bankersService.updateOne(bankers);
//
//        // Verify
//        assertEquals("123 has updated", result); // Assuming you want this message
    }

    //@Test
    void testReadNames(){
        Bankers bankers1=new Bankers();
        bankers1.setBankerId(1232);
        bankers1.setBankerPasscode("alpha");
        bankers1.setBankerName("Brock");
        Bankers bankers2=new Bankers();
        bankers2.setBankerId(87678);
        bankers2.setBankerPasscode("sigma");
        bankers2.setBankerName("Brock");
        List<Bankers> bankers = Stream.of(bankers1,bankers2).collect(Collectors.toList());

        when(jdbcTemplate.query(anyString(),any(Object[].class),any(BankersService.BankersMapper.class))).thenReturn(bankers);

        List<Bankers> actual = bankersService.readNames(bankers2.getBankerName());

        assertEquals(bankers.get(0).getBankerId(),actual.get(0).getBankerId());

    }

    //@Test
    void testReadOne(){
        Bankers bankers1=new Bankers();
        bankers1.setBankerId(1232);
        bankers1.setBankerPasscode("alpha");
        bankers1.setBankerName("Brock");
        Bankers bankers2=new Bankers();
        bankers2.setBankerId(87678);
        bankers2.setBankerPasscode("sigma");
        bankers2.setBankerName("RKO");

        when(jdbcTemplate.queryForObject(anyString(),any(Object[].class),any(BeanPropertyRowMapper.class))).thenReturn(bankers2);

        Optional<Bankers> actual = bankersService.listOne(bankers2.getBankerId());

        assertEquals(Optional.of(bankers2),actual);
    }

    //@Test
    void testReadAll(){
        Bankers bankers1=new Bankers();
        bankers1.setBankerId(1232);
        bankers1.setBankerPasscode("alpha");
        bankers1.setBankerName("Brock");
        Bankers bankers2=new Bankers();
        bankers2.setBankerId(87678);
        bankers2.setBankerPasscode("sigma");
        bankers2.setBankerName("RKO");
        List<Bankers> bankers = Stream.of(bankers1,bankers2).collect(Collectors.toList());

        when(jdbcTemplate.query(anyString(),any(BankersService.BankersMapper.class))).thenReturn(bankers);

        List<Bankers> actual = bankersService.listAll();

        assertSame(bankers.size(),actual.size());
        assertEquals(bankers.get(1).getBankerName(),actual.get(0).getBankerName());
    }



}
